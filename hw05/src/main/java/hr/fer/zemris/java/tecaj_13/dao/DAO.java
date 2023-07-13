package hr.fer.zemris.java.tecaj_13.dao;

import java.util.List;

import hr.fer.zemris.java.tecaj_13.model.BlogComment;
import hr.fer.zemris.java.tecaj_13.model.BlogEntry;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;

public interface DAO {

	/**
	 * Dohvaća entry sa zadanim <code>id</code>-em. Ako takav entry ne postoji,
	 * vraća <code>null</code>.
	 * 
	 * @param id ključ zapisa
	 * @return entry ili <code>null</code> ako entry ne postoji
	 * @throws DAOException ako dođe do pogreške pri dohvatu podataka
	 */
	public BlogEntry getBlogEntry(Long id) throws DAOException;
	
	public BlogUser getBlogUserWithNick(String nick) throws DAOException;
	
	public BlogUser addBlogUser(String firstName, String lastName, String email, String nick, String passwordHash)
			throws DAOException;
	
	public List<BlogUser> getAllAuthors() throws DAOException;
	
	public BlogEntry addBlogEntry(String title, String text, BlogUser creator) throws DAOException;
	
	public BlogComment addBlogComment(BlogEntry blogEntry, String usersEMail, String message) throws DAOException;
	
	public BlogEntry updateBlogEntry(BlogEntry blogEntry, String title, String text) throws DAOException;
	
}