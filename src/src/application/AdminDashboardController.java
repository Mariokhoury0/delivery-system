package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class AdminDashboardController {

    @FXML private Label messageLabel;

    @FXML
    private void handleCustomers() {
        loadScene("CustomerManagement.fxml", "Customer Management", 900, 600);
    }

    @FXML
    private void handleOrders() {
        loadScene("OrderManagement.fxml", "Order Management", 900, 600);
    }

    @FXML
    private void handleReports() {
        loadScene("Reports.fxml", "Reports and Sorting", 900, 600);
    }

    @FXML
    private void handleRestaurants() {
        // partner 2's screen - will be connected when merged
        messageLabel.setText("Restaurant management - handled by partner.");
        messageLabel.setStyle("-fx-text-fill: #e67e22; -fx-font-size: 12px;");
    }

    @FXML
    private void handleRiders() {
        // partner 2's screen - will be connected when merged
        messageLabel.setText("Rider management - handled by partner.");
        messageLabel.setStyle("-fx-text-fill: #e67e22; -fx-font-size: 12px;");
    }

    @FXML
    private void handleRestAdmins() {
        // partner 2's screen - will be connected when merged
        messageLabel.setText("Restaurant admin management - handled by partner.");
        messageLabel.setStyle("-fx-text-fill: #e67e22; -fx-font-size: 12px;");
    }

    @FXML
    private void handleSave() {
        try {
            FileManager.saveAll(Main.getSystem());
            messageLabel.setText("Data saved successfully.");
            messageLabel.setStyle("-fx-text-fill: #27ae60; -fx-font-size: 12px;");
        } catch (Exception e) {
            messageLabel.setText("Error saving data: " + e.getMessage());
            messageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        }
    }

    @FXML
    private void handleLoad() {
        try {
            FileManager.loadAll(Main.getSystem());
            messageLabel.setText("Data loaded successfully.");
            messageLabel.setStyle("-fx-text-fill: #2980b9; -fx-font-size: 12px;");
        } catch (Exception e) {
            messageLabel.setText("Error loading data: " + e.getMessage());
            messageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        }
    }

    @FXML
    private void handleLogout() {
        loadScene("Welcome.fxml", "Food Delivery System", 600, 400);
    }

    private void loadScene(String fxmlFile, String title, int width, int height) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Stage stage = (Stage) messageLabel.getScene().getWindow();
            stage.setScene(new Scene(root, width, height));
            stage.setTitle(title);
        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error loading screen.");
            messageLabel.setStyle("-fx-text-fill: red;");
        }
    }
}