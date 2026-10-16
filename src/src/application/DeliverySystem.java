package application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DeliverySystem {

    private DeliveryDataBase database;

    private ArrayList<Customer> customers;
    private ArrayList<RestAdmin> restAdmins;
    private ArrayList<Restaurant> restaurants;
    private ArrayList<Rider> riders;
    private ArrayList<Order> orders;

    private HashMap<Integer, ArrayList<Order>> ordersByCustomer;
    private Hashtable<Integer, ArrayList<Restaurant>> restaurantsByCustomer;
    private HashMap<Integer, Double> paymentsByCustomer;

    public DeliverySystem() {
        database = new DeliveryDataBase();
        customers = database.getCustomers();
        restAdmins = database.getRestaurantManagers();
        restaurants = database.getRestaurants();
        riders = database.getRiders();
        orders = database.getOrders();
        ordersByCustomer = database.getOrdersByCustomer();
        restaurantsByCustomer = database.getRestaurantsByCustomer();
        paymentsByCustomer = database.getPaymentsByCustomer();
    }

    public DeliveryDataBase getDatabase() {
        return database;
    }

    // ─── Getters for collections (controllers need direct access) ───────────────

    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    public ArrayList<Restaurant> getRestaurants() {
        return restaurants;
    }

    public ArrayList<Rider> getRiders() {
        return riders;
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public ArrayList<RestAdmin> getRestAdmins() {
        return restAdmins;
    }

    // ─── Add methods ─────────────────────────────────────────────────────────────

    public boolean addCustomer(Customer customer) {
        if (findCustomerByCode(customer.getCustomercode()) != null) {
            return false;
        }
        customers.add(customer);
        return true;
    }

    public boolean addRestaurant(Restaurant restaurant) {
        if (findRestaurantByCode(restaurant.getRescode()) != null) {
            return false;
        }
        restaurants.add(restaurant);
        return true;
    }

    public boolean addRider(Rider rider) {
        if (findRiderById(rider.getId()) != null) {
            return false;
        }
        riders.add(rider);
        return true;
    }

    public boolean addOrder(Order order) {
        if (findOrderByCode(order.getOrdercode()) != null) {
            return false;
        }
        orders.add(order);
        updateOrderMaps(order);
        return true;
    }

    public boolean addRestAdmin(RestAdmin admin) {
        if (findRestaurantManager(admin.getUsername()) != null) {
            return false;
        }
        restAdmins.add(admin);
        return true;
    }

    // ─── Find methods (public so controllers can use them) ───────────────────────

    public Customer findCustomerByCode(int code) {
        for (Customer customer : customers) {
            if (customer.getCustomercode() == code) {
                return customer;
            }
        }
        return null;
    }

    public Restaurant findRestaurantByCode(int code) {
        for (Restaurant restaurant : restaurants) {
            if (restaurant.getRescode() == code) {
                return restaurant;
            }
        }
        return null;
    }

    public Rider findRiderById(String id) {
        for (Rider rider : riders) {
            if (rider.getId().equals(id)) {
                return rider;
            }
        }
        return null;
    }

    public Order findOrderByCode(int code) {
        for (Order order : orders) {
            if (order.getOrdercode() == code) {
                return order;
            }
        }
        return null;
    }

    public RestAdmin findRestaurantManager(String username) {
        for (RestAdmin manager : restAdmins) {
            if (manager.getUsername().equals(username)) {
                return manager;
            }
        }
        return null;
    }

    public RestAdmin findRestaurantManagerLogin(String username, String password) {
        for (RestAdmin manager : restAdmins) {
            if (manager.getUsername().equals(username)
                    && manager.getPassword().equals(password)) {
                return manager;
            }
        }
        return null;
    }

    // ─── Admin login check ────────────────────────────────────────────────────────

    public boolean adminLogin(String username, String password) {
        Admin admin = database.getSystemAdmin();
        return admin.getUsername().equals(username) && admin.getPassword().equals(password);
    }

    // ─── Order logic ─────────────────────────────────────────────────────────────

    public String placeOrder(Customer customer, Restaurant restaurant,
                             int orderCode, int day, int month, int year,
                             double basePrice) {
        if (customer == null || restaurant == null) {
            return "Customer or restaurant is missing.";
        }
        if (!isValidDate(day, month, year)) {
            return "Invalid date.";
        }
        if (Double.isNaN(basePrice) || Double.isInfinite(basePrice) || basePrice <= 0) {
            return "Base price must be a positive number.";
        }
        if (findOrderByCode(orderCode) != null) {
            return "Order code already exists.";
        }
        if (!restaurant.isOpen()) {
            return "Restaurant is closed.";
        }
        if (restaurant instanceof PremiumRestaurant) {
            PremiumRestaurant premium = (PremiumRestaurant) restaurant;
            if (!premium.isAboveMinimumOrder(basePrice)) {
                return "Minimum order for this restaurant is " + premium.getMinimumOrderAllowed();
            }
        }
        double finalPrice = restaurant.CalculateFinalPrice(basePrice);
        try {
            checkCustomerBalance(customer, finalPrice);
        } catch (InsufficientBalanceException e) {
            return "Error: " + e.getMessage();
        }
        customer.setCreditbalance(customer.getCreditbalance() - finalPrice);
        Order order = new Order(orderCode, customer.getCustomercode(), restaurant,
                restaurant.getRescode(), "", day, month, year, 0, 0, 0,
                basePrice, finalPrice, "Pending");
        addOrder(order);
        return "Order placed successfully.";
    }

    public boolean assignRiderToOrder(String riderId, int orderCode)
            throws DeliveryPersonUnavailableException {
        Rider rider = findRiderById(riderId);
        if (rider == null) return false;
        Order order = findOrderByCode(orderCode);
        if (order == null) return false;
        if (!rider.isAvailable()) {
            throw new DeliveryPersonUnavailableException("Rider is not available.");
        }
        if (order.getRiderId() != null && !order.getRiderId().equals("")) {
            return false;
        }
        order.setRiderId(riderId);
        order.setStatus("Sent");
        rider.addOrder(order);
        rider.setAvailable(false);
        return true;
    }

    public Order getActiveOrder(Rider rider) {
        for (Order order : rider.getOrders()) {
            if (order.getStatus().equalsIgnoreCase("Sent") ||
                order.getStatus().equalsIgnoreCase("On The Way")) {
                return order;
            }
        }
        return null;
    }

    public ArrayList<Order> getOrdersByCustomer(int customerCode) {
        ArrayList<Order> result = ordersByCustomer.get(customerCode);
        if (result == null) return new ArrayList<>();
        return result;
    }

    public ArrayList<Restaurant> getRestaurantsByCustomer(int customerCode) {
        ArrayList<Restaurant> result = restaurantsByCustomer.get(customerCode);
        if (result == null) return new ArrayList<>();
        return result;
    }

    public ArrayList<Restaurant> getPremiumRestaurantsByCustomer(Customer customer) {
        return database.getPremiumRestaurantsByCustomer(customer);
    }

    public Customer getCustomerWithMostOrders() {
        return database.getCustomerWithMostOrders();
    }

    public Rider getRiderWithMostDeliveries() {
        return database.getRiderWithMostDeliveries();
    }

    public ArrayList<Order> getOrdersForRestaurant(int restaurantCode) {
        ArrayList<Order> result = new ArrayList<>();
        for (Order order : orders) {
            if (order.getRescode() == restaurantCode) {
                result.add(order);
            }
        }
        return result;
    }

    // ─── Sorting methods (return sorted copies, don't mutate the original list) ──

    public ArrayList<Customer> getSortedCustomersByCredit() {
        ArrayList<Customer> sorted = new ArrayList<>(customers);
        Collections.sort(sorted, Collections.reverseOrder()); // high to low
        return sorted;
    }

    public ArrayList<Customer> getSortedCustomersByFirstName() {
        ArrayList<Customer> sorted = new ArrayList<>(customers);
        sorted.sort((c1, c2) -> c1.getFirstname().compareToIgnoreCase(c2.getFirstname()));
        return sorted;
    }

    public ArrayList<Order> getSortedOrdersByFinalPrice() {
        ArrayList<Order> sorted = new ArrayList<>(orders);
        sorted.sort(new OrderPriceComparator());
        return sorted;
    }

    public ArrayList<Order> getSortedOrdersByDate() {
        ArrayList<Order> sorted = new ArrayList<>(orders);
        sorted.sort((o1, o2) -> Integer.compare(o1.getOrderDateNumber(), o2.getOrderDateNumber()));
        return sorted;
    }

    public ArrayList<Restaurant> getSortedRestaurantsByRating() {
        ArrayList<Restaurant> sorted = new ArrayList<>(restaurants);
        sorted.sort(new RestaurantRatingComparator());
        return sorted;
    }

    public ArrayList<Rider> getSortedRidersByDeliveries() {
        ArrayList<Rider> sorted = new ArrayList<>(riders);
        sorted.sort((r1, r2) -> Integer.compare(r2.getOrdercount(), r1.getOrdercount()));
        return sorted;
    }

    // ─── Stream / filter methods (return lists for display in TableView) ─────────

    public ArrayList<Customer> getCustomersWithCreditAbove100() {
        Predicate<Customer> highBalance = c -> c.getCreditbalance() > 100;
        return customers.stream()
                .filter(highBalance)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Restaurant> getOpenRestaurants() {
        return restaurants.stream()
                .filter(Restaurant::isOpen)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Restaurant> getPremiumRestaurants() {
        return restaurants.stream()
                .filter(r -> r instanceof PremiumRestaurant)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Rider> getAvailableRiders() {
        return riders.stream()
                .filter(Rider::isAvailable)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public double getTotalMoneyPaid() {
        return orders.stream()
                .map(Order::getFinalPrice)
                .reduce(0.0, Double::sum);
    }

    // ─── SystemReports (Wildcards) ────────────────────────────────────────────────

    public ArrayList<Restaurant> getAllRestaurantsReport() {
        return new ArrayList<>(restaurants);
    }

    public double getTotalOrdersPrice() {
        return SystemReports.calculateTotalOrdersPrice(orders);
    }

    public Customer getHighestCreditCustomer() {
        return SystemReports.getMax(customers);
    }

    // ─── Balance helpers ──────────────────────────────────────────────────────────

    private void checkCustomerBalance(Customer customer, double finalPrice)
            throws InsufficientBalanceException {
        if (customer.getCreditbalance() < finalPrice) {
            throw new InsufficientBalanceException("Customer does not have enough balance.");
        }
    }

    private void updateOrderMaps(Order order) {
        int customerCode = order.getCustomercode();
        database.addOrderToCustomerMap(customerCode, order);
        if (!restaurantsByCustomer.containsKey(customerCode)) {
            restaurantsByCustomer.put(customerCode, new ArrayList<Restaurant>());
        }
        Restaurant restaurant = order.getRestaurant();
        if (restaurant != null && !restaurantsByCustomer.get(customerCode).contains(restaurant)) {
            restaurantsByCustomer.get(customerCode).add(restaurant);
        }
        double oldTotal = 0;
        if (paymentsByCustomer.containsKey(customerCode)) {
            oldTotal = paymentsByCustomer.get(customerCode);
        }
        paymentsByCustomer.put(customerCode, oldTotal + order.getFinalPrice());
    }

    // ─── Reload support ───────────────────────────────────────────────────────────

    /** Clears all orders and everything derived from them, so orders can be reloaded without duplicates. */
    public void clearOrderData() {
        orders.clear();
        ordersByCustomer.clear();
        restaurantsByCustomer.clear();
        paymentsByCustomer.clear();
        for (Rider rider : riders) {
            rider.getOrders().clear();
        }
    }

    // ─── Input validation helpers ────────────────────────────────────────────────

    public static boolean isValidPhone(String phone) {
        return phone != null && phone.matches("\\d{10}");
    }

    public static boolean isValidEmail(String email) {
        return email != null && email.matches("[^@\\s,;]+@[^@\\s,;]+\\.[^@\\s,;]+");
    }

    /**
     * Updates a customer's contact details. A blank value keeps the current one.
     * Returns null on success, or an error message (and changes nothing) if a value is invalid.
     */
    public String updateCustomerDetails(Customer customer, String phone, String email,
                                        String street, String city) {
        phone  = phone  == null ? "" : phone.trim();
        email  = email  == null ? "" : email.trim();
        street = street == null ? "" : street.trim();
        city   = city   == null ? "" : city.trim();
        if (!phone.isEmpty() && !isValidPhone(phone)) {
            return "Phone must be exactly 10 digits.";
        }
        if (!email.isEmpty() && !isValidEmail(email)) {
            return "Invalid email address.";
        }
        if (!phone.isEmpty())  customer.setPhone(phone);
        if (!email.isEmpty())  customer.setEmail(email);
        if (!street.isEmpty()) customer.setStreet(street);
        if (!city.isEmpty())   customer.setCity(city);
        return null;
    }

    // ─── Date validation ──────────────────────────────────────────────────────────

    public boolean isValidDate(int day, int month, int year) {
        if (year < 2000 || year > 2100) return false;
        if (month < 1 || month > 12) return false;
        if (day < 1) return false;
        int maxDay;
        if (month == 2) {
            maxDay = isLeapYear(year) ? 29 : 28;
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            maxDay = 30;
        } else {
            maxDay = 31;
        }
        return day <= maxDay;
    }

    private boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
    }

    @Override
    public String toString() {
        return "DeliverySystem [customers=" + customers
                + ", restAdmins=" + restAdmins
                + ", restaurants=" + restaurants
                + ", riders=" + riders
                + ", orders=" + orders + "]";
    }
}