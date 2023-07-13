package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/servleti/bad-post-request")
public class BadPostRequest extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String message = req.getParameter("message");
		if(message != null) 
			req.setAttribute("message", message);
		
		req.getRequestDispatcher("/WEB-INF/pages/Error.jsp").forward(req, resp);
	}
	
}