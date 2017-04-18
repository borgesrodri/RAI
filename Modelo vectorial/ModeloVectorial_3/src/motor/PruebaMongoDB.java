package motor;

import java.io.IOException;

import modelo.CreadorDiccionario;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;


public class PruebaMongoDB {
	
	public static void main(String[] args){
		MongoClient client = new MongoClient( "localhost" , 27017);	
		MongoDatabase db = client.getDatabase("motor");
		MongoCollection<Document> dic = db.getCollection("diccionario");
		FindIterable<Document> result = dic.find();
		for (Document document : result) {
			System.out.println(document);
		}
		
	}
}
