package com.group1.frontend;

import com.group1.frontend.components.Board;
import com.group1.frontend.view.elements.BoardView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


import java.io.FileNotFoundException;

import static com.group1.frontend.constants.BoardConstants.*;

public class HelloApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws FileNotFoundException {

        AnchorPane tileMap = new BoardView(new Board());
        Scene content = new Scene(tileMap, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(content);


        primaryStage.show();
//        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
//            TILE_HEIGHT = TILE_HEIGHT * (double) newVal / (double) oldVal;
//            System.out.println(TILE_HEIGHT);
//
//        });
//        primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> {
//            tileMap.setPrefHeight((double) newVal);
//        });
    }


}