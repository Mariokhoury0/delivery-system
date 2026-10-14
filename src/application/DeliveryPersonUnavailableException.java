package application;

public class DeliveryPersonUnavailableException extends Exception{
	// this exception is for unavailable riders in the system 
	private static final long serialVersionUID = 1L;
	 public DeliveryPersonUnavailableException(String message) {
	        super(message);
	    }
}
