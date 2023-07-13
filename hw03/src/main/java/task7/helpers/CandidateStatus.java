package task7.helpers;

public class CandidateStatus {
	
	int id;
	int votes;
	
	public CandidateStatus(int id, int votes) {
		super();
		this.id = id;
		this.votes = votes;
	}

	public int getId() {
		return id;
	}

	public int getVotes() {
		return votes;
	}

	public void setVotes(int votes) {
		this.votes = votes;
	}
}
