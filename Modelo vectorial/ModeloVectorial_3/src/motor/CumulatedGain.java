package motor;

import java.util.ArrayList;
import java.util.Map.Entry;

import org.bson.Document;

public class CumulatedGain {
	public ArrayList<Float> calcNDCG(Document relevantes, 
			ArrayList<String> recuperados, int minRel) {
		Document aux = new Document();
		Relevancia setrel = new Relevancia();
		aux = setrel.minRel(relevantes, minRel);
		ArrayList<Float> dcg = dcg(aux,recuperados);
		ArrayList<Float> dcgi = dcgIdeal(aux,recuperados);
		ArrayList<Float> ndcg = new ArrayList<Float>();
		for(int i = 0;i<dcgi.size();i++){
			ndcg.add(dcg.get(i)/dcgi.get(i));
		}
		return (ndcg);
	} 
	private static ArrayList<Float> dcg(Document relevantes, 
			ArrayList<String> recuperados){
		ArrayList<Integer> g = new ArrayList<Integer>();
		boolean b=false;
		for(int i = 0;i<recuperados.size();i++){
			b=false;
			for (Entry<String, Object> r : relevantes.entrySet()) {
				if(r.getKey().equals(recuperados.get(i))){
					b=true;
					g.add((Integer) r.getValue());
				}
			}
			if(!b)
				g.add(0);
		}
		ArrayList<Integer> cg = new ArrayList<Integer>();
		for(int i=0;i<g.size();i++){
			if(i<1){
				cg.add(g.get(i));
			}else{
				cg.add(g.get(i)+cg.get(i-1));
			}
		}
		ArrayList<Float> dcg = new ArrayList<Float>();
		for(int i=0;i<g.size();i++){
			if(i<2){
				dcg.add((float)cg.get(i));
			}else{
				float d =log(i+1,2);
				dcg.add((float)(dcg.get(i-1)+g.get(i)/d));
			}
		}
		return (dcg);
	}
	private static ArrayList<Float>dcgIdeal(Document relevantes, 
			ArrayList<String> recuperados){
		Document aux = new Document();
		aux=orderDocument(relevantes);
		ArrayList<Integer> g = new ArrayList<Integer>();
		for (Entry<String, Object> r : aux.entrySet()) {
				g.add((Integer) r.getValue());
		}
		ArrayList<Integer> cg = new ArrayList<Integer>();
		for(int i=0;i<g.size();i++){
			if(i<1){
				cg.add(g.get(i));
			}else{
				cg.add(g.get(i)+cg.get(i-1));
			}
		}
		ArrayList<Float> dcg = new ArrayList<Float>();
		for(int i=0;i<g.size();i++){
			if(i<2){
				dcg.add((float)cg.get(i));
			}else{
				float d =log(i+1,2);
				dcg.add((float)(dcg.get(i-1)+g.get(i)/d));
			}
		}
		return (dcg);
		
	}
	private static Document orderDocument (Document d){
		Document aux=new Document();
		for (Entry<String, Object> r : d.entrySet()) {
			if((int)r.getValue()==3){
				aux.append(r.getKey(), r.getValue());
			}
		}
		for (Entry<String, Object> r : d.entrySet()) {
			if((int)r.getValue()==2){
				aux.append(r.getKey(), r.getValue());
			}
		}
		for (Entry<String, Object> r : d.entrySet()) {
			if((int)r.getValue()==1){
				aux.append(r.getKey(), r.getValue());
			}
		}
		for (Entry<String, Object> r : d.entrySet()) {
			if((int)r.getValue()==0){
				aux.append(r.getKey(), r.getValue());
			}
		}
		for (Entry<String, Object> r : d.entrySet()) {
			if((int)r.getValue()==-1){
				aux.append(r.getKey(), r.getValue());
			}
		}
		return (aux);
	}
	private static float log(float x, int base)
	{
	    return (float) (Math.log(x) / Math.log(base));
	}
}
