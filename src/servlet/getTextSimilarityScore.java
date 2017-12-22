package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.TextSimilarityScore;

/**
 * Servlet implementation class getTextSimilarityScore
 */
@WebServlet("/getTextSimilarityScore")
public class getTextSimilarityScore extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getTextSimilarityScore() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String standardText = request.getParameter("text1");
		String sampleText = request.getParameter("text2");
		System.out.println(standardText);
		System.out.println(sampleText);
		Double cosineSimilarity2 = new TextSimilarityScore().computeSimilarityScore(standardText, sampleText);
		response.getWriter().append(Double.toString(cosineSimilarity2));
		// doGet(request, response);
	}

}
