package motor;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Relevancia {
	public Relevancia(){
		
	}
	public Map<String, Integer> minRel(Map<String, Integer> relevantes, int i){
		Map <String, Integer> aux = new HashMap<String, Integer>();
		for (Entry<String, Integer> r: relevantes.entrySet()) {
			if(r.getValue()>=i)
				aux.put(r.getKey(), r.getValue());
	    }
		return(aux);
	}
}
