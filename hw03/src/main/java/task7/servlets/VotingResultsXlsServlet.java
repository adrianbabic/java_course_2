package task7.servlets;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import task7.helpers.CandidateWithResult;
import task7.helpers.FileHandler;

@WebServlet(name = "votingResultsXls", urlPatterns = { "/glasanje-xls" })
public class VotingResultsXlsServlet extends HttpServlet {

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

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Bands and votes");
		int rowNumber = 0;

		HSSFRow row = sheet.createRow(rowNumber++);

		HSSFCell cell1 = row.createCell(0);
		cell1.setCellValue("Band");

		HSSFCell cell2 = row.createCell(1);
		cell2.setCellValue("Votes");
		
		HSSFCell cell3 = row.createCell(2);
		cell3.setCellValue("Link to an example of their song");
		
		for(CandidateWithResult one: results) {
			row = sheet.createRow(rowNumber++);
			cell1 = row.createCell(0);
			cell1.setCellValue(one.getName());

			cell2 = row.createCell(1);
			cell2.setCellValue(one.getVotes());
			
			cell3 = row.createCell(2);
			cell3.setCellValue(one.getSong());
		}

		resp.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		resp.setHeader("Content-Disposition", "attachment; filename=table.xls");
		workbook.write(resp.getOutputStream());
		workbook.close();
	}

}