package motor;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import edu.smu.tspell.wordnet.*;


public class Wordnet {
	private InputStream model;
	private POSTaggerME _posTagger;

	public Wordnet() {

		try {
			// Loading tokenizer model
			model = getClass().getResourceAsStream("/en-pos-maxent.bin");
			final POSModel posModel = new POSModel(model);
			model.close();

			_posTagger = new POSTaggerME(posModel);

		} catch (final IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (model != null) {
				try {
					model.close();
				} catch (final IOException e) {
				} // oh well!
			}
		}

	}

	public Map<String, Boolean> expandir(String[] consulta, String pos[]) {
		//String palabra - Boolean si es polisémica
		Map<String,Boolean> exp = new HashMap<String,Boolean>();
		WordNetDatabase database = WordNetDatabase.getFileInstance();
		System.setProperty("wordnet.database.dir", ".\\dict\\");		
		for (int i = 0; i < consulta.length; i++) {
			exp.put(consulta[i],false);
			if(pos[i].equals("NOUN")){
				Synset[] synsets = (Synset[]) database.getSynsets(consulta[i], SynsetType.NOUN);
				if (synsets.length > 1 && synsets.length <= 3) {
					for (int j = 0; j < synsets.length; j++)
					{
						String[] wordForms = synsets[j].getWordForms();
						for (int k = 0; k < wordForms.length; k++) {
							if (!exp.containsKey(wordForms[j]))exp.put(wordForms[k],true);
						}
					}
				}else{
					if (synsets.length == 1) {
						for (int j = 0; j < synsets.length; j++)
						{
							String[] wordForms = synsets[j].getWordForms();
							for (int k = 0; k < wordForms.length; k++) {
								if (!exp.containsKey(wordForms[j]))exp.put(wordForms[k],false);
							}
						}
					}
				}
			}
		}
		 
		return exp;

		
	}

	public String[] getPOS(String[] palabras) {
		String[] vuelta = _posTagger.tag(palabras);
		return convertSynseType(vuelta);
	}

	private String[] convertSynseType(String[] palabras) {
		String[] transform = new String[palabras.length];
		for (int i = 0; i < palabras.length; i++) {
			if (palabras[i].equals("NN") || palabras[i].equals("NNS")
					|| palabras[i].equals("NNP") || palabras[i].equals("NNPS")) {
				transform[i] = "NOUN";
			} else {
				transform[i] = "No";
			}
		}
		return transform;
	}
}
