package hr.fer.zemris.java.tecaj_13.dao.jpa;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import hr.fer.zemris.java.tecaj_13.dao.DAO;
import hr.fer.zemris.java.tecaj_13.dao.DAOException;
import hr.fer.zemris.java.tecaj_13.model.BlogComment;
import hr.fer.zemris.java.tecaj_13.model.BlogEntry;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;

public class JPADAOImpl implements DAO {

	@Override
	public BlogEntry getBlogEntry(Long id) throws DAOException {
		BlogEntry blogEntry = JPAEMProvider.getEntityManager().find(BlogEntry.class, id);
		return blogEntry;
	}

	@Override
	public BlogUser getBlogUserWithNick(String nick) throws DAOException {

		EntityManager em = JPAEMProvider.getEntityManager();

		@SuppressWarnings("unchecked")
		List<BlogUser> users = (List<BlogUser>) em.createNamedQuery("BlogUser.findNick").setParameter("be", nick)
				.getResultList();

		if (users.size() > 1)
			throw new DAOException("Found more than one user with nick \"" + nick + "\" in the database.");

		if (users.size() == 0)
			return null;
		else
			return users.get(0);
	}

	@Override
	public BlogUser addBlogUser(String firstName, String lastName, String email, String nick, String passwordHash) {

		EntityManager em = JPAEMProvider.getEntityManager();

		BlogUser blogUser = new BlogUser();
		blogUser.setFirstName(firstName);
		blogUser.setLastName(lastName);
		blogUser.setEmail(email);
		blogUser.setNick(nick);
		blogUser.setPasswordHash(passwordHash);

		em.persist(blogUser);

		return blogUser;
	}

	@Override
	public List<BlogUser> getAllAuthors() throws DAOException {

		EntityManager em = JPAEMProvider.getEntityManager();

		@SuppressWarnings("unchecked")
		List<BlogUser> users = (List<BlogUser>) em.createNamedQuery("BlogUser.findAllAuthors").getResultList();

		if (users.size() == 0)
			return null;
		else
			return users;
	}

	@Override
	public BlogEntry addBlogEntry(String title, String text, BlogUser creator) throws DAOException {
		
		EntityManager em = JPAEMProvider.getEntityManager();

		BlogEntry blogEntry = new BlogEntry();
		blogEntry.setComments(new ArrayList<>());
		blogEntry.setCreatedAt(new Date());
		blogEntry.setLastModifiedAt(blogEntry.getCreatedAt());
		blogEntry.setTitle(title);
		blogEntry.setText(text);
		blogEntry.setCreator(creator);
		
		em.persist(blogEntry);
		
		return blogEntry;
	}

	@Override
	public BlogComment addBlogComment(BlogEntry blogEntry, String usersEMail, String message) throws DAOException {
		
		EntityManager em = JPAEMProvider.getEntityManager();

		BlogComment blogComment = new BlogComment();
		blogComment.setBlogEntry(blogEntry);
		blogComment.setUsersEMail(usersEMail);
		blogComment.setMessage(message);
		blogComment.setPostedOn(new Date());
		
		blogEntry.getComments().add(blogComment);
		
		em.persist(blogComment);
		
		return blogComment;
	}

	@Override
	public BlogEntry updateBlogEntry(BlogEntry blogEntry, String title, String text) throws DAOException {
		
		blogEntry.setTitle(title);
		blogEntry.setText(text);
		blogEntry.setLastModifiedAt(new Date());
		
		return blogEntry;
	}

}