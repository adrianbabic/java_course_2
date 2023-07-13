package hr.fer.oprpp2.p08.dao.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import hr.fer.oprpp2.p08.TableManipulation;
import hr.fer.oprpp2.p08.dao.DAO;
import hr.fer.oprpp2.p08.dao.DAOException;
import hr.fer.oprpp2.p08.models.Poll;
import hr.fer.oprpp2.p08.models.PollOption;

/**
 * Ovo je implementacija podsustava DAO uporabom tehnologije SQL. Ova konkretna
 * implementacija očekuje da joj veza stoji na raspolaganju preko
 * {@link SQLConnectionProvider} razreda, što znači da bi netko prije no što
 * izvođenje dođe do ove točke to trebao tamo postaviti. U web-aplikacijama
 * tipično rješenje je konfigurirati jedan filter koji će presresti pozive
 * servleta i prije toga ovdje ubaciti jednu vezu iz connection-poola, a po
 * zavrsetku obrade je maknuti.
 * 
 * @author marcupic
 */
public class SQLDAO implements DAO {

	@Override
	public List<Poll> fetchDefinedPolls() throws DAOException {

		List<Poll> polls = new ArrayList<>();
		Connection con = SQLConnectionProvider.getConnection();
		try {

			if (!TableManipulation.tableExists(con, "Polls"))
				return polls;
		} catch (SQLException ignorable) {
		}
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement("SELECT * FROM Polls order by id");
			try {
				ResultSet rs = pst.executeQuery();
				try {
					while (rs != null && rs.next()) {
						Poll poll = new Poll();
						poll.setId(rs.getLong(1));
						poll.setTitle(rs.getString(2));
						poll.setMessage(rs.getString(3));
						polls.add(poll);
					}
				} finally {
					try {
						rs.close();
					} catch (Exception ignorable) {
					}
				}
			} finally {
				try {
					pst.close();
				} catch (Exception ignorable) {
				}
			}
		} catch (Exception ex) {
			throw new DAOException("Error occured while trying to fetch defined polls.", ex);
		}
		return polls;
	}

	@Override
	public List<PollOption> fetchPollOptionsByPoolId(long pollId) throws DAOException {

		if (pollId < 0)
			throw new DAOException("Poll id must be a positive number.");
		List<PollOption> options = new ArrayList<>();
		Connection con = SQLConnectionProvider.getConnection();
		try {

			if (!TableManipulation.tableExists(con, "PollOptions"))
				return options;
		} catch (SQLException ignorable) {
		}
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement("SELECT * FROM PollOptions WHERE pollID = " + pollId);
			try {
				ResultSet rs = pst.executeQuery();
				try {
					while (rs != null && rs.next()) {
						PollOption option = new PollOption();
						option.setId(rs.getLong(1));
						option.setTitle(rs.getString(2));
						option.setLink(rs.getString(3));
						option.setPollId(pollId);
						option.setVotesCount(rs.getLong(5));
						options.add(option);
					}
				} finally {
					try {
						rs.close();
					} catch (Exception ignorable) {
					}
				}
			} finally {
				try {
					pst.close();
				} catch (Exception ignorable) {
				}
			}
		} catch (Exception ex) {
			throw new DAOException("Error occured while trying to fetch poll options from poll with id: " + pollId, ex);
		}
		return options;
	}

	@Override
	public Poll fetchPollWithId(long pollId) throws DAOException {
		
		Poll poll = null;
		Connection con = SQLConnectionProvider.getConnection();
		try {

			if (!TableManipulation.tableExists(con, "Polls"))
				return poll;
		} catch (SQLException ignorable) {
		}
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement("SELECT * FROM Polls WHERE id = " + pollId);
			try {
				ResultSet rs = pst.executeQuery();
				try {
					while (rs != null && rs.next()) {
						poll = new Poll();
						poll.setId(rs.getLong(1));
						poll.setTitle(rs.getString(2));
						poll.setMessage(rs.getString(3));
					}
				} finally {
					try {
						rs.close();
					} catch (Exception ignorable) {
					}
				}
			} finally {
				try {
					pst.close();
				} catch (Exception ignorable) {
				}
			}
		} catch (Exception ex) {
			throw new DAOException("Error occured while trying to fetch defined polls.", ex);
		}
		return poll;
	}

	@Override
	public boolean incrementVoteCountWithPollOptionId(long id) throws DAOException {
		
		boolean success = false;
		if (id < 0)
			throw new DAOException("PollOption id must be a positive number.");
		Connection con = SQLConnectionProvider.getConnection();
		try {

			if (!TableManipulation.tableExists(con, "PollOptions"))
				return success;
		} catch (SQLException ignorable) {
		}
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement("UPDATE PollOptions SET votesCount = votesCount + 1 WHERE id = " + id);
			
			try {
				int rowsAffected = pst.executeUpdate();
				if(rowsAffected > 0) 
					success = true;
			} finally {
				try {
					pst.close();
				} catch (Exception ignorable) {
				}
			}
		} catch (Exception ex) {
			throw new DAOException("Error occured while trying to increment poll option with id: " + id, ex);
		}
		return success;
	}

	@Override
	public long getPollIdWithPollOptionId(long pollOptionId) throws DAOException {
		
		long pollId = 0;
		Connection con = SQLConnectionProvider.getConnection();
		try {

			if (!TableManipulation.tableExists(con, "PollOptions"))
				return pollId;
		} catch (SQLException ignorable) {
		}
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement("SELECT * FROM PollOptions WHERE id = " + pollOptionId);
			try {
				ResultSet rs = pst.executeQuery();
				try {
					while (rs != null && rs.next()) {
						pollId = rs.getLong(4);
					}
				} finally {
					try {
						rs.close();
					} catch (Exception ignorable) {
					}
				}
			} finally {
				try {
					pst.close();
				} catch (Exception ignorable) {
				}
			}
		} catch (Exception ex) {
			throw new DAOException("Error occured while trying to fetch defined polls.", ex);
		}
		return pollId;
	}

	@Override
	public List<PollOption> fetchPollOptionsWithHighestVotes(long pollId) throws DAOException {
		
		if (pollId < 0)
			throw new DAOException("Poll id must be a positive number.");
		List<PollOption> options = new ArrayList<>();
		Connection con = SQLConnectionProvider.getConnection();
		try {

			if (!TableManipulation.tableExists(con, "PollOptions"))
				return options;
		} catch (SQLException ignorable) {
		}
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement("SELECT * FROM PollOptions "
					+ "WHERE pollId = " + pollId 
					+ " AND votesCount = (SELECT MAX(votesCount) FROM PollOptions WHERE pollId = " + pollId + ")");
			try {
				ResultSet rs = pst.executeQuery();
				try {
					while (rs != null && rs.next()) {
						PollOption option = new PollOption();
						option.setId(rs.getLong(1));
						option.setTitle(rs.getString(2));
						option.setLink(rs.getString(3));
						option.setPollId(pollId);
						option.setVotesCount(rs.getLong(5));
						options.add(option);
					}
				} finally {
					try {
						rs.close();
					} catch (Exception ignorable) {
					}
				}
			} finally {
				try {
					pst.close();
				} catch (Exception ignorable) {
				}
			}
		} catch (Exception ex) {
			throw new DAOException("Error occured while trying to fetch poll options with maximum votes"
					+ " count from poll with id: " + pollId, ex);
		}
		return options;
	}

}