package it.polimi.tiw.projects.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;



import it.polimi.tiw.projects.beans.*;
import it.polimi.tiw.projects.controllers.*;
import it.polimi.tiw.projects.dao.*;

public class ProductOptionDAO {
	private Connection con;

	public ProductOptionDAO(Connection connection) {
		this.con = connection;
	}
	
	public ProductOption findProductOptionByID(int productID) throws SQLException {
		ProductOption productoption=null;

		String query = "SELECT * FROM product_option WHERE ID = ?";
		try (PreparedStatement pstatement = con.prepareStatement(query);) {
			pstatement.setInt(1, productID);
			try (ResultSet result = pstatement.executeQuery();) {
				if (result.next())  // no results, credential check failed
					   {				
					 productoption = new ProductOption();
					productoption.setID(result.getInt("ID"));
					productoption.setProductID(result.getInt("product_ID"));
					productoption.setOptionID(result.getInt("option_ID"));
						
				}
			}
		}
		return productoption;
	}
	
	public List<ProductOption> findtOptionsIDByProductID(int productID) throws SQLException {
		List<ProductOption> productoptions=new ArrayList<ProductOption>();

		String query = "SELECT * FROM product_option WHERE product_ID = ?";
		try (PreparedStatement pstatement = con.prepareStatement(query);) {
			pstatement.setInt(1, productID);
			try (ResultSet result = pstatement.executeQuery();) {
				while (result.next())  // no results, credential check failed
					   {	
					ProductOption productoption=new ProductOption();
					productoption.setID(result.getInt("ID"));
					productoption.setProductID(result.getInt("product_ID"));
					productoption.setOptionID(result.getInt("option_ID"));
					productoptions.add(productoption);			
					   }
			}
		}
		return productoptions;
	}
	
	public ProductOption findProductOptionIDbyProductIDandOptionID(int productID,int optionID) throws SQLException {
		ProductOption productoption=null;

		String query = "SELECT * FROM product_option WHERE product_ID = ? AND option_ID =?";
		try (PreparedStatement pstatement = con.prepareStatement(query);) {
			pstatement.setInt(1, productID);
			pstatement.setInt(2, optionID);
			try (ResultSet result = pstatement.executeQuery();) {
				if (result.next())  // no results, credential check failed
					   {				
					productoption = new ProductOption();
					productoption.setID(result.getInt("ID"));
					productoption.setProductID(result.getInt("product_ID"));
					productoption.setOptionID(result.getInt("option_ID"));
						
				}
			}
		}
		return productoption;
	}
	
	
	
	
	
	
	
}