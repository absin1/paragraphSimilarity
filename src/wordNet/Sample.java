package wordNet;

import java.io.IOException;
import java.util.List;

import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.data.Synset;
import net.sf.extjwnl.data.Word;
import net.sf.extjwnl.dictionary.Dictionary;

public class Sample {
	public static void main(String args[]) throws JWNLException, IOException, CloneNotSupportedException {
		Dictionary dictionary = Dictionary.getDefaultResourceInstance();
		// IndexWord funny = dictionary.lookupIndexWord(POS.ADJECTIVE, "droll");
		IndexWord funny = dictionary.lookupIndexWord(POS.NOUN, "funny");
		List<Synset> senses = funny.getSenses();
		for (Synset synset : senses) {
			
			System.out.println(">>" + synset.getGloss());
			List<Word> words = synset.getWords();
			for (Word word : words) {
				System.out.println(word.getLemma());
			}
			System.out.println("----------");
		}

	}
}
