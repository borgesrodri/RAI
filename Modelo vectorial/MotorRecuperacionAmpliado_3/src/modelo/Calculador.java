package modelo;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;


public class Calculador {

	public Calculador(){
		
	}
	//MÉTODOS PARA EL CÁLCULO DE LAS RELEVANCIAS	
		public ArrayList<String> CosTFIDF(Map<String, Integer> consulta, MongoCollection<Document> dic,MongoCollection<Document> idfColl){
			Document idf = idfColl.find().first();
			FindIterable<Document> result = dic.find();
			double [] relevancia = new double[(int) (dic.count())];
			String [] nombres = new String[(int) (dic.count())];
			int i = 0;
			//Probamos cada palabra de la consulta en todos los documentos
			for (Document doc : result) {
				//obtener el nombre de los ficheros html almacenados en la BD
				nombres[i] = doc.getString("_id").replace(".html", "");
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
			ArrayList<String> respuesta = new ArrayList<String>();
			for (int j = 0; j < nom.length; j++) {
				respuesta.add(nom[j]);
			}
			return respuesta;
		}
		
		public ArrayList<String> CosTFIDFExpandido(Map<String, PesoPolisemia> consulta, MongoCollection<Document> dic,MongoCollection<Document> idfColl){
			Document idf = idfColl.find().first();
			FindIterable<Document> result = dic.find();
			double [] relevancia = new double[(int) (dic.count())];
			String [] nombres = new String[(int) (dic.count())];
			int i = 0;
			//Probamos cada palabra de la consulta en todos los documentos
			for (Document doc : result) {
				//obtener el nombre de los ficheros html almacenados en la BD
				nombres[i] = doc.getString("_id").replace(".html", "");
				double num = 0.0;
				double a = 0.0;
				double b = 0.0;			
				for (Map.Entry<String, PesoPolisemia> palCon : consulta.entrySet()) {
				    String palabra = palCon.getKey();
				    //Si la palabra se encuentra en el documento se suma su relevancia a la relevancia de la consulta 
					if(doc.containsKey(palabra) && idf.containsKey(palabra)){
						//SI LA PALABRA EXPANDIDA ES POLISÉMICA, SE APLICA UN % DE PENALIZACIÓN
						if (palCon.getValue().getPolisemia()) {
							num += doc.getInteger(palabra) * idf.getDouble(palabra) * palCon.getValue().getPeso() * idf.getDouble(palabra) * 0.85;
							b += Math.pow((palCon.getValue().getPeso()*idf.getDouble(palabra)), 2);
						}else{
							num += doc.getInteger(palabra) * idf.getDouble(palabra) * palCon.getValue().getPeso() * idf.getDouble(palabra);
							b += Math.pow((palCon.getValue().getPeso()*idf.getDouble(palabra)), 2);
						}
						
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
			ArrayList<String> respuesta = new ArrayList<String>();
			for (int j = 0; j < nom.length; j++) {
				respuesta.add(nom[j]);
			}
			return respuesta;
		}

		
}
