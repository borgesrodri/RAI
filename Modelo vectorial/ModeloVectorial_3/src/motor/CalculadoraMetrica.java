package motor;

import java.util.ArrayList;
import org.bson.Document;

public class CalculadoraMetrica {

	public static void main(String[] args) {
		Document  rel = new Document();
		rel.put("A",2);
		rel.put("B",3);
		rel.put("D",3);
		rel.put("E",1);
		rel.put("G",2);
		rel.put("H",2);
		rel.put("J",1);
		rel.put("L",1);
		rel.put("N",2);
		rel.put("Q",1);
		ArrayList<String> rec = new ArrayList<String>();
		rec.add("A");
		rec.add("B");
		rec.add("C");
		rec.add("D");
		rec.add("E");
		rec.add("F");
		rec.add("G");
		rec.add("H");
		rec.add("I");
		rec.add("J");
		rec.add("K");
		rec.add("L");
		rec.add("M");
		rec.add("N");
		rec.add("O");
		rec.add("P");
		rec.add("Q");
		rec.add("R");
		rec.add("S");
		rec.add("T");
		CalcEvaluationMetrics cem = new CalcEvaluationMetrics();
		float r5=cem.calcRecall(rel, rec, 0, 5);
		Precision p = new Precision();
		float p5=p.calcPrecision(rel, rec, 0, 5);
		float f5=cem.calcFvalue(r5, p5);
		float r10=cem.calcRecall(rel, rec, 0, 10);
		float p10=cem.calcPrecision(rel, rec, 0, 10);
		float f10=cem.calcFvalue(r10, p10);
		System.out.println("Recall R@5 "+ cem.formatFloat(r5));
		System.out.println("Precision P@5 "+ cem.formatFloat(p5));
		System.out.println("F-valor F@5 "+ cem.formatFloat(f5));
		System.out.println("Recall R@10 "+ cem.formatFloat(r10));
		System.out.println("Precision P@10 "+cem.formatFloat(p10));
		System.out.println("F-valor F@10 "+ cem.formatFloat(f10));
		float reciprocal = cem.calcReciprocalRank(rel, rec, 1);
		System.out.println("ReciprocalRank 1 "+ cem.formatFloat(reciprocal));
		reciprocal = cem.calcReciprocalRank(rel, rec, 2);
		System.out.println("ReciprocalRank 2 "+ cem.formatFloat(reciprocal));
		float average=cem.calcAveragePrecision(rel, rec, 1,100);
		System.out.println("Average Precision "+ cem.formatFloat(average));
		System.out.println(cem.formatArray(cem.calcnDCG(rel, rec)));
	}	

}
