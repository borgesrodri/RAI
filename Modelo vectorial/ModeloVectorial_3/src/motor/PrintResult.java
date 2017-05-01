package motor;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class PrintResult {
	public void printFile(String q, String id, ArrayList<String> recuperados,Resultados r) {
		File fichero = new File("./resultados/" + id);
		PrintWriter pw = null;
		try {
			if (fichero.createNewFile()) {
				System.out.println("El fichero se ha creado correctamente");
				pw = new PrintWriter(fichero);
				pw.println(q);
				for (int i = 0; i < recuperados.size(); i++)
	                pw.println("	" + recuperados.get(i));
				pw.println("		Recall R@5	"+r.getR5());
				pw.println("		Precision P@5	"+r.getP5());
				pw.println("		F-valor 5	"+r.getF5());
				pw.println("		Recall R@10	"+r.getR10());
				pw.println("		Precision P@10	"+r.getP10());
				pw.println("		ReciprocalRank rel 1	 "+r.getReciprocall1());
				pw.println("		ReciprocalRank rel 2	 "+r.getReciprocall2());
				pw.println("		AveragePrecision AP@100	"+r.getAverage());
				pw.println("		nDCG	"+r.getCalcnDCG());
				pw.close();
			}
			else{
				System.out.println("No ha podido ser creado el fichero");
			}		
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} 
	}
}
