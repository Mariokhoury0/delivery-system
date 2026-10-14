package application;

import java.io.*;
import java.util.ArrayList;

public class FileManager {

    // ── Save all collections ──────────────────────────────────────────────────

    public static void saveAll(DeliverySystem system) throws IOException {
        saveCustomers(system.getCustomers());
        saveOrders(system.getOrders());
        saveRiders(system.getRiders());
        saveRestaurants(system.getRestaurants());
        saveRestAdmins(system.getRestAdmins());
    }

    // ── Load all collections ──────────────────────────────────────────────────

    public static void loadAll(DeliverySystem system) throws IOException {
        loadCustomers(system);
        loadRestaurants(system);
        loadRiders(system);
        loadOrders(system);
        loadRestAdmins(system);
    }

    // ── Save customers ────────────────────────────────────────────────────────

    public static void saveCustomers(ArrayList<Customer> customers)
            throws IOException {
        BufferedWriter writer = new BufferedWriter(
            new FileWriter("customers.txt"));
        for (Customer c : customers) {
            writer.write(
                c.getCustomercode() + "," +
                c.getFirstname()    + "," +
                c.getLastname()     + "," +
                c.getStreet()       + "," +
                c.getCity()         + "," +
                c.getZipcode()      + "," +
                c.getPhone()        + "," +
                c.getEmail()        + "," +
                c.getCreditbalance()
            );
            writer.newLine();
        }
        writer.close();
    }

    // ── Load customers ────────────────────────────────────────────────────────

