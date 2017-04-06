package modelo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Modelos {
	public static void main(String[] args) throws IOException {
		indice();
	}
	//Método para generar el indice con las posibilidades que se le da al usuario
	public static void indice() throws IOException {
		ArrayList<Map<String, Integer>> diccionario = new ArrayList<Map<String, Integer>>();
		ArrayList<String> nombreFicheros = new ArrayList<String>();
		Map<String, Double> idf = new HashMap<String, Double>();
		int i = -1;
		while (i != 9) {
			System.out
					.println("\nElija una de las siguientes opciones marcando el número correspondiente: \n "
							+ "1. Crear/Actualizar diccionario \n "
							+ "2. Realizar consulta \n " 
							+ "4. Realizar consultas estáticas \n "
							+ "0. Finalizar");
			Scanner lector = new Scanner(System.in);
			i = lector.nextInt();
			switch (i) {
			case 1:
				CreadorDiccionario creador = new CreadorDiccionario();
				diccionario = creador.crearDiccionario();
				nombreFicheros = CreadorDiccionario.getNombreFicheros();
				Map<String, Integer> aux = CreadorDiccionario.getIdf();
				idf = new HashMap<String, Double>();
				for (Map.Entry<String, Integer> entry : aux.entrySet()) {
					double log = Math
							.log10(((double) diccionario.size() / (double) entry
									.getValue()));
					idf.put(entry.getKey(), log);
				}
				System.out.println("El diccionario ha sido creado/actualizado");
				break;
			case 2:
				if (diccionario.isEmpty()) {
					System.out
							.println("Debe crear el diccionario antes de realizar una consulta");
					break;
				}
				System.out.println("Introduzca la consulta que desea realizar");
				Scanner teclado = new Scanner(System.in);
				String consulta = teclado.nextLine();
				Map<String, Integer> pesos = consulta(consulta);
				Calculador calculador = new Calculador();
				double[] PE_TF = calculador.calcularProdTF(pesos, diccionario);
				double[] PE_TF_IDF = calculador.calcularProdTFIDF(pesos,
						diccionario, idf);
				double[] COS_TF = calculador.calcularCosenoTF(pesos,
						diccionario);
				double[] COS_TF_IDF = calculador.calcularCosenoTFIDF(pesos,
						diccionario, idf);
				System.out
						.println("Los resultados se mostrarán por categoría y relevancia");
				System.out.println("\n-- Producto escalar TF --");
				mostrarResultados(PE_TF, nombreFicheros);
				System.out.println("\n-- Producto escalar TF-IDF --");
				mostrarResultados(PE_TF_IDF, nombreFicheros);
				System.out.println("\n-- Coseno TF --");
				mostrarResultados(COS_TF, nombreFicheros);
				System.out.println("\n-- Coseno TF-IDF --");
				mostrarResultados(COS_TF_IDF, nombreFicheros);
				break;
			case 3:
				if (diccionario.isEmpty()) {
					System.out
							.println("Debe crear el diccionario antes de realizar una consulta");
					break;
				}
				String q1 = "What video game won Spike's best driving game award in 2006?";
				String q2 = "What is the default combination of Kensington cables?";
				String q3 = "Who won the first ACM Gerard Salton prize?";
				Map<String, Integer> p1 = consulta(q1);
				Map<String, Integer> p2 = consulta(q2);
				Map<String, Integer> p3 = consulta(q3);
				Calculador c = new Calculador();
				double[] PE_TF1 = c.calcularProdTF(p1, diccionario);
				double[]PE_TF_IDF1 = c.calcularProdTFIDF(p1,
						diccionario, idf);
				double[]COS_TF1 = c.calcularCosenoTF(p1,
						diccionario);
				double[]COS_TF_IDF1 = c.calcularCosenoTFIDF(p1,
						diccionario, idf);
				double[] PE_TF2 = c.calcularProdTF(p2, diccionario);
				double[]PE_TF_IDF2 = c.calcularProdTFIDF(p2,
						diccionario, idf);
				double[]COS_TF2 = c.calcularCosenoTF(p2,
						diccionario);
				double[]COS_TF_IDF2 = c.calcularCosenoTFIDF(p2,
						diccionario, idf);
				double[] PE_TF3 = c.calcularProdTF(p3, diccionario);
				double[]PE_TF_IDF3 = c.calcularProdTFIDF(p3,
						diccionario, idf);
				double[]COS_TF3 = c.calcularCosenoTF(p3,
						diccionario);
				double[]COS_TF_IDF3 = c.calcularCosenoTFIDF(p3,
						diccionario, idf);
				System.out.println("-- Producto Escalar TF -- \tQ1\tQ2\tQ3");
				for(int a = 0; a < 5; a++){
					System.out.println(nombreFicheros.get(a)+"\t\t"+PE_TF1[a]+"\t"+PE_TF2[a]+"\t"+PE_TF3[a] );
				}
				System.out.println("-- Producto Escalar TF-IDF -- \tQ1\t\t\tQ2\t\t\tQ3");
				for(int a = 0; a < 5; a++){
					System.out.println(nombreFicheros.get(a)+"\t\t"+PE_TF_IDF1[a]+"\t"+PE_TF_IDF2[a]+"\t"+PE_TF_IDF3[a] );
				}
				System.out.println("-- Producto Escalar TF-- \tQ1\t\t\tQ2\t\t\tQ3");
				for(int a = 0; a < 5; a++){
					System.out.println(nombreFicheros.get(a)+"\t\t"+COS_TF1[a]+"\t"+COS_TF2[a]+"\t"+COS_TF3[a] );
				}
				System.out.println("-- Producto Escalar TF-- \tQ1\t\t\tQ2\t\t\tQ3");
				for(int a = 0; a < 5; a++){
					System.out.println(nombreFicheros.get(a)+"\t\t"+COS_TF_IDF1[a]+"\t"+COS_TF_IDF2[a]+"\t"+COS_TF_IDF3[a] );
				}
				break;
			}
		}
		System.out.println("Fin del programa.");
	}

	// MÉTODO PARA FORMATEAR LA CONSULTA
	public static Map<String, Integer> consulta(String consulta) {
		CreadorDiccionario creador = new CreadorDiccionario();
		List<String> aux = new ArrayList<String>();
		consulta = consulta.toLowerCase();
		aux.add(consulta);
		aux = creador.limpiador(aux);
		ArrayList<String> palabras = creador.separador(aux);
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

	// MÉTODO PARA MOSTRAR LOS RESULTADOS POR PANTALLA
	public static void mostrarResultados(double[] r,ArrayList<String> nombreFicheros) {
		for (int i = 0; i < r.length; i++) {
			double aux1 = -1.0;
			int b = 0;
			for (int a = 0; a < r.length; a++) {
				if (r[a] > aux1) {
					aux1 = r[a];
					b = a;
				}
			}
			System.out.println("El documento " + nombreFicheros.get(b)
					+ " tiene una relevancia de " + aux1
					+ " sonbre la consulta realizada");
			r[b] = -10.0;
		}
	}
}
