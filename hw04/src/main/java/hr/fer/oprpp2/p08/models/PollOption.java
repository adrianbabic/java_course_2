package hr.fer.oprpp2.p08.models;

/**	This class is used for encapsulating rows from PollOptions table.
 * 
 * 	@author adrian
 */
public class PollOption {
	
	long id;
	String title;
	String link;
	long pollId;
	long votesCount;
	
	public PollOption() {
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

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public long getPollId() {
		return pollId;
	}

	public void setPollId(long pollId) {
		this.pollId = pollId;
	}

	public long getVotesCount() {
		return votesCount;
	}

	public void setVotesCount(long votesCount) {
		this.votesCount = votesCount;
	}

	@Override
	public String toString() {
		return "PollOption [id=" + id + ", pollId=" + pollId + ", votesCount=" + votesCount + "]";
	}
}
