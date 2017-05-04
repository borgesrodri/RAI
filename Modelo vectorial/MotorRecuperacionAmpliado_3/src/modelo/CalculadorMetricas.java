package modelo;

import java.util.ArrayList;
import java.util.Map.Entry;

import motor.CalcEvaluationMetrics;
import motor.Precision;
import motor.PrintResult;
import motor.Resultados;

import org.bson.Document;

public class CalculadorMetricas {
	
	public void calculador(ArrayList<String> relevancias, Document union, String conS, String topics){
		
		CalcEvaluationMetrics cem = new CalcEvaluationMetrics();
		Resultados result = new Resultados();
		Precision p = new Precision();
		PrintResult pr = new PrintResult();
		float r5=cem.calcRecall(union, relevancias, 1, 5);
		float p5=p.calcPrecision(union, relevancias, 1, 5);
		float f5=cem.calcFvalue(r5, p5);
		float r10=cem.calcRecall(union , relevancias , 1, 10);
		float p10=cem.calcPrecision(union , relevancias , 1, 10);
		float f10=cem.calcFvalue(r10, p10);
		float reciprocal1 = cem.calcReciprocalRank(union , relevancias , 1);
		float reciprocal2 = cem.calcReciprocalRank(union , relevancias , 2);
		float average=cem.calcAveragePrecision(union , relevancias , 1,100);
		result.setR5(cem.formatFloat(r5));
		result.setP5(cem.formatFloat(p5));
		result.setF5(cem.formatFloat(f5));
		result.setR10(cem.formatFloat(r10));
		result.setP10(cem.formatFloat(p10));
		result.setF10(cem.formatFloat(f10));
		result.setReciprocall1(cem.formatFloat(reciprocal1));
		result.setReciprocall2(cem.formatFloat(reciprocal2));
		result.setAverage(cem.formatFloat(average));
		result.setCalcnDCG(cem.formatArray(cem.calcnDCG(union , relevancias )));
		for (int k = 0; k < relevancias .size(); k++) {
			Document b = union ;
			for (Entry<String, Object> s: b.entrySet()) {
				if(s.getKey().equals(relevancias .get(k))){
					relevancias .add(k,relevancias .get(k)+" "+s.getValue());
					relevancias .remove(k+1);
				}
			}
		}
		pr.printFile(conS , topics , relevancias , result);

		
	}
}
