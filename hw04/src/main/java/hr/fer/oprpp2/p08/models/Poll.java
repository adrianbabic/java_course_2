package hr.fer.oprpp2.p08.models;

/**	This class is used for encapsulating rows from Polls table.
 * 
 * 	@author adrian
 */
public class Poll {
	long id;
	String title;
	String message;

	public Poll() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "Poll [id=" + id +"]";
	}
}