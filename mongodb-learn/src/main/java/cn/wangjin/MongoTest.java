package cn.wangjin;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.jndi.MongoClientFactory;
import com.mongodb.client.model.InsertManyOptions;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MongoTest {

	static int size = 70000000;
	static int resSize = 10000000;
	static int perSize = 20000;
	static int threadSize = 10;


	static class InsertDB {

		MongoClient mongoClient = null;

		public void initConn() {

			//连接到MongoDB服务 如果是远程连接可以替换“localhost”为服务器所在IP地址
//ServerAddress()两个参数分别为 服务器地址 和 端口
			ServerAddress serverAddress = new ServerAddress("192.168.20.57", 27018);
			List<ServerAddress> addrs = new ArrayList<ServerAddress>();
			addrs.add(serverAddress);

//MongoCredential.createScramSha1Credential()三个参数分别为 用户名 数据库名称 密码
			MongoCredential credential = MongoCredential.createScramSha1Credential("leke", "admin", "Password@1".toCharArray());
			List<MongoCredential> credentials = new ArrayList<MongoCredential>();
			credentials.add(credential);

//通过连接认证获取MongoDB连接
			mongoClient = new MongoClient(addrs, credentials);
		}


		public void insert() {
			MongoDatabase db = mongoClient.getDatabase("question");
			// 返回当前的数据库名称
//		Set<String> colls = db.getCollectionNames();
//
//		for (String s : colls) {
//		    System.out.println(s);
//		}
			MongoCollection collection = db.getCollection("test.resCount");
			Random random = new Random();
			List<Document> documents = new ArrayList<Document>(10000);
			Document doc = null;
			long start = System.currentTimeMillis();
			for (int i = 0; i < size /threadSize / perSize; i++) {
				for (int j = 0; j < perSize; j++) {
					doc = new Document("_id", new ObjectId());
					doc.append("resId", random.nextInt(resSize) + 1);
					doc.append("usedCount", random.nextInt(1000));
					doc.append("favCount", random.nextInt(1000));
					doc.append("downCount", random.nextInt(500));
					doc.append("praiseCount", random.nextInt(10000));
					documents.add(doc);
				}
				InsertManyOptions insertManyOptions = new InsertManyOptions();
				insertManyOptions.ordered(false);
				collection.insertMany(documents, insertManyOptions);
				documents.clear();
			}
			long end = System.currentTimeMillis();
			System.out.println("cast : " + (end - start) / 1000f + "s");
			mongoClient.close();
		}

	}


	public static void main(String[] args) throws InterruptedException {
		long start = System.currentTimeMillis();
		List<Thread> threads = new ArrayList<Thread>();

		for (int i=0; i< threadSize;i++)
		{
			Thread thread= new Thread(){
				@Override
				public void run() {
						InsertDB db = new InsertDB();
						db.initConn();
						db.insert();
				}
			};

			thread.start();
			threads.add(thread);
		}

		for (Thread t:threads)
		{
			t.join();
		}


		long end = System.currentTimeMillis();
		System.out.println("total耗时" + (end - start) / 1000f + "s");

	}
}