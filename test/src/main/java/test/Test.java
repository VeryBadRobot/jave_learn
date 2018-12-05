package test;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class Test {
	public static void main(String[] args) {
		System.out.println(Runtime.getRuntime().availableProcessors());
		System.out.println(System.getProperty("user.dir"));

		try
		{
			getIpAddress0();
		}catch (Exception e)
		{

		}
	}


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
							System.out.println(ipaddress);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new TestException();
		}
		return "unknown";
	}
}
