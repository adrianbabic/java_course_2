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

import task7.helpers.CandidateStatus;
import task7.helpers.FileHandler;

@WebServlet(name = "votingVote", urlPatterns = { "/glasanje-glasaj" })
public class VotingVoteServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String idText = req.getParameter("id");
		int id = Integer.parseInt(idText);

		String fileName = req.getServletContext().getRealPath("/WEB-INF/voting/voting-results.txt");
		File file = new File(fileName);
		
		if (!file.exists() || file.length() == 0) {
			
			String definitionFileName = req.getServletContext().getRealPath("/WEB-INF/voting/voting-definition.txt");
			if (!Files.exists(Path.of(definitionFileName))) {
				req.setAttribute("error", "File voting-definition.txt couldn't be found!");
				req.getRequestDispatcher("/WEB-INF/voting/votingError.jsp").forward(req, resp);
			}
			
			List<String> loadedCandidates = FileHandler.getInitializedCandidates(definitionFileName);
			FileHandler.writeToFile(fileName, loadedCandidates);
		}
		
		List<CandidateStatus> allResults = FileHandler.loadResults(fileName);
		
		for(CandidateStatus status: allResults) {
			if(status.getId() == id) {
				status.setVotes(status.getVotes() + 1);
				break;
			}
		}
		
		List<String> toWrite = new ArrayList<>();
		
		allResults.forEach(v -> {
			toWrite.add(v.getId() + "\t" + v.getVotes());
		});
		
		FileHandler.writeToFile(fileName, toWrite);
		
		resp.sendRedirect(req.getContextPath() + "/glasanje-rezultati");
	}
	
}