package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.model.BlogEntry;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;

@WebServlet("/servleti/author/*")
public class Author extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String pathInfo = req.getPathInfo();
		if (pathInfo != null) {

			String[] separated = pathInfo.substring(1).split("/");

			if (separated.length == 0) {
				resp.sendRedirect(req.getContextPath() + "/servleti/authors");
			}

			if (separated.length == 1) {
				if(separated[0].length() == 0) {
					alertError(req, resp, "Bad request: " + req.getServletPath());
				}
					
				getAuthorsBlogEntries(req, resp, separated[0]);
				return;
			}

			if (separated.length == 2) {

				if (separated[1].equals("new")) {

					String sessionNick = (String) req.getSession().getAttribute("current.user.nick");
					if (sessionNick != null && sessionNick.equals(separated[0])) {
						req.getRequestDispatcher("/WEB-INF/pages/author/NewBlogEntryForm.jsp").forward(req, resp);
						return;
					} else {
						alertError(req, resp, "Not allowed. Only user " + separated[0]
								+ " can create new entries on his/her behalf.");
						return;
					}
				}
				
				if (separated[1].equals("edit")) {

					String sessionNick = (String) req.getSession().getAttribute("current.user.nick");
					if (sessionNick != null && sessionNick.equals(separated[0])) {

						String entry_id_str = req.getParameter("entry_id");

						if (entry_id_str == null) {

							alertError(req, resp,
									"[Editing blog entry]: Missing information! Please provide entry_id.");
							return;
						}
						
						Long entry_id;
						try {
							entry_id = Long.parseLong(entry_id_str);
						} catch (NumberFormatException e) {
							alertError(req, resp, "[Editing blog entry]: Bad POST request: /" + separated[0] + "/" + separated[1] +
									"\nentry_id should be a number.");
							return;
						}

						BlogEntry blogEntry = DAOProvider.getDAO().getBlogEntry(entry_id);

						if (blogEntry == null) {

							alertError(req, resp,
									"[Editing blog entry]: Blog entry with id: " + entry_id + " doesn't exist.");
							return;
						} else {

							req.setAttribute("blog_entry", blogEntry);
							req.getRequestDispatcher("/WEB-INF/pages/author/EditBlogEntry.jsp").forward(req, resp);
							return;
						}
					} else {
						alertError(req, resp, "Not allowed. Only user " + separated[0]
								+ " can edit entries on his/her behalf.");
						return;
					}
				}

				Long entry_id;
				try {
					entry_id = Long.parseLong(separated[1]);
				} catch (NumberFormatException e) {
					alertError(req, resp, "Bad request: /" + separated[0] + "/" + separated[1]);
					return;
				}

				BlogEntry blog_entry = DAOProvider.getDAO().getBlogEntry(entry_id);

				if (blog_entry == null)
					req.setAttribute("entry_id", entry_id);

				req.setAttribute("blog_entry", blog_entry);

				req.getRequestDispatcher("/WEB-INF/pages/author/ShowBlogEntry.jsp").forward(req, resp);
				return;
			}
		} else {
			alertError(req, resp, "Request ended up in wrong servlet.");
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String pathInfo = req.getPathInfo();
		if (pathInfo != null) {

			String[] separated = pathInfo.substring(1).split("/");

			if (separated.length == 2) {

				if (separated[1].equals("new")) {

					String sessionNick = (String) req.getSession().getAttribute("current.user.nick");
					if (sessionNick != null && sessionNick.equals(separated[0])) {

						String title = req.getParameter("title");
						String text = req.getParameter("text");
						String nick = req.getParameter("nick");

						if (title == null || text == null || nick == null) {

							alertError(req, resp,
									"[Create new blog entry]: Missing information! Please provide title, text and nick.");
							return;
						}

						BlogUser blogUser = DAOProvider.getDAO().getBlogUserWithNick(nick);

						if (blogUser == null) {

							alertError(req, resp,
									"[Create new blog entry]: Blog user with nick '" + nick + "' doesn't exist.");
							return;
						} else {

							DAOProvider.getDAO().addBlogEntry(title, text, blogUser);

							resp.sendRedirect(req.getContextPath() + "/servleti/author/" + nick);
							return;
						}
					} else {
						alertError(req, resp, "Not allowed. Only user " + separated[0]
								+ " can create new entries on his/her behalf.");
						return;
					}
				}

				if (separated[1].equals("edit")) {

					String sessionNick = (String) req.getSession().getAttribute("current.user.nick");
					if (sessionNick != null && sessionNick.equals(separated[0])) {

						String entry_id_str = req.getParameter("entry_id");
						String title = req.getParameter("title");
						String text = req.getParameter("text");

						if (entry_id_str == null || title == null || text == null) {

							alertError(req, resp,
									"[Editing blog entry POST request]: Missing information! Please provide title, text and entry_id.");
							return;
						}
						
						Long entry_id;
						try {
							entry_id = Long.parseLong(entry_id_str);
						} catch (NumberFormatException e) {
							alertError(req, resp, "[Editing blog entry POST request]: Bad POST request: /" + separated[0] + "/" + separated[1] +
									"\nentry_id should be a number.");
							return;
						}

						BlogEntry blogEntry = DAOProvider.getDAO().getBlogEntry(entry_id);

						if (blogEntry == null) {

							alertError(req, resp,
									"[Editing blog entry POST request]: Blog entry with id: " + entry_id + " doesn't exist.");
							return;
						} else {

							DAOProvider.getDAO().updateBlogEntry(blogEntry, title, text);
							
							resp.sendRedirect(req.getContextPath() + "/servleti/author/" + separated[0] + "/" + blogEntry.getId());
							return;
						}
					} else {
						alertError(req, resp, "Not allowed. Only user " + separated[0]
								+ " can edit entries on his/her behalf.");
						return;
					}
				}

				Long entry_id;
				try {
					entry_id = Long.parseLong(separated[1]);
				} catch (NumberFormatException e) {
					alertError(req, resp, "Bad POST request: /" + separated[0] + "/" + separated[1]);
					return;
				}

				BlogEntry blog_entry = DAOProvider.getDAO().getBlogEntry(entry_id);

				if (blog_entry == null) {
					alertError(req, resp, "[Add blog comment]: Blog entry with id: " + entry_id + " doesn't exist.");
					return;
				}

				if (!blog_entry.getCreator().getNick().equals(separated[0])) {
					alertError(req, resp, "[Add blog comment]: Blog user '" + separated[0]
							+ "' doesn't have a blog entry under id: " + entry_id);
					return;
				}

				String message = req.getParameter("message");
				String commentators_nick = req.getParameter("commentators_nick");

				if (message == null || commentators_nick == null) {

					alertError(req, resp,
							"[Add blog comment]: Missing information! Please provide message and users_email.");
					return;
				}
				
				BlogUser commentator = DAOProvider.getDAO().getBlogUserWithNick(commentators_nick);
				if(commentator == null) {
					alertError(req, resp,
							"[Add blog comment]: Commentator's user nick doesn't exist: " + commentators_nick);
					return;
				}

				DAOProvider.getDAO().addBlogComment(blog_entry, commentator.getEmail(), message);

				resp.sendRedirect(req.getContextPath() + "/servleti/author/" + separated[0] + "/" + entry_id);
				return;
			} else {

				String message = "Bad POST request: /";
				for (String i : separated) {
					message += i + "/";
				}
				alertError(req, resp, message);
			}
		} else {
			alertError(req, resp, "Bad POST request.");
		}
	}

	private void alertError(HttpServletRequest req, HttpServletResponse resp, String message)
			throws ServletException, IOException {

		req.setAttribute("message", message);

		req.getRequestDispatcher("/WEB-INF/pages/Error.jsp").forward(req, resp);
	}

	private void getAuthorsBlogEntries(HttpServletRequest req, HttpServletResponse resp, String nick)
			throws ServletException, IOException {

		BlogUser author = DAOProvider.getDAO().getBlogUserWithNick(nick);

		if (author == null) {

			String message = "Author with nick \"" + nick + "\" doesn't exist.";
			req.setAttribute("message", message);

			req.getRequestDispatcher("/WEB-INF/pages/Error.jsp").forward(req, resp);
		} else {

			List<BlogEntry> blog_entries = author.getBlogEntries();

			req.setAttribute("author", author);
			req.setAttribute("blog_entries", blog_entries);

			req.getRequestDispatcher("/WEB-INF/pages/author/BlogEntriesFromNick.jsp").forward(req, resp);
		}
	}

}