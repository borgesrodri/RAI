package motor;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.bson.Document;

public class CalcEvaluationMetrics {
	private DecimalFormat decimals = new DecimalFormat("0.0000");
	public float calcPrecision(Document relevantes, 
			ArrayList<String> recuperados, int minRel, int cut){
		Precision p = new Precision();
		float r=p.calcPrecision(relevantes, recuperados, minRel, cut);
		//String s=decimals.format(r);
		//r=Float.parseFloat(s);
		return r;
	}
	public float calcRecall(Document relevantes, 
			ArrayList<String> recuperados, int minRel, int cut){
		Recall p = new Recall();
		float r=p.calcRecall(relevantes, recuperados, minRel, cut);
		//String s=decimals.format(r);
		//r=Float.parseFloat(s);
		return r;
	}
	public float calcFvalue(float recall, float precision){
		Fvalue f =new Fvalue();
		float r=f.calcFvalue(recall, precision);
		//String s=decimals.format(r);
		//r=Float.parseFloat(s);
		return r;
	}
	public float calcReciprocalRank(Document relevantes, 
			ArrayList<String> recuperados, int minRel){
		ReciprocalRank rr = new ReciprocalRank();
		float r = rr.calcReciprocalRank(relevantes, recuperados, minRel);
		//String s=decimals.format(r);
		//r=Float.parseFloat(s);
		return r;
	}
	public float calcAveragePrecision(Document relevantes, 
			ArrayList<String> recuperados, int minRel,int cut){
		AveragePrecision ap = new AveragePrecision();
		float r=ap.calcAveragePrecision(relevantes, recuperados, minRel,cut);
		//String s=decimals.format(r);
		//r=Float.parseFloat(s);
		return r;
	}
	public ArrayList<Float> calcnDCG(Document relevantes, 
			ArrayList<String> recuperados){
		CumulatedGain cg = new CumulatedGain();
		ArrayList<Float> r=cg.calcNDCG(relevantes, recuperados, -1);
		/*for(int i=0;i<r.size();i++){
			String s=decimals.format(r.get(i));
			//r.set(i, Float.parseFloat(s));
		}*/
		return r;
	}
	public String formatFloat(float f){
		return decimals.format(f);
	}
	public ArrayList<String> formatArray(ArrayList<Float> a){
		ArrayList<String> r =new ArrayList<String>();
		for(int i=0;i<a.size();i++){
			r.add(decimals.format(a.get(i)));
		}
		return r;
	}
}
