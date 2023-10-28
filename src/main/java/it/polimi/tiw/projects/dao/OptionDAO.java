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

public class OptionDAO {
	private Connection con;

	public OptionDAO(Connection con) {
		this.con = con;
	}
	
	
	public List<Option> findOptionForProduct(int productID) throws SQLException {
		List<Option> options = new ArrayList<Option>();

		String query = "SELECT * FROM option WHERE product=?;";
		try (PreparedStatement pstatement = con.prepareStatement(query);) {
			pstatement.setInt(1, productID);
			try (ResultSet result = pstatement.executeQuery();) {
				while (result.next()) {
					Option option = new Option();
					option.setID(result.getInt("ID"));		
					option.setName(result.getString("name"));
					option.setType(result.getString("type"));
					option.setCode(result.getString("code"));
					options.add(option);
				}
			}
		}
		return options;
	}
	
	public List<Option> findOptions() throws SQLException {
		List<Option> options = new ArrayList<Option>();

		String query = "SELECT * FROM quotes_man.option;";
		try (PreparedStatement pstatement = con.prepareStatement(query);) {
			try (ResultSet result = pstatement.executeQuery();) {
				while (result.next()) {
					Option option = new Option();
					option.setID(result.getInt("ID"));				
					option.setName(result.getString("name"));
					option.setType(result.getString("type"));
					option.setCode(result.getString("code"));	
					options.add(option);
				}
			}
		}
		return options;
	}
	
	
	
	
	public Option findOptionByID(int optionID) throws SQLException {
		Option option=null;

		String query = "SELECT * FROM quotes_man.option WHERE ID = ?";
		try (PreparedStatement pstatement = con.prepareStatement(query);) {
			pstatement.setInt(1, optionID);
			try (ResultSet result = pstatement.executeQuery();) {
				if (result.next())  // no results, credential check failed
					   {				
					option = new Option();
					option.setID(result.getInt("ID"));
					option.setName(result.getString("name"));
					option.setType(result.getString("type"));
					option.setCode(result.getString("code"));
		
				}
			}
		}
		return option;
	}
}



	