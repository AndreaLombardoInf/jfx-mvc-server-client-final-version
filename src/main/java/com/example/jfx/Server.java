package com.example.jfx;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;


/**
 * The {@code Server} class represents the server-side application responsible for handling client requests.
 * It listens for client connections, handles client requests, and provides appropriate responses.
 */
public class Server {


    /**
     * Main method to start the server.
     * Listens for incoming client connections and handles them in separate threads.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        try {
            try (ServerSocket serverSocket = new ServerSocket(5555)) {
                System.out.println("Server waiting for connections...");

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Connection accepted: " + clientSocket.getInetAddress().getHostName());

                    // Start a thread to manage the client
                    Thread clientThread = new Thread(() -> handleClient(clientSocket));
                    clientThread.start();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * Loads user credentials from a file named "users.txt".
     * The file should contain user addresses and corresponding passwords separated by a semicolon.
     * Each line represents a user entry.
     *
     * @return A map containing user addresses as keys and corresponding passwords as values.
     */
    public Map<String, String> loadUsersFromFile() {
        try {

            URL url = getClass().getResource("/users.txt");
            if (url == null) {
                throw new FileNotFoundException("File users.txt not found");}
            Path path = Paths.get(url.toURI());

            // Read lines from the file, split each line by semicolon, and create a map of user credentials
            return Files.lines(path)
                    .map(line -> line.split(";"))
                    .filter(parts -> parts.length == 2)
                    .collect(HashMap::new, (map, parts) -> {
                        String address = parts[0].trim();
                        String password = parts[1].trim();
                        map.put(address, password);
                    }, HashMap::putAll);

        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();}
        return null;
    }


    /**
     * Loads products from a file.
     *
     * @param filename The name of the file to load products from.
     * @return A list of Product objects loaded from the file.
     */
    public List<Product> loadProductsFromFile(String filename) {
        List<Product> productList = new ArrayList<>();

        try {

            URL url = getClass().getResource(filename);
            if (url == null) {
                throw new FileNotFoundException("File products.txt not found");}
            Path path = Paths.get(url.toURI());

            // Read lines from the file and create Product objects
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                String[] parts = line.split(";");
                if (parts.length == 3) {
                    String name = parts[0].trim();
                    double price = Double.parseDouble(parts[1].trim());
                    int ID = Integer.parseInt(parts[2].trim());
                    Product product = new Product(name, price, ID);
                    productList.add(product);
                }
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return productList;
    }


    /**
     * Removes a specific line from a file.
     *
     * @param filename     The name of the file to remove the line from.
     * @param lineToRemove The line to remove from the file.
     */
    public static void removeLineFromFile(String filename, String lineToRemove) {
        try {
            URL url = Server.class.getResource(filename);
            if (url == null) {
                throw new FileNotFoundException("File " + filename + " not found");}

            Path path = Paths.get(url.toURI());
            List<String> lines = Files.readAllLines(path);

            // Filter out the line to remove
            List<String> updatedLines = lines.stream()
                    .filter(line -> !line.equals(lineToRemove))
                    .collect(Collectors.toList());

            // Write the updated lines back to the file
            Files.write(path, updatedLines);

        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }


    /**
     * Adds a line to a file.
     *
     * @param filename  The name of the file to add the line to.
     * @param lineToAdd The line to add to the file.
     */
    public static void addLineToFile(String filename, String lineToAdd) {
        try {
            URL url = Server.class.getResource(filename);
            if (url == null) {
                throw new FileNotFoundException("File " + filename + " not found");}

            Path path = Paths.get(url.toURI());
            BufferedWriter writer = Files.newBufferedWriter(path, java.nio.file.StandardOpenOption.APPEND);
            writer.write(lineToAdd);
            writer.newLine();
            writer.close();

        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }


    /**
     * Handles communication with a client.
     *
     * @param clientSocket The socket connected to the client.
     */
    private static void handleClient(Socket clientSocket) {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());

            // Receive the Message from the client
            Message receivedMessage = (Message) objectInputStream.readObject();
            System.out.println("Message received from client: " + receivedMessage.getContent());

            List<Product> listToSend;
            Server server = new Server();
            Product selectedProduct;
            User correntUser;

            switch (receivedMessage.getContent()) {

                case "login":
                    // Load user information from file and send it back to the client
                    Map<String, String> mapToSend = server.loadUsersFromFile();

                    // Create a new Message object to send back to the client
                    Message responseMessage = new Message(mapToSend);
                    objectOutputStream.writeObject(responseMessage);
                    objectOutputStream.flush();
                    System.out.println("HashMap sent to client: " + mapToSend);
                    break;

                case "products":
                    // Load product information from file and send it back to the client
                    listToSend = server.loadProductsFromFile("/products.txt");
                    // Create a new Message object to send back to the client
                    responseMessage = new Message(listToSend);
                    objectOutputStream.writeObject(responseMessage);
                    objectOutputStream.flush();
                    System.out.println("List sent to client: " + listToSend);
                    break;

                case "shopCart":
                    // Load product information from file and send it back to the client
                    listToSend = server.loadProductsFromFile("/purchase.txt");
                    // Create a new Message object to send back to the client
                    responseMessage = new Message(listToSend);
                    // Send the Message object
                    objectOutputStream.writeObject(responseMessage);
                    objectOutputStream.flush();
                    System.out.println("List sent to client: " + listToSend);
                    break;

                case "buy":
                    selectedProduct = receivedMessage.getProduct();
                    addLineToFile("/purchase.txt",selectedProduct.getName() + ";" + selectedProduct.getPrice() + ";" + selectedProduct.getId());
                    removeLineFromFile("/products.txt", selectedProduct.getName() + ";" + selectedProduct.getPrice() + ";" + selectedProduct.getId());
                    break;

                case "refound":
                    selectedProduct = receivedMessage.getProduct();
                    addLineToFile("/products.txt",selectedProduct.getName() + ";" + selectedProduct.getPrice() + ";" + selectedProduct.getId());
                    removeLineFromFile("/purchase.txt", selectedProduct.getName() + ";" + selectedProduct.getPrice() + ";" + selectedProduct.getId());
                    break;

                case "suggestion":
                    correntUser = receivedMessage.getUser();
                    System.out.println("User's suggest Message: " + receivedMessage.getSuggestions());
                    System.out.println("Name: "+ correntUser.getUsername() +"   Suggestion: "+receivedMessage.getSuggestions());
                    addLineToFile("/suggestion.txt","Name: "+ correntUser.getUsername() +"   Suggestion: "+receivedMessage.getSuggestions());
                    break;

            }
            clientSocket.close();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
