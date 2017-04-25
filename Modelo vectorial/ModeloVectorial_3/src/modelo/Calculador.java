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
		public String[] CosTFIDF(Map<String, Integer> consulta, MongoCollection<Document> dic,MongoCollection<Document> idfColl){
			Document idf = idfColl.find().first();
			FindIterable<Document> result = dic.find();
			double [] relevancia = new double[(int) (dic.count())];
			String [] nombres = new String[(int) (dic.count())];
			int i = 0;
			//Probamos cada palabra de la consulta en todos los documentos
			for (Document doc : result) {
				//obtener el nombre de los ficheros html almacenados en la BD
				nombres[i] = doc.getString("_id");
				double num = 0.0;
				double a = 0.0;
				double b = 0.0;			
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
				if((Math.sqrt(a)*Math.sqrt(b)) == 0)
				i++;
				
			}
			
			for (int j = 0; j < relevancia.length; j++) {
				System.out.println(relevancia[i]);
			}
			
			//Array con los valores a devolver
			String[] nom = new String[100];
			double[] res = new double[100];
			
			for(int j = 0; j < 100; j++){
				res[j] = relevancia[j];
				nom[j] = nombres[j];
			}
			for(int a = 100; a < relevancia.length;a++){
				for (int j = 0; j < res.length; j++) {
					if(relevancia[a] > res[a]){
						res[j] = relevancia[a];
						nom[j] = nombres[a];
						j = res.length;
					}
				}
			}
			for (int a = 0; a < res.length; a++) {
				for (int j = 0; j < res.length; j++) {
					if(res[j] > res[a]){
						double aux = res[j];
						String aux2 = nom[j]; 
						res[j] = res[a];
						res[a] = aux;
						nom[j] = nom[a];
						nom[a] = aux2;
					}
				}
			}
			return nom;
		}
		

		
}
