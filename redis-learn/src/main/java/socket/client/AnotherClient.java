package socket.client;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.util.SafeEncoder;

import java.util.Map;

public class AnotherClient {

	static Jedis jedis = new Jedis("127.0.0.1",6379,300000);
	static String key = "material";


	public static void main(String[] args) {
		System.out.println("another 服务正在运行： " +jedis.ping());

		get();
	}


	static void get() {
		long start = System.currentTimeMillis();
		Map<byte[], byte[]> map = jedis.hgetAll(SafeEncoder.encode(key));
		long end = System.currentTimeMillis();
		System.out.println("get cost: " + (end-start));
	}
}
