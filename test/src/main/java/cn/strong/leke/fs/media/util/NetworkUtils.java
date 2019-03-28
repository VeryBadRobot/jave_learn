package cn.strong.leke.fs.media.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;


/**
 * 网络工具类，获取IP地址
 */
public class NetworkUtils {

	private static String ipaddress;

	private static String getIpAddress0() {
		try {
			Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
			while (networks.hasMoreElements()) {
				NetworkInterface network = networks.nextElement();
				Enumeration<InetAddress> addresses = network.getInetAddresses();
				while (addresses.hasMoreElements()) {
					InetAddress address = addresses.nextElement();
					if (address != null && address instanceof Inet4Address) {
						String ipaddress = address.getHostAddress();
						if (ipaddress != null && !ipaddress.startsWith("127")) {
							return ipaddress;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "unknown";
	}

	/**
	 * 获取本机IP地址
	 *
	 * @return
	 */
	public static String getIpAddress() {
		if (ipaddress == null) {
			synchronized (NetworkUtils.class) {
				ipaddress = getIpAddress0();
			}
		}
		return ipaddress;
	}

	public static void main(String[] args) {
		System.out.println(getIpAddress());
	}
}
