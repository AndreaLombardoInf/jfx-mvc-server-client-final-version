package com.example.jfx;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.HashMap;


public class Client extends Application {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Carica l'interfaccia utente da hello-view.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent root = loader.load();

        // Ottieni il controller
        LoginController controller = loader.getController();

        // Inizializza la connessione al server
        try {
            socket = new Socket("localhost", 5555);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Passa la connessione al controller
        controller.initConnection(socket, reader, writer,this);

        // Imposta la scena
        Scene scene = new Scene(root, 225, 250);
        primaryStage.setTitle("Hello!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        // Chiudi la connessione quando l'applicazione si ferma
        reader.close();
        writer.close();
        socket.close();
    }

    public void getListUser(LoginController c) {
        try {
            Socket socket = new Socket("localhost", 5555);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

            // Create a Message object with the content "login"
            Message loginMessage = new Message("login");

            // Send the Message object to the server
            objectOutputStream.writeObject(loginMessage);
            objectOutputStream.flush();

            // Receive the Message object from the server
            Message receivedMessage = (Message) objectInputStream.readObject();

            // Extract the HashMap from the received Message
            Map<String, String> receivedMap = receivedMessage.getMap();
            System.out.println("HashMap received from server: " + receivedMap);
            c.setUserPassword(receivedMap);

            // Close resources
            objectOutputStream.close();
            objectInputStream.close();
            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void getListProduct(productController c) {
        try {
            Socket socket = new Socket("localhost", 5555);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            // Create a Message object with the content "products"
            Message productsMessage = new Message("products");
            // Send the Message object to the server
            objectOutputStream.writeObject(productsMessage);
            objectOutputStream.flush();
            // Receive the Message object from the server
            Message receivedMessage = (Message) objectInputStream.readObject();
            // Extract the list of products from the received Message
            List<Product> receivedList = receivedMessage.getList();
            System.out.println("List received from server: " + receivedList);
            c.setProductList(receivedList);
            // Close resources
            objectOutputStream.close();
            objectInputStream.close();
            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<Product> getListCombobox(ShopController c,int selector) {
        try {
            Socket socket = new Socket("localhost", 5555);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            // Create a Message object with the content "products"
            Message productsMessage;
            if(selector == 1) {
                productsMessage = new Message("products");
            }
            else{
                productsMessage = new Message("shopCart");
            }
            // Send the Message object to the server
            objectOutputStream.writeObject(productsMessage);
            objectOutputStream.flush();
            // Receive the Message object from the server
            Message receivedMessage = (Message) objectInputStream.readObject();
            // Extract the list of products from the received Message
            List<Product> receivedList = receivedMessage.getList();
            System.out.println("List received from server: " + receivedList);
            // Close resources
            objectOutputStream.close();
            objectInputStream.close();
            socket.close();
            return receivedList;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    public void sendSuggestion(String s){
        try{
            Socket socket = new Socket("localhost", 5555);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

            Message sProductMsg = new Message("suggestion",s);
            // Send the Message object to the server

            objectOutputStream.writeObject(sProductMsg);
            objectOutputStream.flush();
            objectOutputStream.close();
            objectInputStream.close();
            socket.close();
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void buyProduct(ShopController c, Product p){
        try{
            Socket socket = new Socket("localhost", 5555);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            // Create a Message object with the content "products"
            Message sProductMsg = new Message("buy",p);
            System.out.println("zaccaria");
            // Send the Message object to the server

        objectOutputStream.writeObject(sProductMsg);
        objectOutputStream.flush();
            objectOutputStream.close();
            objectInputStream.close();
            socket.close();
        }catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void refoundProduct(ShopController c,Product p){
        try{
            Socket socket = new Socket("localhost", 5555);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            // Create a Message object with the content "products"
            Message sProductMsg = new Message("refound",p);
            System.out.println(p);
            // Send the Message object to the server
            objectOutputStream.writeObject(sProductMsg);
            objectOutputStream.flush();
            objectOutputStream.close();
            objectInputStream.close();
            socket.close();
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
