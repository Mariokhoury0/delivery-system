package application;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Optional;

public class CustomerDashboardController {

    // static holder so UserLoginController can pass the logged-in customer
    private static Customer currentCustomer;

    public static void setCurrentCustomer(Customer customer) {
        currentCustomer = customer;
    }

    @FXML private Label welcomeLabel;
    @FXML private Label balanceLabel;
    @FXML private Label messageLabel;

    @FXML private TextField amountField;
    @FXML private TextField orderCodeField;
    @FXML private TextField restaurantCodeField;
    @FXML private TextField basePriceField;
    @FXML private TextField dayField;
    @FXML private TextField monthField;
    @FXML private TextField yearField;

    @FXML private TableView<Order> ordersTable;
    @FXML private TableColumn<Order, Integer> orderCodeCol;
    @FXML private TableColumn<Order, String> restaurantCol;
    @FXML private TableColumn<Order, Double> finalPriceCol;
    @FXML private TableColumn<Order, String> statusCol;
    @FXML private TableColumn<Order, String> dateCol;

    private DeliverySystem system = Main.getSystem();

    @FXML
    public void initialize() {
        orderCodeCol.setCellValueFactory(new PropertyValueFactory<>("ordercode"));
        restaurantCol.setCellValueFactory(new PropertyValueFactory<>("restaurantName"));
        finalPriceCol.setCellValueFactory(new PropertyValueFactory<>("finalPrice"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("orderDate"));

        if (currentCustomer != null) {
            welcomeLabel.setText("Welcome, " + currentCustomer.getFirstname() + "!");
            refreshBalance();
            loadMyOrders();
        }
    }

    private void refreshBalance() {
        balanceLabel.setText("Balance: " + currentCustomer.getCreditbalance() + " NIS");
    }

    private void loadMyOrders() {
        ArrayList<Order> orders =
            system.getOrdersByCustomer(currentCustomer.getCustomercode());
        ordersTable.setItems(FXCollections.observableArrayList(orders));
    }

    @FXML
    private void handleAddMoney() {
        try {
            double amount = Double.parseDouble(amountField.getText().trim());
            if (Double.isNaN(amount) || Double.isInfinite(amount) || amount <= 0) {
                showMessage("Amount must be a positive number.", true);
                return;
            }
            currentCustomer.setCreditbalance(
                currentCustomer.getCreditbalance() + amount);
            refreshBalance();
            amountField.clear();
            showMessage("Added " + amount + " NIS successfully.", false);
        } catch (NumberFormatException e) {
            showMessage("Please enter a valid amount.", true);
        }
    }

    @FXML
    private void handleWithdraw() {
        try {
            double amount = Double.parseDouble(amountField.getText().trim());
            if (Double.isNaN(amount) || Double.isInfinite(amount) || amount <= 0) {
                showMessage("Amount must be a positive number.", true);
                return;
            }
            if (amount > currentCustomer.getCreditbalance()) {
                showMessage("Insufficient balance.", true);
                return;
            }
            currentCustomer.setCreditbalance(
                currentCustomer.getCreditbalance() - amount);
            refreshBalance();
            amountField.clear();
            showMessage("Withdrew " + amount + " NIS successfully.", false);
        } catch (NumberFormatException e) {
            showMessage("Please enter a valid amount.", true);
        }
    }

    @FXML
    private void handlePlaceOrder() {
        try {
            int orderCode = Integer.parseInt(orderCodeField.getText().trim());
            int restCode  = Integer.parseInt(restaurantCodeField.getText().trim());
            double base   = Double.parseDouble(basePriceField.getText().trim());
            int day       = Integer.parseInt(dayField.getText().trim());
            int month     = Integer.parseInt(monthField.getText().trim());
            int year      = Integer.parseInt(yearField.getText().trim());

            Restaurant restaurant = system.findRestaurantByCode(restCode);
            if (restaurant == null) {
                showMessage("Restaurant not found.", true);
                return;
            }

            String result = system.placeOrder(
                currentCustomer, restaurant, orderCode, day, month, year, base);
            showMessage(result, result.startsWith("Error") || !result.contains("success"));

            if (result.contains("success")) {
                refreshBalance();
                loadMyOrders();
                orderCodeField.clear();
                restaurantCodeField.clear();
                basePriceField.clear();
                dayField.clear();
                monthField.clear();
                yearField.clear();
            }
        } catch (NumberFormatException e) {
            showMessage("Please fill all order fields with valid numbers.", true);
        }
    }

    @FXML
    private void handleMyOrders() {
        loadMyOrders();
        showMessage("Showing your orders.", false);
    }

    @FXML
    private void handleMyRestaurants() {
        ArrayList<Restaurant> list =
            system.getRestaurantsByCustomer(currentCustomer.getCustomercode());
        if (list.isEmpty()) {
            showMessage("You have not ordered from any restaurant yet.", false);
            return;
        }
        showListDialog("Restaurants You Ordered From",
            restaurantsToStrings(list));
    }

    @FXML
    private void handlePremiumRestaurants() {
        ArrayList<Restaurant> list =
            system.getPremiumRestaurantsByCustomer(currentCustomer);
        if (list.isEmpty()) {
            showMessage("You have not ordered from any premium restaurant.", false);
            return;
        }
        showListDialog("Premium Restaurants You Ordered From",
            restaurantsToStrings(list));
    }

    @FXML
    private void handleSearchRestaurant() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Search Restaurant");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter restaurant code:");
        Optional<String> result = dialog.showAndWait();
        if (!result.isPresent()) return;
        try {
            int code = Integer.parseInt(result.get().trim());
            Restaurant r = system.findRestaurantByCode(code);
            if (r == null) {
                showMessage("Restaurant not found.", true);
            } else {
                showMessage(r.getResname()
                    + " | " + r.getKitchentype()
                    + " | Rating: " + r.getRating()
                    + " | " + (r.isOpen() ? "Open" : "Closed")
                    + " | Delivery fee: " + r.getBasedeliveryfee(), false);
            }
        } catch (NumberFormatException e) {
            showMessage("Restaurant code must be a number.", true);
        }
    }

    @FXML
    private void handleUpdateDetails() {
        try {
            String phone = showInputDialog("Update Details",
                "New phone (" + currentCustomer.getPhone() + "):");
            String email = showInputDialog("Update Details",
                "New email (" + currentCustomer.getEmail() + "):");
            String street = showInputDialog("Update Details",
                "New street (" + currentCustomer.getStreet() + "):");
            String city = showInputDialog("Update Details",
                "New city (" + currentCustomer.getCity() + "):");

            // a blank answer keeps the current value; invalid values are rejected
            String error = system.updateCustomerDetails(
                currentCustomer, phone, email, street, city);
            if (error != null) {
                showMessage(error, true);
                return;
            }
            showMessage("Details updated successfully.", false);
        } catch (Exception e) {
            showMessage("Update cancelled.", true);
        }
    }

    @FXML
    private void handleLogout() {
        currentCustomer = null;
        loadScene("Welcome.fxml", "Food Delivery System", 600, 400);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void showMessage(String msg, boolean isError) {
        messageLabel.setText(msg);
        messageLabel.setStyle(isError
            ? "-fx-text-fill: red;"
            : "-fx-text-fill: #27ae60;");
    }

    private String showInputDialog(String title, String prompt) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(null);
        dialog.setContentText(prompt);
        Optional<String> result = dialog.showAndWait();
        return result.orElseThrow(() -> new RuntimeException("Cancelled"));
    }

    private void showListDialog(String title, ArrayList<String> items) {
        ListView<String> listView = new ListView<>();
        listView.setItems(FXCollections.observableArrayList(items));
        listView.setPrefSize(600, 300);
        Stage dialog = new Stage();
        dialog.setTitle(title);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setScene(new Scene(listView, 600, 300));
        dialog.showAndWait();
    }

    private ArrayList<String> restaurantsToStrings(ArrayList<Restaurant> list) {
        ArrayList<String> result = new ArrayList<>();
        for (Restaurant r : list) {
            result.add(r.getResname()
                + " | " + r.getKitchentype()
                + " | Rating: " + r.getRating()
                + " | " + (r.isOpen() ? "Open" : "Closed"));
        }
        return result;
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