    public static void loadCustomers(DeliverySystem system)
            throws IOException {
        File file = new File("customers.txt");
        if (!file.exists()) return;

        system.getCustomers().clear();

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty()) continue;
            String[] parts = line.split(",");
            if (parts.length < 9) continue;
            try {
                int    code    = Integer.parseInt(parts[0].trim());
                String fname   = parts[1].trim();
                String lname   = parts[2].trim();
                String street  = parts[3].trim();
                String city    = parts[4].trim();
                String zip     = parts[5].trim();
                String phone   = parts[6].trim();
                String email   = parts[7].trim();
                double balance = Double.parseDouble(parts[8].trim());
                Customer c = new Customer(code, fname, lname,
                    street, city, zip, phone, email, balance);
                system.addCustomer(c);
            } catch (NumberFormatException e) {
                // skip malformed line
            }
        }
        reader.close();
    }

    // ── Save orders ───────────────────────────────────────────────────────────

    public static void saveOrders(ArrayList<Order> orders)
            throws IOException {
        BufferedWriter writer = new BufferedWriter(
            new FileWriter("orders.txt"));
        for (Order o : orders) {
            writer.write(
                o.getOrdercode()    + "," +
                o.getCustomercode() + "," +
                o.getRescode()      + "," +
                (o.getRiderId() != null ? o.getRiderId() : "") + "," +
                o.getBase()         + "," +
                o.getFinalPrice()   + "," +
                o.getStatus()       + "," +
                o.getOrderDate()
            );
            writer.newLine();
        }
        writer.close();
    }

    // ── Load orders ───────────────────────────────────────────────────────────

    public static void loadOrders(DeliverySystem system)
            throws IOException {
        File file = new File("orders.txt");
        if (!file.exists()) return;

        system.getOrders().clear();

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty()) continue;
            String[] parts = line.split(",");
            if (parts.length < 8) continue;
            try {
                int    orderCode  = Integer.parseInt(parts[0].trim());
                int    custCode   = Integer.parseInt(parts[1].trim());
                int    restCode   = Integer.parseInt(parts[2].trim());
                String riderId    = parts[3].trim();
                double base       = Double.parseDouble(parts[4].trim());
                double finalPrice = Double.parseDouble(parts[5].trim());
                String status     = parts[6].trim();
                // date stored as dd/MM/yyyy
                String[] dateParts = parts[7].trim().split("/");
                int day   = Integer.parseInt(dateParts[0]);
                int month = Integer.parseInt(dateParts[1]);
                int year  = Integer.parseInt(dateParts[2]);

                Restaurant restaurant = system.findRestaurantByCode(restCode);
                if (restaurant == null) continue;

                Customer customer = system.findCustomerByCode(custCode);
                if (customer == null) continue;

                Order o = new Order(orderCode, custCode, restaurant,
                    restCode, riderId, day, month, year,
                    0, 0, 0, base, finalPrice, status);
                system.addOrder(o);
            } catch (Exception e) {
                // skip malformed line
            }
        }
        reader.close();
    }

    // ── Save riders ───────────────────────────────────────────────────────────

    public static void saveRiders(ArrayList<Rider> riders)
            throws IOException {
        BufferedWriter writer = new BufferedWriter(
            new FileWriter("riders.txt"));
        for (Rider r : riders) {
            writer.write(
                r.getId()        + "," +
                r.getFirstname() + "," +
                r.getLastname()  + "," +
                r.getPhone()     + "," +
                r.getVehicle()   + "," +
                r.isAvailable()
            );
            writer.newLine();
        }
        writer.close();
    }

    // ── Load riders ───────────────────────────────────────────────────────────

    public static void loadRiders(DeliverySystem system)
            throws IOException {
        File file = new File("riders.txt");
        if (!file.exists()) return;

        system.getRiders().clear();

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty()) continue;
            String[] parts = line.split(",");
            if (parts.length < 6) continue;
            try {
                String  id        = parts[0].trim();
                String  fname     = parts[1].trim();
                String  lname     = parts[2].trim();
                String  phone     = parts[3].trim();
                String  vehicle   = parts[4].trim();
                boolean available = Boolean.parseBoolean(parts[5].trim());
                Rider r = new Rider(id, fname, lname, phone, vehicle, available);
                system.addRider(r);
            } catch (Exception e) {
                // skip malformed line
            }
        }
        reader.close();
    }

    // ── Save restaurants ──────────────────────────────────────────────────────

    public static void saveRestaurants(ArrayList<Restaurant> restaurants)
            throws IOException {
        BufferedWriter writer = new BufferedWriter(
            new FileWriter("restaurants.txt"));
        for (Restaurant r : restaurants) {
            // type prefix so we know which subclass to reconstruct
            if (r instanceof PremiumRestaurant) {
                PremiumRestaurant pr = (PremiumRestaurant) r;
                writer.write(
                    "PREMIUM,"       +
                    r.getRescode()   + "," +
                    r.getResname()   + "," +
                    r.getKitchentype() + "," +
                    r.getRating()    + "," +
                    r.isOpen()       + "," +
                    r.getBasedeliveryfee() + "," +
                    pr.getMinimumOrderAllowed() + "," +
                    pr.getExtraComissionPrecent()
                );
            } else if (r instanceof FastFoodRestaurant) {
                FastFoodRestaurant ff = (FastFoodRestaurant) r;
                writer.write(
                    "FASTFOOD,"      +
                    r.getRescode()   + "," +
                    r.getResname()   + "," +
                    r.getKitchentype() + "," +
                    r.getRating()    + "," +
                    r.isOpen()       + "," +
                    r.getBasedeliveryfee() + "," +
                    ff.getAvgpreptime() + "," +
                    ff.getExpressdeliveryextrafee()
                );
            } else {
                writer.write(
                    "REGULAR,"       +
                    r.getRescode()   + "," +
                    r.getResname()   + "," +
                    r.getKitchentype() + "," +
                    r.getRating()    + "," +
                    r.isOpen()       + "," +
                    r.getBasedeliveryfee()
                );
            }
            writer.newLine();
        }
        writer.close();
    }

    // ── Load restaurants ──────────────────────────────────────────────────────

    public static void loadRestaurants(DeliverySystem system)
            throws IOException {
        File file = new File("restaurants.txt");
        if (!file.exists()) return;

        system.getRestaurants().clear();

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty()) continue;
            String[] parts = line.split(",");
            try {
                String type = parts[0].trim();
                int    code = Integer.parseInt(parts[1].trim());
                String name = parts[2].trim();
                String kitchen = parts[3].trim();
                double rating  = Double.parseDouble(parts[4].trim());
                boolean open   = Boolean.parseBoolean(parts[5].trim());
                double baseFee = Double.parseDouble(parts[6].trim());

                if (type.equals("PREMIUM") && parts.length >= 9) {
                    double minOrder   = Double.parseDouble(parts[7].trim());
                    double extraCharge = Double.parseDouble(parts[8].trim());
                    system.addRestaurant(new PremiumRestaurant(
                    	    code, name, kitchen, rating, open,
                    	    baseFee, minOrder, extraCharge));
                } else if (type.equals("FASTFOOD") && parts.length >= 9) {
                    double avgPrep   = Double.parseDouble(parts[7].trim());
                    double expressFee = Double.parseDouble(parts[8].trim());
                    system.addRestaurant(new FastFoodRestaurant(
                        code, name, kitchen, rating, open,
                        baseFee, avgPrep, expressFee));
                } else {
                    system.addRestaurant(new Restaurant(
                        code, name, kitchen, rating, open, baseFee));
                }
            } catch (Exception e) {
                // skip malformed line
            }
        }
        reader.close();
    }

    // ── Save restAdmins ───────────────────────────────────────────────────────

    public static void saveRestAdmins(ArrayList<RestAdmin> admins)
            throws IOException {
        BufferedWriter writer = new BufferedWriter(
            new FileWriter("restAdmins.txt"));
        for (RestAdmin ra : admins) {
            writer.write(
                ra.getManagername() + "," +
                ra.getUsername()    + "," +
                ra.getPassword()
            );
            writer.newLine();
        }
        writer.close();
    }

    // ── Load restAdmins ───────────────────────────────────────────────────────

    public static void loadRestAdmins(DeliverySystem system)
            throws IOException {
        File file = new File("restAdmins.txt");
        if (!file.exists()) return;

        system.getRestAdmins().clear();

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty()) continue;
            String[] parts = line.split(",");
            if (parts.length < 3) continue;
            try {
                String name     = parts[0].trim();
                String username = parts[1].trim();
                String password = parts[2].trim();
                system.addRestAdmin(new RestAdmin(name, username, password));
            } catch (Exception e) {
                // skip malformed line
            }
        }
        reader.close();
    }
}