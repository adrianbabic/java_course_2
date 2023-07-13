package hr.fer.oprpp2.p08.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.oprpp2.p08.dao.DAOProvider;
import hr.fer.oprpp2.p08.models.PollOption;

/**
 * Servlet prepares the results of the voting.
 * 
 * @author adrian
 */
@WebServlet(name = "votingResults", urlPatterns = { "/servleti/glasanje-rezultati" })
public class PollResultsServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String pollIdString = req.getParameter("pollID");

		long pollId = -1;
		try {
			pollId = Long.parseLong(pollIdString);
		} catch (NumberFormatException ex) {
		}
			
		List<PollOption> pollOptions = DAOProvider.getDao().fetchPollOptionsByPoolId(pollId);
		List<PollOption> winners = DAOProvider.getDao().fetchPollOptionsWithHighestVotes(pollId);
		
		req.setAttribute("options", pollOptions);
		req.setAttribute("winners", winners);
		req.setAttribute("pollid", pollId);

		req.getRequestDispatcher("/WEB-INF/pages/pollResults.jsp").forward(req, resp);
	}

}
