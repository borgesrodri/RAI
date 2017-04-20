package modelo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.bson.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class LectorMetricas {
	public Document leerUnion(){
		File dir = new File("./2010.union.trel");
		FileReader fr = null;
		BufferedReader br = null;
		Document doc = new Document("_id","union");
		try{
			fr= new FileReader(dir);
			br=new BufferedReader(fr);
			String ln;
			while((ln=br.readLine())!=null){
				String[] sp = ln.split("\t");
				doc.append(sp[0]+" "+sp[1], sp[2]);
			}
		}catch(Exception e){
			System.out.println(e);
		}
		return doc;
	}
	public Document leerTopics(){
		File fXmlFile = new File("./2010-topics.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		Document r = new Document("_id","topic");
		try{
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			org.w3c.dom.Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("topic");
			for(int temp = 0; temp < nList.getLength(); temp++){
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					String clave = eElement.getAttribute("id");
					String consulta = eElement.getElementsByTagName("title").item(0).getTextContent();
					r.append(clave, consulta);
				}
			}
		}catch(Exception e){
			 e.printStackTrace();
		}
		return r;
	}
	
	public void lecturas (){
		MongoClient client = new MongoClient( "localhost" , 27017);	
		MongoDatabase db = client.getDatabase("motor");
		MongoCollection<Document> con = db.getCollection("consultas");
		Document doc1 = leerUnion();
		Document doc2 = leerTopics();
		con.drop();
		con.insertOne(doc2);
		con.insertOne(doc1);
	}
}
