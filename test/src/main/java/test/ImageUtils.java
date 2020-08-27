package test;

import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.strong.leke.core.dfs.FileUtils;
import net.coobird.thumbnailator.Thumbnails;

public class ImageUtils {

	private static final Logger logger = LoggerFactory.getLogger(ImageUtils.class);

	public static final String IMG_FMT_JPEG = "jpeg";
	public static final String IMG_FMT_JPG = "jpg";
	public static final String IMG_FMT_GIF = "gif";
	public static final String IMG_FMT_PNG = "png";
	public static final String IMG_FMT_BMP = "bmp";

	private static final String TMP_PREFIX = "image_";
	private static final List<String> IMG_SUPPORT_FMTS = Arrays.asList(IMG_FMT_JPEG, IMG_FMT_JPG, IMG_FMT_GIF,
			IMG_FMT_PNG, IMG_FMT_BMP);
	private static final long IMG_MAX_SIZE = 1024 * 1024 * 5; // 最大图片尺寸 5M

	/**
	 * 检测格式是否支持
	 * @param ext
	 * @return
	 */
	public static boolean inspectSupportFormat(String ext) {
		if (StringUtils.isBlank(ext)) {
			return false;
		}
		return IMG_SUPPORT_FMTS.contains(ext.toLowerCase());
	}

	/**
	 * 检测大小是否超大
	 * @param size
	 * @return
	 */
	public static boolean inspectSupportSize(long size) {
		return size > IMG_MAX_SIZE;
	}

	/**
	 * 处理图片文件
	 * @param source 源文件
	 * @param sampleSize 缩放比例
	 * @return
	 */
//	public static File executeImg(FileSource source, int sampleSize) {
//		File dest = null;
//		String ext = source.getExt();
//		if (IMG_FMT_PNG.equals(ext)) {
//			// PNG 必要时缩放大小
//			dest = ImageUtils.executePng(source, sampleSize);
//		} else if (IMG_FMT_GIF.equals(ext)) {
//			// GIF 直接存储
//			dest = ImageUtils.executeGif(source, sampleSize);
//		} else {
//			// JPG 缩放大小、调整方式，其它缩放大小、转换格式
//			dest = ImageUtils.executeJpg(source.get, sampleSize);
//		}
//		return dest;
//	}

	/**
	 * 格式：GIF
	 * 过程：直接存储
	 */
	private static File executeGif(FileSource source, int sampleSize) {
		File dest = null;
		try {
			dest = File.createTempFile(TMP_PREFIX, "." + IMG_FMT_GIF);
			source.transferTo(dest);
			return dest;
		} catch (IOException e) {
			logger.error("execute gif error.", e);
			FileUtils.deleteQuietly(dest);
			return null;
		}
	}

	/**
	 * 将上传图片转换后存储，返回临时文件。
	 * 过程：缩放大小。
	 * 注意：调用方需主动清理临时文件。
	 */
	private static File executePng(FileSource source, int sampleSize) {
		File dest = null;
		try (InputStream input = source.getInputStream()) {
			dest = File.createTempFile(TMP_PREFIX, "." + IMG_FMT_PNG);
			if (sampleSize > 1) {
				Thumbnails.of(input).scale(1d / sampleSize).toFile(dest);
			} else {
				source.transferTo(dest);
			}
			return dest;
		} catch (IOException e) {
			logger.error("execute png error.", e);
			FileUtils.deleteQuietly(dest);
			return null;
		}
	}

	/**
	 * 格式：JPG
	 * 过程：缩放大小、调整方向、目标JPG
	 */
	public static File executeJpg(File source, int sampleSize) {
		File dest = null;
		try (InputStream input = new FileInputStream(source)) {
			dest = File.createTempFile(TMP_PREFIX, "." + IMG_FMT_JPG);
			Thumbnails.of(input).scale(1d / sampleSize).useExifOrientation(supportExifOrientation("jpg"))
					.outputFormat(IMG_FMT_JPG).toFile(dest);
			return dest;
		} catch (IOException e) {
			logger.error("execute png error.", e);
			FileUtils.deleteQuietly(dest);
			return null;
		}
	}

	private static boolean supportExifOrientation(FileSource source) {
		String ext = source.getExt();
		return IMG_FMT_JPG.equalsIgnoreCase(ext) || IMG_FMT_JPEG.equalsIgnoreCase(ext);
	}

	private static boolean supportExifOrientation(String ext) {

		return IMG_FMT_JPG.equalsIgnoreCase(ext) || IMG_FMT_JPEG.equalsIgnoreCase(ext);
	}

	public static Dimension readDimension(InputStream input, String ext) {
		try (ImageInputStream stream = ImageIO.createImageInputStream(input)) {
			Iterator<ImageReader> iter = ImageIO.getImageReaders(stream);
			if (iter.hasNext()) {
				ImageReader reader = iter.next();
				reader.setInput(stream);
				int width = reader.getWidth(reader.getMinIndex());
				int height = reader.getHeight(reader.getMinIndex());
				reader.dispose();
				return new Dimension(width, height);
			}
		} catch (IOException e) {
			logger.warn("exception occur when read image dimension.", e);
		}
		return null;
	}

	public static Dimension readDimension(File file) {
		try (InputStream stream = new FileInputStream(file)) {
			return readDimension(stream, null);
		} catch (IOException e) {
			throw new RuntimeException("unable to read image dimension.", e);
		}
	}

	/**
	 * 计算图片的缩放比例系数。
	 * @param is
	 * @param ext
	 * @return
	 */
	public static int computeSampleSize(InputStream is, String ext) {
		Dimension dimension = readDimension(is, ext);
		if (dimension == null) {
			logger.warn("Compute image sample size, except occur when read image dimension.");
			return 1;
		}
		return computeSampleSize(dimension);
	}

	/**
	 * 根据尺寸计算缩放比例系数。
	 * @param dimension
	 * @return
	 */
	public static int computeSampleSize(Dimension dimension) {
		if (dimension == null) {
			return 1;
		}

		int width = dimension.width, height = dimension.height;
		//对宽度和高度取偶数值
		width = width % 2 == 1 ? width + 1 : width;
		height = height % 2 == 1 ? height + 1 : height;

		width = Math.min(width, height);
		height = Math.max(width, height);
		double scale = ((double) width / height);

		int sampleSize;
		if (scale <= 1 && scale > 0.5625) {
			if (height < 1664) {
				sampleSize = 1;
			} else if (height >= 1664 && height < 4990) {
				sampleSize = 2;
			} else if (height >= 4990 && height < 10240) {
				sampleSize = 4;
			} else {
				sampleSize = height / 1280 == 0 ? 1 : height / 1280;
			}
		} else if (scale <= 0.5625 && scale > 0.5) {
			sampleSize = height / 1280 == 0 ? 1 : height / 1280;
		} else {
			sampleSize = (int) Math.ceil(height / (1280.0 / scale));
		}

		return sampleSize;
	}
}
