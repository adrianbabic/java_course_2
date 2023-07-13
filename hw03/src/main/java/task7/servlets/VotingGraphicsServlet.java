package task7.servlets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
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

import task7.helpers.CandidateWithResult;
import task7.helpers.FileHandler;


@WebServlet(name = "votingGraphics", urlPatterns = { "/glasanje-grafika" })
public class VotingGraphicsServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String definitionFileName = req.getServletContext().getRealPath("/WEB-INF/voting/voting-definition.txt");
		String fileName = req.getServletContext().getRealPath("/WEB-INF/voting/voting-results.txt");
		File file = new File(fileName);

		
		if (!file.exists() || file.length() == 0) {

			if (!Files.exists(Path.of(definitionFileName))) {
				req.setAttribute("error", "File voting-definition.txt couldn't be found!");
				req.getRequestDispatcher("/WEB-INF/voting/votingError.jsp").forward(req, resp);
			}

			List<String> loadedCandidates = FileHandler.getInitializedCandidates(definitionFileName);
			FileHandler.writeToFile(fileName, loadedCandidates);
		}
		
		List<CandidateWithResult> results = FileHandler.getCandidateResults(fileName, definitionFileName);
		
		resp.setContentType("image/png");

		OutputStream outputStream = resp.getOutputStream();

		JFreeChart chart = getChart(results);
		int width = 500;
		int height = 350;
		ChartUtilities.writeChartAsPNG(outputStream, chart, width, height);
	}

	public JFreeChart getChart(List<CandidateWithResult> results) {
		DefaultPieDataset dataset = new DefaultPieDataset();
		
		for(CandidateWithResult one: results) {
			dataset.setValue(one.getName(), one.getVotes());
		}
		boolean legend = true;
		boolean tooltips = false;
		boolean urls = false;

		JFreeChart chart = ChartFactory.createPieChart("Koji je Vaš najdraži bend?", dataset, legend,
				tooltips, urls);

		chart.setBorderPaint(Color.BLACK);
		chart.setBorderStroke(new BasicStroke(2.0f));
		chart.setBorderVisible(true);

		return chart;
	}

}