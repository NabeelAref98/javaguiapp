package org.wgu.software2.misc;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.wgu.software2.Database.ConnectDB;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;

/**
 @author Nabeel Aref
 Generates login page
 */
public class Main extends Application {

    /**
        Javafx function entry
      @param primaryStage stage for fxml loading
      @throws Exception general exception that may occur
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        URL url = new File("src/main/resources/org/wgu/software2/LoginScreen.fxml").toURI().toURL();

        Parent root = FXMLLoader.load(url);
        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    /**
     * The main entry point for the java appliction
     * @param args arguments passed into the java function
     */
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        ConnectDB.openConnection();
        launch(args);
        ConnectDB.closeConnection();
    }
}
