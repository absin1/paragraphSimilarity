package test;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class KeyWordFrequencyTest {
	static String baseURL = "http://localhost:8080/";

	public static void main(String[] args) throws IOException {
		InputStream stream = null;
		String relativeURL = "paragraphSimilarity/KeyWordFrequency";
		String keywords = "shorten,timeframe,hire,improve,quality,reduce,time,screening,interviewing,applicants,placing,teams";
		String text = "Great. The purpose of my call is that we help hiring managers to: shorten the timeframe it takes to place a new hire improve the quality of the new hire reduce internal time spent searching, screening, and interviewing applicants improve how they qualify and screen applicants reduce the costs associated with finding and placing new hires build top caliber teams leading to the best business results";
		String urlParameters = "keywords=" + keywords + "&text=" + text;
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
	}
}
