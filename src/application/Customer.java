package application;

public class Customer implements Comparable<Customer> {
	private int customercode;
	private String firstname;
	private String lastname;
	private String street;
	private String city;
	private String zipcode;
	private String phone;
	private String email;
	private double creditbalance;
	// fields 
	public Customer(int customercode, String fisrtname, String lastname, String street, String city, String zipcode,
			String phone, String email, double creditbalance) {
		super();
		this.customercode = customercode;
		this.firstname = fisrtname;
		this.lastname = lastname;
		this.street = street;
		this.city = city;
		this.zipcode = zipcode;
		this.phone = phone;
		this.email = email;
		this.creditbalance = creditbalance;
	}
	// constructor 
	
	public void setStreet(String street) {
		this.street = street;
	}

	public String getFirstname() {
		return firstname;
	}

	public int getCustomercode() {
		return customercode;
	}
	public String getStreet() {
		return street;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	public void setCreditbalance(double creditbalance) {
		this.creditbalance = creditbalance;
	}
	public String getLastname() { return lastname; }
	public String getCity() { return city; }
	public String getZipcode() { return zipcode; }
	public String getPhone() { return phone; }
	public String getEmail() { return email; }

	public double getCreditbalance() {
		return creditbalance;
	}
	// the needed setters/getters
	@Override
	public int compareTo(Customer other) {
	    return Double.compare(other.getCreditbalance(), this.getCreditbalance());
	}
	// implementing the Comparable class 
	@Override
	public String toString() {
		return "Customer [Customer Code=" + customercode + ", Fisrt Name=" + firstname + ", Last Name=" + lastname
				+ ", Street=" + street + ", City=" + city + ", Zipcode=" + zipcode + ", Phone=" + phone + ", Email="
				+ email + ", Credit Balance=" + creditbalance + "]";
	}
	//toString

}

