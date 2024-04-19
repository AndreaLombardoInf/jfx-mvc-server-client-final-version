package com.example.jfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import java.util.Map;
import java.io.IOException;
import java.util.HashMap;
import javafx.stage.Stage;
import java.io.*;
import java.net.Socket;

/**
 * Controller class for the login view of the application.
 * Handles user login, initializes server connection, and navigates to the home view upon successful login.
 */
public class LoginController {

    private Socket clientSocket;
    private BufferedReader reader;
    private PrintWriter writer;
    boolean check = false;
    private Client client;
    private Map<String, String> userPassword;

    @FXML
    private TextField usrField;

    @FXML
    private TextField pwField;


    /**
     * Validates user credentials for login.
     *
     * @param username      The username entered by the user.
     * @param password      The password entered by the user.
     * @param userPassword  A map containing username-password pairs.
     * @return              True if the login is successful, false otherwise.
     */
    public boolean loginManager(String username, String password, Map<String, String> userPassword) {

        String passwordStored = userPassword.get(username);

        if (userPassword.containsKey(username) && passwordStored.equalsIgnoreCase(password)) {
            return true;  // Access granted
        } else if (userPassword.containsKey(username) && !passwordStored.equalsIgnoreCase(password)) {
            return false;  //Access denied
        } else {
            return false; // User not found
        }
    }


    /**
     * Initializes the connection parameters for communication with the server and sets the client instance.
     *
     * @param socket    The client socket for communication.
     * @param reader    The buffered reader for reading data from the server.
     * @param writer    The print writer for sending data to the server.
     * @param client    The client instance for sending suggestions.
     */
    public void initConnection(Socket socket, BufferedReader reader, PrintWriter writer, Client client, User currentUser) {
        this.clientSocket = socket;
        this.reader = reader;
        this.writer = writer;
        this.client = client;
    }


    /**
     * Handles the event when the login button is clicked.
     * Validates user credentials, initializes server connection,
     * and navigates to the home view upon successful login.
     *
     * @param event The action event triggered by clicking the button.
     */
    @FXML
    private void loginButtonClick(ActionEvent event) {
        this.userPassword = new HashMap<>();

        client.getListUser(this);
        String username = usrField.getText();
        String password = pwField.getText();
        User currentUser = new User(username, password);

        check = loginManager(username, password, userPassword);

        if (check) {
            try {

                // Load the "home-view.fxml" page
                FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
                Parent root = loader.load();

                HelloController homeController = loader.getController();

                homeController.initConnection(clientSocket,reader,writer,client, currentUser);

                // Create a new scene and set it on the stage
                Scene scene = new Scene(root, 350, 450);
                Stage stage = new Stage();
                stage.setTitle("Hello!");
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
    }

    /**
     * Sets the user-password map received from the server.
     *
     * @param m The map containing username-password pairs.
     */
    public void setUserPassword(Map<String,String> m){
        this.userPassword = m;
    }

}