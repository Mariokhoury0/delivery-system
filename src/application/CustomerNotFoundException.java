package application;

public class CustomerNotFoundException extends Exception {
	// this exception is for when a user references a non existing customer 
	private static final long serialVersionUID = 1L;
	public CustomerNotFoundException(String message) {
        super(message);
    }

}
