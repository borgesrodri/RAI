package motor;


import java.util.Map.Entry;

import org.bson.Document;

public class Relevancia {
	public Relevancia(){
		
	}
	public Document minRel(Document relevantes, int i){
		Document aux = new Document();
		for (Entry<String, Object> r: relevantes.entrySet()) {
				if(Integer.parseInt(r.getValue().toString())>=i)
					aux.put(r.getKey(), Integer.parseInt(r.getValue().toString()));

			
	    }
		return(aux);
	}
}
