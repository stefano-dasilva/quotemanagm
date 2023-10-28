package it.polimi.tiw.projects.controllers;


import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.tomcat.util.file.Matcher;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.tiw.projects.beans.User;
import it.polimi.tiw.projects.dao.UserDAO;
import it.polimi.tiw.projects.utils.ConnectionHandler;

/**
 * Servlet implementation class SignUp
 */
@WebServlet("/SignUp")
public class SignUp extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	private TemplateEngine templateEngine;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SignUp() {
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
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String usrn = request.getParameter("username");
		String pwd = request.getParameter("pwd");
		String pwd2 = request.getParameter("pwd2");
		String name=request.getParameter("name");
		String surname=request.getParameter("surname");
		String type=request.getParameter("type");
		String email =request.getParameter("email");
				
		 String regex = "^(.+)@(\\S+)$";

	    
		 
			ServletContext servletContext = getServletContext();	
		
		String path = getServletContext().getContextPath();
		
		if (usrn == null || pwd == null ||name==null||surname==null||type==null|| email==null|| usrn.isEmpty() || pwd.isEmpty()||name.isEmpty()||surname.isEmpty()||type.isEmpty()) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Credenziali mancanti o inesistenti");
			return;
		}
		
		if (!pwd.equals(pwd2)) {
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
			ctx.setVariable("errorMsg", "Le password non corrispondono");
			path = "/SignUp.html";
			templateEngine.process(path, ctx, response.getWriter());
			return;
		}
		
		
		if(!Pattern.compile(regex)
	      .matcher(email)
	      .matches()) {
			
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
			ctx.setVariable("errorMsg", "Email non corretta");
			path = "/SignUp.html";
			templateEngine.process(path, ctx, response.getWriter());
			return;
			
		}
		
		if(pwd.length() < 6) {
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
			ctx.setVariable("errorMsg", "Password troppo corta");
			path = "/SignUp.html";
			templateEngine.process(path, ctx, response.getWriter());
				return;				
		}
		
			
		
		
		UserDAO usr = new UserDAO(connection);
		boolean isPresent;
		User u = null;
		
		
		try {
			isPresent = usr.checkUsername(usrn);
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not Possible to check credentials");
			return;
		}
		
		if (isPresent ==true) {
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
			ctx.setVariable("errorMsg", "L'username non è disponibile");
			path = "/SignUp.html";
			templateEngine.process(path, ctx, response.getWriter());
			return;
		} else {
			try {
				usr.RegisterUser(usrn, pwd,email,type,name,surname);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not Possible to check credentials");
				return;
			}
			try {
				u=usr.checkCredentials(usrn, pwd);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not Possible to check credentials");
				return;
			}
			request.getSession().setAttribute("user", u);
			String target = (u.getRole().equals("employee")) ? "/GoToHomeEmployee" : "/GoToHomeClient";
			path = path + target;
			response.sendRedirect(path);
		}
	}
	

}
