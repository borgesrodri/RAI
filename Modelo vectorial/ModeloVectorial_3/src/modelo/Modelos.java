package modelo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Modelos {
	public static void main(String[] args) throws IOException {
		Map<String, Double> idf = new HashMap<String, Double>();
		MongoClient client = new MongoClient( "localhost" , 27017);	
		MongoDatabase db = client.getDatabase("motor");
		MongoCollection<Document> dic = db.getCollection("diccionario");
		MongoCollection<Document> con = db.getCollection("consultas");
		indice(dic, con);
	}
	//Método para generar el indice con las posibilidades que se le da al usuario
	public static void indice(MongoCollection<Document> dic, MongoCollection<Document> con) throws IOException {
		
		int i = -1;
		while (i != 9) {
			System.out
					.println("\nElija una de las siguientes opciones marcando el número correspondiente: \n "
							+ "1. Crear/Actualizar diccionario \n "
							+ "2. Realizar consulta \n " 
							+ "4. Realizar consultas estáticas \n "
							+ "9. Finalizar");
			Scanner lector = new Scanner(System.in);
			i = lector.nextInt();
			switch (i) {
			
			case 1:
				LectorMetricas lm = new LectorMetricas();
				lm.lecturas();
				CreadorDiccionario cd = new CreadorDiccionario();
				dic.drop();
				try {
					cd.crearDiccionario();
				} catch (IOException e) {
					// TODO Auto-generated catch block
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
				double[] relevanciaManual = calculadorManual.CosTFIDF(pesosManual, dic);
				break;
			case 3:
				if (dic.count() == 0) {
					System.out
							.println("Debe crear el diccionario antes de realizar una consulta");
					break;
				}
				ArrayList<double[]> relevancias = new ArrayList<double[]>();
				ArrayList<Map<String, Integer>> consultas = new ArrayList<Map<String, Integer>>();
				Calculador calculador = new Calculador();
				Document cons = con.find().first();
				for ( Entry<String, Object>  consulta : cons.entrySet()) {
					Map<String, Integer> pesos = consulta(consulta.getValue().toString());
					consultas.add(pesos);
					relevancias.add(calculador.CosTFIDF(pesos,dic));
				}
				break;
			}
		}
		System.out.println("Fin del programa.");
	}

	// MÉTODO PARA FORMATEAR LA CONSULTA
	public static Map<String, Integer> consulta(String consulta) {
		CreadorDiccionario creador = new CreadorDiccionario();
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


}
