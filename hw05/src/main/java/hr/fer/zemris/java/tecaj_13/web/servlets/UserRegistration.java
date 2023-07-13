package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.handlers.DataHandler;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;

@WebServlet("/servleti/register")
public class UserRegistration extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		
		String sessionNick = (String) req.getSession().getAttribute("current.user.nick");
		if(sessionNick != null) {
			req.setAttribute("message", "You are already logged in as a blog user: " + sessionNick);
			req.getRequestDispatcher("/WEB-INF/pages/auth/RegistrationForm.jsp").forward(req, resp);
		}
		
		String firstName = req.getParameter("firstName");
		if (firstName == null)
			firstName = "";

		String lastName = req.getParameter("lastName");
		if (lastName == null)
			lastName = "";

		String email = req.getParameter("email");
		if (email == null)
			email = "";

		String nick = req.getParameter("nick");
		if (nick == null)
			nick = "";
		
		req.setAttribute("firstName", firstName);
		req.setAttribute("lastName", lastName);
		req.setAttribute("email", email);
		req.setAttribute("nick", nick);

		req.getRequestDispatcher("/WEB-INF/pages/auth/RegistrationForm.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		
		String firstName = req.getParameter("firstName");
		String lastName = req.getParameter("lastName");
		String email = req.getParameter("email");
		String nick = req.getParameter("nick");
		String password = req.getParameter("password");

		if (firstName == null || lastName == null || email == null || nick == null || password == null) {

			String message = "Missing information! Please provide firstName, lastName, email, nick and password.";
			req.setAttribute("message", message);

			req.getRequestDispatcher("/WEB-INF/pages/auth/RegistrationForm.jsp").forward(req, resp);
			return;
		}

		BlogUser blogUser = DAOProvider.getDAO().getBlogUserWithNick(nick);
		if (blogUser == null) {
			
			String passwordHash;
			try {
				passwordHash = DataHandler.generateSHA1Hash(password);
			} catch (NoSuchAlgorithmException e) {
				
				String message = "Error occured on the server side. Try again.";
				req.setAttribute("message", message);
				
				req.setAttribute("firstName", firstName);
				req.setAttribute("lastName", lastName);
				req.setAttribute("email", email);
				req.setAttribute("nick", nick);
				
				req.getRequestDispatcher("/WEB-INF/pages/auth/RegistrationForm.jsp").forward(req, resp);
				return;
			}
			
			DAOProvider.getDAO().addBlogUser(firstName, lastName, email, nick, passwordHash);
			
			resp.sendRedirect(req.getContextPath() + "/servleti/main");
		} else {

			String message = "User with nick: \"" + nick + "\" already exists! Try a different nick.";
			req.setAttribute("message", message);
			
			req.setAttribute("firstName", firstName);
			req.setAttribute("lastName", lastName);
			req.setAttribute("email", email);

			req.getRequestDispatcher("/WEB-INF/pages/auth/RegistrationForm.jsp").forward(req, resp);
		}

	}
}

