package motor;

import java.util.ArrayList;
import java.util.Map.Entry;

import org.bson.Document;

public class AveragePrecision {
	public float calcAveragePrecision(Document relevantes, 
			ArrayList<String> recuperados, int minRel){
		float aprecision=0;
		ArrayList<Float> average = new ArrayList<Float>();
		Relevancia setrel = new Relevancia();
		Document aux = new Document();
		aux = setrel.minRel(relevantes, minRel);
		float p = 0,order=1;
		for (Entry<String, Object> r : aux.entrySet()) {
			for (int i = 0; i < recuperados.size(); i++) {
				if (r.getKey().equals(recuperados.get(i))) {
					p=i+1;
					average.add((float) (order/p));
					order++;
				}
			}
		}
		for(int i=0; i<average.size();i++){
			aprecision=aprecision+average.get(i);
		}
		aprecision=aprecision/average.size();
		return aprecision;
	}
}