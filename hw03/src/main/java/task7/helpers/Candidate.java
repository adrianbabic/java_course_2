package task7.helpers;

public class Candidate {
	int id;
	String name;
	String song;

	public Candidate(int id, String name, String song) {
		super();
		this.id = id;
		this.name = name;
		this.song = song;
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
}