package cn.strong.leke.fs.media.service;

import cn.strong.leke.core.exec.CmdExecException;
import cn.strong.leke.core.exec.CmdUtils;
import cn.strong.leke.fs.media.context.FileInfo;
import cn.strong.leke.fs.media.context.MediaInfo;
import cn.strong.leke.fs.media.context.TransContext;
import cn.strong.leke.fs.media.context.TransError;
import org.apache.commons.exec.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文件转换处理服务。
 */
public class TranscodeService implements InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(TranscodeService.class);

	public static final String TSFL_FILE_NAME = "file%04d.ts";
	public static final String M3U8_FILE_NAME = "playlist.m3u8";

	/**
	 * 信息提取超时时长：十秒钟
	 */
	@Value("${extract.media.timeout:10000}")
	private int extractMediaTimeout = 10 * 1000;

	/**
	 * 音频转换超时时长：五分钟
	 */
	@Value("${transcode.audio.timeout:300000}")
	private int transcodeAudioTimeout = 5 * 60 * 1000;

	/**
	 * 视频转换超时时长：一小时
	 */
	@Value("${transcode.video.timeout:3600000}")
	private int transcodeVideoTimeout = 60 * 60 * 1000;

	/**
	 * 转换命令模式
	 */
	@Value("${transcode.command.mode:guess}")
	private String commandMode = "guess";

	private CommandBuilder commandBuilder;

	/**
	 * 属性设置后执行，确定转换模式
	 *
	 * @throws Exception
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		switch (this.commandMode) {
			case "copy":
				this.commandBuilder = new CopyCommandBuilder();
				break;
			case "codec":
				this.commandBuilder = new CodecCommandBuilder();
				break;
			default:
				this.commandBuilder = new GuessCommandBuilder();
		}
	}

	/**
	 * 执行转换业务处理
	 *
	 * @param context 上下文
	 * @param handler 进度处理器
	 */
	public void execute(TransContext context, ProgressHandler handler) {
		try {
			// 提取媒体信息
			this.extractInfo(context);
			// 如果有旋转，做旋转校正
			// this.rectifyRotate(context);
			// 转码与切片
			this.transcode(context, handler);
		} catch (CommandException e) {
			throw TransError.TRANSCODE.newTransformException(e);
		} catch (TransformException e) {
			throw e;
		}
	}

	/**
	 * 提取媒体信息
	 *
	 * @param context 上下文
	 */
	private void extractInfo(TransContext context) {
		FileInfo fileInfo = context.getFileInfo();
		// ffmpeg.exe -i course.mp4
		CommandLine cmd = new CommandLine("ffmpeg");
		cmd.addArgument("-i").addArgument(fileInfo.getFile().getAbsolutePath());

		MediaInfo mediaInfo = new MediaInfo();
		mediaInfo.setFilesize(fileInfo.getSize());
		ExtractInfoOutputStream output = new ExtractInfoOutputStream(mediaInfo);
		this.execCommand(context.getFileInfo().getFileId(), cmd, output, extractMediaTimeout, true);
		context.setMediaInfo(mediaInfo);
		logger.info("File {} media info: {}", context.getFileInfo().getFileId(), mediaInfo.getMediaString());
	}

	/**
	 * 校正视频旋转角度，如果发生异常就忽略。
	 *
	 * @param context
	 */
	@SuppressWarnings("unused")
	private void rectifyRotate(TransContext context) {
		FileInfo fileInfo = context.getFileInfo();
		try {
			// ffmpeg -i source.mp4 -codec copy -metadata:s:v rotate="0" -y target.mp4
			String dest = context.createPath("revised." + fileInfo.getExt());
			CommandLine cmd = new CommandLine("ffmpeg");
			cmd.addArgument("-i").addArgument(fileInfo.getFile().getAbsolutePath());
			cmd.addArgument("-codec").addArgument("copy");
			cmd.addArgument("-metadata:s:v").addArgument("rotate=0");
			cmd.addArgument("-y").addArgument(dest);
			TransLogOutputStream output = new TransLogOutputStream();
			this.execCommand(context.getFileInfo().getFileId(), cmd, output, extractMediaTimeout, false);
			fileInfo.setFile(new File(dest));
		} catch (Exception e) {
			// empty.
		}
	}

	/**
	 * 转码和切片,首先已非兼容模式转码，转码失败尝试以兼容模式转码
	 *
	 * @param context 上下文
	 * @param handler 进度处理器
	 */
	private void transcode(TransContext context, ProgressHandler handler) {
		try {
			this.transcode(context, handler, false);
		} catch (CommandException e) {
			this.transcode(context, handler, true);
		}
	}

	/**
	 * 执行转码与切片
	 *
	 * @param context
	 * @param handler
	 * @param compatibleMode
	 */
	private void transcode(TransContext context, ProgressHandler handler, boolean compatibleMode) {
		//构建命令行
		CommandLine cmd = this.commandBuilder.build(context, compatibleMode);
		//输出流
		TranscodeOutputStream output = new TranscodeOutputStream(handler::onTranscodeProgress);
		//超时时间
		long timeout = context.getFileInfo().getType().equals("audio") ? transcodeAudioTimeout : transcodeVideoTimeout;
		//执行命令
		this.execCommand(context.getFileInfo().getFileId(), cmd, output, timeout, false);
	}

	// 命令行执行
	private void execCommand(String fileId, CommandLine cmd, TransLogOutputStream output, long timeout,
	                         boolean ignoreErrorExit) {
		String command = cmd.getExecutable() + " " + StringUtils.join(cmd.getArguments(), " ");
		logger.info("File {} command exec: {}", fileId, command);
		try {
			output.writeCommand(command);
			Executor executor = new DefaultExecutor();
			if (ignoreErrorExit) {
				executor.setExitValues(new int[]{0, 1});
			}
			executor.setStreamHandler(new PumpStreamHandler(output));
			CmdUtils.exec(cmd, timeout, executor);
			if (output.isInvalid()) {
				throw new CommandException(output.getLogs());
			}
		} catch (CmdExecException e) {
			throw new CommandException(output.getLogs());
		} catch (CommandException e) {
			throw TransError.FILE_INVALID.newTransformException(e);
		} finally {
			IOUtils.closeQuietly(output);
		}
	}

	private static abstract class CommandBuilder {

		/**
		 * 构建命令
		 *
		 * @param context
		 * @param compatibleMode
		 * @return
		 */
		public CommandLine build(TransContext context, boolean compatibleMode) {
			FileInfo fileInfo = context.getFileInfo();
			CommandLine cmd = new CommandLine("ffmpeg");
			// 输入：-i test.mp4
			cmd.addArgument("-i").addArgument(fileInfo.getFile().getAbsolutePath());
			if (context.getFileInfo().getType().equals("video")) {
				// 视频编码
				this.setVCodecArguments(cmd, context);
			} else {
				// 音频编码
				this.setACodecArguments(cmd, context);
			}
			// 控制参数
			this.setControlArguments(cmd, context);
			// 兼容参数
			if (compatibleMode) {
				this.setCompatibleArguments(cmd, context);
			}
			// 切片：-f segment -segment_time 10 -segment_list playlist.m3u8 -y file%04d.ts
			cmd.addArgument("-f").addArgument("segment");
			cmd.addArgument("-segment_time").addArgument("10");
			cmd.addArgument("-segment_list").addArgument(context.createPath(M3U8_FILE_NAME));
			cmd.addArgument("-y").addArgument(context.createPath(TSFL_FILE_NAME));
			return cmd;
		}

		/**
		 * 设置视频转码参数，一般也需要调用设置音频转码的方法
		 *
		 * @param cmd
		 * @param context
		 */
		public abstract void setVCodecArguments(CommandLine cmd, TransContext context);

		/**
		 * 设置音频转码参数
		 *
		 * @param cmd
		 * @param context
		 */
		public abstract void setACodecArguments(CommandLine cmd, TransContext context);

		/**
		 * 设置控制参数
		 *
		 * @param cmd
		 * @param context
		 */
		private void setControlArguments(CommandLine cmd, TransContext context) {
			// 控制参数：-threads 4
			// cmd.addArgument("-threads").addArgument("4");
		}

		/**
		 * 设置兼容性参数
		 *
		 * @param cmd
		 * @param context
		 */
		private void setCompatibleArguments(CommandLine cmd, TransContext context) {
			cmd.addArguments("-max_muxing_queue_size").addArgument("9999");
		}
	}

	/**
	 * 拷贝模式，直接拷贝原来编码
	 */
	private static class CopyCommandBuilder extends CommandBuilder {

		@Override
		public void setVCodecArguments(CommandLine cmd, TransContext context) {
			// -codec copy
			cmd.addArgument("-codec").addArgument("copy");
		}

		@Override
		public void setACodecArguments(CommandLine cmd, TransContext context) {
			// -codec copy
			cmd.addArgument("-codec").addArgument("copy");
		}
	}


	/**
	 * 转码，设置视频转码为h264,音频为aac
	 */
	private static class CodecCommandBuilder extends CommandBuilder {

		@Override
		public void setVCodecArguments(CommandLine cmd, TransContext context) {
			// Video：-vcodec h264 -vf scale=-2:480
			cmd.addArgument("-vcodec").addArgument("h264");
			cmd.addArgument("-vf").addArgument("scale=-2:480");
			//设置音频转码
			this.setACodecArguments(cmd, context);
		}

		@Override
		public void setACodecArguments(CommandLine cmd, TransContext context) {
			// Audio：-acodec aac -ab 128000 -ar 44100
			cmd.addArgument("-acodec").addArgument("aac");
			cmd.addArguments("-ab").addArgument("128000");
			cmd.addArgument("-ar").addArgument("44100");
		}
	}

	/**
	 * 根据媒体信息，确定是转码还是拷贝
	 */
	private static class GuessCommandBuilder extends CommandBuilder {

		@Override
		public void setVCodecArguments(CommandLine cmd, TransContext context) {
			MediaInfo mediaInfo = context.getMediaInfo();

			boolean ignoreVcodec = isIgnoreVcodec(mediaInfo), ignoreResize = isIgnoreResize(mediaInfo);
			if (ignoreResize) {
				if (ignoreVcodec) {
					// 拷贝：-vcodec copy
					cmd.addArgument("-vcodec").addArgument("copy");
				} else {
					// 转码：-vcodec h264
					cmd.addArgument("-vcodec").addArgument("h264");
				}
			} else {
				// 转码：-vcodec h264 -vf scale=-2:480
				cmd.addArgument("-vcodec").addArgument("h264");
				cmd.addArgument("-vf").addArgument("scale=-2:480");
			}

			if (isIgnoreAcodec(mediaInfo)) {
				// 拷贝：-acodec copy
				cmd.addArgument("-acodec").addArgument("copy");
			} else {
				// 转码：-acodec aac -ab 64000 -ac 2
				cmd.addArgument("-acodec").addArgument("aac");
				cmd.addArguments("-ab").addArgument("128000");
				cmd.addArgument("-ar").addArgument("44100");
				cmd.addArgument("-ac").addArgument("2");
			}
		}

		/**
		 * 原本就是h264不需要转码
		 *
		 * @param mediaInfo
		 * @return
		 */
		private boolean isIgnoreVcodec(MediaInfo mediaInfo) {
			String codec = mediaInfo.getVcodec();
			return codec != null && codec.startsWith("h264");
		}

		private boolean isIgnoreResize(MediaInfo mediaInfo) {
			return mediaInfo.getDataRate() < 80;
		}

		@Override
		public void setACodecArguments(CommandLine cmd, TransContext context) {
			MediaInfo mediaInfo = context.getMediaInfo();
			cmd.addArgument("-vn");
			if (isIgnoreAcodec(mediaInfo)) {
				// 拷贝：-acodec copy
				cmd.addArgument("-acodec").addArgument("copy");
			} else {
				// 转码：-acodec aac -ab 64000 -ac 2
				cmd.addArgument("-acodec").addArgument("aac");
				cmd.addArguments("-ab").addArgument("128000");
				cmd.addArgument("-ar").addArgument("44100");
				cmd.addArgument("-ac").addArgument("2");
			}
		}

		/**
		 * 原本就是aac不需要转码
		 *
		 * @param mediaInfo
		 * @return
		 */
		private boolean isIgnoreAcodec(MediaInfo mediaInfo) {
			String codec = mediaInfo.getAcodec();
			return codec != null && codec.startsWith("aac");
		}
	}

	// 可记录字符日志的输出流
	private static class TransLogOutputStream extends LogOutputStream {

		private StringWriter writer = new StringWriter();
		private boolean invalid = false;

		@Override
		protected void processLine(String line, int logLevel) {
			if (!line.startsWith("frame")) {
				writer.write("\n");
				writer.write(line);
			}
		}

		public void writeCommand(String command) {
			this.writer.write(command);
		}

		public String getLogs() {
			return this.writer.toString();
		}

		public boolean isInvalid() {
			return this.invalid;
		}

		public void setInvalid(boolean invalid) {
			this.invalid = invalid;
		}
	}

	// 转码过程输出处理，增加了进度回调。
	private static class TranscodeOutputStream extends TransLogOutputStream {

		private BiConsumer<Long, Long> handler;
		private Long duration;

		public TranscodeOutputStream(BiConsumer<Long, Long> handler) {
			this.handler = handler;
		}

		@Override
		protected void processLine(String line, int logLevel) {
			super.processLine(line, logLevel);

			if (this.handler != null) {
				if (this.duration == null) {
					Long value = parseTime(durationPattern, line);
					if (value != null) {
						this.duration = value;
					}
				}
				if (this.duration != null && line.lastIndexOf("frame") == 0) {
					Long value = parseTime(progressPattern, line);
					if (value != null) {
						handler.accept(value, this.duration);
					}
				}
			}
		}

		static Pattern durationPattern = Pattern.compile("Duration: (\\d+):(\\d+):(\\d+)\\.(\\d+)");
		static Pattern progressPattern = Pattern.compile("time=(\\d+):(\\d+):(\\d+)\\.(\\d+)");

		/**
		 * 转码时间
		 *
		 * @param pattern
		 * @param line
		 * @return
		 */
		static Long parseTime(Pattern pattern, String line) {
			try {
				Matcher matcher = pattern.matcher(line);
				if (matcher.find()) {
					int hour = Integer.parseInt(matcher.group(1));
					int minute = Integer.parseInt(matcher.group(2));
					int second = Integer.parseInt(matcher.group(3));
					int millis = Integer.parseInt(matcher.group(4));
					return ((hour * 60 + minute) * 60 + second) * 1000L + millis;
				}
			} catch (Exception e) {
			}
			return null;
		}
	}

	public static void main(String[] args) {
		// "/Video: (.*?), (.*?), (.*?)[,\s]/"
		Pattern pattern = Pattern.compile("Stream \\S* Video: (.*?), (.*?), (.*?), ((.*?) kb/s, (.*?) fps,)?");
		// Stream #0:0(eng): Video: h264, yuv420p, 
		String str = "Stream #0:0(eng): Video: h264, yuv420p, 426x240, 391 kb/s, 25 fps, 25 tbr, 12800 tbn, 50 tbc (default)";
		Matcher matcher = pattern.matcher(str);
		if (matcher.find()) {
			int len = matcher.groupCount();
			for (int i = 0; i < len; i++) {
				System.out.println(matcher.group(i + 1));
			}
		}

		str = "Stream #0:1: Video: mjpeg, yuvj444p, 500x500, 90k tbr, 90k tbn, 90k tbc";
		matcher = pattern.matcher(str);
		if (matcher.find()) {
			int len = matcher.groupCount();
			for (int i = 0; i < len; i++) {
				System.out.println(matcher.group(i + 1));
			}
		}
	}

	// 信息提取过程输出处理，可提取时长。
	// 目标：提取媒体文件格式格式、尺寸等，符合标准的文件不转码只切分。
	private static class ExtractInfoOutputStream extends TransLogOutputStream {

		protected static List<BiConsumer<String, MediaInfo>> parsers = new ArrayList<>();

		static {
			Pattern duration = Pattern.compile("Duration: (\\d+):(\\d+):(\\d+)\\.(\\d+)");
			parsers.add((line, mediaInfo) -> {
				// Duration: 00:04:22.56, start: 0.000000, bitrate: 128 kb/s
				Matcher matcher = duration.matcher(line);
				if (matcher.find()) {
					int hour = Integer.parseInt(matcher.group(1));
					int minute = Integer.parseInt(matcher.group(2));
					int second = Integer.parseInt(matcher.group(3));
					int millis = Integer.parseInt(matcher.group(4));
					mediaInfo.setDuration(((hour * 60 + minute) * 60 + second) * 1000 + millis);
				}
			});

			Pattern video = Pattern.compile("Stream \\S* Video: (.*?), (.*?), (.*?),");
			parsers.add((line, mediaInfo) -> {
				// Stream #0:0(eng): Video: h264, yuv420p, 426x240 [SAR 1:1 DAR 71:40], 391 kb/s, 25 fps, 25 tbr, 12800 tbn, 50 tbc (default)
				Matcher matcher = video.matcher(line);
				if (matcher.find()) {
					mediaInfo.setVcodec(matcher.group(1));
					mediaInfo.setVsize(matcher.group(3));
				}
			});

			Pattern audio = Pattern.compile("Stream \\S* Audio: (.*?), (.*?) Hz, ((.*?), (.*?), (.*?) kb/s)");
			parsers.add((line, mediaInfo) -> {
				// Stream #0:1(eng): Audio: aac, 44100 Hz, stereo, fltp, 125 kb/s (default)
				Matcher matcher = audio.matcher(line);
				if (matcher.find()) {
					mediaInfo.setAcodec(matcher.group(1));
					mediaInfo.setAbrate(Integer.parseInt(matcher.group(6)));
					mediaInfo.setAsrate(Integer.parseInt(matcher.group(2)));
				}
			});

			Pattern rotate = Pattern.compile("rotate\\s*: (\\d+)");
			parsers.add((line, mediaInfo) -> {
				//  rotate          : 90
				Matcher matcher = rotate.matcher(line);
				if (matcher.find()) {
					mediaInfo.setRotate(Integer.parseInt(matcher.group(1)));
				}
			});

			Pattern displayRotate = Pattern.compile("rotation of (\\d+) degrees");
			parsers.add((line, mediaInfo) -> {
				//  displaymatrix: rotation of 90.00 degrees
				Matcher matcher = displayRotate.matcher(line);
				if (matcher.find()) {
					mediaInfo.setDisplayRotate(Integer.parseInt(matcher.group(1)));
				}
			});
		}

		/**
		 * 媒体文件信息
		 */
		private MediaInfo mediaInfo;

		public ExtractInfoOutputStream(MediaInfo mediaInfo) {
			this.mediaInfo = mediaInfo;
		}

		@Override
		public void processLine(String line, int logLevel) {
			super.processLine(line, logLevel);
			if (line == null || line.isEmpty()) {
				return;
			}
			this.markInvalid(line);
			for (BiConsumer<String, MediaInfo> parser : parsers) {
				parser.accept(line, mediaInfo);
			}
		}

		private static final String[] INVALID_KEYWORDS = new String[]{"Invalid data found when processing input",
				"does not contain any stream", "Duration: N/A"};

		private void markInvalid(String line) {
			for (String keyword : INVALID_KEYWORDS) {
				if (line.indexOf(keyword) >= 0) {
					this.setInvalid(true);
				}
			}
		}
	}
}
