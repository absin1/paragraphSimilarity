/**
 * 
 */
package service;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

/**
 * @author absin
 *
 */
public class CosineDifferenceServices {

	public double cosineSimilarity(double[] vectorA, double[] vectorB) {
		double dotProduct = 0.0;
		double normA = 0.0;
		double normB = 0.0;
		for (int i = 0; i < vectorA.length; i++) {
			dotProduct += vectorA[i] * vectorB[i];
			normA += Math.pow(vectorA[i], 2);
			normB += Math.pow(vectorB[i], 2);
		}
		return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
	}

	public SortedMap<String, Double> getTermFrequencyMap(String text) {
		SortedMap<String, Integer> dictionary = getDictionary(text);
		int totalUsefulWordCount = 0;
		for (String word : dictionary.keySet()) {
			totalUsefulWordCount += dictionary.get(word);
		}
		SortedMap<String, Double> tfDictionary = new TreeMap<>();
		for (String word : dictionary.keySet()) {
			tfDictionary.put(word, ((double) dictionary.get(word)) / (double) totalUsefulWordCount);
		}
		return tfDictionary;
	}

	public SortedMap<String, Integer> getDictionary(String text) {
		SortedMap<String, Integer> dictionary = new TreeMap<>();
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		Annotation document = new Annotation(text);
		pipeline.annotate(document);
		for (CoreMap token : document.get(TokensAnnotation.class)) {
			String lemma = token.get(LemmaAnnotation.class);
			if (dictionary.containsKey(lemma)) {
				int lemmacount = dictionary.get(lemma);
				dictionary.remove(lemma);
				dictionary.put(lemma, ++lemmacount);
			} else {
				dictionary.put(lemma, 1);
			}
		}
		return dictionary;
	}

	public SortedMap<String, Integer> getRelativeTermFrequencyMap(String text, Set<String> set) {
		SortedMap<String, Integer> relativeTermFrequencyMap = new TreeMap<>();
		SortedMap<String, Integer> dictionaryMap = getDictionary(text);
		for (String word : dictionaryMap.keySet()) {
			if (set.contains(word)) {
				if (relativeTermFrequencyMap.containsKey(word)) {
					int count = relativeTermFrequencyMap.get(word);
					relativeTermFrequencyMap.remove(word);
					relativeTermFrequencyMap.put(word, ++count);
				} else {
					relativeTermFrequencyMap.put(word, 1);
				}
			}
		}
		return relativeTermFrequencyMap;
	}

	public SortedMap<String, Double> convertAbsoluteFrequencyToRelativeFrequency(
			SortedMap<String, Integer> relativeTermFrequencyMap) {
		int totalWordCount = 0;
		for (String word : relativeTermFrequencyMap.keySet()) {
			totalWordCount += relativeTermFrequencyMap.get(word);
		}
		SortedMap<String, Double> relativeTermFrequencyMapRelative = new TreeMap<>();
		for (String word : relativeTermFrequencyMap.keySet()) {
			relativeTermFrequencyMapRelative.put(word,
					(double) relativeTermFrequencyMap.get(word) / (double) totalWordCount);
		}
		return relativeTermFrequencyMapRelative;
	}
}
