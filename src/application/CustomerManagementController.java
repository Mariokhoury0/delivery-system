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
import javafx.stage.Modality;
import java.util.ArrayList;
import java.util.Optional;

public class CustomerManagementController {

    @FXML private TableView<Customer> customerTable;
    @FXML private TableColumn<Customer, Integer> codeCol;
    @FXML private TableColumn<Customer, String> firstNameCol;
    @FXML private TableColumn<Customer, String> lastNameCol;
    @FXML private TableColumn<Customer, String> cityCol;
    @FXML private TableColumn<Customer, String> phoneCol;
    @FXML private TableColumn<Customer, String> emailCol;
    @FXML private TableColumn<Customer, Double> balanceCol;
    @FXML private TextField searchField;
    @FXML private Label messageLabel;

    private DeliverySystem system = Main.getSystem();

    @FXML
    public void initialize() {
        // wire each column to the correct getter in Customer
        codeCol.setCellValueFactory(new PropertyValueFactory<>("customercode"));
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        cityCol.setCellValueFactory(new PropertyValueFactory<>("city"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        balanceCol.setCellValueFactory(new PropertyValueFactory<>("creditbalance"));

        loadAllCustomers();
    }

    private void loadAllCustomers() {
        ObservableList<Customer> data =
            FXCollections.observableArrayList(system.getCustomers());
        customerTable.setItems(data);
    }

    @FXML
    private void handleShowAll() {
        loadAllCustomers();
        searchField.clear();
        messageLabel.setText("Showing all customers.");
        messageLabel.setStyle("-fx-text-fill: #555555;");
    }

    @FXML
    private void handleSearch() {
        String input = searchField.getText().trim();
        if (input.isEmpty()) {
            messageLabel.setText("Please enter a customer code.");
            messageLabel.setStyle("-fx-text-fill: red;");
            return;
        }
        try {
            int code = Integer.parseInt(input);
            Customer customer = system.findCustomerByCode(code);
            if (customer == null) {
                messageLabel.setText("Customer with code " + code + " not found.");
                messageLabel.setStyle("-fx-text-fill: red;");
                customerTable.setItems(FXCollections.observableArrayList());
            } else {
                customerTable.setItems(
                    FXCollections.observableArrayList(customer));
                messageLabel.setText("Customer found.");
                messageLabel.setStyle("-fx-text-fill: #27ae60;");
            }
        } catch (NumberFormatException e) {
            messageLabel.setText("Customer code must be a number.");
            messageLabel.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    private void handleAdd() {
        // collect input via dialogs
        try {
            int code = Integer.parseInt(showInputDialog("Add Customer", "Customer code:"));
            if (system.findCustomerByCode(code) != null) {
                showMessage("Customer code already exists.", true);
                return;
            }
            String firstName = showInputDialog("Add Customer", "First name:");
            String lastName  = showInputDialog("Add Customer", "Last name:");
            String street    = showInputDialog("Add Customer", "Street:");
            String city      = showInputDialog("Add Customer", "City:");
            String zipcode   = showInputDialog("Add Customer", "Zipcode:");
            String phone     = showInputDialog("Add Customer", "Phone (10 digits):");
            String email     = showInputDialog("Add Customer", "Email:");
            double balance   = Double.parseDouble(
                showInputDialog("Add Customer", "Credit balance:"));

            Customer customer = new Customer(code, firstName, lastName,
                street, city, zipcode, phone, email, balance);
            system.addCustomer(customer);
            loadAllCustomers();
            showMessage("Customer added successfully.", false);
        } catch (NumberFormatException e) {
            showMessage("Invalid number entered.", true);
        } catch (Exception e) {
            showMessage("Cancelled or error: " + e.getMessage(), true);
        }
    }

    @FXML
    private void handleUpdate() {
        Customer selected = customerTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showMessage("Please select a customer from the table first.", true);
            return;
        }
        try {
            String phone = showInputDialog("Update Customer",
                "New phone (" + selected.getPhone() + "):");
            String email = showInputDialog("Update Customer",
                "New email (" + selected.getEmail() + "):");
            String street = showInputDialog("Update Customer",
                "New street (" + selected.getStreet() + "):");
            String city = showInputDialog("Update Customer",
                "New city (" + selected.getCity() + "):");

            selected.setPhone(phone);
            selected.setEmail(email);
            selected.setStreet(street);
            selected.setCity(city);

            loadAllCustomers();
            showMessage("Customer updated successfully.", false);
        } catch (Exception e) {
            showMessage("Update cancelled.", true);
        }
    }

    @FXML
    private void handleShowOrders() {
        Customer selected = customerTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showMessage("Please select a customer from the table first.", true);
            return;
        }
        ArrayList<Order> orders =
            system.getOrdersByCustomer(selected.getCustomercode());
        if (orders.isEmpty()) {
            showMessage("This customer has no orders.", false);
            return;
        }
        showListDialog("Orders for " + selected.getFirstname(),
            ordersToStrings(orders));
    }

    @FXML
    private void handleShowRestaurants() {
        Customer selected = customerTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showMessage("Please select a customer from the table first.", true);
            return;
        }
        ArrayList<Restaurant> restaurants =
            system.getRestaurantsByCustomer(selected.getCustomercode());
        if (restaurants.isEmpty()) {
            showMessage("This customer has not ordered from any restaurant yet.", false);
            return;
        }
        showListDialog("Restaurants ordered from by " + selected.getFirstname(),
            restaurantsToStrings(restaurants));
    }

    @FXML
    private void handleShowPremium() {
        Customer selected = customerTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showMessage("Please select a customer from the table first.", true);
            return;
        }
        ArrayList<Restaurant> premium =
            system.getPremiumRestaurantsByCustomer(selected);
        if (premium.isEmpty()) {
            showMessage("This customer has not ordered from any premium restaurant.", false);
            return;
        }
        showListDialog("Premium restaurants for " + selected.getFirstname(),
            restaurantsToStrings(premium));
    }

    @FXML
    private void handleBack() {
        loadScene("AdminDashboard.fxml", "Admin Dashboard", 800, 600);
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
        return result.orElseThrow(() ->
            new RuntimeException("Cancelled"));
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

    private ArrayList<String> ordersToStrings(ArrayList<Order> orders) {
        ArrayList<String> result = new ArrayList<>();
        for (Order o : orders) {
            result.add("Order #" + o.getOrdercode()
                + " | Restaurant: " + o.getRestaurant().getResname()
                + " | Final Price: " + o.getFinalPrice()
                + " | Status: " + o.getStatus());
        }
        return result;
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
            Stage stage = (Stage) customerTable.getScene().getWindow();
            stage.setScene(new Scene(root, width, height));
            stage.setTitle(title);
        } catch (Exception e) {
            e.printStackTrace();
            showMessage("Error loading screen.", true);
        }
    }
}
