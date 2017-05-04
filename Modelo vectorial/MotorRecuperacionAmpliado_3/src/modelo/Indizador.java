package modelo;
import org.tartarus.snowball.ext.PorterStemmer;
public class Indizador {
	public Indizador(){
		
	}
	/*public String  stemTerm (String term) {
		PorterStemmer stem = new PorterStemmer();
		stem.setCurrent(term);
		stem.stem();
		String result = stem.getCurrent();
        return result;
	}*/
	public String stemTerm(String term){
		if(term.endsWith("ed")){
			return term.substring(0, term.length()-1);
		}
		if(term.endsWith("ing")){
			return term.substring(0, term.length()-3);
		}
		if(term.endsWith("s")){
			return term.substring(0, term.length()-1);
		}
		return term;
	}
}