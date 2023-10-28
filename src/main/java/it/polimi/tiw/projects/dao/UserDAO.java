package it.polimi.tiw.projects.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import it.polimi.tiw.projects.beans.Quote;
import it.polimi.tiw.projects.beans.User;

public class UserDAO {
	private Connection conn;

	public UserDAO(Connection connection) {
		this.conn = connection;
	}

	public User checkCredentials(String usrn, String pwd) throws SQLException {
		String query = "SELECT  ID, username, password,email, role, name, surname FROM user  WHERE username = ? AND password =?";
		try (PreparedStatement pstatement = conn.prepareStatement(query);) {
			pstatement.setString(1, usrn);
			pstatement.setString(2, pwd);
			try (ResultSet result = pstatement.executeQuery();) {
				if (!result.isBeforeFirst()) // no results, credential check failed
					return null;
				else {
					result.next();
					User user = new User();
					user.setID(result.getInt("ID"));
					user.setUsername(result.getString("username"));
					user.setEmail(result.getString("email"));
					user.setRole(result.getString("role"));
					user.setName(result.getString("name"));
					user.setSurname(result.getString("surname"));
					return user;
				}
			}
		}
	}
	
	
	   public void RegisterUser(String usrn, String pwd,String email, String type, String name, String surname) throws SQLException {
	        String query = "INSERT into quotes_man.user (username, password,email, role, name, surname) VALUES(?, ?, ?, ?, ?, ?)";
	        try (PreparedStatement pstatement = conn.prepareStatement(query);) {
	            pstatement.setString(1, usrn);
	            pstatement.setString(2, pwd);
	            pstatement.setString(3, email);
	            pstatement.setString(4, type);
	            pstatement.setString(5, name);
	            pstatement.setString(6, surname);
	            pstatement.executeUpdate();
	        }
	    }
	
	
	
	public boolean checkUsername(String usrn) throws SQLException {
        String query = "SELECT username FROM quotes_man.user WHERE user.username LIKE ?";
        boolean returnValue = false;

        try (PreparedStatement pstatement = conn.prepareStatement(query);) {

            pstatement.setString(1, usrn);
            ResultSet resultSet = pstatement.executeQuery();

            while (resultSet.next())
                if (usrn.equals(resultSet.getString("username")))
                    returnValue = true;
        }
        return returnValue;
        
        
}
	
	public User findUserByID(int userID) throws SQLException {
		User user = null;

		String query = "SELECT * FROM user WHERE ID = ?";
		try (PreparedStatement pstatement = conn.prepareStatement(query);) {
			pstatement.setInt(1, userID);
			try (ResultSet result = pstatement.executeQuery();) {
				if (result.next()) {
					user = new User();
					user.setID(result.getInt("ID"));
					user.setName(result.getString("name"));
					user.setSurname(result.getString("surname"));
					user.setUsername(result.getString("username"));
					
				}
			}
		}
		return user;
	}
}
	


