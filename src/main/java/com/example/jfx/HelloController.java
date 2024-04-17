package com.example.jfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;


public class HelloController {
   @FXML
   private Label welcomeText;

   @FXML
   private Button productBtn;

   @FXML
   private TextField productRequestField;

   private Socket clientSocket;
   private BufferedReader reader;
   private PrintWriter writer;
   private Client client;

   public void initConnection(Socket socket, BufferedReader reader, PrintWriter writer,Client client) {
      this.clientSocket = socket;
      this.reader = reader;
      this.writer = writer;
      this.client = client;
   }



   @FXML
   private void shopButtonClick(ActionEvent event) {
      try {
         // Carica la pagina "shop.fxml"
         FXMLLoader loader = new FXMLLoader(getClass().getResource("shop.fxml"));
         Parent root = loader.load();

         // Ottieni il controller
         ShopController shopController = loader.getController();

         // Inizializza la connessione al server
         shopController.initConnection(clientSocket, reader, writer,client);

         // Crea una nuova scena e impostala sulla finestra
         Scene scene = new Scene(root, 291, 473);
         Stage stage = new Stage();
         stage.setTitle("Shopping Manager");
         stage.setScene(scene);

         // Chiudi la finestra della home iniziale
         Stage currentStage = (Stage) productBtn.getScene().getWindow();
         currentStage.close();

         stage.show();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }


   @FXML
   private void productButtonClick(ActionEvent event) {
      try {
         // Carica la pagina "productsview.fxml"
         FXMLLoader loader = new FXMLLoader(getClass().getResource("productsview.fxml"));
         Parent root = loader.load();

         //Ottieni il controller
         productController productsController = loader.getController();

         // Initialize the data in the ProductsViewController
         productsController.initData(this.client); // Note the change from productController to ProductController
         // Inizializza la connessione al server se necessario

         // Crea una nuova scena e impostala sulla finestra
         Scene scene = new Scene(root, 228, 273);
         Stage stage = new Stage();
         stage.setTitle("Products View");
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
   private void sendRequest(ActionEvent event) {
      productRequestField.clear();
   }


   @FXML
   private void homeClick(ActionEvent event) {
      try {
         // Carica la pagina "home-view.fxml"
         FXMLLoader loader = new FXMLLoader(getClass().getResource("home-view.fxml"));
         Parent root = loader.load();

         // Ottieni il controller
         HelloController homeController = loader.getController();

         // Inizializza la connessione al server se necessario

         // Crea una nuova scena e impostala sulla finestra
         Scene scene = new Scene(root, 320, 240);
         Stage stage = new Stage();
         stage.setTitle("Hello!");
         stage.setScene(scene);

         // Chiudi la finestra corrente
         Stage currentStage = (Stage) welcomeText.getScene().getWindow();
         currentStage.close();

         stage.show();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}
