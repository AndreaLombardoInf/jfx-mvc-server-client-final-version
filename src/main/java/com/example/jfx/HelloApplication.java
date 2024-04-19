package com.example.jfx;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The main application class for the Hello application.
 * It loads the login.fxml file and displays the login interface.
 */
public class HelloApplication extends Application {


   /**
    * The entry point for the JavaFX application.
    * It loads the login.fxml file and sets up the primary stage.
    *
    * @param stage The primary stage for the application.
    * @throws IOException If an error occurs while loading the FXML file.
    */
   public void start(Stage stage) throws IOException {
      FXMLLoader fxmlLoader = new FXMLLoader();
      fxmlLoader.setLocation(getClass().getResource("login.fxml"));

      Scene scene = new Scene(fxmlLoader.load(), 225, 250);
      stage.setTitle("Hello!");
      stage.setScene(scene);
      stage.show();
   }

   /**
    * The main method to launch the JavaFX application.
    *
    * @param args The command-line arguments.
    */
   public static void main(String[] args) {
      launch(args);
   }
}
