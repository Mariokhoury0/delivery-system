package application;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class WelcomeController {

    @FXML private Button adminLoginBtn;
    @FXML private Button userLoginBtn;
    @FXML private Button exitBtn;

    @FXML
    private void handleAdminLogin() {
        loadScene("AdminLogin.fxml", "Admin Login", 600, 400);
    }

    @FXML
    private void handleUserLogin() {
        loadScene("UserLogin.fxml", "User Login", 600, 400);
    }

    @FXML
    private void handleExit() {
        Platform.exit();
    }

    private void loadScene(String fxmlFile, String title, int width, int height) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Stage stage = (Stage) adminLoginBtn.getScene().getWindow();
            stage.setScene(new Scene(root, width, height));
            stage.setTitle(title);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}