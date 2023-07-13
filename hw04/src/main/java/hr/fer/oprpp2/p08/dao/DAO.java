package hr.fer.oprpp2.p08.dao;

import java.util.List;

import hr.fer.oprpp2.p08.models.Poll;
import hr.fer.oprpp2.p08.models.PollOption;

/**
 * Sučelje prema podsustavu za perzistenciju podataka.
 * 
 * @author marcupic
 *
 */
public interface DAO {

	/**	Fetches all polls available in the database. Stores all information.
	 * 
	 * 	@return list of polls 
	 * 	@throws DAOException
	 */
	public List<Poll> fetchDefinedPolls() throws DAOException;
	
	/**	Fetches all options for the poll with provided id.
	 * 
	 * 	@param pollId
	 * 	@return list of options
	 * 	@throws DAOException
	 */
	public List<PollOption> fetchPollOptionsByPoolId(long pollId) throws DAOException;
	
	/**	Returns poll mapped to the given id.
	 * 
	 * 	@param pollId
	 * 	@return	poll
	 * 	@throws DAOException
	 */
	public Poll fetchPollWithId(long pollId) throws DAOException;
	
	/**	Updates the poll options row with the given id. Increments votesCount by one.
	 * 	Returns if the operation was a success.
	 * 
	 * 	@param id
	 * 	@return	true if operation was a success, false if it was a failure
	 * 	@throws DAOException
	 */
	public boolean incrementVoteCountWithPollOptionId(long id) throws DAOException;
	
	/**	Method will return poll id matched with the given poll option's id.
	 * 
	 * 	@param pollOptionId
	 * 	@return long value of poll's id
	 * 	@throws DAOException
	 */
	public long getPollIdWithPollOptionId(long pollOptionId) throws DAOException;
	
	/**	Finds all poll options that are matched to the given poll id. Out of those poll options,
	 * 	return the one with the highest votes count. If there are multiple poll options with the same 
	 * 	highest value, return all of those with that value.
	 * 
	 * 	@param pollId
	 * 	@return	poll options with highest votes count
	 * 	@throws DAOException
	 */
	public List<PollOption> fetchPollOptionsWithHighestVotes(long pollId) throws DAOException;
}