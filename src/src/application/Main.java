package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static DeliverySystem system = new DeliverySystem();

    public static DeliverySystem getSystem() {
        return system;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        seedData();
        Parent root = FXMLLoader.load(getClass().getResource("Welcome.fxml"));
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("Food Delivery System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void seedData() {
        Customer c1 = new Customer(1, "Mario", "Khoury", "Allenby", "Haifa", "3500000", "0506432343", "mariokhoury0@gmail.com", 200);
        Customer c2 = new Customer(2, "Adam", "Willy", "Hanamal", "Haifa", "1111111", "0547590540", "Adam@gmail.com", 200);
        Customer c3 = new Customer(3, "Lina", "Hassan", "Palmach 8", "Acre", "2400000", "0503333333", "lina@gmail.com", 500);
        Customer c4 = new Customer(4, "Omar", "Saleh", "Jaffa 21", "Jerusalem", "9100000", "0504444444", "omar@gmail.com", 300);
        system.addCustomer(c1);
        system.addCustomer(c2);
        system.addCustomer(c3);
        system.addCustomer(c4);

        Rider r1 = new Rider("123456789", "Ahmad", "Ali", "0501111111", "Scooter", true);
        Rider r2 = new Rider("987654321", "Lina", "Hassan", "0502222222", "Bike", true);
        system.addRider(r1);
        system.addRider(r2);

        Restaurant rest1 = new FastFoodRestaurant(1, "Burger House", "American", 4.5, true, 15, 25, 12);
        Restaurant rest2 = new FastFoodRestaurant(2, "Pizza City", "Italian", 4.2, true, 12, 20, 10);
        Restaurant rest3 = new Restaurant(3, "Sushi Time", "Japanese", 4.8, false, 20);
        Restaurant rest4 = new PremiumRestaurant(4, "Pasta Place", "Italian", 4.6, true, 14, 25.6, 3);
        system.addRestaurant(rest1);
        system.addRestaurant(rest2);
        system.addRestaurant(rest3);
        system.addRestaurant(rest4);

        RestAdmin ra1 = new RestAdmin("Ahmad", "ahmadAdmin", "1111");
        system.addRestAdmin(ra1);
        ra1.addRestaurant(rest1);
        ra1.addRestaurant(rest2);

        Order o1 = new Order(1, c1.getCustomercode(), rest1, rest1.getRescode(), "", 1, 6, 2026, 0, 0, 0, 50, rest1.CalculateFinalPrice(50), "Pending");
        Order o2 = new Order(2, c2.getCustomercode(), rest2, rest2.getRescode(), "", 2, 6, 2026, 0, 0, 0, 60, rest2.CalculateFinalPrice(60), "Pending");
        Order o3 = new Order(3, c3.getCustomercode(), rest4, rest4.getRescode(), "", 3, 6, 2026, 0, 0, 0, 100, rest4.CalculateFinalPrice(100), "Pending");
        system.addOrder(o1);
        system.addOrder(o2);
        system.addOrder(o3);
    }

    public static void main(String[] args) {
        launch(args);
    }
}