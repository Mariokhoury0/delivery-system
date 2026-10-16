package application;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class UserLoginController {

    @FXML private ComboBox<String> userTypeCombo;
    @FXML private TextField idField;
    @FXML private Label errorLabel;

    private DeliverySystem system = Main.getSystem();

    @FXML
    public void initialize() {
        // populate the ComboBox with user types — this is also where the Enum
        // requirement is satisfied (UserType enum drives these options)
        userTypeCombo.setItems(FXCollections.observableArrayList(
            UserType.CUSTOMER.getDisplayName(),
            UserType.RIDER.getDisplayName()
        ));
    }

    @FXML
    private void handleLogin() {
        String selected = userTypeCombo.getValue();
        String input = idField.getText().trim();

        if (selected == null) {
            errorLabel.setText("Please select a user type.");
            return;
        }
        if (input.isEmpty()) {
            errorLabel.setText("Please enter your ID or code.");
            return;
        }

        if (selected.equals(UserType.CUSTOMER.getDisplayName())) {
            loginAsCustomer(input);
        } else if (selected.equals(UserType.RIDER.getDisplayName())) {
            loginAsRider(input);
        }
    }

    private void loginAsCustomer(String input) {
        try {
            int code = Integer.parseInt(input);
            Customer customer = system.findCustomerByCode(code);
            if (customer == null) {
                errorLabel.setText("Customer with code " + code + " not found.");
                return;
            }
            // pass the customer to the dashboard via a static holder
            CustomerDashboardController.setCurrentCustomer(customer);
            loadScene("CustomerDashboard.fxml", "Customer Dashboard", 800, 600);
        } catch (NumberFormatException e) {
            errorLabel.setText("Customer code must be a number.");
        }
    }

    private void loginAsRider(String input) {
        // partner 2 owns the Rider screen — just show a message for now
        errorLabel.setText("Rider login: handled by partner. Coming soon.");
    }

    @FXML
    private void handleBack() {
        loadScene("Welcome.fxml", "Food Delivery System", 600, 400);
    }

    private void loadScene(String fxmlFile, String title, int width, int height) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Stage stage = (Stage) idField.getScene().getWindow();
            stage.setScene(new Scene(root, width, height));
            stage.setTitle(title);
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Error loading screen.");
        }
    }
}