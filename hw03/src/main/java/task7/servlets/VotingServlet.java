package task7.servlets;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import task7.helpers.Candidate;
import task7.helpers.FileHandler;


@WebServlet(name = "voting", urlPatterns = { "/glasanje" })
public class VotingServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String fileName = req.getServletContext().getRealPath("/WEB-INF/voting/voting-definition.txt");
		
		if(!Files.exists(Path.of(fileName))) {
			req.setAttribute("error", "File voting-definition.txt couldn't be found!");
			req.getRequestDispatcher("/WEB-INF/voting/votingError.jsp").forward(req, resp);
		}
		
		List<Candidate> candidates = FileHandler.loadCandidates(fileName);
		
		req.setAttribute("candidates", candidates);
		req.getRequestDispatcher("/WEB-INF/pages/votingIndex.jsp").forward(req, resp);
	}

}
