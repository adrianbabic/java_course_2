package task2.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ts1", urlPatterns = { "/trigonometric" })
public class TrigonometricServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public static class AngleAndTrigFunctions {
		int angle;
		double sinValue;
		double cosValue;

		public AngleAndTrigFunctions(int angle) {
			super();
			this.angle = angle;
			
//			DecimalFormat df= new DecimalFormat("#.####");
//			this.sinValue = df.format(Math.sin(angle));
//			this.cosValue = df.format(Math.cos(angle));
			this.sinValue = Math.sin(angle);
			this.cosValue = Math.cos(angle);
		}

		public int getAngle() {
			return angle;
		}

		public double getSinValue() {
			return sinValue;
		}

		public double getCosValue() {
			return cosValue;
		}


	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		Integer a = 0, b = 360;

		try {
			a = Integer.valueOf(req.getParameter("a"));
		} catch (Exception ignorable) {
		}

		try {
			b = Integer.valueOf(req.getParameter("b"));
		} catch (Exception ignorable) {
		}

		if (a > b) {
			Integer tmp = a;
			a = b;
			b = tmp;
		}
		
		if(b > a + 720) {
			b = a + 720;
		}

		List<AngleAndTrigFunctions> results = new ArrayList<>();
		for (int i = a; i <= b; i++) 
			results.add(new AngleAndTrigFunctions(i));

		req.setAttribute("results", results);

		req.getRequestDispatcher("/WEB-INF/pages/trigonometric.jsp").forward(req, resp);
	}

}
