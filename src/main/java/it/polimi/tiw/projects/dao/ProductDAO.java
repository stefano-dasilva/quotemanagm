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

public class ProductDAO {
	private Connection con;

	public ProductDAO(Connection connection) {
		this.con = connection;
	}
	
	public Product findProductByID(int productID) throws SQLException {
		Product product=null;

		String query = "SELECT * FROM product WHERE ID = ?";
		try (PreparedStatement pstatement = con.prepareStatement(query);) {
			pstatement.setInt(1, productID);
			try (ResultSet result = pstatement.executeQuery();) {
				if (result.next())  // no results, credential check failed
					   {				
					 product = new Product();
					product.setID(result.getInt("ID"));
					product.setName(result.getString("name"));
					product.setCode(result.getString("code"));
					byte[] imgData = result.getBytes("image");
					String encodedImg=Base64.getEncoder().encodeToString(imgData);
					product.setImage(encodedImg);		
				}
			}
		}
		return product;
	}
	
	
	public List<Product> findProducts() throws SQLException {
		List<Product> products = new ArrayList<Product>();

		String query = "SELECT * FROM product;";
		try (PreparedStatement pstatement = con.prepareStatement(query);) {
			try (ResultSet result = pstatement.executeQuery();) {
				while (result.next()) {
					Product product = new Product();
					product.setID(result.getInt("ID"));				
					product.setName(result.getString("name"));
					product.setCode(result.getString("code"));
					byte[] imgData = result.getBytes("image");
					String encodedImg=Base64.getEncoder().encodeToString(imgData);
					product.setImage(encodedImg);	
					products.add(product);
				}
			}
		}
		return products;
	}
}
