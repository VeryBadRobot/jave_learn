package cn.strong.leke.fs.media.context;

/**
 * 媒体文件提取信息。
 */
public class MediaInfo {

	/**
	 * 文件路径
	 */
	private String path;
	/**
	 * 文件大小
	 */
	private long filesize;
	/**
	 * 媒体品质
	 */
	private int quality;
	/**
	 * 音频时长
	 */
	private int duration;
	private int rotate;
	private int displayRotate;

	// 视频参数
	private String vcodec;
	private String vsize;
	private Integer vbrate; // bit rate
	private Integer vfps;

	// 音频参数
	private String acodec;
	private Integer abrate; // bit rate
	private Integer asrate; // simple rate

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public long getFilesize() {
		return filesize;
	}

	public void setFilesize(long filesize) {
		this.filesize = filesize;
	}

	public int getDataRate() {
		return (int) Math.round((double) filesize / duration);
	}

	public int getQuality() {
		return quality;
	}

	public void setQuality(int quality) {
		this.quality = quality;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getRotate() {
		return rotate;
	}

	public int getDisplayRotate() {
		return displayRotate;
	}

	public void setDisplayRotate(int displayRotate) {
		this.displayRotate = displayRotate;
	}

	public void setRotate(int rotate) {
		this.rotate = rotate;
	}

	public String getVcodec() {
		return vcodec;
	}

	public void setVcodec(String vcodec) {
		this.vcodec = vcodec;
	}

	public String getVsize() {
		return vsize;
	}

	public void setVsize(String vsize) {
		this.vsize = vsize;
	}

	public Integer getVbrate() {
		return vbrate;
	}

	public void setVbrate(Integer vbrate) {
		this.vbrate = vbrate;
	}

	public Integer getVfps() {
		return vfps;
	}

	public void setVfps(Integer vfps) {
		this.vfps = vfps;
	}

	public String getAcodec() {
		return acodec;
	}

	public void setAcodec(String acodec) {
		this.acodec = acodec;
	}

	public Integer getAbrate() {
		return abrate;
	}

	public void setAbrate(Integer abrate) {
		this.abrate = abrate;
	}

	public Integer getAsrate() {
		return asrate;
	}

	public void setAsrate(Integer asrate) {
		this.asrate = asrate;
	}

	public String getMediaString() {
		return "filesize=" + filesize + ", duration=" + duration + ", dataRate=" + getDataRate() + ", rotate=" + rotate
				+ ", displayRotate=" + displayRotate + ", vcodec=" + vcodec + ", vsize=" + vsize + ", vbrate=" + vbrate
				+ ", vfps=" + vfps + ", acodec=" + acodec + ", abrate=" + abrate + ", asrate=" + asrate;
	}
}