package hr.fer.oprpp2.p08.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "indexRedirectServlet", urlPatterns = { "/index.html" })
public class IndexRedirectServlet extends HttpServlet {
	
    private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
        String redirectUrl = request.getContextPath() + "/servleti/index.html";
        response.sendRedirect(redirectUrl);
    }
}