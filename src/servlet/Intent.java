package servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;

import intent.IntentDocumentSampleStream;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSample;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.NameSample;
import opennlp.tools.namefind.NameSampleDataStream;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.ObjectStreamUtils;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.Span;
import opennlp.tools.util.TrainingParameters;
import opennlp.tools.util.featuregen.AdaptiveFeatureGenerator;

@WebServlet("/intent")
public class Intent extends HttpServlet {
	private final static Logger log = org.apache.log4j.Logger.getLogger(Intent.class);

	private static final long serialVersionUID = 1L;
	private static DocumentCategorizerME categorizer;
	private static NameFinderME[] nameFinderMEs;

	public Intent() throws IOException, URISyntaxException {
		super();
		URL url = this.getClass().getResource("/resources/example/smallTalk/train");
		File trainingDirectory = new File(url.toURI());
		if (!trainingDirectory.isDirectory()) {
			throw new IllegalArgumentException(
					"TrainingDirectory is not a directory: " + trainingDirectory.getAbsolutePath());
		}
		List<ObjectStream<DocumentSample>> categoryStreams = new ArrayList<ObjectStream<DocumentSample>>();
		for (File trainingFile : trainingDirectory.listFiles()) {
			String intent = trainingFile.getName().replaceFirst("[.][^.]+$", "");
			ObjectStream<String> lineStream = new PlainTextByLineStream(new FileInputStream(trainingFile), "UTF-8");
			ObjectStream<DocumentSample> documentSampleStream = new IntentDocumentSampleStream(intent, lineStream);
			categoryStreams.add(documentSampleStream);
		}
		ObjectStream<DocumentSample> combinedDocumentSampleStream = ObjectStreamUtils
				.createObjectStream(categoryStreams.toArray(new ObjectStream[0]));

		DoccatModel doccatModel = DocumentCategorizerME.train("en", combinedDocumentSampleStream, 0, 100);
		combinedDocumentSampleStream.close();

		List<TokenNameFinderModel> tokenNameFinderModels = new ArrayList<TokenNameFinderModel>();

		categorizer = new DocumentCategorizerME(doccatModel);
		nameFinderMEs = new NameFinderME[tokenNameFinderModels.size()];
		for (

				int i = 0; i < tokenNameFinderModels.size(); i++) {
			nameFinderMEs[i] = new NameFinderME(tokenNameFinderModels.get(i));
		}
		// System.out.println("This message shouldnt appear every time the servlet is
		// called.");
		// System.out.println("Training complete. Ready.");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String intent = "fallback";
		String s = request.getParameter("q");
		double[] outcome = categorizer.categorize(s);
		if (Collections.max(Arrays.asList(ArrayUtils.toObject(outcome))) > 0.2) {
			intent = categorizer.getBestCategory(outcome);
		}
		response.getWriter().append(intent);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}
}
