package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.data.Synset;
import net.sf.extjwnl.data.Word;
import net.sf.extjwnl.dictionary.Dictionary;

/**
 * This class offers basic NLP services like cleaning text, removing stop words,
 * 
 * @author absin
 */
public class NLPServices {
	/**
	 * Removes all the stop words like a, for, the etc. from the text. For a
	 * full list of stop words check
	 * <a href="file:../resources/stopWords.txt">/resources/stopWords.txt</a>
	 * 
	 * @return String free of all the stop-words
	 * @param text
	 *            The string to be cleaned, do null check at your end
	 */
	public String removeStopWords(String text) throws IOException {
		HashSet<String> stopWordsSet = getStopWords();
		String cleanedText = "";
		for (String word : text.split(" ")) {
			if (!stopWordsSet.contains(word.toLowerCase())) {
				cleanedText += word + " ";
			}
		}
		return cleanedText;
	}

	private HashSet<String> getStopWords() throws IOException {
		HashSet<String> stopWordsSet = new HashSet<String>();
		InputStream in = getClass().getResourceAsStream("/resources/stopWords.txt");
		Reader fr = new InputStreamReader(in, "utf-8");
		BufferedReader bufferedReader = new BufferedReader(fr);
		String line = "";
		while ((line = bufferedReader.readLine()) != null) {
			stopWordsSet.add(line);
		}
		return stopWordsSet;
	}

	/**
	 * Removes all the characters except the alphabet a-z, A-Z and 0-9 and also
	 * takes care of multiple spaces
	 * 
	 * @return A cleaned string
	 * @param text
	 *            The string to be cleaned, do null check at your end
	 */
	public String cleanText(String text) {
		return text.replaceAll("[^a-zA-Z0-9 ]", " ").replaceAll("\\s+", " ");
	}

	/**
	 * Returns a map with keys as the lemmatized version of words in the
	 * supplied text and values as a set of all the POSs the word corresponding
	 * to that lemma was used in the text
	 * 
	 * @return A map with keys as the lemmatized version of words in the
	 *         supplied text and values as a set of all the POSs the word
	 *         corresponding to that lemma
	 * 
	 * @param text
	 *            The string to be lemmatized with POS tags
	 */
	public HashMap<String, HashSet<String>> lemmatizerPOSTagger(String text) {
		HashMap<String, HashSet<String>> lemmatizedPOSTags = new HashMap<>();
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		Annotation document = new Annotation(text);
		pipeline.annotate(document);
		List<CoreLabel> tokens = document.get(TokensAnnotation.class);
		for (CoreLabel token : tokens) {
			String word = token.get(TextAnnotation.class);
			String lemma = token.get(LemmaAnnotation.class);
			String pos = token.get(PartOfSpeechAnnotation.class);
			/*
			 * System.out.print("Word: " + word); System.out.print(" pos: " +
			 * pos); System.out.print(" lemma: " + lemma); System.out.println();
			 */
			if (lemmatizedPOSTags.containsKey(lemma)) {
				HashSet<String> POSs = lemmatizedPOSTags.get(lemma);
				POSs.add(pos);
				lemmatizedPOSTags.remove(lemma);
				lemmatizedPOSTags.put(lemma, POSs);
			} else {
				HashSet<String> POSs = new HashSet<>();
				POSs.add(pos);
				lemmatizedPOSTags.put(lemma, POSs);
			}
		}
		return lemmatizedPOSTags;
	}

	public HashMap<String, Integer> performSoftComparision(HashMap<String, HashSet<String>> keyLemmas,
			HashMap<String, HashSet<String>> textLemmas) {
		HashMap<String, Integer> frequencyCount = new HashMap<>();
		Set<String> textLemmaWords = textLemmas.keySet();
		Dictionary dictionary = null;
		try {
			dictionary = Dictionary.getDefaultResourceInstance();
			HashMap<String, HashSet<POS>> keyWordNetSpecificLemmaPOSMap = getWordNetSpecificLemmaPOSMap(keyLemmas);
			for (String keyword : keyWordNetSpecificLemmaPOSMap.keySet()) {
				int keyWordFrequencyCount = 0;
				for (POS keyWordPOS : keyWordNetSpecificLemmaPOSMap.get(keyword)) {
					IndexWord keywordIndex = dictionary.lookupIndexWord(keyWordPOS, keyword);
					try {
						List<Synset> senses = keywordIndex.getSenses();
						for (Synset synset : senses) {
							List<Word> words = synset.getWords();
							for (Word word : words) {
								for (String textLemmaWord : textLemmaWords) {
									if (word.getLemma().toLowerCase().contains(textLemmaWord.toLowerCase())
											|| textLemmaWord.toLowerCase().contains(word.getLemma().toLowerCase())) {
										keyWordFrequencyCount++;
									}
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						for (String textLemmaWord : textLemmaWords) {
							if (keyword.equalsIgnoreCase(textLemmaWord) || keyword.toLowerCase().contains(textLemmaWord)
									|| textLemmaWord.toLowerCase().contains(keyword.toLowerCase())) {
								keyWordFrequencyCount++;
							}
						}
					}
				}
				frequencyCount.put(keyword, keyWordFrequencyCount);
			}
		} catch (JWNLException e) {
			e.printStackTrace();
		}
		return frequencyCount;
	}

	private HashMap<String, HashSet<POS>> getWordNetSpecificLemmaPOSMap(HashMap<String, HashSet<String>> keyLemmas) {
		HashMap<String, HashSet<POS>> wordNetLemmaMap = new HashMap<>();
		AdvancedNLPServices advancedNLPServices = new AdvancedNLPServices();
		HashMap<String, POS> coreNLPvsWordNetPOSMap = advancedNLPServices.getCoreNLPvsWordNetPOSMap();
		for (String lemma : keyLemmas.keySet()) {
			HashSet<POS> wordNetPOS = new HashSet<>();
			HashSet<String> coreNLPPOSs = keyLemmas.get(lemma);
			for (String coreNLPPOS : coreNLPPOSs) {
				if (coreNLPvsWordNetPOSMap.containsKey(coreNLPPOS)) {
					if (coreNLPvsWordNetPOSMap.get(coreNLPPOS) != null) {
						wordNetPOS.add(coreNLPvsWordNetPOSMap.get(coreNLPPOS));
					}
				}
			}
			if (wordNetPOS.size() > 0) {
				wordNetLemmaMap.put(lemma, wordNetPOS);
			}
		}
		return wordNetLemmaMap;
	}

}
