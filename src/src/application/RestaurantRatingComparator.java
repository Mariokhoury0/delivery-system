package application;

import java.util.Comparator;

public class RestaurantRatingComparator implements Comparator<Restaurant>{
	// this Comparator class is for comparing restaurant ratings and its implemented in the Restaurant class 
	@Override
    public int compare(Restaurant r1, Restaurant r2) {
        return Double.compare(r2.getRating(), r1.getRating());
    }

}
