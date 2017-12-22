package cosProduct;

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

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class SampleDotProduct {
	public static void main(String[] args) {
		CosineSimilarity cosineSimilarity = new CosineSimilarity();
		Map<CharSequence, Integer> standard = getVectorMap("/Users/feroz/Documents/salesScript.txt");
		Map<CharSequence, Integer> sample = getVectorMap("/Users/feroz/Documents/sales1.txt");
		Double cosineSimilarity2 = cosineSimilarity.cosineSimilarity(standard, sample);
		System.out.println(cosineSimilarity2);
	}
	
	public static Map<CharSequence, Integer> getVectorMap(String pathname) {
		Map<CharSequence, Integer> response = new HashMap<CharSequence, Integer>();
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		String text = "";
		try {
			text = cleanText(getTextFromFile(pathname));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	private static String getTextFromFile(String pathname) throws IOException {
		File file = new File(pathname);
		Reader in = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(in);
		String line;
		String text = "";
		while ((line = bufferedReader.readLine()) != null) {
			text += line;
		}
		return text;
	}

	private static String cleanText(String text) {
		return text.replaceAll("[^a-zA-Z0-9 ]", " ").replaceAll("\\s+", " ");
	}
}
