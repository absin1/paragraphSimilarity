package service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.text.similarity.CosineSimilarity;

import cosProduct.SampleDotProduct;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class TextSimilarityScore {
	public static void main(String args[]) {
		String standardText = "/Users/feroz/Documents/salesScript.txt";
		String sampleText = "/Users/feroz/Documents/sales1.txt";
		Double cosineSimilarity2 = new TextSimilarityScore().computeSimilarityScore(standardText, sampleText);
		System.out.println(cosineSimilarity2);
	}

	public Double computeSimilarityScore(String standardText, String sampleText) {
		CosineSimilarity cosineSimilarity = new CosineSimilarity();
		TextSimilarityScore score = new TextSimilarityScore();
		Map<CharSequence, Integer> standard = score.getVectorMap(standardText);
		Map<CharSequence, Integer> sample = score.getVectorMap(sampleText);
		Double cosineSimilarity2 = cosineSimilarity.cosineSimilarity(standard, sample);
		return cosineSimilarity2;
	}

	public Map<CharSequence, Integer> getVectorMap(String dirtyText) {
		Map<CharSequence, Integer> response = new HashMap<CharSequence, Integer>();
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		String text = cleanText(dirtyText);

		Annotation document = new Annotation(text);
		pipeline.annotate(document);
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		for (CoreMap sentence : sentences) {
			System.out.println("Sentence: " + sentence.toString());
			for (CoreMap token : sentence.get(TokensAnnotation.class)) {
				String word = token.get(TextAnnotation.class);
				String lemma = token.get(LemmaAnnotation.class);
				if (response.containsKey(lemma)) {
					int lemmacount = response.get(lemma);
					response.remove(lemma);
					response.put(lemma, ++lemmacount);
				} else {
					response.put(lemma, 1);
				}
			}
		}
		return response;
	}

	private String cleanText(String text) {
		try {
			return text.replaceAll("[^a-zA-Z0-9 ]", " ").replaceAll("\\s+", " ");
		} catch (Exception e) {
			return text;
		}
	}
}
