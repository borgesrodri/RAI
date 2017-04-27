package modelo;

import java.util.Map;
import java.util.Map.Entry;

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
						b += Math.pow((palCon.getValue()*idf.getDouble(palabra)), 2);
					}
				}
				for (Entry<String, Object> todo : doc.entrySet()) {
					if(!todo.getKey().equals("_id")){
					a += Math.pow((Integer.parseInt(todo.getValue().toString()) * idf.getDouble(todo.getKey())), 2);
					}
				}
				double den = (Math.sqrt(a)*Math.sqrt(b));
				double rel = num /den;
				relevancia[i] = rel ;
				i++;
			}
			
			//Array con los valores a devolver
			String[] nom = new String[100];
			double[] res = new double[100];
			
			for (int j = 0; j < res.length; j++) {
				double valor = -1.0;
				int indice = 0;
				for(int a = 0; a < relevancia.length; a++){
					if(relevancia[a] > valor){
						valor = relevancia[a];
						indice = a;
					}
				}
				res[j] = valor;
				nom[j] = nombres[indice];
				relevancia[indice] = -1.0;
			}
			return nom;
		}
		

		
}
