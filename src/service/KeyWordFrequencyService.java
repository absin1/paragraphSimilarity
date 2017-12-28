/**
 * 
 */
package service;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author absin
 *
 */
public class KeyWordFrequencyService {
	public String generateResponse(String keyWords, String text) {
		NLPServices nlpServices = new NLPServices();
		text = nlpServices.cleanText(text);
		try {
			text = nlpServices.removeStopWords(text);
		} catch (IOException e) {
			e.printStackTrace();
		}
		keyWords = nlpServices.cleanText(keyWords);
		try {
			keyWords = nlpServices.removeStopWords(keyWords);
		} catch (IOException e) {
			e.printStackTrace();
		}
		HashMap<String, HashSet<String>> keyLemmas = nlpServices.lemmatizerPOSTagger(keyWords);
		HashMap<String, HashSet<String>> textLemmas = nlpServices.lemmatizerPOSTagger(text);
		HashMap<String, Integer> frequencyKeyword = nlpServices.performSoftComparision(keyLemmas, textLemmas);
		JSONObject keyStatus = new JSONObject();
		for (String keyWord : frequencyKeyword.keySet()) {
			keyStatus.put(keyWord, frequencyKeyword.get(keyWord));
		}
		System.out.println(keyStatus.toString());
		return keyStatus.toString();
	}

}
