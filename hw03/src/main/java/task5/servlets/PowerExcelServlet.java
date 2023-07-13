package task5.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

@WebServlet(name="powerExcelServlet",urlPatterns={"/powers"})
public class PowerExcelServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		int a = -200, b = -200, n = 0;

		try {
			a = Integer.valueOf(req.getParameter("a"));
			b = Integer.valueOf(req.getParameter("b"));
			n = Integer.valueOf(req.getParameter("n"));
		} catch (Exception ignorable) {
		}

		if( a < -100 || a > 100 || b < -100 || b > 100 || n < 1 || n > 5 ) {
			req.getRequestDispatcher("/WEB-INF/powers/InvalidParameters.jsp").forward(req, resp);
		}

		if (a > b) {
			Integer tmp = a;
			a = b;
			b = tmp;
		}
		

	    HSSFWorkbook workbook = new HSSFWorkbook();
	    for (int i = 1; i <= n; i++) {
	        HSSFSheet sheet = workbook.createSheet("Powers of " + i);
	        int rowNumber = 0;
	        
	        HSSFRow row = sheet.createRow(rowNumber++);
            
            HSSFCell cell1 = row.createCell(0);
            cell1.setCellValue("Number");
            
            HSSFCell cell2 = row.createCell(1);
            cell2.setCellValue("To the power of " + i);
	        for (int j = a; j <= b; j++) {
	            row = sheet.createRow(rowNumber++);
	            
	            cell1 = row.createCell(0);
	            cell1.setCellValue(j);
	            
	            cell2 = row.createCell(1);
	            cell2.setCellValue(Math.pow(j, i));
	        }
	    }

	    resp.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	    resp.setHeader("Content-Disposition", "attachment; filename=table.xls");
	    workbook.write(resp.getOutputStream());
	    workbook.close();
	}
}