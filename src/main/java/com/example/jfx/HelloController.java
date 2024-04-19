package com.example.jfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Controller class for the main view of the application.
 * Handles user interactions and navigation between different views.
 */
public class HelloController {

   @FXML
   private Button productBtn;

   @FXML
   private TextField productRequestField;

   private Socket clientSocket;
   private BufferedReader reader;
   private PrintWriter writer;
   private Client client;
   private User user;


   /**
    * Initializes the connection parameters for communication with the server.
    *
    * @param socket The client socket for communication.
    * @param reader The buffered reader for reading data from the server.
    * @param writer The print writer for sending data to the server.
    * @param client The client instance for sending suggestions.
    */
   public void initConnection(Socket socket, BufferedReader reader, PrintWriter writer,Client client, User user) {
      this.clientSocket = socket;
      this.reader = reader;
      this.writer = writer;
      this.client = client;
      this.user = user;
   }



   /**
    * Handles the event when the "Shop" button is clicked.
    * Loads the shop view and initializes the connection to the server.
    *
    * @param event The action event triggered by clicking the button.
    */
   @FXML
   private void shopButtonClick(ActionEvent event) {
      try {
         // Load the "shop.fxml" page
         FXMLLoader loader = new FXMLLoader(getClass().getResource("shop.fxml"));
         Parent root = loader.load();

         ShopController shopController = loader.getController();
         shopController.initConnection(clientSocket, reader, writer,client, user);

         // Create a new scene and set it on the window
         Scene scene = new Scene(root, 291, 473);
         Stage stage = new Stage();
         stage.setTitle("Shopping Manager");
         stage.setScene(scene);

         // Close the initial home window
         Stage currentStage = (Stage) productBtn.getScene().getWindow();
         currentStage.close();
         stage.show();

      } catch (IOException e) {
         e.printStackTrace();
      }
   }


   /**
    * Handles the event when the "Product" button is clicked.
    * Loads the product view and initializes the connection to the server.
    *
    * @param event The action event triggered by clicking the button.
    */
   @FXML
   private void productButtonClick(ActionEvent event) {
      try {
         // Load the "productsview.fxml" page
         FXMLLoader loader = new FXMLLoader(getClass().getResource("productsview.fxml"));
         Parent root = loader.load();

         productController productsController = loader.getController();

         // Initialize the data in the ProductsViewController
         productsController.initData(this.client, this.user);

         // Create a new scene and set it on the window
         Scene scene = new Scene(root, 228, 273);
         Stage stage = new Stage();
         stage.setTitle("Products View");
         stage.setScene(scene);

         // Close the current window
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
    * Handles the event when the "Send" button is clicked.
    * Sends a suggestion to the server.
    *
    * @param event The action event triggered by clicking the button.
    */
   @FXML
   private void sendRequest(ActionEvent event) {
      if (this.user != null) {
         String suggestion = productRequestField.getText();
         client.sendSuggestion(suggestion, user);
         productRequestField.clear();
      } else {
         System.err.println("User object is null. Cannot send suggestion.");
      }
   }
}
