package modelo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;


import org.bson.Document;
import org.jsoup.Jsoup;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class CreadorDiccionario {
	private static Document idf;
	String nombreHTML = "";
	//Constructores
	public CreadorDiccionario(){
		idf = new Document("_id", "IDF");
	}
	

	//Metodos
	//Este método es el encargado de leer todos los ficheros HTML que se encuentran en 
	//el direcctorio "./htmls/" y guardarlos en el array de nombreFicheros
	public void crearDiccionario() throws IOException{
		MongoClient client = new MongoClient( "localhost" , 27017);	
		MongoDatabase db = client.getDatabase("motor");
		MongoCollection<Document> dic = db.getCollection("diccionario");
		MongoCollection<Document> idfColl = db.getCollection("idfColl");
		for (int a = 0; a < 100; a++) {
			String ruta = "";
			if(a<10){
				ruta = "./html/0"+a;
			}else{
				ruta = "./html/"+a;
			}
			File dir = new File(ruta);
			File[] archivos = dir.listFiles();
			for(int i = 0; i < archivos.length; i++){
				File archivo = null;
			    FileReader fr = null;
			    BufferedReader br = null;
			    String fichero = "";
			      try {
			         archivo = new File (archivos[i].toString());
			         if(a<10){
			        	 nombreHTML = archivos[i].toString().replace(".\\html\\0"+a+"\\", ""); 
						}else{
						 nombreHTML = archivos[i].toString().replace(".\\html\\"+a+"\\", ""); 
						}
			         nombreHTML = nombreHTML.replace(".html", "");
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
		}
		int i = 0;
		for (Map.Entry<String, Object> entry : idf.entrySet()) {
			if(i !=0 ){
				int a = (int) entry.getValue();
				long b = dic.count();
				double log = Math.log10(((double) b / (double) a));
				idf.append(entry.getKey(), log);
			}
			i = 1;
		}
		idfColl.insertOne(idf);
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
		        	idf.append(name, 1);
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