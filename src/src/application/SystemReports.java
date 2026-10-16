package application;
import java.util.ArrayList;

public class SystemReports {
	public static void printRestaurants(ArrayList<? extends Restaurant> restaurants) {
		// wildcard method responsible for printing out all the restaurants in the system 
        for (Restaurant r : restaurants) {
            System.out.println(r);
        }
    }

    public static double calculateTotalOrdersPrice(ArrayList<? extends Order> orders) {
    	// wildcard method responsible for calculating the total for all the orders in the system 
        double sum = 0;
        for (Order o : orders) {
            sum += o.getFinalPrice();
        }
        return sum;
    }

    public static void addFastFoodRestaurant(ArrayList<? super FastFoodRestaurant> restaurants,FastFoodRestaurant restaurant){
    	// wildcard method responsible for adding a fast food restaurant to the system 
        restaurants.add(restaurant);
    }

    
    public static <T extends Comparable<T>> T getMax(ArrayList<T> list) {
    	// this generic method returns the max object from each class using Comparable 
        if (list == null || list.isEmpty()) {
            return null;
        }
        T max = list.get(0);
        for (T item : list) {
            if (item.compareTo(max) > 0) {
                max = item;
            }
        }
        return max;
    }
}
