package com.example.jfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.*;
import java.net.Socket;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller class for the shopping interface.
 */
public class ShopController {

    private Socket clientSocket;
    private BufferedReader reader;
    private PrintWriter writer;
    private Client client;
    private User user;

    @FXML
    private ComboBox<String> comboProd;

    @FXML
    private ListView<String> listBuy;


    /**
     * Adds products to the ComboBox.
     */
    @FXML
    private void addToComboBox() {
            List<Product> products = client.getListCombobox(this,1);
            List<String> productStrings = products.stream()
                    .map(product -> product.getName() + " - $" + product.getPrice() + " - ID: " + product.getId())
                    .collect(Collectors.toList());
            comboProd.getItems().setAll(productStrings);
    }


    /**
     * Adds products to the list view by reading them directly from the file and populating a combo box.
     */
    private void addToListView() {
        // Read products directly from the file
        List<Product> products = client.getListCombobox(this,0);

        // Convert the list of products into a list of strings
        List<String> productStrings = products.stream()
                .map(product -> product.getName() + " - $" + product.getPrice() + " - ID: " + product.getId())
                .collect(Collectors.toList());

        // Add the elements to the ComboBox
        listBuy.getItems().setAll(productStrings);
    }



    /**
     * Initializes the connection with the server and populates the ComboBox and ListView.
     *
     * @param socket The socket used for communication with the server.
     * @param reader The BufferedReader for reading data from the server.
     * @param writer The PrintWriter for writing data to the server.
     * @param client The client object for handling server communication.
     */
    public void initConnection(Socket socket, BufferedReader reader, PrintWriter writer,Client client, User user) {
        this.clientSocket = socket;
        this.reader = reader;
        this.writer = writer;
        this.client = client;
        this.user = user;
        this.addToComboBox();
        this.addToListView();
    }


    /**
     * Handles the "Home" button click event.
     *
     * @param event The ActionEvent representing the button click.
     */
    @FXML
    private void homeClick(ActionEvent event) {
        try {
            // Load the "home-view.fxml" page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Parent root = loader.load();

            // Get the controller
            HelloController homeController = loader.getController();
            homeController.initConnection(clientSocket,reader,writer,client, user);

            // Create a new scene and set it on the stage
            Scene scene = new Scene(root, 350, 450);
            Stage stage = new Stage();
            stage.setTitle("Hello!");
            stage.setScene(scene);

            // Close the current stage
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the action of refunding a product.
     *
     * @param event The ActionEvent representing the button click.
     */
    @FXML
    private void refundProduct(ActionEvent event) {
        // Get the selected product from the ListView
        String selectedProductString = listBuy.getSelectionModel().getSelectedItem();

        // Check if a product is selected
        if (selectedProductString != null && !selectedProductString.isEmpty()) {

            // Split the selected product string to extract name, price, and id
            String[] parts = selectedProductString.split(" - ");
            String name = parts[0];
            // Extracting price
            double price = Double.parseDouble(parts[1].substring(parts[1].indexOf('$') + 1).trim());
            // Extracting id
            int id = Integer.parseInt(parts[2].substring(parts[2].indexOf(':') + 1).trim());

            Product refundedProduct = new Product(name, price, id);
            client.refoundProduct(this,refundedProduct);
            listBuy.getItems().remove(selectedProductString);
            comboProd.getItems().add(selectedProductString);
        }
    }


    /**
     * Handles the action of buying a product.
     *
     * @param event The ActionEvent representing the button click.
     */
    @FXML
    private void buyProduct(ActionEvent event) {
        // Get the selected product from the ComboBox
        String selectedProductString = comboProd.getValue();

        // Check if a product is selected
        if (selectedProductString != null && !selectedProductString.isEmpty()) {

            // Split the product
            String[] parts = selectedProductString.split(" - ");
            String name = parts[0];
            // Extract price
            double price = Double.parseDouble(parts[1].substring(parts[1].indexOf('$') + 1).trim());
            // Extract id
            int id = Integer.parseInt(parts[2].substring(parts[2].indexOf(':') + 1).trim());

            // Create a product object with the extracted information
            Product selectedProduct = new Product(name, price, id);
            client.buyProduct(this,selectedProduct);

            // Remove the selected product from the ComboBox
            comboProd.getItems().remove(selectedProductString);

            // Add the selected product to the ListView
            listBuy.getItems().add(selectedProductString);
        }
    }
}
