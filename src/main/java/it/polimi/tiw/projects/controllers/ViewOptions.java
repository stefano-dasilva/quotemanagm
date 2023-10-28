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

import it.polimi.tiw.projects.beans.*;
import it.polimi.tiw.projects.controllers.*;
import it.polimi.tiw.projects.dao.*;
import it.polimi.tiw.projects.utils.ConnectionHandler;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ViewOptions
 */
@WebServlet("/ViewOptions")
public class ViewOptions extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	private TemplateEngine templateEngine;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ViewOptions() {
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
String productID_param = request.getParameter("productid");
		
		if (productID_param == null || productID_param.isEmpty()) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
			return;
		}
		}
		

	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String loginpath = getServletContext().getContextPath() + "/LoginPage.html";
		User u = null;
		HttpSession session = request.getSession();
		if (session.isNew() || session.getAttribute("user") == null) {
			response.sendRedirect(loginpath);
			return;
		} else {
			u = (User) session.getAttribute("user");
			if (!u.getRole().equals("client")) {
				String path = getServletContext().getContextPath();
				String target = "/GoToHomeEmployee";
				response.sendRedirect(path+target);
				return;
			}
		}

		
		
		String productID_param = request.getParameter("productid");
		
		if (productID_param == null || productID_param.isEmpty()) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
			return;
		}
		
		Integer productID = -1;
		
		try {
			productID = Integer.parseInt(productID_param);	

		} catch (NumberFormatException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
			return;
		}
		
		ProductDAO pDAO = new ProductDAO(connection);
		Product product = new Product();
		
		try {
			product = pDAO.findProductByID(productID);	

		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Questo prodotto non esiste");
		}
		
		if(product==null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Questo prodotto non esiste");
		}
		
		Integer clientID = null;
		
		try {
			clientID = Integer.parseInt(request.getParameter("clientid"));	
		
		} catch (NullPointerException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect or missing param values");
			return;
		}
		
		
		
		ProductOptionDAO poDAO = new ProductOptionDAO(connection);
		List<ProductOption> productoptions = new ArrayList<ProductOption>();
		
		try {
			productoptions = poDAO.findtOptionsIDByProductID(productID);
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to recover products");
			return;
		}
	
		int i=0;
		OptionDAO oDAO = new OptionDAO(connection);
		List<Option> options=new ArrayList<Option>();
		Option option=new Option();
		
		for(i=0;i<productoptions.size();i++) {
			try {
				option = oDAO.findOptionByID(productoptions.get(i).getOptionID());
			} catch (SQLException e) {
				e.printStackTrace();
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to recover CIAO");
				return;
			}
			options.add(option);
		}
		
		QuoteDAO qDAO=new QuoteDAO(connection);
		List<Quote> quotes=new ArrayList<Quote>();
		
		try {
			quotes = qDAO.findQuotesByUser(clientID);
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to recover products");
			return;
		}
		
		
		/**
		ProductDAO pDAO=new ProductDAO(connection);
		Product product=new Product();
		
		try {
			product = pDAO.findProductByID(productID);
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to recover product");
			return;
		}
		
		**/
		
	/**
		OptionDAO oDAO=new OptionDAO(connection);
		Option option=new Option();
		
		try {
			option = oDAO.findOptionByID(3);
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to recover quotes");
			return;
		}
		
		**/
		
		/**
		int i=0;
		OptionDAO oDAO=new OptionDAO(connection);
		List<Option> options=new ArrayList<Option>();
		Option option=new Option();
		
		for(i=0;i<productoptions.size();i++) {
			try {
				option = oDAO.findOptionByID(productoptions.get(i).getOptionID());
			} catch (SQLException e) {
				e.printStackTrace();
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to recover CIAO");
				return;
			}
			options.add(option);
		}
	**/

	String path = "/WEB-INF/HomeClient.html";
	ServletContext servletContext = getServletContext();
	final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
	
	//ctx.setVariable("opzioni", options);
	ctx.setVariable("selected", 1);
	ctx.setVariable("opzioni", options);
	ctx.setVariable("prodottoid", productID);
	ctx.setVariable("quotes", quotes);

	//ctx.setVariable("opzioni", options);
	
	
	templateEngine.process(path, ctx, response.getWriter());
	
	
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
