package hr.fer.oprpp2.p08.sql;

/**	Class contains some SQL commands which are used mainly for initializing the database and populating it with examples.
 * 
 * 	@author adrian
 *
 */
public class Commands {
	
	public static final String INSERT_POLLS_FAVORITE_BAND = "INSERT INTO Polls (title, message) VALUES ('Glasanje"
			+ " za omiljeni bend:', 'Od sljedećih bendova, koji Vam je bend najdraži? Kliknite na link kako biste glasali!')";
	
	public static final String INSERT_POLLS_FAVORITE_MOVIE = "INSERT INTO Polls (title, message) VALUES ('Glasanje za"
			+ " omiljeni film:', 'Od sljedećih filmova, koji Vam je film najdraži? Kliknite na link kako biste glasali!')";
	
	public static final String CREATE_TABLE_POLLOPTIONS = "CREATE TABLE PollOptions "
			+ "(id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " + "optionTitle VARCHAR(100) NOT NULL, "
			+ "optionLink VARCHAR(150) NOT NULL, " + "pollID BIGINT, " + "votesCount BIGINT, "
			+ "FOREIGN KEY (pollID) REFERENCES Polls(id))";
	
	public static final String CREATE_TABLE_POLLS = "CREATE TABLE Polls " + "(id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
			+ "title VARCHAR(150) NOT NULL, " + "message CLOB(2048) NOT NULL)";
	
	public static String insertBandOptions(Long pollId) {
		return "INSERT INTO PollOptions (optionTitle, optionLink, pollID, votesCount) VALUES "
				+ "('The Beatles', 'https://www.youtube.com/watch?v=z9ypq6_5bsg', " + pollId + ", 0),"
				+ "('The Platters','https://www.youtube.com/watch?v=H2di83WAOhU', "+ pollId +", 0),"
				+ "('The Beach Boys','https://www.youtube.com/watch?v=2s4slliAtQU', "+ pollId +", 0),"
				+ "('The Four Seasons','https://www.youtube.com/watch?v=y8yvnqHmFds'," + pollId + " , 0),"
				+ "('The Marcels','https://www.youtube.com/watch?v=qoi3TH59ZEs', " + pollId + ", 0),"
				+ "('The Everly Brothers','https://www.youtube.com/watch?v=tbU3zdAgiX8', " + pollId + ", 0),"
				+ "('The Mamas And The Papas', 'https://www.youtube.com/watch?v=N-aK6JnyFmk', " + pollId + ", 0)";
	}
	
	public static String insertMovieOptions(Long pollId) {
		return "INSERT INTO PollOptions (optionTitle, optionLink, pollID, votesCount) VALUES "
				+ "('Fight Club', 'https://www.youtube.com/watch?v=qtRKdVHc-cE', " + pollId + ", 0),"
				+ "('The Godfather','https://www.youtube.com/watch?v=UaVTIH8mujA', "+ pollId +", 0),"
				+ "('The Usual Suspects','https://www.youtube.com/watch?v=oiXdPolca5w', "+ pollId +", 0),"
				+ "('Joker','https://www.youtube.com/watch?v=zAGVQLHvwOY'," + pollId + " , 0),"
				+ "('The Prestige','https://www.youtube.com/watch?v=RLtaA9fFNXU', " + pollId + ", 0)";
	}
}
