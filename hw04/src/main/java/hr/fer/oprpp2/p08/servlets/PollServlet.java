package hr.fer.oprpp2.p08.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.oprpp2.p08.dao.DAOProvider;
import hr.fer.oprpp2.p08.models.Poll;
import hr.fer.oprpp2.p08.models.PollOption;

/**
 * Servlet prepares all possible candidates for the voting process.
 * 
 * @author adrian
 */
@WebServlet(name = "pollVoting", urlPatterns = { "/servleti/glasanje" })
public class PollServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String pollIdString = req.getParameter("pollID");

		long pollId = -1;
		try {
			pollId = Long.parseLong(pollIdString);
		} catch (NumberFormatException ex) {
		}
		
		Poll poll = DAOProvider.getDao().fetchPollWithId(pollId);
		List<PollOption> pollOptions = DAOProvider.getDao().fetchPollOptionsByPoolId(pollId);
		
		req.setAttribute("poll", poll);
		req.setAttribute("options", pollOptions);
		req.getRequestDispatcher("/WEB-INF/pages/pollAllCandidates.jsp").forward(req, resp);
	}

}