package motor;

import java.util.ArrayList;
import org.bson.Document;

public class AveragePrecision {
	public float calcAveragePrecision(Document relevantes, ArrayList<String> recuperados, int minRel, int cut) {
		float aprecision = 0;
		float average = 0.0f;
		Precision p = new Precision();
		int count = 0;
		for (int i = 1; i <= cut; i++) {
			float a = p.calcPrecision(relevantes, recuperados, minRel, i);
			if(a != 0.0 && !Float.isInfinite(a) && !Float.isNaN(a)){
			average += a;
			count ++;
			}
		}
		aprecision = average / count;
		return aprecision;
	}
}
