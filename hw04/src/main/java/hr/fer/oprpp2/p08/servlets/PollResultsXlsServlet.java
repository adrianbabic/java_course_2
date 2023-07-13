package hr.fer.oprpp2.p08.servlets;

import java.io.IOException;
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

import hr.fer.oprpp2.p08.dao.DAOProvider;
import hr.fer.oprpp2.p08.models.PollOption;

/**	Servlet creates all XLS file based on voting results.
 * 
 * 	@author adrian
 */
@WebServlet(name = "votingResultsXls", urlPatterns = { "/servleti/glasanje-xls" })
public class PollResultsXlsServlet extends HttpServlet {

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

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Votes for poll " + pollId);
		int rowNumber = 0;

		HSSFRow row = sheet.createRow(rowNumber++);

		HSSFCell cell1 = row.createCell(0);
		cell1.setCellValue("Title");

		HSSFCell cell2 = row.createCell(1);
		cell2.setCellValue("Votes");
		
		HSSFCell cell3 = row.createCell(2);
		cell3.setCellValue("Link");
		
		for(PollOption one: options) {
			row = sheet.createRow(rowNumber++);
			cell1 = row.createCell(0);
			cell1.setCellValue(one.getTitle());

			cell2 = row.createCell(1);
			cell2.setCellValue(one.getVotesCount());
			
			cell3 = row.createCell(2);
			cell3.setCellValue(one.getLink());
		}

		resp.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		resp.setHeader("Content-Disposition", "attachment; filename=table.xls");
		workbook.write(resp.getOutputStream());
		workbook.close();
	}

}