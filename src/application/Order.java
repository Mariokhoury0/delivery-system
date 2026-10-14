package application;

public class Order {
	private int ordercode;
	private int customercode;
	private Restaurant restaurant;
	private int rescode;
	private String riderId;
	private int orderday;
	private int ordermonth;
	private int orderyear;
	private int deliveryday;
	private int deliverymonth;
	private int deliveryyear;
	private double Base;
	private double finalPrice;
	private String status;
	// fields 
	public Order(int ordercode, int customercode, Restaurant restaurant, int rescode,
            String riderId, int orderday, int ordermonth, int orderyear,
            int deliveryday, int deliverymonth, int deliveryyear,
            double base, double finalPrice, String status) {

   this.ordercode = ordercode;
   this.customercode = customercode;
   this.restaurant = restaurant;
   this.rescode = rescode;
   this.riderId = riderId;
   this.orderday = orderday;
   this.ordermonth = ordermonth;
   this.orderyear = orderyear;
   this.deliveryday = deliveryday;
   this.deliverymonth = deliverymonth;
   this.deliveryyear = deliveryyear;
   this.Base = base;

   this.finalPrice = restaurant.CalculateFinalPrice(base);

   this.status = status;
}
	// constructor 
	public int getOrdercode() {
		return ordercode;
	}
	public void setOrdercode(int ordercode) {
		this.ordercode = ordercode;
	}
	public int getCustomercode() {
		return customercode;
	}
	public void setCustomercode(int customercode) {
		this.customercode = customercode;
	}
	public String getRiderId() {
		return riderId;
	}
	public void setRiderId(String riderId) {
		this.riderId = riderId;
	}
	public double getFinalPrice() {
		return finalPrice;
	}
	public void setFinalPrice(double finalPrice) {
		this.finalPrice = restaurant.CalculateFinalPrice(Base);
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public Restaurant getRestaurant() {
		return restaurant;
	}
	
	public String getRestaurantName() {
	    return restaurant != null ? restaurant.getResname() : "N/A";
	}

	public String getOrderDate() {
	    return orderday + "/" + ordermonth + "/" + orderyear;
	}
	
	public double getBase() {
	    return Base;
	}
	
	public int getRescode() {
		return rescode;
	}
	public void setDeliveryDate(int day, int month, int year) {
		this.deliveryday = day;
		this.deliverymonth = month;
		this.deliveryyear = year;
	}
	// getters/setters
	public int getOrderDateNumber() {
	    return orderyear * 10000 + ordermonth * 100 + orderday;
	}
	@Override
	public String toString() {
		return "Order [Order Code=" + ordercode + ", Customer Code=" + customercode + ", Restaurant=" + restaurant
				+ ", Restaurant Code=" + rescode + ", Rider ID=" + riderId + ", Order Day=" + orderday + ", Order Month="
				+ ordermonth + ", Order Year=" + orderyear + ", Delivery Day=" + deliveryday + ", Delivery Month="
				+ deliverymonth + ", Delivery Year=" + deliveryyear + ", Base=" + Base + ", Final Price=" + finalPrice
				+ ", Order Status=" + status + "]";
	}
	//toString
	

}
