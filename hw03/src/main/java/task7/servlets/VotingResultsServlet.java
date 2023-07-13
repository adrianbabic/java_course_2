package task7.servlets;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import task7.helpers.CandidateWithResult;
import task7.helpers.FileHandler;

@WebServlet(name = "votingResults", urlPatterns = { "/glasanje-rezultati" })
public class VotingResultsServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String definitionFileName = req.getServletContext().getRealPath("/WEB-INF/voting/voting-definition.txt");
		String fileName = req.getServletContext().getRealPath("/WEB-INF/voting/voting-results.txt");
		File file = new File(fileName);

		
		if (!file.exists() || file.length() == 0) {

			if (!Files.exists(Path.of(definitionFileName))) {
				req.setAttribute("error", "File voting-definition.txt couldn't be found!");
				req.getRequestDispatcher("/WEB-INF/voting/votingError.jsp").forward(req, resp);
			}

			List<String> loadedCandidates = FileHandler.getInitializedCandidates(definitionFileName);
			FileHandler.writeToFile(fileName, loadedCandidates);
		}
		
		List<CandidateWithResult> results = FileHandler.getCandidateResults(fileName, definitionFileName);
		
		List<CandidateWithResult> winners = new ArrayList<>();
		int votes = results.get(0).getVotes();
		for(CandidateWithResult one: results) {
			if(one.getVotes() == votes) {
				winners.add(one);
			} else {
				break;
			}
		}
		
		req.setAttribute("results", results);
		req.setAttribute("winners", winners);
		
		req.getRequestDispatcher("/WEB-INF/pages/votingRes.jsp").forward(req, resp);
	}

}