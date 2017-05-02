package motor;

import java.util.ArrayList;
import java.util.Map.Entry;

import org.bson.Document;

public class ReciprocalRank {
	public float calcReciprocalRank(Document relevantes, 
			ArrayList<String> recuperados, int minRel){
		float rrank;
		Relevancia setrel = new Relevancia();
		Document aux = new Document();
		aux = setrel.minRel(relevantes, minRel);
		boolean first=true;
		int p = 0;
		for (Entry<String, Object> r : aux.entrySet()) {
			for (int i = 0; i < recuperados.size()&&first; i++) {
				if (r.getKey().equals(recuperados.get(i))&&first) {
					p=i+1;
					first=false;
				}
			}
		}
		rrank=(float) 1 / p;
		return rrank;
	}

}
