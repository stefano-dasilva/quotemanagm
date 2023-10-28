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
import it.polimi.tiw.projects.dao.*;
import it.polimi.tiw.projects.controllers.*;

import it.polimi.tiw.projects.utils.ConnectionHandler;

/**
 * Servlet implementation class GetDetails
 */
@WebServlet("/GetDetails")
public class GetDetails extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	private TemplateEngine templateEngine;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetDetails() {
        super();
        // TODO Auto-generated constructor stub
    }

	public void init() throws ServletException {
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");

		connection = ConnectionHandler.getConnection(getServletContext());
	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// If the user is not logged in (not present in session) redirect to the login
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

				// get and check params
				Integer quoteID = null;
				try {
					quoteID = Integer.parseInt(request.getParameter("quoteid"));
				} catch (NumberFormatException | NullPointerException e) {
					// only for debugging e.printStackTrace();
					response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect param values");
					return;
				}

				// If a mission with that ID exists for that USER,
				// obtain the expense report for it
				User user = (User) session.getAttribute("user");
				QuoteDAO quoteDAO = new QuoteDAO(connection);
				Quote quote = null;
				
				try {
					quote = quoteDAO.findQuoteByID(quoteID);}
				catch (SQLException e) {
					e.printStackTrace();
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to recover mission");
					return;
				}
				
				if(quote ==null) {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Non esiste questo preventivo");
					return;
				}
				
				if(quote.getOwnerClient() != user.getID()) {
						response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Non hai creato questo preventivo");
						return;
					}
				
				
				UserDAO userDAO=new UserDAO(connection);
				User useremployee=null;
				
				try {
					useremployee = userDAO.findUserByID(quote.getOwnerEmployee());}
				catch (SQLException e) {
					e.printStackTrace();
					//response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to recover mission");
					return;
				}
				
				
					
					ProductOptionDAO poDAO = new ProductOptionDAO(connection);
					ProductOption productoption = null;
					try {
					productoption = poDAO.findProductOptionByID(quote.getProductoption());
				} catch (SQLException e) {
					e.printStackTrace();
					//response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to recover mission");
					return;
				}
				
				ProductDAO pDAO=new ProductDAO(connection);
				Product product=null;
				
				try {
					product = pDAO.findProductByID(productoption.getProductID());}
					catch (SQLException e) {
						e.printStackTrace();
						//response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to recover mission");
						return;
					}
			/*
			 	
			 DA FARE DOMANI
				OptionDAO oDAO=new OptionDAO(connection);
				Option option=null;
				
				try {
					option = oDAO.findOptionByID(productoption.getOptionID());}
					catch (SQLException e) {
						e.printStackTrace();
						//response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to recover mission");
						return;
					}
									ctx.setVariable("opzione", option);

				*/
				
				try {
					product = pDAO.findProductByID(productoption.getProductID());}
					catch (SQLException e) {
						e.printStackTrace();
						//response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to recover mission");
						return;
					}
				
				QuoteDAO qDAO = new QuoteDAO(connection);
				List<Quote> quotes = new ArrayList<Quote>();
				
				
				try {
					quotes = qDAO.findQuotesByUser(user.getID());
				} catch (SQLException e) {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to recover quotes");
					return;
				}
				
				
				// Redirect to the Home page and add missions to the parameters
				int clickedlink = 1;
				
				
				String image = "data:image/png;base64," + product.getImage();
				
				String path = "/WEB-INF/HomeClient.html";
				ServletContext servletContext = getServletContext();
				final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
				ctx.setVariable("quotenuovo", quote);
				ctx.setVariable("useremployee", useremployee);
				ctx.setVariable("prodotto", product);
				ctx.setVariable("clickedlink", 1);
				ctx.setVariable("hide",1);
				ctx.setVariable("quotes", quotes);
				ctx.setVariable("image", image);
				templateEngine.process(path, ctx, response.getWriter());
			}
				
		
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
