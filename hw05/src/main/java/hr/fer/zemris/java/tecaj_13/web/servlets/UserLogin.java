package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.handlers.DataHandler;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;

@WebServlet("/servleti/main")
public class UserLogin extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		req.setCharacterEncoding("UTF-8");
		
		String sessionNick = (String) req.getSession().getAttribute("current.user.nick");
		if(sessionNick != null) {
			req.setAttribute("message", "You are already logged in as a blog user: " + sessionNick);
			req.getRequestDispatcher("/WEB-INF/pages/auth/LoginForm.jsp").forward(req, resp);
		}
		
		String nick = req.getParameter("nick");
		if (nick == null)
			nick = "";

		req.setAttribute("nick", nick);

		req.getRequestDispatcher("/WEB-INF/pages/auth/LoginForm.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		
		String nick = req.getParameter("nick");
		String password = req.getParameter("password");

		if (nick == null || password == null) {

			String message = "Missing information! Please provide nick and password.";
			req.setAttribute("message", message);

			req.getRequestDispatcher("/WEB-INF/pages/auth/LoginForm.jsp").forward(req, resp);
			return;
		}

		BlogUser blogUser = DAOProvider.getDAO().getBlogUserWithNick(nick);
		if (blogUser != null) {

			String passwordHash;
			try {
				passwordHash = DataHandler.generateSHA1Hash(password);
			} catch (NoSuchAlgorithmException e) {

				String message = "Error occured on the server side. Try again.";
				req.setAttribute("message", message);
				req.setAttribute("nick", nick);

				req.getRequestDispatcher("/WEB-INF/pages/auth/LoginForm.jsp").forward(req, resp);
				return;
			}

			if (passwordHash.equals(blogUser.getPasswordHash())) {
				
				HttpSession session = req.getSession();
				
				session.setAttribute("current.user.id", blogUser.getId());
				session.setAttribute("current.user.fn", blogUser.getFirstName());
				session.setAttribute("current.user.ln", blogUser.getLastName());
				session.setAttribute("current.user.nick", blogUser.getNick());
				
				resp.sendRedirect(req.getContextPath() + "/servleti/authors");
			}
			else {

				String message = "Incorrect nick or password!";
				req.setAttribute("message", message);

				req.setAttribute("nick", nick);

				req.getRequestDispatcher("/WEB-INF/pages/auth/LoginForm.jsp").forward(req, resp);
			}

		} else {

			String message = "Incorrect nick or password!";
			req.setAttribute("message", message);

			req.setAttribute("nick", nick);

			req.getRequestDispatcher("/WEB-INF/pages/auth/LoginForm.jsp").forward(req, resp);
		}

	}
}