package wordNet;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.SentenceUtils;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class Sample1 {
	public static void main(String[] args) {
		String text = "My name. is Sam. I am a. software developer. I live in London. I have three dogs and two cats who are like family to me";
		// attempt1(text);
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

		// create an empty Annotation just with the given text
		Annotation document = new Annotation(text);

		// run all Annotators on this text
		pipeline.annotate(document);

		// these are all the sentences in this document
		// a CoreMap is essentially a Map that uses class objects as keys and
		// has values with custom types
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);

		for (CoreMap sentence : sentences) {
			// traversing the words in the current sentence
			System.out.println(sentence.toString());

		}
	}

	private static void attempt1(String text) {
		Reader input = new StringReader(text);
		edu.stanford.nlp.process.DocumentPreprocessor documentPreprocessor = new edu.stanford.nlp.process.DocumentPreprocessor(
				input);
		List<String> sentenceList = new ArrayList<String>();

		for (List<HasWord> sentence : documentPreprocessor) {
			// SentenceUtils not Sentence
			String sentenceString = SentenceUtils.listToString(sentence);
			sentenceList.add(sentenceString);
		}

		for (String sentence : sentenceList) {
			System.out.println(sentence);
		}
	}
}
