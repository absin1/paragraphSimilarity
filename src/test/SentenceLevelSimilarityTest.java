package test;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * This test class shows a sample implementation of the web service exposed to
 * perform a similarity analysis between 2 texts entered by the user and
 * generates a sentence wise coverage of the semantic information present.
 * 
 * @return A JSON object with keys as the sentences in the scripted text
 *         (standardText) supplied by the user and value a double point number
 *         varying between 0 and 1 indicating the efficacy of coverage in the
 *         speech translated text (userText) supplied by the user
 * 
 * @param standardText
 *            A standard text also called the script whose sentences will form
 *            the basis of comparison against userText
 * 
 * @param userText
 *            Typically a text-speech output given by a speech translation API
 *            or any non-standard text which will be matched for semantic
 *            coverage against the standard text
 * @author absin
 */
public class SentenceLevelSimilarityTest {
	static String baseURL = "http://localhost:8080/";

	public static void main(String[] args) throws IOException {
		InputStream stream = null;
		String relativeURL = "paragraphSimilarity/SentenceLevelSimilarity";
		String userText = "Great The purpose of my call is that we help hiring managers to shorten the timeframe it takes to place a new hire improve the quality of the new hire reduce internal time spent searching, screening, and interviewing  improve how they qualify and screen reduce the costs associated with finding and placing new hires build top caliber teams leading to the best business results";
		String standardText = "Great. The purpose of my call is that we help hiring managers to: shorten the timeframe it takes to place a new hire. improve the quality of the new hire. reduce internal time spent searching, screening, and interviewing applicants. improve how they qualify and screen applicants. reduce the costs associated with finding and placing new hires. build top caliber teams leading to the best business results";
		String urlParameters = "userText=" + userText + "&standardText=" + standardText;
		URL url = new URL(baseURL + relativeURL);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoOutput(true);
		connection.setUseCaches(false);
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		connection.setRequestProperty("charset", StandardCharsets.UTF_8.name());
		connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.length()));
		try (DataOutputStream out = new DataOutputStream(connection.getOutputStream())) {
			out.write(urlParameters.getBytes(StandardCharsets.UTF_8));
		}
		stream = connection.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8.name()));
		String result = reader.readLine();
		reader.close();
		System.out.println(result);
		JsonParser jsonParser = new JsonParser();
		JsonObject sentenceScores = (JsonObject) jsonParser.parse(result);
		for (String sentence : sentenceScores.keySet()) {
			Double sentenceScore = sentenceScores.get(sentence).getAsDouble();
			System.out.println(sentence + ">>" + sentenceScore);
			// sentence score will vary between 0-1, 1 being the best and 0
			// being the worst
		}
	}
}
