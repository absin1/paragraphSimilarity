package manualCosProduct;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class ManualCosProduct {
	public static void main(String[] args) throws IOException {
		String text = "Hello sir, I m Richa calling from MotoMax Insurance. Your car’s insurance is scheduled to expire in forty five days.  Do you have a couple of minutes to discuss your policy renewal We at MotoMax have an extremely competitive policy offering with unique features like roadside assist, free towing support and post accident health insurance coverage. Could I ask you a few questions so I can give you a quick estimate Can you confirm the model and vehicle details Can you confirm the date of purchase The suggested IDV is Rupees ten lakhs. Can I make a quotation on this basis Have you claimed insurance for any accident in the last year Could I have your email ID to send you the quotation I’ll repeat it please confirm if I’ve got it down correctly The premium is rupees five hundred Can I help you complete the processing now with Online if you have your credit card available";
		ManualCosProduct cosProduct = new ManualCosProduct();
		SortedMap<String, Double> baseDictionary = cosProduct.getSortedTreeMap(text);
		String userText = "Hi sir, I m Richa calling from MotoMax Insurance. Your auto's protection is planned to expire in forty five days.  Do you have two or three minutes to talk about your approach restoration We at MotoMax have a to a great degree focused strategy offering with remarkable highlights like roadside help, free towing help and post mishap medical coverage scope. Would I be able to pose a couple of inquiries so I can give you a fast gauge Can you affirm the model and vehicle subtle elements Can you affirm the date of procurement The recommended IDV is Rupees ten lakhs. Would i be able to influence a citation on this premise To have you guaranteed protection for any mischance in the most recent year Could I have your email ID to send you the citation I'll rehash it please affirm on the off chance that I have it down effectively The premium is rupees five hundred Can I enable you to finish the handling now with Online on the off chance that you have your Visa accessible";
		SortedMap<String, Double> userDictionary = cosProduct.getSortedTreeMap(userText);
		int baseDictionarySize = baseDictionary.keySet().size();
		double[] vectorA = new double[baseDictionarySize];
		double[] vectorB = new double[baseDictionarySize];
		int i = 0;
		for (String s : baseDictionary.keySet()) {
			vectorA[i++] = baseDictionary.get(s);
		}
		i = 0;
		for (String s : baseDictionary.keySet()) {
			Double userDictTF = userDictionary.get(s);
			if (userDictTF != null)
				vectorB[i++] = userDictTF;
		}
		System.out.println(cosProduct.cosineSimilarity(vectorA, vectorB));
	}

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

	private SortedMap<String, Double> getSortedTreeMap(String text) throws IOException {
		ManualCosProduct cosProduct = new ManualCosProduct();
		text = cosProduct.removeStopWords(text);
		text = cosProduct.cleanText(text);
		SortedMap<String, Integer> dictionary = cosProduct.getDictionary(text);
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

	private SortedMap<String, Integer> getDictionary(String text) throws IOException {
		text = cleanText(text);
		text = removeStopWords(text);
		SortedMap<String, Integer> dictionary = new TreeMap<>();
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		Annotation document = new Annotation(text);
		pipeline.annotate(document);
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		for (CoreMap sentence : sentences) {
			System.out.println("Sentence: " + sentence.toString());
			for (CoreMap token : sentence.get(TokensAnnotation.class)) {
				String word = token.get(TextAnnotation.class);
				String lemma = token.get(LemmaAnnotation.class);
				if (dictionary.containsKey(lemma)) {
					int lemmacount = dictionary.get(lemma);
					dictionary.remove(lemma);
					dictionary.put(lemma, ++lemmacount);
				} else {
					dictionary.put(lemma, 1);
				}
			}
		}
		return dictionary;
	}

	private String removeStopWords(String text) throws IOException {
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

	private String cleanText(String text) {
		return text.replaceAll("[^a-zA-Z0-9 ]", " ").replaceAll("\\s+", " ");
	}
}
