package application;

public class Restaurant {
	protected int rescode;
	protected String resname;
	protected String kitchentype;
	protected double rating;
	protected boolean open;
	protected double basedeliveryfee;
	//feilds 
	public Restaurant(int rescode, String resname, String kitchentype, double rating, boolean open,
			double basedeliveryfee) {
		this.rescode = rescode;
		this.resname = resname;
		this.kitchentype = kitchentype;
		this.rating = rating;
		this.open = open;
		this.basedeliveryfee = basedeliveryfee;
	}
	//constructor
	public int getRescode() {
		return rescode;
	}
	public void setRescode(int rescode) {
		this.rescode = rescode;
	}
	public String getResname() {
		return resname;
	}
	public void setResname(String resname) {
		this.resname = resname;
	}
	public String getKitchentype() {
		return kitchentype;
	}
	public void setKitchentype(String kitchentype) {
		this.kitchentype = kitchentype;
	}
	public double getRating() {
		return rating;
	}
	public void setRating(double rating) {
		this.rating = rating;
	}
	public boolean isOpen() {
		return open;
	}
	public void setOpen(boolean open) {
		this.open = open;
	}
	public double getBasedeliveryfee() {
		return basedeliveryfee;
	}
	public void setBasedeliveryfee(double basedeliveryfee) {
		this.basedeliveryfee = basedeliveryfee;
	}
	public double CalculateFinalPrice(double Base) {
		return Base + basedeliveryfee;
	}
	//setters/getters
	
	@Override
	public String toString() {
	    String status = open ? "Open" : "Closed";
	    return "Restaurant [Restaurant Code=" + rescode +
	            ", Restaurant Name=" + resname +
	            ", Kitchen Type=" + kitchentype +
	            ", Rating=" + rating +
	            ", Status=" + status +
	            ", Base Delivery Fee=" + basedeliveryfee + "]";
	}
	//toString
	
	

}
