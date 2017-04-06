package modelo;

import java.util.ArrayList;
import java.util.Map;

public class Calculador {

	public Calculador(){
		
	}
	//MÉTODOS PARA EL CÁLCULO DE LAS RELEVANCIAS
		public double[] calcularProdTF(Map<String, Integer> pesos, ArrayList<Map<String,Integer>> diccionario ){
			double[] relevancia = new double[diccionario.size()];
			for (int i = 0; i < relevancia.length; i++) {
				relevancia[i] = 0.0;
			}
			//Probamos cada palabra de la consulta en todos los documentos
			for (Map.Entry<String, Integer> entry : pesos.entrySet()) {
			    String palabra = entry.getKey();
			    //Si la palabra se encuentra en un documento se suma su relevancia a la relevancia de la consulta || R = R + (TF * QF) ||
			    for(int i = 0; i < relevancia.length;i++){
			    	if(diccionario.get(i).get(palabra)!=null)
			    	relevancia[i] += diccionario.get(i).get(palabra)* entry.getValue();
			    }
			}
			return relevancia;	
		}
		
		public double[] calcularProdTFIDF(Map<String, Integer> consulta, ArrayList<Map<String,Integer>> diccionario, Map<String, Double> idf){
			double [] relevancia = new double[diccionario.size()];
			for (int i = 0; i < relevancia.length; i++) {
				relevancia[i] = 0.0;
			}
			//Probamos cada palabra de la consulta en todos los documentos
			for (Map.Entry<String, Integer> entry : consulta.entrySet()) {
			    String palabra = entry.getKey();
			    //Si la palabra se encuentra en un documento se suma su relevancia a la relevancia de la consulta || R = R + (TF * QF) ||
			    for(int i = 0; i < relevancia.length;i++){
			    	if(diccionario.get(i).get(palabra)!=null && idf.get(palabra)!= null){
			    	relevancia[i] += ((diccionario.get(i).get(palabra)*idf.get(palabra))*(consulta.get(palabra)*idf.get(palabra)));
			    	}
			    }
			}
			return relevancia;
		}

		public double[] calcularCosenoTF (Map<String, Integer> pesos, ArrayList<Map<String,Integer>> diccionario){
			double [] relevancia = calcularProdTF(pesos, diccionario);
			double p = 0.0;
			for (Map.Entry<String, Integer> entry : pesos.entrySet()) {
			    p += Math.pow(entry.getValue(), 2);
			}
			for(int i = 0; i < relevancia.length;i++){
				double d = 0.0;
		    	for(Map.Entry<String, Integer> entry : diccionario.get(i).entrySet()){
		    		d += Math.pow(entry.getValue(), 2);
		    	}
		    	relevancia[i] = relevancia[i] / (Math.sqrt(p*d));
		    }
			return relevancia;
		}
		public double[] calcularCosenoTFIDF(Map<String, Integer> pesos, ArrayList<Map<String,Integer>> diccionario,Map<String, Double> idf){
			double [] relevancia = calcularProdTFIDF(pesos, diccionario, idf);
			double p = 0.0;
			for (Map.Entry<String, Integer> entry : pesos.entrySet()) {
				if(idf.get(entry.getKey())!= null)
			    p += Math.pow(entry.getValue()*idf.get(entry.getKey()), 2);
			}
			for(int i = 0; i < relevancia.length;i++){
				double d = 0.0;
		    	for(Map.Entry<String, Integer> entry : diccionario.get(i).entrySet()){
		    		if(idf.get(entry.getKey())!= null)
		    		d += Math.pow(entry.getValue()*idf.get(entry.getKey()), 2);
		    	}
		    	if(Math.sqrt(p*d)!=0)
		    	relevancia[i] = relevancia[i] / (Math.sqrt(p*d));
		    	else
		    		relevancia[i] = 0.0;
		    }
			return relevancia;
		}
}
