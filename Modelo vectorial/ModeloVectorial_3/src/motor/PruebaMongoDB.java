package motor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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
		MongoCollection<Document> con = db.getCollection("consultas");
		FindIterable<Document> unionList = con.find(new Document("_id","union"));
		Document union = unionList.first();
		ArrayList<Map<String,Integer>> res = new ArrayList<Map<String,Integer>>();
		for(int i = 0; i < 20; i++){
			res.add(new HashMap<String,Integer>());
		}
		String c = "";
		int i = -1;
		for (Entry<String, Object> consulta : union.entrySet()) {
			String [] a = consulta.getKey().split(" ");
			if( !a[0].equals("_id")){
			if(a[0].equals(c)){
				res.get(i).put(a[1], Integer.parseInt((String) consulta.getValue()));
			}else{
				c = a[0];
				i++;
				res.get(i).put(a[1], Integer.parseInt((String) consulta.getValue()));
			}
			}
		}
		for(int a = 0; a < res.size(); a++){
			System.out.println(res.get(a));
		}

	}
}
