package application;

public class FastFoodRestaurant extends Restaurant{
	private double avgpreptime;
	private double expressdeliveryextrafee;
	// fields 
	public FastFoodRestaurant(int rescode, String resname, String kitchentype, double rating, boolean open,
			double basedeliveryfee, double d, double expressdeliveryextrafee) {
		super(rescode, resname, kitchentype, rating, open, basedeliveryfee);
		this.avgpreptime = d;
		this.expressdeliveryextrafee = expressdeliveryextrafee;
	}
	// constructor
	public double getAvgpreptime() {
		return avgpreptime;
	}
	public void setAvgpreptime(double avgpreptime) {
		this.avgpreptime = avgpreptime;
	}
	public double getExpressdeliveryextrafee() {
		return expressdeliveryextrafee;
	}
	public void setExpressdeliveryextrafee(double expressdeliveryextrafee) {
		this.expressdeliveryextrafee = expressdeliveryextrafee;
	}
	@Override
	public double CalculateFinalPrice(double Base) {
		return Base + basedeliveryfee + expressdeliveryextrafee;
		
	}
	public String getRestaurantType(){
		return "Fast Food Restaurant";
	}
	// setters/getters
	@Override
	public String toString() {
		return super.toString() + " FastFoodRestaurant [avgpreptime=" + avgpreptime + ", expressdeliveryextrafee=" + expressdeliveryextrafee
				+ "]";
	}
	// toString

}
