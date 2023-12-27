package com.group1.frontend;

import com.group1.frontend.components.Board;
import com.group1.frontend.controllers.Controller;
import com.group1.frontend.utils.Service;
import com.group1.frontend.view.elements.BoardView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;



import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

import static com.group1.frontend.constants.BoardConstants.*;

public class MainApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws IOException {
        Service service = new Service();
        //service.setBackendURL("https://backend-qdu1.onrender.com/api");
        service.setBackendURL("http://localhost:8080/api");
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("host-lobby-view.fxml"));
//      Scene content = new Scene(loader.load(), WINDOW_WIDTH, WINDOW_HEIGHT);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
        Scene content = new Scene(loader.load(), DEFAULT_WIDTH, DEFAULT_HEIGHT);

        Controller loginController = loader.getController();
        loginController.construct(primaryStage, service);
        loginController.init();

        primaryStage.setTitle("Catan - Group 1");
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("assets/icon.jpeg")));
        primaryStage.getIcons().add(icon);
        primaryStage.setScene(content);
        primaryStage.show();
    }
}