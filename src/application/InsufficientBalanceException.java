package application;

public class InsufficientBalanceException extends Exception {
	// this exception is for customers with insufficient balance 
	private static final long serialVersionUID = 1L;
	public InsufficientBalanceException(String message) {
        super(message);
    }
}
