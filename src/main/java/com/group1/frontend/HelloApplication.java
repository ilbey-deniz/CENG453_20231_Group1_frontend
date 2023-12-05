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

public class HelloApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws IOException {
        justGame(primaryStage);
        //wholeThing(primaryStage);

//        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
//            TILE_HEIGHT = TILE_HEIGHT * (double) newVal / (double) oldVal;
//            System.out.println(TILE_HEIGHT);
//
//        });
//        primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> {
//            tileMap.setPrefHeight((double) newVal);
//        });
    }
    public void justGame(Stage primaryStage) throws FileNotFoundException {
        AnchorPane tileMap = new BoardView(new Board());
        Scene content = new Scene(tileMap, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(content);
        primaryStage.show();
    }
    public void wholeThing(Stage primaryStage) throws IOException {
        Service service = new Service();
        //service.setBackendURL("https://backend-qdu1.onrender.com/api");
        service.setBackendURL("http://localhost:8080/api");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
        //FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
        Scene content = new Scene(loader.load(), DEFAULT_WIDTH, DEFAULT_HEIGHT);

        Controller loginController = loader.getController();
        loginController.construct(primaryStage, service);

        primaryStage.setTitle("Catan - Group 1");
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("assets/icon.jpeg")));
        primaryStage.getIcons().add(icon);
        primaryStage.setScene(content);
        primaryStage.show();
    }

}