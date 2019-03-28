package cn.strong.leke.fs.media.service;

import cn.strong.fastdfs.core.FastdfsClient;
import cn.strong.fastdfs.model.StoragePath;
import cn.strong.fastdfs.sink.FileSink;
import cn.strong.leke.fs.media.context.FileInfo;
import cn.strong.leke.fs.media.context.MediaInfo;
import cn.strong.leke.fs.media.context.TransContext;
import cn.strong.leke.fs.media.context.TransError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

import static cn.strong.leke.fs.media.service.TranscodeService.M3U8_FILE_NAME;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

/**
 * 存储服务。
 */
public class StorageService {

	private static final Logger logger = LoggerFactory.getLogger(TransformService.class);

	/**
	 * Fastdfs客户端
	 */
	@Resource
	private FastdfsClient fastdfsClient;

	/**
	 * 根据转换上下文下载文件。
	 *
	 * @param context
	 * @return
	 */
	public File download(TransContext context, ProgressHandler handler) {
		//获取转换文件信息
		FileInfo fileInfo = context.getFileInfo();
		//创建源文件(source.ext)
		File file = context.createFile(fileInfo.buildFileName("source"));
		try (FileSink sink = new FileSink(file)) {
			//下载文件
			this.fastdfsClient.download(StoragePath.valueOf(fileInfo.getPath()), sink, handler::onDownloadProgress)
					.get();
		} catch (InterruptedException | ExecutionException | IOException e) {
			throw TransError.DOWNLOAD.newTransformException(e);
		}
		//设置转换文件内容
		fileInfo.setFile(file);
		return file;
	}

	/**
	 * 上传切片后的文件，包括m3u8文件和ts文件。
	 * 步骤：
	 * 1、读取m3u8文件并解析；
	 * 2、同目录下找到所有ts文件并上传；
	 * 3、更新ts文件并上传。
	 *
	 * @param file m3u8文件
	 * @return
	 */
	public void upload(TransContext context, ProgressHandler handler) {
		//读取媒体文件信息
		MediaInfo mediaInfo = context.getMediaInfo();
		//创建转换后的文件
		File file = context.createFile(M3U8_FILE_NAME);
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
			//读取m3u8文件中的所有行
			List<String> lines = reader.lines().collect(toList());
			for (int i = 0; i < lines.size(); i++) {
				String line = lines.get(i);
				if (line.endsWith(".ts")) {
					//对于每一个ts文件，上传到服务器中。并返回存储路径
					line = this.uploadWorkFile(context, line);
					if (line == null) {
						throw TransError.UPLOAD.newTransformException();
					}
					//设置ts文件存储路径为新的服务器路径
					lines.set(i, "/" + line);
					//更新进度
					handler.onUploadProgress(i, lines.size() + 1);
				}
			}
			//以换行符拼接所有行
			byte[] bytes = lines.stream().collect(joining("\n")).getBytes();
			int length = bytes.length;
			//上传修改后的m3u8内容，并设置媒体信息的路径为上传后的文件
			mediaInfo.setPath(this.uploadAndRetry(() -> fastdfsClient.upload(bytes, length, "m3u8", null)).join());
		} catch (IOException e) {
			throw TransError.UPLOAD.newTransformException(e);
		}
	}

	/**
	 * 上传切片后的文件，包括m3u8文件和ts文件。
	 * 步骤：
	 * 1、读取m3u8文件并解析；
	 * 2、同目录下找到所有ts文件并上传；
	 * 3、更新ts文件并上传。
	 *
	 * @param file m3u8文件
	 * @return
	 */
	public void upload(TransContext context) {
		MediaInfo mediaInfo = context.getMediaInfo();
		File file = context.createFile(M3U8_FILE_NAME);
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
			List<String> lines = reader.lines().parallel().map(line -> {
				if (line.endsWith(".ts")) {
					return this.uploadAndRetry(() -> this.fastdfsClient.upload(context.createFile(line), null));
				} else {
					return CompletableFuture.completedFuture(line);
				}
			}).map(CompletableFuture::join).map(line -> (line.endsWith(".ts") ? "/" : "") + line).collect(toList());
			if (lines.stream().filter(v -> v == null).count() > 0) {
				throw TransError.UPLOAD.newTransformException();
			}
			byte[] bytes = lines.stream().collect(joining("\n")).getBytes();
			int length = bytes.length;
			mediaInfo.setPath(this.uploadAndRetry(() -> fastdfsClient.upload(bytes, length, "m3u8", null)).join());
		} catch (IOException e) {
			throw TransError.UPLOAD.newTransformException(e);
		}
	}

	/**
	 * 上传工作目录文件
	 *
	 * @param context
	 * @param fileName
	 * @return
	 */
	private String uploadWorkFile(TransContext context, String fileName) {
		File file = context.createFile(fileName);
		return this.uploadAndRetry(() -> fastdfsClient.upload(file, null)).join();
	}

	/**
	 * 上传文件，如果失败重试一次，再次失败返回空值。
	 *
	 * @param func 上传处理逻辑
	 * @return
	 */
	private CompletableFuture<String> uploadAndRetry(Supplier<CompletableFuture<StoragePath>> func) {
		return func.get().thenApplyAsync(v -> v.toString()).exceptionally(ex -> {
			logger.info("File upload failed, retry again.", ex);
			try {
				return func.get().thenApplyAsync(v -> v.toString()).get();
			} catch (InterruptedException | ExecutionException e) {
				logger.info("File again upload failed, give it up.", e);
				return null;
			}
		});
	}
}
