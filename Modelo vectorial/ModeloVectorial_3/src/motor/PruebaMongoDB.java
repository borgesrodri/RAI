package motor;

import java.util.List;
import java.util.Set;

import com.mongodb.DB;
import com.mongodb.MongoClient;

public class PruebaMongoDB {
MongoClient client = new MongoClient( "localhost" , 27017);
	public void pruebas(){
		DB db = client.getDB("mydb");
		Set<String> collectionNames = db.getCollectionNames();
			for (final String s : collectionNames) {
			    System.out.println(s);
			}
	}
}
