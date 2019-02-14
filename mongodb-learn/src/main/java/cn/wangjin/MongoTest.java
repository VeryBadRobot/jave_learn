package cn.wangjin;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

import java.net.UnknownHostException;

public class MongoTest {
	

	public static void main(String[] args) throws UnknownHostException {
		MongoClient mongoClient = new MongoClient( "localhost" , 30000 );
		DB db = mongoClient.getDB( "mytest" );
	// 返回当前的数据库名称
//		Set<String> colls = db.getCollectionNames();
//		
//		for (String s : colls) {
//		    System.out.println(s);
//		}
		DBCollection collection=db.getCollection("test");
		BasicDBObject doc = new BasicDBObject("name", "xiaohua2");
		collection.insert(doc);
			System.out.println(collection.count());
		mongoClient.close();
		/*
	
		//得到一个集合。这个集合就是用来做crud的接口
		DBCollection coll = db.getCollection("mydb");
		//插入一个document，和sql的表差点儿相同
		//The _id element has been added automatically by MongoDB to your document.
		//Remember, MongoDB reserves element names that start with “_”/”$” for internal use
		BasicDBObject doc = new BasicDBObject("name", "MongoDB")
        .append("type", "database")
        .append("count", 1)
        .append("info", new BasicDBObject("x", 203).append("y", 102));
		coll.insert(doc);
		//得到第一条document
		DBObject myDoc = coll.findOne();
		System.out.println(myDoc);
		//多条数据插入
		for (int i=0; i < 100; i++) {
		    coll.insert(new BasicDBObject("i", i));
		}
		//统计document的行数
		System.out.println(coll.getCount());
		
		//使用游标
		DBCursor cursor = coll.find();
		try {
		   while(cursor.hasNext()) {
		       System.out.println(cursor.next());
		   }
		} finally {
		   cursor.close();
		}
		mongoClient.close();
		//查询
//		BasicDBObject query = new BasicDBObject("i", 71);
//		cursor = coll.find(query);
//
//		try {
//		   while(cursor.hasNext()) {
//		       System.out.println(cursor.next());
//		   }
//		} finally {
//		   cursor.close();
//		}
		  */
	}
}