package com.group1.frontend;

import com.group1.frontend.components.Board;
import com.group1.frontend.enums.ResourceType;
import com.group1.frontend.view.elements.BoardView;
import com.group1.frontend.view.elements.TileView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
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


//        for (double[] tileCoordinate : TILE_COORDINATES) {
//            double x = tileCoordinate[0] * TILE_HEIGHT*V2/2 + xStartOffset;
//            double y = tileCoordinate[1] * TILE_HEIGHT*3/4  + yStartOffset;
//            Polygon tile = new TileView(8, ResourceType.BRICK, x, y);
//            tileMap.getChildren().add(tile);
//        }

        primaryStage.show();
        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
            TILE_HEIGHT = TILE_HEIGHT * (double) newVal / (double) oldVal;
            System.out.println(TILE_HEIGHT);

        });
        primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> {
            tileMap.setPrefHeight((double) newVal);
        });
    }


}