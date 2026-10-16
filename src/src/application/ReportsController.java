package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.util.ArrayList;

public class ReportsController {

    @FXML private TableView<Object> resultTable;
    @FXML private TableColumn<Object, String> col1;
    @FXML private TableColumn<Object, String> col2;
    @FXML private TableColumn<Object, String> col3;
    @FXML private TableColumn<Object, String> col4;
    @FXML private Label messageLabel;

    private DeliverySystem system = Main.getSystem();

    // ── Customer sorts ────────────────────────────────────────────────────────

    @FXML
    private void handleSortByCredit() {
        // uses Comparable (Customer implements Comparable)
        ArrayList<Customer> sorted = system.getSortedCustomersByCredit();
        showCustomers(sorted, "Customers sorted by credit balance (high to low)");
    }

    @FXML
    private void handleSortByName() {
        // uses Lambda Expression
        ArrayList<Customer> sorted = system.getSortedCustomersByFirstName();
        showCustomers(sorted, "Customers sorted by first name");
    }

    @FXML
    private void handleCreditAbove100() {
        // uses Predicate + Stream
        ArrayList<Customer> filtered = system.getCustomersWithCreditAbove100();
        showCustomers(filtered, "Customers with credit above 100");
    }

    @FXML
    private void handleHighestCredit() {
        // uses generic getMax from SystemReports (Wildcards)
        Customer top = system.getHighestCreditCustomer();
        if (top == null) {
            showMessage("No customers found.", true);
            return;
        }
        ArrayList<Customer> list = new ArrayList<>();
        list.add(top);
        showCustomers(list, "Customer with highest credit");
    }

    // ── Order sorts ───────────────────────────────────────────────────────────

    @FXML
    private void handleSortOrdersByPrice() {
        // uses OrderPriceComparator
        ArrayList<Order> sorted = system.getSortedOrdersByFinalPrice();
        showOrders(sorted, "Orders sorted by final price (high to low)");
    }

    @FXML
    private void handleSortOrdersByDate() {
        // uses Lambda Expression
        ArrayList<Order> sorted = system.getSortedOrdersByDate();
        showOrders(sorted, "Orders sorted by date");
    }

    // ── Restaurant / Rider sorts ──────────────────────────────────────────────

    @FXML
    private void handleSortRestaurantsByRating() {
        // uses RestaurantRatingComparator
        ArrayList<Restaurant> sorted = system.getSortedRestaurantsByRating();
        showRestaurants(sorted, "Restaurants sorted by rating (high to low)");
    }

    @FXML
    private void handleSortRidersByDeliveries() {
        // uses Lambda Expression
        ArrayList<Rider> sorted = system.getSortedRidersByDeliveries();
        showRiders(sorted, "Riders sorted by number of deliveries");
    }

    // ── Stream reports ────────────────────────────────────────────────────────

    @FXML
    private void handleOpenRestaurants() {
        // uses Stream + Method Reference (Restaurant::isOpen)
        ArrayList<Restaurant> open = system.getOpenRestaurants();
        showRestaurants(open, "Open restaurants");
    }

    @FXML
    private void handlePremiumRestaurants() {
        // uses Stream + filter
        ArrayList<Restaurant> premium = system.getPremiumRestaurants();
        showRestaurants(premium, "Premium restaurants");
    }

    @FXML
    private void handleAvailableRiders() {
        // uses Stream + Method Reference (Rider::isAvailable)
        ArrayList<Rider> available = system.getAvailableRiders();
        showRiders(available, "Available riders");
    }

    @FXML
    private void handleTotalMoney() {
        // uses Stream + map + reduce + Method Reference (Order::getFinalPrice)
        double total = system.getTotalMoneyPaid();
        resultTable.setItems(FXCollections.observableArrayList());
        showMessage("Total money paid across all orders: " + total + " NIS", false);
    }

    @FXML
    private void handleAllRestaurantsReport() {
        // uses Wildcard method from SystemReports
        ArrayList<Restaurant> all = system.getAllRestaurantsReport();
        showRestaurants(all, "All restaurants (Wildcard report)");
    }

    // ── Back ──────────────────────────────────────────────────────────────────

    @FXML
    private void handleBack() {
        loadScene("AdminDashboard.fxml", "Admin Dashboard", 800, 600);
    }

    // ── Display helpers ───────────────────────────────────────────────────────

    @SuppressWarnings("unchecked")
    private void showCustomers(ArrayList<Customer> list, String message) {
        col1.setText("Code");
        col2.setText("Name");
        col3.setText("City");
        col4.setText("Balance");

        col1.setCellValueFactory(new PropertyValueFactory<>("customercode"));
        col2.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        col3.setCellValueFactory(new PropertyValueFactory<>("city"));
        col4.setCellValueFactory(new PropertyValueFactory<>("creditbalance"));

        ObservableList<Object> data = FXCollections.observableArrayList(list);
        resultTable.setItems(data);
        showMessage(message + " — " + list.size() + " result(s).", false);
    }

    @SuppressWarnings("unchecked")
    private void showOrders(ArrayList<Order> list, String message) {
        col1.setText("Order Code");
        col2.setText("Restaurant");
        col3.setText("Final Price");
        col4.setText("Status");

        col1.setCellValueFactory(new PropertyValueFactory<>("ordercode"));
        col2.setCellValueFactory(new PropertyValueFactory<>("restaurantName"));
        col3.setCellValueFactory(new PropertyValueFactory<>("finalPrice"));
        col4.setCellValueFactory(new PropertyValueFactory<>("status"));

        ObservableList<Object> data = FXCollections.observableArrayList(list);
        resultTable.setItems(data);
        showMessage(message + " — " + list.size() + " result(s).", false);
    }

    @SuppressWarnings("unchecked")
    private void showRestaurants(ArrayList<Restaurant> list, String message) {
        col1.setText("Code");
        col2.setText("Name");
        col3.setText("Rating");
        col4.setText("Status");

        col1.setCellValueFactory(new PropertyValueFactory<>("rescode"));
        col2.setCellValueFactory(new PropertyValueFactory<>("resname"));
        col3.setCellValueFactory(new PropertyValueFactory<>("rating"));
        col4.setCellValueFactory(new PropertyValueFactory<>("open"));

        ObservableList<Object> data = FXCollections.observableArrayList(list);
        resultTable.setItems(data);
        showMessage(message + " — " + list.size() + " result(s).", false);
    }

    @SuppressWarnings("unchecked")
    private void showRiders(ArrayList<Rider> list, String message) {
        col1.setText("ID");
        col2.setText("Name");
        col3.setText("Vehicle");
        col4.setText("Deliveries");

        col1.setCellValueFactory(new PropertyValueFactory<>("id"));
        col2.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        col3.setCellValueFactory(new PropertyValueFactory<>("vehicle"));
        col4.setCellValueFactory(new PropertyValueFactory<>("ordercount"));

        ObservableList<Object> data = FXCollections.observableArrayList(list);
        resultTable.setItems(data);
        showMessage(message + " — " + list.size() + " result(s).", false);
    }

    private void showMessage(String msg, boolean isError) {
        messageLabel.setText(msg);
        messageLabel.setStyle(isError
            ? "-fx-text-fill: red;"
            : "-fx-text-fill: #27ae60;");
    }

    private void loadScene(String fxmlFile, String title, int width, int height) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Stage stage = (Stage) messageLabel.getScene().getWindow();
            stage.setScene(new Scene(root, width, height));
            stage.setTitle(title);
        } catch (Exception e) {
            e.printStackTrace();
            showMessage("Error loading screen.", true);
        }
    }
}