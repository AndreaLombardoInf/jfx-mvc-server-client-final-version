package com.example.jfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class productController {



    @FXML
    private Button homeButton;
    private Socket clientSocket;
    private BufferedReader reader;
    private PrintWriter writer;
    private Client client;
    private List<Product> productList;
    @FXML
    private  ListView<String> productListView;


    public void initData(Client client) {
        // Load the product data from the file
        this.client = client;
        client.getListProduct(this);
        // Convert the list of products to a list of strings
        List<String> productStrings = new ArrayList<>();
        for (Product product : productList) {
            productStrings.add(product.getName() + " - $" + product.getPrice() + " - ID: " + product.getId());
        }

        // Initialize the ListView with product data
        addToListView();
    }

    private void addToListView() {

        // Converti la lista di prodotti in una lista di stringhe
        List<String> productStrings = productList.stream()
                .map(product -> product.getName() + " - $" + product.getPrice() + " - ID: " + product.getId())
                .collect(Collectors.toList());

        // Aggiungi gli elementi alla ComboBox
        productListView.getItems().setAll(productStrings);
    }


    public void initConnection(Socket socket, BufferedReader reader, PrintWriter writer,Client client) {
        this.clientSocket = socket;
        this.reader = reader;
        this.writer = writer;
        this.client = client;
        // Initialize any necessary data or communication with the server
    }

    @FXML
    private void homeClick(ActionEvent event) {
        try {
            // Carica la pagina "home-view.fxml"
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Parent root = loader.load();

            // Ottieni il controller
            HelloController homeController = loader.getController();
            homeController.initConnection(clientSocket, reader, writer, client);
            // Inizializza la connessione al server se necessario

            // Crea una nuova scena e impostala sulla finestra
            Scene scene = new Scene(root, 350, 450);
            Stage stage = new Stage();
            stage.setTitle("Hello!");
            stage.setScene(scene);

            // Chiudi la finestra corrente
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            // Stampa ulteriori dettagli sull'errore
            e.printStackTrace();
        }
    }

    public void setProductList(List<Product> receivedList) {
        this.productList=receivedList;
    }
}
