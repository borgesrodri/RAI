package modelo;
import org.tartarus.snowball.ext.PorterStemmer;
public class Indizador {
	public Indizador(){
		
	}
	public String  stemTerm (String term) {
		PorterStemmer stem = new PorterStemmer();
		stem.setCurrent(term);
		stem.stem();
		String result = stem.getCurrent();
        return result;
	}
}
