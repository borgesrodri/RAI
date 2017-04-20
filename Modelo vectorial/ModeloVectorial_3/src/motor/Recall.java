package motor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Recall {
	private float rcall;
	public float calcRecall(Map<String, Integer> relevantes, 
			ArrayList<String> recuperados, int minRel, int cut) {
		Relevancia setrel = new Relevancia();
		Map<String, Integer> aux = new HashMap<String, Integer>();
		aux = setrel.minRel(relevantes, minRel);
		int p = 0;
		for (Entry<String, Integer> r : aux.entrySet()) {
			for (int i = 0; i < cut; i++) {
				if (r.getKey().equals(recuperados.get(i))) {
					p++;
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
