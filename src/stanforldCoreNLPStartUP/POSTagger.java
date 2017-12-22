package stanforldCoreNLPStartUP;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;

public class POSTagger {
	static HashSet<String> nes = new HashSet<>();

	public static void main(String args[]) {
		// action();
		onlyLemma();

	}

	private static String cleanText(String text) {
		return text.replaceAll("[^a-zA-Z0-9 ]", " ").replaceAll("\\s+", " ");
	}

	private static void RegexMagic() {
		String instring = "policy renewal?We9";
		String[] words = instring.replaceAll("[^a-zA-Z0-9 ]", ">><<").replaceAll("\\s+", ">><<").toLowerCase()
				.split(">><<");
		for (int i = 0; i < words.length; i++) {
			System.out.println(words[i]);
		}
	}

	private static void onlyLemma() {
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		String text = "";
		try {
			text = cleanText(getTextFromFile("/Users/feroz/Documents/salesScript.txt"));
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
				// this is the text of the token
				String word = token.get(TextAnnotation.class);
				System.out.print("Word: " + word);
				String lemma = token.get(LemmaAnnotation.class);
				System.out.print(" Lemma: " + lemma);
				System.out.println();

			}
		}
	}

	private static void action() {
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		String text = "";
		try {
			text = getTextFromFile("/Users/feroz/Documents/salesScript.txt");
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
				// this is the text of the token
				String word = token.get(TextAnnotation.class);
				// this is the POS tag of the token
				String pos = token.get(PartOfSpeechAnnotation.class);
				// this is the NER label of the token
				String ne = token.get(NamedEntityTagAnnotation.class);
				System.out.print("Word: " + word);
				System.out.print(" pos: " + pos);
				System.out.print(" ne: " + ne);
				System.out.println();

			}
			// this is the parse tree of the current sentence
			Tree tree = sentence.get(TreeAnnotation.class);

			// this is the Stanford dependency graph of the current sentence
			SemanticGraph dependencies = sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);
		}

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
}
