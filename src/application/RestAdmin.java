package application;

import java.util.ArrayList;

public class RestAdmin extends Admin {
	 private ArrayList<Restaurant> restaurants;
	 //fields 

	    public RestAdmin(String managername, String username, String password)
	    {
	        super(managername, username, password);
	        restaurants = new ArrayList<>();
	    }
	    //constructor

	    public ArrayList<Restaurant> getRestaurants()
	    {
	        return restaurants;
	    }

	    public void setRestaurants(ArrayList<Restaurant> restaurants)
	    {
	        this.restaurants = restaurants;
	    }
	    //setters/getters

	    public void addRestaurant(Restaurant restaurant)
	    // this method adds a restaurant to the restaurants the the manager is responsible for 
	    {
	        if (restaurant == null)
	        {
	            return;
	        }

	        if (!restaurants.contains(restaurant))
	        {
	            restaurants.add(restaurant);
	        }
	    }
	    
	    public String getManagername() {
	        return managername;
	    }

	    public boolean isresponsiblefor(int restaurantCode)
	    // this method checks if a manager is responsible for a certain restaurant 
	    {
	        for (Restaurant restaurant : restaurants)
	        {
	            if (restaurant.getRescode() == restaurantCode)
	            {
	                return true;
	            }
	        }

	        return false;
	    }

	    @Override
	    public String toString()
	    {
	        return super.toString()
	                + "\nRestaurants: " + restaurants;
	    }
	    //toString
}
