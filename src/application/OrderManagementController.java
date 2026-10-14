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

public class OrderManagementController {

    @FXML private TableView<Order> orderTable;
    @FXML private TableColumn<Order, Integer> codeCol;
    @FXML private TableColumn<Order, Integer> customerCol;
    @FXML private TableColumn<Order, String> restaurantCol;
    @FXML private TableColumn<Order, Double> basePriceCol;
    @FXML private TableColumn<Order, Double> finalPriceCol;
    @FXML private TableColumn<Order, String> statusCol;
    @FXML private TableColumn<Order, String> dateCol;
    @FXML private TableColumn<Order, String> riderCol;
    @FXML private TextField searchField;
    @FXML private TextField customerCodeField;
    @FXML private TextField restaurantCodeField;
    @FXML private Label messageLabel;

    private DeliverySystem system = Main.getSystem();

    @FXML
    public void initialize() {
        codeCol.setCellValueFactory(new PropertyValueFactory<>("ordercode"));
        customerCol.setCellValueFactory(new PropertyValueFactory<>("customercode"));
        restaurantCol.setCellValueFactory(new PropertyValueFactory<>("restaurantName"));
        basePriceCol.setCellValueFactory(new PropertyValueFactory<>("base"));
        finalPriceCol.setCellValueFactory(new PropertyValueFactory<>("finalPrice"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        riderCol.setCellValueFactory(new PropertyValueFactory<>("riderId"));

        loadAllOrders();
    }

    private void loadAllOrders() {
        ObservableList<Order> data =
            FXCollections.observableArrayList(system.getOrders());
        orderTable.setItems(data);
    }

    @FXML
    private void handleShowAll() {
        loadAllOrders();
        searchField.clear();
        customerCodeField.clear();
        restaurantCodeField.clear();
        showMessage("Showing all orders.", false);
    }

    @FXML
    private void handleSearchByCode() {
        String input = searchField.getText().trim();
        if (input.isEmpty()) {
            showMessage("Please enter an order code.", true);
            return;
        }
        try {
            int code = Integer.parseInt(input);
            Order order = system.findOrderByCode(code);
            if (order == null) {
                showMessage("Order with code " + code + " not found.", true);
                orderTable.setItems(FXCollections.observableArrayList());
            } else {
                orderTable.setItems(FXCollections.observableArrayList(order));
                showMessage("Order found.", false);
            }
        } catch (NumberFormatException e) {
            showMessage("Order code must be a number.", true);
        }
    }

    @FXML
    private void handleSearchByCustomer() {
        String input = customerCodeField.getText().trim();
        if (input.isEmpty()) {
            showMessage("Please enter a customer code.", true);
            return;
        }
        try {
            int code = Integer.parseInt(input);
            ArrayList<Order> orders = system.getOrdersByCustomer(code);
            if (orders.isEmpty()) {
                showMessage("No orders found for customer " + code + ".", true);
                orderTable.setItems(FXCollections.observableArrayList());
            } else {
                orderTable.setItems(FXCollections.observableArrayList(orders));
                showMessage("Found " + orders.size() + " order(s).", false);
            }
        } catch (NumberFormatException e) {
            showMessage("Customer code must be a number.", true);
        }
    }

    @FXML
    private void handleSearchByRestaurant() {
        String input = restaurantCodeField.getText().trim();
        if (input.isEmpty()) {
            showMessage("Please enter a restaurant code.", true);
            return;
        }
        try {
            int code = Integer.parseInt(input);
            ArrayList<Order> orders = system.getOrdersForRestaurant(code);
            if (orders.isEmpty()) {
                showMessage("No orders found for restaurant " + code + ".", true);
                orderTable.setItems(FXCollections.observableArrayList());
            } else {
                orderTable.setItems(FXCollections.observableArrayList(orders));
                showMessage("Found " + orders.size() + " order(s).", false);
            }
        } catch (NumberFormatException e) {
            showMessage("Restaurant code must be a number.", true);
        }
    }

    @FXML
    private void handleShowHighest() {
        ArrayList<Order> sorted = system.getSortedOrdersByFinalPrice();
        if (sorted.isEmpty()) {
            showMessage("No orders in the system.", true);
            return;
        }
        Order highest = sorted.get(0);
        orderTable.setItems(FXCollections.observableArrayList(highest));
        showMessage("Highest price order: #" + highest.getOrdercode()
            + " — " + highest.getFinalPrice() + " NIS", false);
    }

    @FXML
    private void handleBack() {
        loadScene("AdminDashboard.fxml", "Admin Dashboard", 800, 600);
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
            Stage stage = (Stage) orderTable.getScene().getWindow();
            stage.setScene(new Scene(root, width, height));
            stage.setTitle(title);
        } catch (Exception e) {
            e.printStackTrace();
            showMessage("Error loading screen.", true);
        }
    }
}