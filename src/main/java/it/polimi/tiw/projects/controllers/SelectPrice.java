package it.polimi.tiw.projects.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import it.polimi.tiw.projects.beans.*;
import it.polimi.tiw.projects.controllers.*;
import it.polimi.tiw.projects.dao.*;


import it.polimi.tiw.projects.utils.ConnectionHandler;

/**
 * Servlet implementation class SelectPrice
 */
@WebServlet("/SelectPrice")
public class SelectPrice extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	private TemplateEngine templateEngine;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SelectPrice() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public void init() throws ServletException {
		connection = ConnectionHandler.getConnection(getServletContext());
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		User u = null;
		
		HttpSession session = request.getSession();
		if (session.isNew() || session.getAttribute("user") == null) {
			String loginpath = getServletContext().getContextPath() + "/LoginPage.html";
			response.sendRedirect(loginpath);
			return;
		} 
		else {
			u = (User) session.getAttribute("user");
			if (!u.getRole().equals("employee")) {
				String path = getServletContext().getContextPath();
				String target = "/GoToHomeClient";
				response.sendRedirect(path+target);
				return;
			}
		}
		
		Integer price = null;
		Integer quoteID = null;
		Integer employeeID=null;
		List<Quote> quotes = null;
		
		employeeID = u.getID();
		
		try {
			quoteID = Integer.parseInt(request.getParameter("quoteid"));
			
			
		
		} catch (NullPointerException|NumberFormatException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect or missing param values");
			return;
		}
		
		try {
			price = Integer.parseInt(request.getParameter("price"));
			if(price <= 5) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Valore troppo basso");
				return;
			}
			
		
		} catch (NullPointerException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect or missing param values");
			return;
		}
		price = Integer.parseInt(request.getParameter("price"));
		
		
		
		
		
		QuoteDAO qDAO = new QuoteDAO(connection);
		
		Quote quoteexists = new Quote();
		
		try {
			quoteexists = qDAO.findQuoteByID(quoteID);
		}
		catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "CIAO");
			return;
		}
		if(quoteexists == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "NON ESISTE QUESTO PREVENTIVO");
			return;
		}
		
		if(quoteexists!= null && quoteexists.getOwnerEmployee()== u.getID()) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "HAI GIà PREZZATO QUESTO PREVENTIVO");
			return;
		}
		
		
		try {
			quotes = qDAO.findQuotesByEmployee(u.getID());
		}
		catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "CIAO");
			return;
		}
		
		for(Quote quote : quotes) {
			if(quote.getID() == quoteID) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Hai già prezzato questo preventivo");
				return;
			}
		}
	
		
		try {
			qDAO.priceQuote(quoteID,price,employeeID);
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "CIAO");
			return;
		}
	
		
		response.sendRedirect("GoToHomeEmployee");
		
		
	/**	String path = "/WEB-INF/HomeEmployee.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		
		templateEngine.process(path, ctx, response.getWriter());**/
		
	
		
		


	}

}
