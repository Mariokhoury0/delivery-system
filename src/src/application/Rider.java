package application;

import java.util.ArrayList;

public class Rider {
	private String id;
    private String firstname;
    private String lastname;
    private String phone;
    private String vehicle;
    private boolean available;
    private ArrayList<Order> orders;
    //fields 

    public Rider(String id, String firstname, String lastname, String phone, String vehicle, boolean available)
    {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
        this.vehicle = vehicle;
        this.available = available;
        this.orders = new ArrayList<Order>();
    }
    //constructor 

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getFirstname()
    {
        return firstname;
    }

    public void setFirstname(String firstname)
    {
        this.firstname = firstname;
    }

    public String getLastname()
    {
        return lastname;
    }

    public void setLastname(String lastname)
    {
        this.lastname = lastname;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getVehicle()
    {
        return vehicle;
    }

    public void setVehicle(String vehicle)
    {
        this.vehicle = vehicle;
    }

    public boolean isAvailable()
    {
        return available;
    }

    public void setAvailable(boolean available)
    {
        this.available = available;
    }

    public ArrayList<Order> getOrders()
    {
        return orders;
    }

    public void setOrders(ArrayList<Order> orders)
    {
        this.orders = orders;
    }

    public int getOrdercount()
    {
        return orders.size();
    }
    //setters/getters

    public boolean addOrder(Order order)
    //this method adds an order and does not allow duplicates
    {
        if (order == null)
        {
            return false;
        }

        for (Order currentOrder : orders)
        {
            if (currentOrder.getOrdercode() == order.getOrdercode())
            {
                return false;
            }
        }

        orders.add(order);
        return true;
    }

    public Order findOrder(int ordercode)
    // this method allows to find an order via ordercode 
    {
        for (Order order : orders)
        {
            if (order.getOrdercode() == ordercode)
            {
                return order;
            }
        }

        return null;
    }

    public void printOrders()
    // this method returns all the orders for a rider 
    {
        if (orders.isEmpty())
        {
            System.out.println("No Orders For This Rider!");
            return;
        }

        for (Order order : orders)
        {
            System.out.println(order);
        }
    }

    public void showOrders()
    // this mehtod shows all the orders for a certain user 
    {
        if (orders.isEmpty())
        {
            System.out.println("No orders.");
            return;
        }

        for (Order order : orders)
        {
            System.out.println(order);
        }
    }

    @Override
    public String toString()
    {
        return "Rider [Rider ID=" + id
                + ", First Name=" + firstname
                + ", Last Name=" + lastname
                + ", Phone=" + phone
                + ", Vehicle=" + vehicle
                + ", Status=" + (available ? "Available" : "Unavailable")
                + ", Orders=" + orders
                + ", Order Count=" + orders.size()
                + "]";
    }	
    //toString

}
