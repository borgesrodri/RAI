package motor;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class PrintResult {
	private float r5,r10,p5,p10,f5,f10,rr1,rr2,ap,ndcg10,ndcg100;
	private int count;
	public PrintResult(){
		r5=0;
		r10=0;
		p5=0;
		p10=0;
		f5=0;
		f10=0;	
		rr1=0;
		rr2=0;
		ap=0;
		ndcg10=0;
		ndcg100=0;
		count=0;
	}
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
				r5+=Float.parseFloat(r.getR5());
				pw.println("		Precision P@5	"+r.getP5());
				p5+=Float.parseFloat(r.getP5());
				pw.println("		F-valor 5	"+r.getF5());
				f5+=Float.parseFloat(r.getF5());
				pw.println("		Recall R@10	"+r.getR10());
				r10+=Float.parseFloat(r.getR10());
				pw.println("		Precision P@10	"+r.getP10());
				p10+=Float.parseFloat(r.getP10());
				pw.println("		F-valor 10	"+r.getF10());
				f10+=Float.parseFloat(r.getF10());
				pw.println("		ReciprocalRank rel 1	 "+r.getReciprocall1());
				rr1+=Float.parseFloat(r.getReciprocall1());
				pw.println("		ReciprocalRank rel 2	 "+r.getReciprocall2());
				rr2+=Float.parseFloat(r.getReciprocall2());
				pw.println("		AveragePrecision AP@100	"+r.getAverage());
				ap+=Float.parseFloat(r.getAverage());
				pw.println("		nDCG 10	"+r.getCalcnDCG().get(0));
				ndcg10+=Float.parseFloat(r.getCalcnDCG().get(0));
				pw.println("		nDCG 100	"+r.getCalcnDCG().get(1));
				ndcg10+=Float.parseFloat(r.getCalcnDCG().get(1));
				pw.close();
				count++;
			}
			else{
				System.out.println("No ha podido ser creado el fichero");
			}		
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} 
	}
	public void printAverage(){
		File fichero = new File("./resultados/TotalAverage" );
		PrintWriter pw = null;
		try {
			if (fichero.createNewFile()) {
				System.out.println("El fichero se ha creado correctamente");
				pw = new PrintWriter(fichero);
				pw.println("Average Recall R@5				"+r5/count);
				pw.println("------------*------------");
				pw.println("Average Precision P@5			"+p5/count);
				pw.println("------------*------------");
				pw.println("Average F-valor 5				"+f5/count);
				pw.println("------------*------------");
				pw.println("Average Recall R@10				"+r5/count);
				pw.println("------------*------------");
				pw.println("Average Precision P@10			"+p5/count);
				pw.println("------------*------------");
				pw.println("Average ReciprocalRank rel 1	"+rr1/count);
				pw.println("------------*------------");
				pw.println("Average ReciprocalRank rel 2	"+rr2/count);
				pw.println("------------*------------");
				pw.println("Average AveragePrecision AP@100	"+ap/count);
				pw.println("------------*------------");
				pw.println("Average nDCG 10					"+ndcg10/count);
				pw.println("------------*------------");
				pw.println("Average nDCG 100				"+ndcg100/count);
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
