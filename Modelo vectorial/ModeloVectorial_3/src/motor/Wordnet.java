package motor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.lucene.wordnet.SynonymMap;

public class Wordnet {
	public String sinonimos(String args) {
		 String[] words = args.split(" ");
		 SynonymMap map = null;
		try {
			map = new SynonymMap(new FileInputStream("./wn_s.pl"));
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		 for (int i = 0; i < words.length; i++) {
		     String[] synonyms = map.getSynonyms(words[i]);
		     for (int j = 0; j < synonyms.length; j++) {
				args += " " + synonyms[j]; 
			}
		 }
		 System.out.println(args);
		 return args;
	}
}
