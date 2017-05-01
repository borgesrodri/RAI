package modelo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import motor.CalcEvaluationMetrics;
import motor.Precision;
import motor.Wordnet;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Modelos {
	public static void main(String[] args) throws IOException {
		MongoClient client = new MongoClient( "localhost" , 27017);	
		MongoDatabase db = client.getDatabase("motor");
		MongoCollection<Document> dic = db.getCollection("diccionario");
		MongoCollection<Document> con = db.getCollection("consultas");
		MongoCollection<Document> idf = db.getCollection("idfColl");
		indice(dic, con, idf);
		client.close();
	}
	//Método para generar el indice con las posibilidades que se le da al usuario
	public static void indice(MongoCollection<Document> dic, MongoCollection<Document> con,MongoCollection<Document> idf) throws IOException {
		
		int i = -1;
		while (i != 9) {
			System.out
					.println("\nElija una de las siguientes opciones marcando el número correspondiente: \n "
							+ "1. Crear/Actualizar diccionario \n "
							+ "2. Realizar consulta \n " 
							+ "3. Realizar consultas estáticas \n "
							+ "9. Finalizar");
			Scanner lector = new Scanner(System.in);
			i = lector.nextInt();
			switch (i) {
			
			case 1:
				LectorMetricas lm = new LectorMetricas();
				lm.lecturas();
				CreadorDiccionario cd = new CreadorDiccionario();
				//dic.drop();
				//idf.drop();
				try {
					cd.crearDiccionario();
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println("El diccionario ha sido creado/actualizado");
				break;
			case 2:
				if (dic.count() == 0) {
					System.out
							.println("Debe crear el diccionario antes de realizar una consulta");
					break;
				}
				System.out.println("Introduzca la consulta que desea realizar");
				Scanner teclado = new Scanner(System.in);
				String consultaManual = teclado.nextLine();
				Map<String, Integer> pesosManual = consulta(consultaManual);
				Calculador calculadorManual = new Calculador();
				//String[] relevanciaManual = calculadorManual.CosTFIDF(pesosManual, dic,idf);
				break;
			case 3:
				if (dic.count() == 0) {
					System.out
							.println("Debe crear el diccionario antes de realizar una consulta");
					break;
				}
				//nombre de los 100 documentos más relevantes para cada consulta
				ArrayList<ArrayList<String>> relevancias = new ArrayList<ArrayList<String>>();
				//ficheros más relevantes según el union para cada consulta
				ArrayList<Document> union = separadorUnion(con);
				ArrayList<Map<String, Integer>> consultas = new ArrayList<Map<String, Integer>>();
				Calculador calculador = new Calculador();
				Document cons = con.find().first();
				for ( Entry<String, Object>  consulta : cons.entrySet()) {
					if(!consulta.getValue().toString().equals("topic")){
					Map<String, Integer> pesos = consulta(consulta.getValue().toString());
					Map<String, Integer> pesosExt = consultaExp(consulta.getValue().toString());
					consultas.add(pesos);
					consultas.add(pesosExt);
					relevancias.add(calculador.CosTFIDF(pesos,dic,idf));
					relevancias.add(calculador.CosTFIDF(pesosExt,dic,idf));
					}
				}
				
				CalcEvaluationMetrics cem = new CalcEvaluationMetrics();
				float r5=cem.calcRecall(union.get(0), relevancias.get(0), 1, 5);
				Precision p = new Precision();
				float p5=p.calcPrecision(union.get(0), relevancias.get(0), 1, 5);
				float f5=cem.calcFvalue(r5, p5);
				float r10=cem.calcRecall(union.get(0), relevancias.get(0), 0, 10);
				float p10=cem.calcPrecision(union.get(0), relevancias.get(0), 0, 10);
				float f10=cem.calcFvalue(r10, p10);
				System.out.println("Recall R@5 "+ cem.formatFloat(r5));
				System.out.println("Precision P@5 "+ cem.formatFloat(p5));
				System.out.println("F-valor F@5 "+ cem.formatFloat(f5));
				System.out.println("Recall R@10 "+ cem.formatFloat(r10));
				System.out.println("Precision P@10 "+cem.formatFloat(p10));
				System.out.println("F-valor F@10 "+ cem.formatFloat(f10));
				float reciprocal = cem.calcReciprocalRank(union.get(0), relevancias.get(0), 1);
				System.out.println("ReciprocalRank 1 "+ cem.formatFloat(reciprocal));
				reciprocal = cem.calcReciprocalRank(union.get(0), relevancias.get(0), 2);
				System.out.println("ReciprocalRank 2 "+ cem.formatFloat(reciprocal));
				float average=cem.calcAveragePrecision(union.get(0), relevancias.get(0), 1,100);
				System.out.println("Average Precision "+ cem.formatFloat(average));
				System.out.println(cem.formatArray(cem.calcnDCG(union.get(0), relevancias.get(0))));
				
				
				break;
			}
		}
		System.out.println("Fin del programa.");
	}

	// MÉTODO PARA FORMATEAR LA CONSULTA
	public static Map<String, Integer> consulta(String consulta) {
		CreadorDiccionario creador = new CreadorDiccionario();
		Wordnet wn = new Wordnet();
		consulta = consulta.toLowerCase();
		consulta = creador.limpiador(consulta);
		String [] palabras = creador.separador(consulta);
		Map<String, Integer> pesos = new HashMap<>();
		for (String name : palabras) {
			Integer count = pesos.get(name);
			if (!name.equals("")) {
				if (count == null) {
					pesos.put(name, 1);
				} else {
					pesos.put(name, ++count);
				}
			}
		}
		return pesos;
	}
	// MÉTODO PARA FORMATEAR LA CONSULTA Y EXPANDIRLA
		public static Map<String, Integer> consultaExp(String consulta) {
			CreadorDiccionario creador = new CreadorDiccionario();
			Wordnet wn = new Wordnet();
			consulta = consulta.toLowerCase();
			consulta = creador.limpiador(consulta);
			consulta = wn.sinonimos(consulta);
			String [] palabras = creador.separador(consulta);
			Map<String, Integer> pesos = new HashMap<>();
			for (String name : palabras) {
				Integer count = pesos.get(name);
				if (!name.equals("")) {
					if (count == null) {
						pesos.put(name, 1);
					} else {
						pesos.put(name, ++count);
					}
				}
			}
			return pesos;
		}
	
	public static ArrayList<Document> separadorUnion( MongoCollection<Document> con){
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
