package gc;

public class TestAge {

	private static final int _1MB = 1024 * 1024;

	/**
	 * VM 参数: -verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:MaxTenuringThreshold=1 -XX:+PrintTenuringDistribution
	 */
	@SuppressWarnings("unused")
	public static void testTenuringThreshold() {
		byte[] allocation1, allocation2, allocation3;
		allocation1 = new byte[_1MB / 4]; // 新生代 256K
		// 什么时候进入老年代取决于XX:MaxTenuringThreshold 设置
		allocation2 = new byte[4 * _1MB]; // 新生代 4352K
		allocation3 = new byte[4 * _1MB]; // minor gc 永久代 4096
		allocation3 = null;
		allocation3 = new byte[4 * _1MB]; // minor gc  新生代 8192K

		allocation3 = new byte[4 * _1MB]; // minor gc  新生代 8192K
	}

	/**
	 * VM 参数: -verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:MaxTenuringThreshold=15
	 */
	@SuppressWarnings("unused")
	public static void testTenuringThreshold2() {
		byte[] allocation1, allocation2, allocation3, allocation4;
		allocation1 = new byte[_1MB / 4];
		// allocation1 + allocation2大于survivo空间一半
		allocation2 = new byte[_1MB / 4];
		allocation3 = new byte[4 * _1MB];
		allocation4 = new byte[4*_1MB];
		allocation4 = null;
		allocation4 = new byte[4*_1MB];
	}


	public static void main(String[] args) {
		testTenuringThreshold2();
	}

}
