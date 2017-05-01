package motor;

import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;



public class PruebaMongoDB {
	
	public static void main(String[] args){
		MongoClient client = new MongoClient( "localhost" , 27017);	
		MongoDatabase db = client.getDatabase("motor");
		MongoCollection<Document> con = db.getCollection("nombres");
		FindIterable<Document> unionList = con.find();
		Document nomb = unionList.first();
		System.out.println(nomb);
		client.close();
		
	}
}
