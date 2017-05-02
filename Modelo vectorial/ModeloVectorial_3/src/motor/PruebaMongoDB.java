package motor;

import java.io.FileInputStream;

import org.apache.lucene.wordnet.SynonymMap;
import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;



public class PruebaMongoDB {
	
	public static void main(String[] args){
		//String[] words = args.split(" ");
		 SynonymMap map = null;
		try {
			map = new SynonymMap(new FileInputStream("./wn_g.pl"));
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	
		     String[] synonyms = map.getSynonyms("best");
		     for (int i = 0; i < synonyms.length; i++) {
				System.out.println(synonyms[i]);
			}
		 }
		
}
