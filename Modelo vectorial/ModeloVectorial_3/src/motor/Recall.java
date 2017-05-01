package motor;

import java.util.ArrayList;
import java.util.Map.Entry;

import org.bson.Document;

public class Recall {
	private float rcall;
	public float calcRecall(Document relevantes, 
			ArrayList<String> recuperados, int minRel, int cut) {
		Relevancia setrel = new Relevancia();
		Document aux = new Document();
		aux = setrel.minRel(relevantes, minRel);
		int p = 0;
		for (Entry<String, Object> r : aux.entrySet()) {
			if(!r.getKey().equals("_id")){
			for (int i = 0; i < cut; i++) {
				if (r.getKey().equals(recuperados.get(i))) {
					p++;
				}
			}
			}
		}
		rcall=(float) p / aux.size();
		return rcall;
	}
	public float getRcall() {
		return rcall;
	}
}
