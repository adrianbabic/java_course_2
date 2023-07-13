package hr.fer.oprpp2.p08.servlets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import hr.fer.oprpp2.p08.dao.DAOProvider;
import hr.fer.oprpp2.p08.models.Poll;
import hr.fer.oprpp2.p08.models.PollOption;

/**	Servlet that generates pie chart based on the current voting count.
 * 
 * 	@author adrian
 */
@WebServlet(name = "pollResultsGraphics", urlPatterns = { "/servleti/glasanje-grafika" })
public class PollResultsGraphicsServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
				
		String pollIdString = req.getParameter("pollID");
		long pollId = -1;
		try {
			pollId = Long.parseLong(pollIdString);
		} catch (NumberFormatException ex) {
		}
		
		List<PollOption> options = DAOProvider.getDao().fetchPollOptionsByPoolId(pollId);
		Poll poll = DAOProvider.getDao().fetchPollWithId(pollId);
		
		resp.setContentType("image/png");

		OutputStream outputStream = resp.getOutputStream();

		JFreeChart chart = getChart(options, poll);
		int width = 500;
		int height = 350;
		ChartUtilities.writeChartAsPNG(outputStream, chart, width, height);
	}

	/**	Creates a pie chart based on the provided list of results.
	 * 
	 * 	@param results
	 * 	@return	JFreeChart representing pie chart object
	 */
	private JFreeChart getChart(List<PollOption> options, Poll poll) {
		DefaultPieDataset dataset = new DefaultPieDataset();
		
		for(PollOption one: options) {
			dataset.setValue(one.getTitle(), one.getVotesCount());
		}
		boolean legend = true;
		boolean tooltips = false;
		boolean urls = false;

		JFreeChart chart = ChartFactory.createPieChart(poll.getTitle(), dataset, legend,
				tooltips, urls);

		chart.setBorderPaint(Color.BLACK);
		chart.setBorderStroke(new BasicStroke(2.0f));
		chart.setBorderVisible(true);

		return chart;
	}

}