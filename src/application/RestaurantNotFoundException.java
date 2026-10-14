package application;

public class RestaurantNotFoundException extends Exception {
	// this exception is for when a user references a non existing restaurant in the system 
	private static final long serialVersionUID = 1L;
	public RestaurantNotFoundException(String message) {
        super(message);
    }
}

