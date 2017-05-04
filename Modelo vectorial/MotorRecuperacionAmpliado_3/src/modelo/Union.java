package modelo;

import java.util.ArrayList;
import java.util.Map.Entry;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

public class Union {
	public ArrayList<Document> separadorUnion( MongoCollection<Document> con){
		FindIterable<Document> unionList = con.find(new Document("_id","union"));
		Document union = unionList.first();
		ArrayList<Document> res = new ArrayList<Document>();
		for(int i = 0; i < 20; i++){
			res.add(new Document());
		}
		String c = "";
		int i = -1;
		for (Entry<String, Object> consulta : union.entrySet()) {
			String [] a = consulta.getKey().split(" ");
			if( !a[0].equals("_id")){
			if(a[0].equals(c)){
				res.get(i).put(a[1], (String) consulta.getValue());
			}else{
				c = a[0];
				i++;
				res.get(i).put(a[1], Integer.parseInt((String) consulta.getValue()));
			}
			}
		}
		return res;
	}
}
