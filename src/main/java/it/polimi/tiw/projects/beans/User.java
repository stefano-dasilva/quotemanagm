package it.polimi.tiw.projects.beans;

public class User {

	private int id;
	private String username;
	private String password;
	private String role;
	private String name;
	private String surname;
	private String email;
	
	
	

	

	public int getID() {
		return id;
	}

	public void setID(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password=password;
	}
	
	public String getRole() {
		return role;
	}
	
	public void setRole(String role) {
		this.role=role;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
		}
	
}

