package application;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    // The order matters: orders need customers, restaurants and riders to exist,
    // and restaurant admins need restaurants to exist.

    public static void loadAll(DeliverySystem system) throws IOException {
        loadCustomers(system);
        loadRestaurants(system);
        loadRiders(system);
        loadOrders(system);
        loadRestAdmins(system);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    // Fields are separated by commas, so a comma typed inside a value (for example in a
    // street name) would corrupt the line. esc/unesc encode the special characters on
    // save and decode them on load, so every value comes back exactly as it was typed.

    private static String esc(String s) {
        if (s == null) return "";
        return s.replace("%", "%25")
                .replace(",", "%2C")
                .replace(";", "%3B")
                .replace("\r", "%0D")
                .replace("\n", "%0A");
    }

    private static String unesc(String s) {
        return s.replace("%2C", ",")
                .replace("%3B", ";")
                .replace("%0D", "\r")
                .replace("%0A", "\n")
                .replace("%25", "%");
    }

    private static BufferedWriter openWriter(String fileName) throws IOException {
        return Files.newBufferedWriter(Paths.get(fileName), StandardCharsets.UTF_8);
    }

    private static BufferedReader openReader(File file) throws IOException {
        return Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8);
    }

    // ── Save customers ────────────────────────────────────────────────────────

    public static void saveCustomers(ArrayList<Customer> customers)
            throws IOException {
        try (BufferedWriter writer = openWriter("customers.txt")) {
            for (Customer c : customers) {
                writer.write(
                    c.getCustomercode()   + "," +
                    esc(c.getFirstname()) + "," +
                    esc(c.getLastname())  + "," +
                    esc(c.getStreet())    + "," +
                    esc(c.getCity())      + "," +
                    esc(c.getZipcode())   + "," +
                    esc(c.getPhone())     + "," +
                    esc(c.getEmail())     + "," +
                    c.getCreditbalance()
                );
                writer.newLine();
            }
        }
    }

    // ── Load customers ────────────────────────────────────────────────────────

    public static void loadCustomers(DeliverySystem system)
            throws IOException {
        File file = new File("customers.txt");
        if (!file.exists()) return;

        system.getCustomers().clear();

        try (BufferedReader reader = openReader(file)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",", -1);
                if (parts.length < 9) continue;
                try {
                    int    code    = Integer.parseInt(parts[0].trim());
                    String fname   = unesc(parts[1].trim());
                    String lname   = unesc(parts[2].trim());
                    String street  = unesc(parts[3].trim());
                    String city    = unesc(parts[4].trim());
                    String zip     = unesc(parts[5].trim());
                    String phone   = unesc(parts[6].trim());
                    String email   = unesc(parts[7].trim());
                    double balance = Double.parseDouble(parts[8].trim());
                    Customer c = new Customer(code, fname, lname,
                        street, city, zip, phone, email, balance);
                    system.addCustomer(c);
                } catch (NumberFormatException e) {
                    // skip malformed line
                }
            }
        }
    }

    // ── Save orders ───────────────────────────────────────────────────────────

    public static void saveOrders(ArrayList<Order> orders)
            throws IOException {
        try (BufferedWriter writer = openWriter("orders.txt")) {
            for (Order o : orders) {
                writer.write(
                    o.getOrdercode()    + "," +
                    o.getCustomercode() + "," +
                    o.getRescode()      + "," +
                    esc(o.getRiderId()) + "," +
                    o.getBase()         + "," +
                    o.getFinalPrice()   + "," +
                    esc(o.getStatus())  + "," +
                    o.getOrderDate()    + "," +
                    o.getDeliveryDate()
                );
                writer.newLine();
            }
        }
    }

    // ── Load orders ───────────────────────────────────────────────────────────

    public static void loadOrders(DeliverySystem system)
            throws IOException {
        File file = new File("orders.txt");
        if (!file.exists()) return;

        // clears the orders AND everything derived from them (per-customer maps,
        // payments, riders' order lists) so reloading never creates duplicates
        system.clearOrderData();

        try (BufferedReader reader = openReader(file)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",", -1);
                if (parts.length < 8) continue;
                try {
                    int    orderCode  = Integer.parseInt(parts[0].trim());
                    int    custCode   = Integer.parseInt(parts[1].trim());
                    int    restCode   = Integer.parseInt(parts[2].trim());
                    String riderId    = unesc(parts[3].trim());
                    double base       = Double.parseDouble(parts[4].trim());
                    double finalPrice = Double.parseDouble(parts[5].trim());
                    String status     = unesc(parts[6].trim());
                    // dates are stored as d/M/yyyy
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

                    // delivery date (older files without this column simply keep 0/0/0)
                    if (parts.length >= 9) {
                        String[] del = parts[8].trim().split("/");
                        if (del.length == 3) {
                            o.setDeliveryDate(Integer.parseInt(del[0]),
                                Integer.parseInt(del[1]), Integer.parseInt(del[2]));
                        }
                    }

                    if (system.addOrder(o) && !riderId.isEmpty()) {
                        // re-attach the order to its rider
                        Rider rider = system.findRiderById(riderId);
                        if (rider != null) {
                            rider.addOrder(o);
                        }
                    }
                } catch (Exception e) {
                    // skip malformed line
                }
            }
        }
    }

    // ── Save riders ───────────────────────────────────────────────────────────

    public static void saveRiders(ArrayList<Rider> riders)
            throws IOException {
        try (BufferedWriter writer = openWriter("riders.txt")) {
            for (Rider r : riders) {
                writer.write(
                    esc(r.getId())        + "," +
                    esc(r.getFirstname()) + "," +
                    esc(r.getLastname())  + "," +
                    esc(r.getPhone())     + "," +
                    esc(r.getVehicle())   + "," +
                    r.isAvailable()
                );
                writer.newLine();
            }
        }
    }

    // ── Load riders ───────────────────────────────────────────────────────────

    public static void loadRiders(DeliverySystem system)
            throws IOException {
        File file = new File("riders.txt");
        if (!file.exists()) return;

        system.getRiders().clear();

        try (BufferedReader reader = openReader(file)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",", -1);
                if (parts.length < 6) continue;
                try {
                    String  id        = unesc(parts[0].trim());
                    String  fname     = unesc(parts[1].trim());
                    String  lname     = unesc(parts[2].trim());
                    String  phone     = unesc(parts[3].trim());
                    String  vehicle   = unesc(parts[4].trim());
                    boolean available = Boolean.parseBoolean(parts[5].trim());
                    Rider r = new Rider(id, fname, lname, phone, vehicle, available);
                    system.addRider(r);
                } catch (Exception e) {
                    // skip malformed line
                }
            }
        }
    }

    // ── Save restaurants ──────────────────────────────────────────────────────

    public static void saveRestaurants(ArrayList<Restaurant> restaurants)
            throws IOException {
        try (BufferedWriter writer = openWriter("restaurants.txt")) {
            for (Restaurant r : restaurants) {
                // type prefix so we know which subclass to reconstruct
                if (r instanceof PremiumRestaurant) {
                    PremiumRestaurant pr = (PremiumRestaurant) r;
                    writer.write(
                        "PREMIUM,"       +
                        r.getRescode()   + "," +
                        esc(r.getResname())     + "," +
                        esc(r.getKitchentype()) + "," +
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
                        esc(r.getResname())     + "," +
                        esc(r.getKitchentype()) + "," +
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
                        esc(r.getResname())     + "," +
                        esc(r.getKitchentype()) + "," +
                        r.getRating()    + "," +
                        r.isOpen()       + "," +
                        r.getBasedeliveryfee()
                    );
                }
                writer.newLine();
            }
        }
    }

    // ── Load restaurants ──────────────────────────────────────────────────────

    public static void loadRestaurants(DeliverySystem system)
            throws IOException {
        File file = new File("restaurants.txt");
        if (!file.exists()) return;

        system.getRestaurants().clear();

        try (BufferedReader reader = openReader(file)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",", -1);
                try {
                    String type = parts[0].trim();
                    int    code = Integer.parseInt(parts[1].trim());
                    String name = unesc(parts[2].trim());
                    String kitchen = unesc(parts[3].trim());
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
        }
    }

    // ── Save restAdmins ───────────────────────────────────────────────────────
    // 4th column: the codes of the restaurants the admin manages, separated by ';'

    public static void saveRestAdmins(ArrayList<RestAdmin> admins)
            throws IOException {
        try (BufferedWriter writer = openWriter("restAdmins.txt")) {
            for (RestAdmin ra : admins) {
                StringBuilder codes = new StringBuilder();
                for (Restaurant r : ra.getRestaurants()) {
                    if (codes.length() > 0) codes.append(";");
                    codes.append(r.getRescode());
                }
                writer.write(
                    esc(ra.getManagername()) + "," +
                    esc(ra.getUsername())    + "," +
                    esc(ra.getPassword())    + "," +
                    codes
                );
                writer.newLine();
            }
        }
    }

    // ── Load restAdmins ───────────────────────────────────────────────────────

    public static void loadRestAdmins(DeliverySystem system)
            throws IOException {
        File file = new File("restAdmins.txt");
        if (!file.exists()) return;

        system.getRestAdmins().clear();

        try (BufferedReader reader = openReader(file)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",", -1);
                if (parts.length < 3) continue;
                try {
                    String name     = unesc(parts[0].trim());
                    String username = unesc(parts[1].trim());
                    String password = unesc(parts[2].trim());
                    RestAdmin admin = new RestAdmin(name, username, password);

                    // re-attach the restaurants this admin manages
                    if (parts.length >= 4 && !parts[3].trim().isEmpty()) {
                        for (String codeText : parts[3].split(";")) {
                            try {
                                Restaurant r = system.findRestaurantByCode(
                                    Integer.parseInt(codeText.trim()));
                                if (r != null) {
                                    admin.addRestaurant(r);
                                }
                            } catch (NumberFormatException e) {
                                // skip bad code
                            }
                        }
                    }
                    system.addRestAdmin(admin);
                } catch (Exception e) {
                    // skip malformed line
                }
            }
        }
    }
}
