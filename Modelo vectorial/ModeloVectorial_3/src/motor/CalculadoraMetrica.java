package motor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class CalculadoraMetrica {
	private static ArrayList<String> recuperados;
	private static Map<String, Integer> relevantes_totales;
	private static Map<String, Integer> relevantes;
	private static float recall;
	private static float precision;
	private static float frecall;
	private static float fprecision;
	
	public static void main(String[] args) {
		Map <String, Integer> rel = new HashMap<String, Integer>();
		rel.put("A",1);
		rel.put("B",2);
		rel.put("D",3);
		rel.put("E",3);
		rel.put("G",3);
		rel.put("H",3);
		rel.put("J",3);
		rel.put("L",3);
		rel.put("N",3);
		rel.put("Q",3);
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

		Recall r = new Recall();
		float r5=r.calcRecall(rel, rec, 0, 5);
		Precision p = new Precision();
		float p5=p.calcPrecision(rel, rec, 0, 5);
		Fvalue f =new Fvalue();
		float f5=f.calcFvalue(r, p);
		float r10=r.calcRecall(rel, rec, 0, 10);
		float p10=p.calcPrecision(rel, rec, 0, 10);
		System.out.println("Recall R@5 "+ r5);
		float f10=f.calcFvalue(r, p);
		System.out.println("Precision P@5 "+ p5);
		System.out.println("F-valor F@5 "+ f5);
		System.out.println("Recall R@5 "+ r10);
		System.out.println("Precision P@5 "+ p10);
		System.out.println("F-valor F@5 "+ f10);
	}	
	
	/*public CalculadoraMetrica(){
		
	}
	public static void setMaps(ArrayList<String> recuperados, Map<String,Integer>relevantes){
		//this.setRecuperados(recuperados);
		//this.setRelevantes(relevantes);
		setRecuperados(recuperados);
		setRelevantes(relevantes);
		setRelevantes_totales(relevantes);
	}
	private static void calcPrecisionAndRecall(){
		int p=0;
		 for (Entry<String, Integer> r: relevantes.entrySet()) {
		        for(int i=0;i<recuperados.size();i++){
		        	if(r.getKey().equals(recuperados.get(i))){
		        		p++;
		        	}
		        }
		    }
		 precision=(float)p/recuperados.size();
		 recall=(float)p/relevantes.size();
	}
	private static float calcFvalue(){
		float f;
		f=(2*recall*precision)/(recall+precision);
		return f;
	}
	public ArrayList<String> getRecuperados() {
		return recuperados;
	}
	public static void setRecuperados(ArrayList<String> rec) {
		//this.recuperados = recuperados;
		recuperados=rec;
	}
	public Map<String, Integer> getRelevantes() {
		return relevantes;
	}
	public static void setRelevantes(Map<String, Integer> rel) {
		//this.relevantes = relevantes;
		relevantes=rel;
	}
	public static Map<String, Integer> getRelevantes_totales() {
		return relevantes_totales;
	}

	public static void setRelevantes_totales(Map<String, Integer> relevantes_totales) {
		CalculadoraMetrica.relevantes_totales = relevantes_totales;
	}
	public static float pCutOff(int c){
		float pcutt;
		int p=0;
		for (Entry<String, Integer> r: relevantes.entrySet()) {
	        for(int i=0;i<c;i++){
	        	if(r.getKey().equals(recuperados.get(i))){
	        		p++;
	        	}
	        }
	    }
		pcutt=(float)p/c;
		fprecision=pcutt;
		return pcutt;
	}
	public static float rCutOff(int c){
		float pcutt;
		int p=0;
		for (Entry<String, Integer> r: relevantes.entrySet()) {
	        for(int i=0;i<c;i++){
	        	if(r.getKey().equals(recuperados.get(i))){
	        		p++;
	        	}
	        }
	    }
		pcutt=(float)p/relevantes.size();
		frecall=pcutt;
		return pcutt;
	}
	public static void minRel(int i){
		Map <String, Integer> aux = new HashMap<String, Integer>();
		for (Entry<String, Integer> r: relevantes_totales.entrySet()) {
			if(r.getValue()>=i)
				aux.put(r.getKey(), r.getValue());
	    }
		relevantes=aux;
	}

*/
}
