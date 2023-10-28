package it.polimi.tiw.projects.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
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
 * Servlet implementation class CreateQuote
 */
@WebServlet("/CreateQuote")
public class CreateQuote extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	private TemplateEngine templateEngine;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateQuote() {
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
		
		
		String loginpath = getServletContext().getContextPath() + "/LoginPage.html";
		User u = null;
		HttpSession s = request.getSession();
		if (s.isNew() || s.getAttribute("user") == null) {
			response.sendRedirect(loginpath);
			return;
		} else {
			u = (User) s.getAttribute("user");
			if (!u.getRole().equals("client")) {
				String path = getServletContext().getContextPath();
				String target = "/GoToHomeEmployee";
				response.sendRedirect(path+target);
				return;
			}
		}
		
		
		Integer productID = null;
		Integer userID = null;
		int optionsID[] = null;

		
		//Integer optionID= null;
		
		String[] options= request.getParameterValues("checkboxid");
		
		optionsID = Arrays.stream(options)
                 .mapToInt(Integer::parseInt)
                 .toArray();
		
		try {
			productID = Integer.parseInt(request.getParameter("prodottoid"));	
			
		
		} catch (NullPointerException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "INCORRECT PARAMETER VALUES");
			return;
		}
		
		
	
		/**
		try {
			optionID = Integer.parseInt(request.getParameter("optionid"));	
			
		
		} catch (NullPointerException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "non riesco a passare productID");
			return;
		}
		*/
		
		/**
		
		try {
			clientID = Integer.parseInt(request.getParameter("clientid"));	
			
		
		} catch (NullPointerException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "non riesco a passare productID");
			return;
		}
		*/
		
		ProductOptionDAO poDAO = new ProductOptionDAO(connection);
		ProductOption productoption = new ProductOption();
	
		/**
		try {
			productoption = poDAO.findProductOptionIDbyProductIDandOptionID(productID, optionID);
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to recover quotes");
			return;
		}
		
		
		QuoteDAO qDAO=new QuoteDAO(connection);
		
		try {
			qDAO.insertQuote(productoption.getID(),clientID);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not Possible to insert quote");
			
	}
	*/
for(int optionID : optionsID) {
			
			try {
				productoption = poDAO.findProductOptionIDbyProductIDandOptionID(productID, optionID);
			} catch (SQLException e) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().println("Not possible to recover productoption");
				return;
			}
			
			if(productoption == null) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Hai immesso una offerta o prodotto inesistenti");
				return;
			}
			
			HttpSession session = request.getSession();	
			
			userID = u.getID();
			
			QuoteDAO qDAO = new QuoteDAO(connection);
			
			try {
				qDAO.insertQuote(productoption.getID(), userID);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().println("Not possible to insert quotes");
				return;
				
		}
}
		
		response.sendRedirect("GoToHomeClient");
		
		/**
		String path = "/WEB-INF/HomeClient.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		templateEngine.process(path, ctx, response.getWriter());
		**/

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
