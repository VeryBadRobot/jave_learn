package test;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.*;

public class Test {


	public static int count = 10000;

	public static void main(String[] args) {

		Integer t =256;

		System.out.println(t==256);
		Set<Integer> documentTypes = new HashSet<>();
		documentTypes.add(1);
		documentTypes.add(2);
		documentTypes.add(3);
		System.out.println(documentTypes.contains(new Integer(1)));
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
