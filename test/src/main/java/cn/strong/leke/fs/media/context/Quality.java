package cn.strong.leke.fs.media.context;

/**
 * 媒体品质接口
 */
public interface Quality {

	public String[] getArguments();

	public static enum Video implements Quality {
		HD360("nhd"), HD480("hd480"), HD540("qhd"), HD720("hd720"), HD1080("hd1080");

		private String dpi;

		private Video(String dpi) {
			this.dpi = dpi;
		}

		public String[] getArguments() {
			return new String[]{"-s", this.dpi};
		}
	}

	public static enum Audio {

	}
}
