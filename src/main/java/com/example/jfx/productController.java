package com.example.jfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Controller class for the product view.
 */
public class productController {

    private Socket clientSocket;
    private BufferedReader reader;
    private PrintWriter writer;
    private Client client;
    private User user;
    private List<Product> productList;

    @FXML
    private  ListView<String> productListView;


    /**
     * Initializes product data and sets up the ListView.
     *
     * @param client The client instance for server communication.
     */
    public void initData(Client client, User user) {
        // Load the product data from the file
        this.client = client;
        this.user = user;
        client.getListProduct(this);

        // Convert the list of products to a list of strings
        List<String> productStrings = new ArrayList<>();
        for (Product product : productList) {
            productStrings.add(product.getName() + " - $" + product.getPrice() + " - ID: " + product.getId());
        }
        addToListView();
    }


    /**
     * Converts the list of products into a list of strings and populates the ListView.
     */
    private void addToListView() {

        List<String> productStrings = productList.stream()
                .map(product -> product.getName() + " - $" + product.getPrice() + " - ID: " + product.getId())
                .collect(Collectors.toList());

        productListView.getItems().setAll(productStrings);
    }


    /**
     * Handles the event when the "Home" button is clicked.
     *
     * @param event The ActionEvent triggered by clicking the button.
     */
    @FXML
    private void homeClick(ActionEvent event) {
        try {
            // Load the "hello-view.fxml" page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Parent root = loader.load();

            HelloController homeController = loader.getController();
            homeController.initConnection(clientSocket, reader, writer, client, user);

            // Create a new scene and set it on the window
            Scene scene = new Scene(root, 350, 450);
            Stage stage = new Stage();
            stage.setTitle("Hello!");
            stage.setScene(scene);

            // close current windows
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
     * Sets the received list of products.
     *
     * @param receivedList The list of products received from the server.
     */
    public void setProductList(List<Product> receivedList) {
        this.productList = receivedList;
        // update ListView
        addToListView();
    }
}
