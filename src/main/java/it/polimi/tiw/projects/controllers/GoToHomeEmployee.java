package it.polimi.tiw.projects.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.tiw.projects.beans.Quote;
import it.polimi.tiw.projects.beans.User;
import it.polimi.tiw.projects.dao.QuoteDAO;
import it.polimi.tiw.projects.utils.ConnectionHandler;


@WebServlet("/GoToHomeEmployee")
public class GoToHomeEmployee extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	private TemplateEngine templateEngine;
       

    public GoToHomeEmployee() {
        super();
       
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

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String loginpath = getServletContext().getContextPath() + "/LoginPage.html";
		User u = null;
		HttpSession s = request.getSession();
		if (s.isNew() || s.getAttribute("user") == null) {
			response.sendRedirect(loginpath);
			return;
		} else {
			u = (User) s.getAttribute("user");
			if (!u.getRole().equals("employee")) {
				String path = getServletContext().getContextPath();
				String target = "/GoToHomeClient";
				response.sendRedirect(path+target);
				return;
			}
		}
		
		QuoteDAO ndqDAO = new QuoteDAO(connection);
		List<Quote> notdealedquotes = new ArrayList<Quote>();
		
		try {
			notdealedquotes = ndqDAO.findnotDealed();
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to recover quotes");
			return;
		}
		
		QuoteDAO qDAO = new QuoteDAO(connection);
		List<Quote> quotes = new ArrayList<Quote>();
		try {
			quotes = qDAO.findQuotesByEmployee(u.getID());
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to recover quotes");
			return;
		}
		
		
		String path = "/WEB-INF/HomeEmployee.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		ctx.setVariable("notdealedquotes", notdealedquotes);
		ctx.setVariable("quotes", quotes);
		
		templateEngine.process(path, ctx, response.getWriter());
	}
	

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	public void destroy() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException sqle) {
		}
	}

}
