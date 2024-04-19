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


/**
 * The {@code Client} class represents the client-side application that interact with the server.
 * It establishes connections, sends requests, and receives responses from the server.
 */
public class Client extends Application {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private User user;


    public static void main(String[] args) { launch(args);}



    /**
     * Initializes and starts the JavaFX application.
     * Load the user interface from login.fxml file, initializes connection to the server,
     * passes the connection to the controller, sets up the scene, and displays the primary stage.
     *
     * @param primaryStage The primary stage of the JavaFX application.
     * @throws Exception If an error occurs during initialization or loading of resources.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        // Load the login interface
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/jfx/login.fxml"));
        Parent root = loader.load();
        LoginController controller = loader.getController();

        // Establish connection to the server
        try {
            socket = new Socket("localhost", 5555);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Pass the connection to the login controller and set up the scene
        controller.initConnection(socket, reader, writer,this, user);
        Scene scene = new Scene(root, 225, 250);
        primaryStage.setTitle("Hello!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }



    /**
     * Stops the JavaFX application.
     * Closes the connection to the server by closing the input and output streams
     * and the socket.
     *
     * @throws Exception If an error occurs while closing the connection.
     */
    @Override
    public void stop() throws Exception {
        // Close the connection when the application stops
        reader.close();
        writer.close();
        socket.close();
    }


    /**
     * Retrieves a list of users from the server.
     * Establishes a connection to the server, sends a login message,
     * receives a response containing a HashMap of user credentials,
     * and sets the user password HashMap in the provided LoginController.
     *
     * @param c The LoginController instance used to set the received user password HashMap.
     */
    public void getListUser(LoginController c) {
        try {
            Socket socket = new Socket("localhost", 5555);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

            // Create a Message object with the content "login"
            Message loginMessage = new Message("login");

            // Send and recive the Message object to the server
            objectOutputStream.writeObject(loginMessage);
            objectOutputStream.flush();
            Message receivedMessage = (Message) objectInputStream.readObject();

            // Extract the HashMap from the received Message
            Map<String, String> receivedMap = receivedMessage.getMap();
            System.out.println("HashMap received from server: " + receivedMap);
            c.setUserPassword(receivedMap);

            objectOutputStream.close();
            objectInputStream.close();
            socket.close();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * Retrieves a list of products from the server.
     * Establishes a connection to the server, sends a request for products,
     * receives a response containing a list of products,
     * and sets the product list in the provided ProductController.
     *
     * @param c The ProductController instance used to set the received product list.
     */
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

            // Receive the Message object from the server and extract the list
            Message receivedMessage = (Message) objectInputStream.readObject();
            List<Product> receivedList = receivedMessage.getList();
            System.out.println("List received from server: " + receivedList);
            c.setProductList(receivedList);

            objectOutputStream.close();
            objectInputStream.close();
            socket.close();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * Retrieves a list of products from the server to populate a ComboBox.
     * Establishes a connection to the server, sends a request for products or shop cart,
     * receives a response containing a list of products,
     * and returns the list.
     *
     * @param c The ShopController instance requesting the list.
     * @param selector An integer representing the type of list to retrieve (1 for products, 2 for shop cart).
     * @return The list of products received from the server.
     */
    public List<Product> getListCombobox(ShopController c, int selector) {
        try {
            Socket socket = new Socket("localhost", 5555);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

            // Create a Message object with the content "products"
            Message productsMessage;
            if(selector == 1) {
                productsMessage = new Message("products");
            } else{
                productsMessage = new Message("shopCart");
            }

            // Send and receive the Message object to the server
            objectOutputStream.writeObject(productsMessage);
            objectOutputStream.flush();
            Message receivedMessage = (Message) objectInputStream.readObject();

            // Extract the list of products from the received Message
            List<Product> receivedList = receivedMessage.getList();
            System.out.println("List received from server: " + receivedList);

            objectOutputStream.close();
            objectInputStream.close();
            socket.close();
            return receivedList;

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Sends a request to the server to buy a specific product.
     *
     * @param c The ShopController instance initiating the purchase.
     * @param p The product to be purchased.
     */
    public void buyProduct(ShopController c, Product p){
        try{
            Socket socket = new Socket("localhost", 5555);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

            // Create and send a Message object with the content "products"
            Message sProductMsg = new Message("buy",p);
            objectOutputStream.writeObject(sProductMsg);
            objectOutputStream.flush();
                objectOutputStream.close();
                objectInputStream.close();
                socket.close();

        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Sends a request to the server to refund a specific product.
     *
     * @param c The ShopController instance initiating the refund.
     * @param p The product to be refunded.
     */
    public void refoundProduct(ShopController c, Product p){
        try{
            Socket socket = new Socket("localhost", 5555);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

            //Create send a Message object with the content "products"
            Message sProductMsg = new Message("refound", p);
            objectOutputStream.writeObject(sProductMsg);
            objectOutputStream.flush();
            objectOutputStream.close();
            objectInputStream.close();
            socket.close();

        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Sends a suggestion message to the server.
     *
     * @param s The suggestion message to send.
     * @param currentUsr The current user sending the suggestion.
     */
    public void sendSuggestion(String s, User currentUsr){
        try{
            Socket socket = new Socket("localhost", 5555);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

            Message sProductMsg = new Message("suggestion", s, currentUsr);

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
