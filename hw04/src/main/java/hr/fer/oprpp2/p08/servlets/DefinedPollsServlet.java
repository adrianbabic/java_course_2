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


@WebServlet(name = "definedPollsServlet", urlPatterns = { "/servleti/index.html" })
public class DefinedPollsServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		List<Poll> polls = DAOProvider.getDao().fetchDefinedPolls();
		req.setAttribute("polls", polls);

		req.getRequestDispatcher("/WEB-INF/pages/definedPolls.jsp").forward(req, resp);
	}

}
