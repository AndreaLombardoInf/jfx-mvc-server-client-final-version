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

public class ShopController {

    private Socket clientSocket;
    private BufferedReader reader;
    private PrintWriter writer;

    private Client client;

    @FXML
    private ComboBox<String> comboProd;

    @FXML
    private ListView<String> listBuy;

    @FXML
    private TextField productRequestField;

    @FXML
    private void addToComboBox() {
            // Leggi i prodotti direttamente dal file
            List<Product> products = client.getListCombobox(this,1);
            // Converti la lista di prodotti in una lista di stringhe
            List<String> productStrings = products.stream()
                    .map(product -> product.getName() + " - $" + product.getPrice() + " - ID: " + product.getId())
                    .collect(Collectors.toList());

            // Aggiungi gli elementi alla ComboBox
            comboProd.getItems().setAll(productStrings);
    }

    private void addToListView() {
        // Leggi i prodotti direttamente dal file
        List<Product> products = client.getListCombobox(this,0);
        // Converti la lista di prodotti in una lista di stringhe
        List<String> productStrings = products.stream()
                .map(product -> product.getName() + " - $" + product.getPrice() + " - ID: " + product.getId())
                .collect(Collectors.toList());

        // Aggiungi gli elementi alla ComboBox
        listBuy.getItems().setAll(productStrings);
    }

    public static List<Product> loadProductsFromFile(String absoluteFilePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(absoluteFilePath))) {
            return reader.lines()
                    .map(line -> line.split(";"))
                    .filter(parts -> parts.length == 3)
                    .map(parts -> {
                        String name = parts[0].trim();
                        double price = Double.parseDouble(parts[1].trim());
                        int id = Integer.parseInt(parts[2].trim());
                        return new Product(name, price, id);
                    })
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList(); // Return an empty list if an error occurs
    }


    public void initConnection(Socket socket, BufferedReader reader, PrintWriter writer,Client client) {
        this.clientSocket = socket;
        this.reader = reader;
        this.writer = writer;
        this.client = client;
        this.addToComboBox();
        this.addToListView();
    }

    @FXML
    private void homeClick(ActionEvent event) {
        try {
            // Carica la pagina "home-view.fxml"
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Parent root = loader.load();

            // Ottieni il controller
            HelloController homeController = loader.getController();
            homeController.initConnection(clientSocket,reader,writer,client);
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
    @FXML
    private Button buyBtn;

    @FXML
    private void refundProduct(ActionEvent event) {
        // Get the selected product from the ListView
        String selectedProductString = listBuy.getSelectionModel().getSelectedItem();

        // Check if a product is selected
        if (selectedProductString != null && !selectedProductString.isEmpty()) {
            // Split the selected product string to extract name, price, and id
            String[] parts = selectedProductString.split(" - ");
            String name = parts[0];
            // Extracting price by removing "$" and trimming any leading or trailing whitespace
            double price = Double.parseDouble(parts[1].substring(parts[1].indexOf('$') + 1).trim());
            // Extracting id by removing "ID:" and trimming any leading or trailing whitespace
            int id = Integer.parseInt(parts[2].substring(parts[2].indexOf(':') + 1).trim());

            // Create a product object with the extracted information
            Product refundedProduct = new Product(name, price, id);
            client.refoundProduct(this,refundedProduct);

            listBuy.getItems().remove(selectedProductString);

            // Add the refunded product back to the ComboBox
            comboProd.getItems().add(selectedProductString);
        }
    }
    @FXML
    private void buyProduct(ActionEvent event) {
        // Get the selected product from the ComboBox
        String selectedProductString = comboProd.getValue();

        // Check if a product is selected
        if (selectedProductString != null && !selectedProductString.isEmpty()) {
            // Split the selected product string to extract name, price, and id
            String[] parts = selectedProductString.split(" - ");
            String name = parts[0];
            // Extracting price by removing "$" and trimming any leading or trailing whitespace
            double price = Double.parseDouble(parts[1].substring(parts[1].indexOf('$') + 1).trim());
            // Extracting id by removing "ID:" and trimming any leading or trailing whitespace
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

    private void removeLineFromFile(String filePath, String lineToRemove) {
        try {
            File file = new File(filePath);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder stringBuilder = new StringBuilder();
            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                String trimmedLine = currentLine.trim();
                if (!trimmedLine.equals(lineToRemove)) {
                    System.out.print("paolo"+trimmedLine+"pp"+lineToRemove); //????
                    stringBuilder.append(currentLine).append(System.getProperty("line.separator"));
                }
            }
            reader.close();

            FileWriter fileWriter = new FileWriter(filePath);
            fileWriter.write(stringBuilder.toString());
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
