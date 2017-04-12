package modelo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.jsoup.Jsoup;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;

public class CreadorDiccionario {
	private static Document idf;
	String nombreHTML = "";
	//Constructores
	public CreadorDiccionario(){
		idf = new Document("_id", "IDF");
	}
	
	//Setters and getters	
	public static Document getIdf() {
		return idf;
	}

	public static void setIdf(Document idf) {
		CreadorDiccionario.idf = idf;
	}

	//Metodos
	//Este método es el encargado de leer todos los ficheros HTML que se encuentran en 
	//el direcctorio "./htmls/" y guardarlos en el array de nombreFicheros
	public void crearDiccionario() throws IOException{
		MongoClient client = new MongoClient( "localhost" , 27017);	
		MongoDatabase db = client.getDatabase("motor");
		MongoCollection<Document> dic = db.getCollection("diccionario");
		
		String ruta = "C:/Users/borge/Desktop/clase/2016-2017/Segundo cuatri/rai/Motor/2010-documents.biased/49";
		File dir = new File(ruta);
		File[] archivos = dir.listFiles();
		for(int i = 0; i < archivos.length; i++){
			File archivo = null;
		    FileReader fr = null;
		    BufferedReader br = null;
		    String fichero = "";
		      try {
		         archivo = new File (archivos[i].toString());
		         nombreHTML = archivos[i].toString().replace(".\\htmls\\", "");
		         fr = new FileReader (archivo);
		         br = new BufferedReader(fr);
		         // Lectura del fichero
		         String linea;
		         while((linea = br.readLine()) != null){
		        	 fichero += " " + linea.toLowerCase();
		         } 
		         fichero = limpiador(fichero);
		         String[] palabras = separador(fichero);
		         Document doc = contador(palabras);
		         dic.insertOne(doc);
		         
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
		//dic.insertOne(idf);
	}
	//Eliminar las etiquetas de html y dejar solo el texto plano
	public String limpiador(String html){
		html = html.replace(">", "> ");
		html = Jsoup.parse(html).text();
		html = html.replaceAll("[^a-zA-Z0-9 ]"," ");
		return html;
	}
	//Separar las palabras en String diferentes
	public String[] separador(String html){
		Indizador index = new Indizador();
		String[] aux = html.split(" ");
		for(int a = 0; a < aux.length; a++){
			aux[a] = index.stemTerm(aux[a]);
		}
		return aux;
	}
	//Guardar las palabras con su frecuencia
	public Document contador(String[] separadas){
		Document nameAndCount = new Document("_id", nombreHTML); // SI HAY HILOS BORRA ESTA LINEA
		for (String name : separadas) { 
		    if(!name.equals("") && name.length()>1){
		    if (!nameAndCount.containsKey(name) ){
		        nameAndCount.put(name, 1);
		        if(!idf.containsKey(name)){
		        	idf.put(name, 1);
		        }else { 
			        idf.append(name, idf.getInteger(name)+1);
			        
			    } 
		    } else { 
		        nameAndCount.put(name, nameAndCount.getInteger(name)+1); 
		    } 
		    }
		}
		return nameAndCount;
	}
	
}