package modelo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import motor.CalcEvaluationMetrics;
import motor.Precision;
import motor.PrintResult;
import motor.Resultados;
import motor.Wordnet;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Modelos {
	public static ArrayList<String> conS = new ArrayList<String>();
	public static void main(String[] args) throws IOException {
		MongoClient client = new MongoClient( "localhost" , 27017);	
		MongoDatabase db = client.getDatabase("motor");
		MongoCollection<Document> dic = db.getCollection("diccionario");
		MongoCollection<Document> con = db.getCollection("consultas");
		MongoCollection<Document> idf = db.getCollection("idfColl");
		indice(dic, con, idf);
		client.close();
	}
	//M�todo para generar el indice con las posibilidades que se le da al usuario
	public static void indice(MongoCollection<Document> dic, MongoCollection<Document> con,MongoCollection<Document> idf) throws IOException {
		
		int i = -1;
		while (i != 9) {
			System.out
					.println("\nElija una de las siguientes opciones marcando el n�mero correspondiente: \n "
							+ "1. Crear/Actualizar diccionario \n "
							+ "2. Realizar consultas est�ticas \n "
							+ "9. Finalizar");
			Scanner lector = new Scanner(System.in);
			i = lector.nextInt();
			switch (i) {
			
			case 1:
				LectorMetricas lm = new LectorMetricas();
				lm.lecturas();
				CreadorDiccionario cd = new CreadorDiccionario();
				dic.drop();
				idf.drop();
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
				//nombre de los 100 documentos m�s relevantes para cada consulta
				ArrayList<ArrayList<String>> relevancias = new ArrayList<ArrayList<String>>();
				//ficheros m�s relevantes seg�n el union para cada consulta
				ArrayList<Document> union = separadorUnion(con);
				//Id's de las consultas del topics
				ArrayList<String> topics = new ArrayList<String>();
				Calculador calculador = new Calculador();
				Document cons = con.find().first();
				for ( Entry<String, Object>  consulta : cons.entrySet()) {
					if(!consulta.getValue().toString().equals("topic")){
					topics.add(consulta.getKey().toString());
					topics.add(consulta.getKey().toString()+"Exp");
					Map<String, Integer> pesos = consulta(consulta.getValue().toString());
					Map<String, Integer> pesosExt = consultaExp(consulta.getValue().toString());
					relevancias.add(calculador.CosTFIDF(pesos,dic,idf));
					relevancias.add(calculador.CosTFIDF(pesosExt,dic,idf));
					}
				}
				for(int j = 0; j < relevancias.size(); j++){
					int a = (j-(j%2))/2;
					CalcEvaluationMetrics cem = new CalcEvaluationMetrics();
					Resultados result = new Resultados();
					Precision p = new Precision();
					PrintResult pr = new PrintResult();
					float r5=cem.calcRecall(union.get(a), relevancias.get(j), 1, 5);
					float p5=p.calcPrecision(union.get(a), relevancias.get(j), 1, 5);
					float f5=cem.calcFvalue(r5, p5);
					float r10=cem.calcRecall(union.get(a), relevancias.get(j), 1, 10);
					float p10=cem.calcPrecision(union.get(a), relevancias.get(j), 1, 10);
					float f10=cem.calcFvalue(r10, p10);
					float reciprocal1 = cem.calcReciprocalRank(union.get(a), relevancias.get(j), 1);
					float reciprocal2 = cem.calcReciprocalRank(union.get(a), relevancias.get(j), 2);
					float average=cem.calcAveragePrecision(union.get(a), relevancias.get(j), 1,100);
					result.setR5(cem.formatFloat(r5));
					result.setP5(cem.formatFloat(p5));
					result.setF5(cem.formatFloat(f5));
					result.setR10(cem.formatFloat(r10));
					result.setP10(cem.formatFloat(p10));
					result.setF10(cem.formatFloat(f10));
					result.setReciprocall1(cem.formatFloat(reciprocal1));
					result.setReciprocall2(cem.formatFloat(reciprocal2));
					result.setAverage(cem.formatFloat(average));
					result.setCalcnDCG(cem.formatArray(cem.calcnDCG(union.get(a), relevancias.get(j))));
					for (int k = 0; k < relevancias.get(j).size(); k++) {
						Document b = union.get(a);
						for (Entry<String, Object> s: b.entrySet()) {
							if(s.getKey().equals(relevancias.get(j).get(k))){
								relevancias.get(j).add(k,relevancias.get(j).get(k)+" "+s.getValue());
								relevancias.get(j).remove(k+1);
							}
						}
					}
					pr.printFile(conS.get(j), topics.get(j), relevancias.get(j), result);

				}
							
				break;
			}
		}
		System.out.println("Fin del programa.");
	}

	// M�TODO PARA FORMATEAR LA CONSULTA
	public static Map<String, Integer> consulta(String consulta) {
		CreadorDiccionario creador = new CreadorDiccionario();
		consulta = consulta.toLowerCase();
		consulta = creador.limpiador(consulta);
		conS.add(consulta);
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
	// M�TODO PARA FORMATEAR LA CONSULTA Y EXPANDIRLA
		public static Map<String, Integer> consultaExp(String consulta) {
			CreadorDiccionario creador = new CreadorDiccionario();
			Wordnet wn = new Wordnet();
			consulta = consulta.toLowerCase();
			consulta = creador.limpiador(consulta);
			consulta = wn.sinonimos(consulta);
			conS.add(consulta);
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