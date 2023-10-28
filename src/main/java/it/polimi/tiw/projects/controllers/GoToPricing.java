package it.polimi.tiw.projects.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

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
 * Servlet implementation class GoToPricing
 */
@WebServlet("/GoToPricing")
public class GoToPricing extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	private TemplateEngine templateEngine;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoToPricing() {
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
		
		Integer quoteID = null;
		try {
			quoteID = Integer.parseInt(request.getParameter("quoteid"));
		} catch (NumberFormatException | NullPointerException e) {
			// only for debugging e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect param values");
			return;
		}
		
		QuoteDAO qDAO = new QuoteDAO(connection);
		Quote quote=new Quote();
		
		try {
			quote = qDAO.findQuoteByID(quoteID);
			if(quote.getOwnerEmployee()==u.getID())
			{
				
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Hai già prezzato questo preventivo");
				return;
			}
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to recover quotes");
			return;
		}
		
		UserDAO uDAO=new UserDAO(connection);
		User user=new User();
		
		try {
			user = uDAO.findUserByID(quote.getOwnerClient());
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to recover quotes");
			return;
		}
		
		ProductOptionDAO poDAO=new ProductOptionDAO(connection);
		ProductOption productoption=new ProductOption();
		
		try {
			productoption = poDAO.findProductOptionByID(quote.getProductoption());
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to recover quotes");
			return;
		}
		
		ProductDAO pDAO=new ProductDAO(connection);
		Product product=new Product();
		
		try {
			product = pDAO.findProductByID(productoption.getProductID());
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to recover quotes");
			return;
		}
		
		
		
		try {
			product = pDAO.findProductByID(productoption.getProductID());
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to recover quotes");
			return;
		}
		
		
		OptionDAO oDAO = new OptionDAO(connection);
		Option option = null;
		try {
			option = oDAO.findOptionByID(productoption.getOptionID());
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to recover quotes");
			return;
		}
		

		
	
		String path = "/WEB-INF/PriceQuote.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
			
		ctx.setVariable("quote", quote);
		ctx.setVariable("user", user);
		ctx.setVariable("product", product);
		ctx.setVariable("option", option);
		
		templateEngine.process(path, ctx, response.getWriter());
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
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
