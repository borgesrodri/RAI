package motor;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;


public class PruebaMongoDB {
	
	public static void main(String[] args){
		MongoClient client = new MongoClient( "localhost" , 27017);
			
		MongoDatabase db = client.getDatabase("mydb");
		MongoIterable<String> collectionNames = db.listCollectionNames();
			for (final String s : collectionNames) {
			    System.out.println(s);
			}
	}
}
