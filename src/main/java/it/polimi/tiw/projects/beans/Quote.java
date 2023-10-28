package it.polimi.tiw.projects.beans;

public class Quote {

	private int ID;
	private int price;
	private int ownerclient;
	private int owneremployee;
	private int product_option;
	
	
	
	public int getPrice() {
		return price;
	}
	
	public void setPrice(int price) {
		this.price=price;
	}
	
	public int getID() {
		return ID;
	}
	
	public void setID(int ID) {
		this.ID=ID;
	}
	
	public int getOwnerClient() {
		return ownerclient;
	}
	
	public void setOwnerClient(int ownerclient) {
		 this.ownerclient=ownerclient;
	}
	

	public int getOwnerEmployee() {
		return owneremployee;
	}
	
	public void setOwnerEmployee(int owneremployee) {
		 this.owneremployee=owneremployee;
	}
	
	public int getProductoption() {
		return product_option;
	}
	public void setProductoption(int product_option) {
		this.product_option=product_option;
	}
	 
	
}
