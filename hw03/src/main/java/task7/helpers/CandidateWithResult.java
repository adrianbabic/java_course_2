package task7.helpers;

public class CandidateWithResult {
	
	int id;
	String name;
	String song;
	int votes;
	
	public CandidateWithResult(Candidate can, int votes) {
		this.id = can.getId();
		this.name = can.getName();
		this.song = can.getSong();
		this.votes = votes;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getSong() {
		return song;
	}

	public int getVotes() {
		return votes;
	}
}