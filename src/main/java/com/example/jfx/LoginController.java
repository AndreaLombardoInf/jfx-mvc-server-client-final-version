package com.example.jfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import java.util.Map;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import javafx.stage.Stage;
import java.io.*;
import java.net.Socket;


public class LoginController {

    private Socket clientSocket;
    private BufferedReader reader;
    private PrintWriter writer;
    boolean check = false;
    private Client client;

    private Map<String, String> userPassword;
    @FXML
    private Button loginBtn;

    @FXML
    private TextField usrField;

    @FXML
    private TextField pwField;



    public boolean loginManager(String username, String password, Map<String, String> userPassword) {
        // Verifica se le credenziali fornite sono valide
        final String ACCESS_GRANTED_MSG = "Access granted, send commands:";
        final String ERR = "Wrong user/password field";

        String passwordStored = userPassword.get(username);

        if (userPassword.containsKey(username) && passwordStored.equalsIgnoreCase(password)) {
            return true; // Accesso consentito
        } else if (userPassword.containsKey(username) && !passwordStored.equalsIgnoreCase(password)) {
            return false; // Password non corretta
        } else {
            return false; // Utente non trovato
        }
    }


    public void initConnection(Socket socket, BufferedReader reader, PrintWriter writer) {
        this.clientSocket = socket;
        this.reader = reader;
        this.writer = writer;
    }
    public void initConnection(Socket socket, BufferedReader reader, PrintWriter writer,Client client) {
        this.clientSocket = socket;
        this.reader = reader;
        this.writer = writer;
        this.client = client;
    }


    @FXML
    private void loginButtonClick(ActionEvent event) {
        this.userPassword = new HashMap<>();

        client.getListUser(this);
        String username = usrField.getText();
        String password = pwField.getText();

        check = loginManager(username, password, userPassword);

        if (check) {
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
    }
    public void setUserPassword(Map<String,String> m){
        this.userPassword = m;
    }
    public Map<String,String> getUserpw(){
        return this.userPassword;
    }
}