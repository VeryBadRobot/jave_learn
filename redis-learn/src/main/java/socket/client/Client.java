package socket.client;

import cn.strong.leke.core.nosql.support.SerializableAdapter;
import cn.strong.leke.core.nosql.support.redis.serialize.HessianSerializerAdapter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.util.SafeEncoder;

import java.util.*;

public class Client {


	static Jedis jedis = new Jedis("127.0.0.1",6379,300000);
	static String key = "material";
	static SerializableAdapter serializer = new HessianSerializerAdapter();

	public static void main(String[] args) {

		final Jedis another = new Jedis("127.0.0.1",6379,300000);
		System.out.println("连接成功");
		//查看服务是否运行
		System.out.println("jedis 服务正在运行： " + jedis.ping());
		System.out.println("another 服务正在运行： " +another.ping());

		Thread thread = new Thread(){
			@Override
			public void run() {
				long start = System.currentTimeMillis();
				for (int i = 0;i<100000;i++)
				{
					if(i%1000 == 0){
						System.out.println(new Date()+"\t"+key+"_"+i);
					}
					String key = another.get("test");

				}
				long end = System.currentTimeMillis();
				System.out.println("read: cost:"+(end-start));
			}
		};
		thread.start();

		//put();
		int size = 3;
		for (int i = 0;i<size;i++)
		{

			long start = System.currentTimeMillis();
			System.out.println("start:"+new Date(start));
			get();
			long end = System.currentTimeMillis();
			System.out.println("end:"+new Date(end));
			System.out.println("hgetall: cost:"+(end-start));


		}
	}

	static void put() {

		List<Material> materials = produce(5000000);
		long start = System.currentTimeMillis();
		for (Material n : materials) {
			jedis.hset(SafeEncoder.encode(key), SafeEncoder.encode(n.getMaterialId().toString()), serializer.serialize(n));
		}
		long end = System.currentTimeMillis();
		System.out.println("put cost: " + (end - start));
	}

	static void get() {
		long start = System.currentTimeMillis();
		Map<byte[], byte[]> map = jedis.hgetAll(SafeEncoder.encode(key));

	}


	static List<Material> produce(int size) {
		List<Material> materials = new ArrayList<Material>();

		for (int i = 0; i < size; i++) {
			Material material = new Material();
			material.setSchoolStageId(Long.valueOf(i));
			material.setSchoolStageName("_" + i);
			material.setSubjectId(Long.valueOf(i));
			material.setSubjectName("_" + i);
			material.setGradeId(Long.valueOf(i));
			material.setGradeName("_" + i);
			material.setMaterialId(Long.valueOf(i));
			material.setMaterialName("_" + i);
			material.setPressId(Long.valueOf(i));
			material.setPressName("_" + i);
			material.setOrd(i);
			materials.add(material);
		}
		return materials;
	}


}
