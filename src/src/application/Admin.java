package application;

public class Admin {
	protected String managername;
	protected String username;
	protected String password;
	// defining fields 
	public Admin(String managername, String username, String password) {
		super();
		this.managername = managername;
		this.username = username;
		this.password = password;
	}
	// constructor
	public String getManagername() {
		return managername;
	}

	public void setManagername(String managername) {
		this.managername = managername;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	// defining the needed setters/getters

	public boolean login(String username, String password) {
		return this.username.equals(username) && this.password.equals(password);	
	}
	// this method is responsible for when the main admin tries to log in and makes sure the correct username and password are entered.

	@Override
	public String toString() {
		return "Admin [Manager Name=" + managername + ", Username=" + username + "]";
	}
	// toString (the password is intentionally not printed)
	
}
