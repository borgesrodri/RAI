package motor;

import java.util.ArrayList;
import java.util.Map.Entry;

import org.bson.Document;

public class Precision {
	private float precision;

	public float calcPrecision(Document relevantes, ArrayList<String> recuperados, int minRel, int cut) {
		Relevancia setrel = new Relevancia();
		Document aux = new Document();
		aux = setrel.minRel(relevantes, minRel);
		int p = 0;
		for (int i = 0; i < cut; i++) {
			for (Entry<String, Object> r : aux.entrySet()) {
				if (r.getKey().equals(recuperados.get(i))) {
					p++;
				}
			}
		}
		precision = (float) p / cut;
		return precision;
	}

	public float getPrecision() {
		return precision;
	}
}
