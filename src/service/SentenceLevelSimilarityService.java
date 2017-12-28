/**
 * 
 */
package service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.SortedMap;

import org.json.JSONObject;

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

/**
 * @author absin
 *
 */
public class SentenceLevelSimilarityService {

	public String generateResponse(String standardText, String userText) {
		HashMap<String, Double> sentenceLevelScores = new HashMap<>();
		NLPServices nlpServices = new NLPServices();
		CosineDifferenceServices cosineDifferenceServices = new CosineDifferenceServices();
		try {
			userText = nlpServices.removeStopWords(userText);
		} catch (IOException e) {
			e.printStackTrace();
		}
		userText = nlpServices.cleanText(userText);
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		Annotation document = new Annotation(standardText);
		pipeline.annotate(document);
		List<CoreMap> standardSentences = document.get(SentencesAnnotation.class);
		for (CoreMap standardSentence : standardSentences) {
			System.out.println("Sentence: " + standardSentence.toString());
			String standardSentenceText = standardSentence.toString();
			try {
				standardSentenceText = nlpServices.removeStopWords(standardSentenceText);
			} catch (IOException e) {
				e.printStackTrace();
			}
			standardSentenceText = nlpServices.cleanText(standardSentenceText);
			SortedMap<String, Double> standardMap = cosineDifferenceServices.getTermFrequencyMap(standardSentenceText);
			SortedMap<String, Integer> relativeTermFrequencyMap = cosineDifferenceServices
					.getRelativeTermFrequencyMap(userText, standardMap.keySet());
			SortedMap<String, Double> userMap = cosineDifferenceServices
					.convertAbsoluteFrequencyToRelativeFrequency(relativeTermFrequencyMap);
			int baseDictionarySize = standardMap.keySet().size();
			double[] vectorA = new double[baseDictionarySize];
			double[] vectorB = new double[baseDictionarySize];
			int i = 0;
			for (String s : standardMap.keySet()) {
				vectorA[i++] = standardMap.get(s);
			}
			i = 0;
			for (String s : userMap.keySet()) {
				Double userDictTF = userMap.get(s);
				if (userDictTF != null)
					vectorB[i++] = userDictTF;
			}
			double sentenceCosineSimilarity = cosineDifferenceServices.cosineSimilarity(vectorA, vectorB);
			sentenceLevelScores.put(standardSentence.toString(), sentenceCosineSimilarity);
		}
		JSONObject jsonObject = new JSONObject();
		for (String sentence : sentenceLevelScores.keySet()) {
			if (!sentenceLevelScores.get(sentence).isNaN())
				jsonObject.put(sentence, sentenceLevelScores.get(sentence));
		}
		return jsonObject.toString();
	}

}
