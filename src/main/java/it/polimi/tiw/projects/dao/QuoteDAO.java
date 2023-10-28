package it.polimi.tiw.projects.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polimi.tiw.projects.beans.*;
import it.polimi.tiw.projects.controllers.*;



public class QuoteDAO {
	private Connection con;
	private int id;
	
	public QuoteDAO(Connection con) {
		this.con = con;
	}
	
	
	public List<Quote> findQuotesByUser(int userId) throws SQLException {
		List<Quote> quotes = new ArrayList<Quote>();

		String query = "SELECT * from quotes where ownerclient = ? ";
		try (PreparedStatement pstatement = con.prepareStatement(query);) {
			pstatement.setInt(1, userId);
			try (ResultSet result = pstatement.executeQuery();) {
				while (result.next()) {
					Quote quote = new Quote();
					quote.setID(result.getInt("ID"));				
					quote.setPrice(result.getInt("price"));
					quote.setOwnerClient(result.getInt("ownerclient"));
					quote.setProductoption(result.getInt("product_option_ID"));
					quotes.add(quote);
				}
			}
		}
		return quotes;
	}
	
	public List<Quote> findQuotesByEmployee(int userId) throws SQLException {
		List<Quote> quotes = new ArrayList<Quote>();

		String query = "SELECT * from quotes where owneremployee = ? ";
		try (PreparedStatement pstatement = con.prepareStatement(query);) {
			pstatement.setInt(1, userId);
			try (ResultSet result = pstatement.executeQuery();) {
				while (result.next()) {
					Quote quote = new Quote();
					quote.setID(result.getInt("ID"));				
					quote.setPrice(result.getInt("price"));
					quote.setOwnerEmployee(result.getInt("owneremployee"));
					quote.setProductoption(result.getInt("product_option_ID"));
					quotes.add(quote);
				}
			}
		}
		return quotes;
	}
	
	public List<Quote> findnotDealed() throws SQLException {
		List<Quote> quotes = new ArrayList<Quote>();

		String query = "SELECT * from quotes where owneremployee is null";
		try (PreparedStatement pstatement = con.prepareStatement(query);) {
			
			try (ResultSet result = pstatement.executeQuery();) {
				while (result.next()) {
					Quote quote = new Quote();
					quote.setID(result.getInt("ID"));	
					quote.setPrice(result.getInt("price"));	
					quote.setOwnerClient(result.getInt("ownerclient"));
					quote.setOwnerEmployee(result.getInt("owneremployee"));
					quote.setProductoption(result.getInt("product_option_ID"));
					quotes.add(quote);
				}
			}
		}
		return quotes;
	}
	
	public Quote findQuoteByID(int quoteID) throws SQLException {
		Quote quote = null;

		String query = "SELECT * FROM quotes WHERE ID = ?";
		try (PreparedStatement pstatement = con.prepareStatement(query);) {
			pstatement.setInt(1, quoteID);
			try (ResultSet result = pstatement.executeQuery();) {
				if (result.next()) {
					quote = new Quote();
					quote.setID(result.getInt("ID"));
					quote.setPrice(result.getInt("price"));
					quote.setOwnerClient(result.getInt("ownerclient"));
					quote.setOwnerEmployee(result.getInt("owneremployee"));
					quote.setProductoption(result.getInt("product_option_ID"));
					
				}
			}
		}
		return quote;
	}
	
	public int priceQuote(int quoteid, int price,int employeeID) throws SQLException {

		String query = "UPDATE quotes SET price = ? , owneremployee=?  WHERE ID = ? ";
		PreparedStatement pstatement = null;
		int code=0;
		try {
			pstatement = con.prepareStatement(query);
			pstatement.setInt(1, price);
			pstatement.setInt(2, employeeID);
			pstatement.setInt(3, quoteid);	
			code = pstatement.executeUpdate();
		} catch (SQLException e) {
		    e.printStackTrace();
			throw new SQLException(e);
		} finally {
			try {
				pstatement.close();
			} catch (Exception e1) {}
		}		
		return code;
	}
	
	 public void insertQuote(int productoption,int clientID) throws SQLException {
	        String query = "INSERT INTO quotes_man.quotes (ownerclient, product_option_ID) VALUES (?, ?);";
	        try (PreparedStatement pstatement = con.prepareStatement(query);) {
	        	pstatement.setInt(1, clientID);
	            pstatement.setInt(2, productoption);
	            pstatement.executeUpdate();
	        }
	    }
	
	
	
	/**
	public void createQuote(int productid,int ownerclient) {
		String query = "INSERT quotes SET price = ? , owneremployee=?  WHERE ID = ? ";
		PreparedStatement pstatement = null;
		int code=0;
		try {
			pstatement = con.prepareStatement(query);
			pstatement.setInt(1, price);
			pstatement.setInt(2, employeeID);
			pstatement.setInt(3, quoteid);	
			code = pstatement.executeUpdate();
		} catch (SQLException e) {
		    e.printStackTrace();
			throw new SQLException(e);
		} finally {
			try {
				pstatement.close();
			} catch (Exception e1) {}
		}		
		return code;
	}
	}
	**/
	
		}
	



