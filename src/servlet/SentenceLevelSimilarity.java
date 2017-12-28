package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.SentenceLevelSimilarityService;

/**
 * Servlet implementation class SentenceLevelSimilarity
 */
@WebServlet("/SentenceLevelSimilarity")
public class SentenceLevelSimilarity extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SentenceLevelSimilarity() {
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
		String standardText = request.getParameter("standardText");
		String userText = request.getParameter("userText");
		System.out.println(standardText);
		System.out.println(userText);
		response.getWriter().append(new SentenceLevelSimilarityService().generateResponse(standardText, userText));
	}

}
