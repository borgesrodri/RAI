package modelo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;

public class CreadorDiccionario {
	private static ArrayList<List<String>> ficheros;
	private static ArrayList<String> nombreFicheros;
	private static Map<String, Integer> idf;
	//Constructores
	public CreadorDiccionario(){
		ficheros = new ArrayList<List<String>>();
		nombreFicheros = new ArrayList<String>();
		idf = new HashMap<>();
	}
	
	//Setters and getters
	public static ArrayList<List<String>> getFicheros() {
		return ficheros;
	}


	public static void setFicheros(ArrayList<List<String>> ficheros) {
		CreadorDiccionario.ficheros = ficheros;
	}

	public static ArrayList<String> getNombreFicheros() {
		return nombreFicheros;
	}

	public static void setNombreFicheros(ArrayList<String> nombreFicheros) {
		CreadorDiccionario.nombreFicheros = nombreFicheros;
	}
	
	public static Map<String, Integer> getIdf() {
		return idf;
	}

	public static void setIdf(Map<String, Integer> idf) {
		CreadorDiccionario.idf = idf;
	}

	//Metodos
	//Este método es el encargado de leer todos los ficheros HTML que se encuentran en 
	//el direcctorio "./htmls/" y guardarlos en el array de nombreFicheros
	public void leerFicherosHtml() throws IOException{
		String ruta = "./htmls/";
		File dir = new File(ruta);
		File[] archivos = dir.listFiles();
		for(int i = 0; i < archivos.length; i++){
			File archivo = null;
		    FileReader fr = null;
		    BufferedReader br = null;
		    List<String> fichero = null;
		      try {
		         archivo = new File (archivos[i].toString());
		         nombreFicheros.add(archivos[i].toString().replace(".\\htmls\\", ""));
		         fr = new FileReader (archivo);
		         br = new BufferedReader(fr);
		         fichero = new ArrayList<String>();
		         // Lectura del fichero
		         String linea;
		         while((linea = br.readLine()) != null){
		        	 fichero.add(linea.toLowerCase());
		         } 
		         ficheros.add(fichero); 
		      }
		      catch(Exception e){
		         e.printStackTrace();
		      }finally{
		         try{                    
		            if( null != fr ){   
		               fr.close();     
		            }                  
		         }catch (Exception e2){ 
		            e2.printStackTrace();
		         }
		      }
		}
	}
	//Eliminar las etiquetas de html y dejar solo el texto plano
	public List<String> limpiador(List<String> html){
		List<String> result = new ArrayList<String>();
		for(int i = 0; i < html.size(); i++){
			String linea = html.get(i);
			linea = linea.replace(">", "> ");
			String palabras = Jsoup.parse(linea).text();
			result.add(palabras.replaceAll("[^a-zA-Z0-9 ]"," "));
		}
		return result;
	}
	//Separar las palabras en String diferentes
	public ArrayList<String> separador(List<String> texto){
		ArrayList<String> palabras = new ArrayList<String>();;
		for(int i = 0; i < texto.size(); i++){
			String[] aux = texto.get(i).split(" ");
			for(int a = 0; a < aux.length; a++){
				palabras.add(aux[a]);
			}
			
		}
		
		return palabras;
	}
	//Guardar las palabras con su frecuencia
	public Map<String, Integer> contador(ArrayList<String> separadas){
		Map<String, Integer> nameAndCount = new HashMap<>(); // build hash table with count 
		for (String name : separadas) { 
		    if(!name.equals("") && name.length()>1){
		    if (!nameAndCount.containsKey(name) ){
		        nameAndCount.put(name, 1);
		        if(!idf.containsKey(name)){
		        	idf.put(name, 1);
		        }else { 
			        idf.put(name, idf.get(name)+1);
			        
			    } 
		    } else { 
		        nameAndCount.put(name, nameAndCount.get(name)+1); 
		    } 
		    }
		}
		return nameAndCount;
	}
	
	public ArrayList<Map<String,Integer>> crearDiccionario() throws IOException{
		ArrayList<Map<String,Integer>> diccionario = new ArrayList<Map<String,Integer>>();
		leerFicherosHtml();
		for(int i =0; i < ficheros.size(); i++){
			List<String> linea = limpiador(ficheros.get(i));
			ArrayList<String> separadas = separador(linea);
			
			diccionario.add(contador(separadas));
		}
		return diccionario;
	}
}
