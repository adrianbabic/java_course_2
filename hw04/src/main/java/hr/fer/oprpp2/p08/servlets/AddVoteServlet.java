package hr.fer.oprpp2.p08.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.oprpp2.p08.dao.DAOProvider;

/**	Servlet represents an action of a single vote for some candidate. Updates the vote count of that one candidate.
 * 
 * 	@author adrian
 */
@WebServlet(name = "addVoteServlet", urlPatterns = { "/servleti/glasanje-glasaj" })
public class AddVoteServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String idString = req.getParameter("id");
		long id = -1;
		try {
			id = Long.parseLong(idString);
		} catch (NumberFormatException ex) {
		}
		
		DAOProvider.getDao().incrementVoteCountWithPollOptionId(id);
		long pollId = DAOProvider.getDao().getPollIdWithPollOptionId(id);
		
		resp.sendRedirect(req.getContextPath() + "/servleti/glasanje-rezultati?pollID=" + pollId);
	}
}