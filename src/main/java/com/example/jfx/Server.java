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


public class Server {
    public static void main(String[] args) {
        try {
            try (ServerSocket serverSocket = new ServerSocket(5555)) {
                System.out.println("Server waiting for connections...");

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Connection accepted: " + clientSocket.getInetAddress().getHostName());

                    // Avvia un thread per gestire il client
                    Thread clientThread = new Thread(() -> handleClient(clientSocket));
                    clientThread.start();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, String> loadUsersFromFile() {
        try {

            // Ottieni l'URL del file utenti.txt all'interno del package vendite
            URL url = getClass().getResource("/utenti.txt");

            if (url == null) {
                throw new FileNotFoundException("File utenti.txt not found");
            }

            // Converti l'URL in un percorso
            Path path = Paths.get(url.toURI());

            return Files.lines(path)
                    .map(line -> line.split(";"))
                    .filter(parts -> parts.length == 2)
                    .collect(HashMap::new, (map, parts) -> {
                        String address = parts[0].trim();
                        String password = parts[1].trim();
                        map.put(address, password);
                    }, HashMap::putAll);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Product> loadProductsFromFile(String filename) {
        List<Product> productList = new ArrayList<>();

        try {
            URL url = getClass().getResource(filename);
            if (url == null) {
                throw new FileNotFoundException("File products.txt not found");
            }

            Path path = Paths.get(url.toURI());

            // Leggi le righe dal file e crea gli oggetti Product
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                String[] parts = line.split(";");
                if (parts.length == 3) { // Assicurati che ogni riga abbia il formato corretto
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

    public static void removeLineFromFile(String filename, String lineToRemove) {
        try {
            // Get the URL of the file
            URL url = Server.class.getResource(filename);
            if (url == null) {
                throw new FileNotFoundException("File " + filename + " not found");
            }

            // Convert the URL to a Path
            Path path = Paths.get(url.toURI());

            // Read all lines from the file into a list
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

    public static void addLineToFile(String filename, String lineToAdd) {
        try {
            // Get the URL of the file
            URL url = Server.class.getResource(filename);
            if (url == null) {
                throw new FileNotFoundException("File " + filename + " not found");
            }

            // Convert the URL to a Path
            Path path = Paths.get(url.toURI());

            // Create a BufferedWriter to append the line to the file
            BufferedWriter writer = Files.newBufferedWriter(path, java.nio.file.StandardOpenOption.APPEND);

            // Write the line to the file
            writer.write(lineToAdd);
            writer.newLine();

            // Close the writer
            writer.close();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());

            // Receive the Message object from the client
            Message receivedMessage = (Message) objectInputStream.readObject();
            System.out.println("Message received from client: " + receivedMessage.getContent());

            String response;
            List<Product> listToSend;
            Server server = new Server();
            Product selectedProduct;
            switch (receivedMessage.getContent()) {
                case "login":
                    // Load user information from file and send it back to the client
                    Map<String, String> mapToSend = server.loadUsersFromFile();
                    // Create a new Message object to send back to the client
                    Message responseMessage = new Message(mapToSend);
                    // Send the Message object
                    objectOutputStream.writeObject(responseMessage);
                    objectOutputStream.flush();
                    System.out.println("HashMap sent to client: " + mapToSend);
                    response = "Running shopping operation...";
                    break;
                case "products":
                    // Load product information from file and send it back to the client
                    listToSend = server.loadProductsFromFile("/prodotti.txt");
                    // Create a new Message object to send back to the client
                    responseMessage = new Message(listToSend);
                    // Send the Message object
                    objectOutputStream.writeObject(responseMessage);
                    objectOutputStream.flush();
                    System.out.println("List sent to client: " + listToSend);

                    break;
                case "shopCart":
                    // Load product information from file and send it back to the client
                    listToSend = server.loadProductsFromFile("/acquisti.txt");
                    // Create a new Message object to send back to the client
                    responseMessage = new Message(listToSend);
                    // Send the Message object
                    objectOutputStream.writeObject(responseMessage);
                    objectOutputStream.flush();
                    System.out.println("List sent to client: " + listToSend);

                    break;
                case "buy":
                    selectedProduct = receivedMessage.getProduct();
                    addLineToFile("/acquisti.txt",selectedProduct.getName() + ";" + selectedProduct.getPrice() + ";" + selectedProduct.getId());
                    removeLineFromFile("/prodotti.txt", selectedProduct.getName() + ";" + selectedProduct.getPrice() + ";" + selectedProduct.getId());
                    break;
                case "refound":
                    selectedProduct = receivedMessage.getProduct();
                    addLineToFile("/prodotti.txt",selectedProduct.getName() + ";" + selectedProduct.getPrice() + ";" + selectedProduct.getId());
                    removeLineFromFile("/acquisti.txt", selectedProduct.getName() + ";" + selectedProduct.getPrice() + ";" + selectedProduct.getId());
                case "suggestion":
                    System.out.println("Messaqggio di suggerimento dall'utente: "+receivedMessage.getSuggestions());
                default:

                    break;
            }

            clientSocket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
