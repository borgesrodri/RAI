package modelo;

import java.util.Map;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Calculador {

	public Calculador(){
		
	}
	//MÉTODOS PARA EL CÁLCULO DE LAS RELEVANCIAS	
		public double[] CosTFIDF(Map<String, Integer> consulta, MongoCollection<Document> dic){
			FindIterable<Document> idfList = dic.find(new BasicDBObject("_id", "IDF"));
			Document idf = idfList.first();
			FindIterable<Document> result = dic.find();
			double [] relevancia = new double[(int) (dic.count()-1)];
			int i = 0;
			//Probamos cada palabra de la consulta en todos los documentos
			for (Document doc : result) {
				double num = 0.0;
				double a = 0.0;
				double b = 0.0;
			    //Si el documento no es el IDF (que se encuentra en la última posición)
				if(i < dic.count() -1){
					for (Map.Entry<String, Integer> palCon : consulta.entrySet()) {
					    String palabra = palCon.getKey();
					    //Si la palabra se encuentra en el documento se suma su relevancia a la relevancia de la consulta 
						if(doc.containsKey(palabra) && idf.containsKey(palabra)){
							num += doc.getInteger(palabra) * idf.getDouble(palabra) * palCon.getValue() * idf.getDouble(palabra);
							a += Math.pow((doc.getInteger(palabra) * idf.getDouble(palabra)), 2);
							b += Math.pow((palCon.getValue()*idf.getDouble(palabra)), 2);
						}
					}
					relevancia[i] = num / (Math.sqrt(a)*Math.sqrt(b));
					i++;
				}
			}
			return relevancia;
		}
		
}
