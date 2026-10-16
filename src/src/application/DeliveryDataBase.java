package application;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
public class DeliveryDataBase {
	private Admin systemAdmin;
	
	private ArrayList<RestAdmin> restaurantManagers;
	private ArrayList<Restaurant> restaurants;
	private ArrayList<Customer> customers;
	private ArrayList<Rider> riders;
	private ArrayList<Order> orders;
	
	private HashMap<Integer, ArrayList<Order>> ordersByCustomer;
	private Hashtable<Integer, ArrayList<Restaurant>> restaurantsByCustomer;
	private HashMap<Integer, Double> paymentsByCustomer;
	// defined the fields using ArrayLists, HashMaps and Hashtable.
	
	public DeliveryDataBase()
    {
        systemAdmin = new Admin("MainAdmin","admin", "12345");

        restaurantManagers = new ArrayList<>();
        restaurants = new ArrayList<>();
        customers = new ArrayList<>();
        riders = new ArrayList<>();
        orders = new ArrayList<>();

        ordersByCustomer = new HashMap<>();
        restaurantsByCustomer = new Hashtable<>();
        paymentsByCustomer = new HashMap<>();
    }
	// constructor 

	public Admin getSystemAdmin() {
		return systemAdmin;
	}

	public ArrayList<RestAdmin> getRestaurantManagers() {
		return restaurantManagers;
	}

	public ArrayList<Restaurant> getRestaurants() {
		return restaurants;
	}

	public ArrayList<Customer> getCustomers() {
		return customers;
	}

	public ArrayList<Rider> getRiders() {
		return riders;
	}

	public ArrayList<Order> getOrders() {
		return orders;
	}

	public HashMap<Integer, ArrayList<Order>> getOrdersByCustomer() {
		return ordersByCustomer;
	}

	public Hashtable<Integer, ArrayList<Restaurant>> getRestaurantsByCustomer() {
		return restaurantsByCustomer;
	}

	public HashMap<Integer, Double> getPaymentsByCustomer() {
		return paymentsByCustomer;
	}
	// setters/ getters 
	public void addOrderToCustomerMap(int customerCode, Order order)
	/*  this method adds an order to the orders-by-customer HashMap.
	 If the customer does not exist in the map, a new list is created.
	 Duplicate orders are not added.*/
	{
	    if (!ordersByCustomer.containsKey(customerCode))
	    {
	        ordersByCustomer.put(customerCode, new ArrayList<Order>());
	    }

	    if (!ordersByCustomer.get(customerCode).contains(order))
	    {
	        ordersByCustomer.get(customerCode).add(order);
	    }
	}
	
	public ArrayList<Order> getActiveOrdersByRider(String riderId)
	/* this method returns all orders assigned to a specific rider that are currently
	in the "Sent" or "On The Way" status.*/
	{
	    ArrayList<Order> result = new ArrayList<Order>();

	    for (Order order : orders)
	    {
	        if (order.getRiderId() != null
	                && order.getRiderId().equals(riderId)
	                && (order.getStatus().equals("Sent")
	                || order.getStatus().equals("On The Way")))
	        {
	            result.add(order);
	        }
	    }

	    return result;
	}
	public ArrayList<Restaurant> getPremiumRestaurantsByCustomer(Customer customer)
	/* this method returns all premium restaurants from which the given customer
	 has previously placed orders.*/
	{
	    ArrayList<Restaurant> result = new ArrayList<Restaurant>();

	    ArrayList<Restaurant> customerRestaurants =
	            restaurantsByCustomer.get(customer.getCustomercode());

	    if (customerRestaurants == null)
	    {
	        return result;
	    }

	    for (Restaurant restaurant : customerRestaurants)
	    {
	        if (restaurant instanceof PremiumRestaurant)
	        {
	            result.add(restaurant);
	        }
	    }

	    return result;
	}
	public Customer getCustomerWithMostOrders()
	/* this method finds and returns the customer who has placed the largest number
	 of orders in the system.*/
	{
	    Customer topCustomer = null;
	    int maxOrders = -1;

	    for (Customer customer : customers)
	    {
	        ArrayList<Order> customerOrders =
	                ordersByCustomer.get(customer.getCustomercode());

	        int count = 0;

	        if (customerOrders != null)
	        {
	            count = customerOrders.size();
	        }

	        if (count > maxOrders)
	        {
	            maxOrders = count;
	            topCustomer = customer;
	        }
	    }

	    return topCustomer;
	}
	public Rider getRiderWithMostDeliveries()
	/* this method finds and returns the rider who has completed the largest number
	 of deliveries in the system.*/
	{
	    Rider topRider = null;
	    int maxDeliveries = -1;

	    for (Rider rider : riders)
	    {
	        int count = rider.getOrders().size();

	        if (count > maxDeliveries)
	        {
	            maxDeliveries = count;
	            topRider = rider;
	        }
	    }

	    return topRider;
	}
	public ArrayList<Restaurant> getOpenRestaurantsByKitchenType(String kitchenType)
	// this method returns all open restaurants that match the specified cuisine type.
	{
	    ArrayList<Restaurant> result = new ArrayList<Restaurant>();

	    for (Restaurant restaurant : restaurants)
	    {
	        if (restaurant.isOpen()
	                && restaurant.getKitchentype().equalsIgnoreCase(kitchenType))
	        {
	            result.add(restaurant);
	        }
	    }

	    return result;
	}
	
	
	

}
