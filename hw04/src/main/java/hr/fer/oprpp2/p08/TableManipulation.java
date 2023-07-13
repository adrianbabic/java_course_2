package hr.fer.oprpp2.p08;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import hr.fer.oprpp2.p08.sql.Commands;

/**	Class has some static methods that are used for initializing the database with needed examples.
 * 
 * 	@author adrian
 */
public class TableManipulation {
	
	/**	Checks if Polls and PollOptions tables exist and if they are empty.
	 * 
	 * 	@param connection
	 */
	public static void initializeTables(Connection connection) {

		try {

			if (!tableExists(connection, "Polls"))
				createPollsTable(connection);

			if (!tableExists(connection, "PollOptions"))
				createPollOptionsTable(connection);

			if (!tableIsNotEmpty(connection, "Polls") || !tableIsNotEmpty(connection, "PollOptions"))
				populateTables(connection);
			
		} catch (SQLException e) {
			throw new RuntimeException("Error occured while trying to initialize Polls and PollOptions tables.", e);
		}
	}

	/**	Check if the table with the given table name exists.
	 * 
	 * 	@param connection for connecting to the database
	 * 	@param tableName
	 * 	@return true if table exists, false if table doesn't exist
	 * 	@throws SQLException
	 */
	public static boolean tableExists(Connection connection, String tableName) throws SQLException {

		DatabaseMetaData metaData = connection.getMetaData();
		ResultSet resultSet = metaData.getTables(null, null, tableName.toUpperCase(), null);

		boolean tableExists = resultSet.next();

		resultSet.close();

		return tableExists;
	}

	/**	Checks if the table with given table name is empty or not.
	 * 
	 * 	@param connection for connecting to the database
	 * 	@param tableName
	 * 	@return true if table is not empty, false if table is empty
	 * 	@throws SQLException
	 */
	public static boolean tableIsNotEmpty(Connection connection, String tableName) throws SQLException {

		Statement statement = connection.createStatement();
		String selectSQL = "SELECT * FROM " + tableName;
		ResultSet resultSet = statement.executeQuery(selectSQL);
		boolean tableIsEmpty = resultSet.next();

		try {
			statement.close();
			resultSet.close();
		} catch (SQLException ignorable) {

		}

		return tableIsEmpty;
	}

	/**	Creates Polls table with all necessary attributes.
	 * 
	 * 	@param connection for connecting to the database
	 * 	@throws SQLException if failure occurs during communication with database
	 */
	private static void createPollsTable(Connection connection) throws SQLException {

		try (Statement statement = connection.createStatement()) {

			String createTableSQL = Commands.CREATE_TABLE_POLLS;
			statement.execute(createTableSQL);
			System.out.println("Polls table created.");
		} catch (SQLException e) {
			throw new SQLException("Failure while creating Polls table.", e);
		}
	}

	/**	Creates PollOptions table with all the necessary attributes.
	 * 
	 * 	@param connection for connecting to the database
	 * 	@throws SQLException if failure occurs during communication with database
	 */
	private static void createPollOptionsTable(Connection connection) throws SQLException {

		try (Statement statement = connection.createStatement()) {

			String createTableSQL = Commands.CREATE_TABLE_POLLOPTIONS;
			statement.executeUpdate(createTableSQL);
			System.out.println("PollOptions table created.");

		} catch (SQLException e) {

			throw new SQLException("Failure while creating PollOptions table.", e);
		}
	}

	/**	Populates Polls and PollOptions table with bands poll and movies poll.
	 * 
	 *	@param connection for connecting to the database
	 * 	@throws SQLException if failure occurs during communication with database
	 */
	@SuppressWarnings("resource")
	private static void populateTables(Connection connection) throws SQLException {

		System.out.println("Populating tables...");

		ResultSet generatedKeys = null;
		try (Statement statement = connection.createStatement()) {

			String insertIntoPolls = Commands.INSERT_POLLS_FAVORITE_BAND;
			statement.executeUpdate(insertIntoPolls, Statement.RETURN_GENERATED_KEYS);
			generatedKeys = statement.getGeneratedKeys();
			long key;
			if (generatedKeys != null && generatedKeys.next()) {
				key = generatedKeys.getLong(1);
			} else
				throw new SQLException("INSERT bands command didn't return generated key id.");

			statement.executeUpdate(Commands.insertBandOptions(key));
			System.out.println("Initial bands poll added to the tables.");

			insertIntoPolls = Commands.INSERT_POLLS_FAVORITE_MOVIE;
			statement.executeUpdate(insertIntoPolls, Statement.RETURN_GENERATED_KEYS);
			generatedKeys = statement.getGeneratedKeys();
			if (generatedKeys != null && generatedKeys.next()) {
				key = generatedKeys.getLong(1);
			} else
				throw new SQLException("INSERT movies command didn't return generated key id.");

			statement.executeUpdate(Commands.insertMovieOptions(key));
			System.out.println("Initial movies poll added to the tables.");

		} catch (SQLException e) {

			throw new SQLException("Failure while creating PollOptions table.", e);
		} finally {
			try {
				generatedKeys.close();
			} catch (Exception ignorable) {
			}
		}
	}
}
