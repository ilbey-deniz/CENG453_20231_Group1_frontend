package com.group1.frontend;

import com.group1.frontend.enums.ResourceType;
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

import java.io.IOException;

import static com.group1.frontend.constants.BoardConstants.*;

public class HelloApplication extends Application {



    private final static int WINDOW_WIDTH = 800;
    private final static int WINDOW_HEIGHT = 600;


    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        AnchorPane tileMap = new AnchorPane();
        Scene content = new Scene(tileMap, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(content);

        int rowCount = 4; // how many rows of tiles should be created
        int tilesPerRow = 6; // the amount of tiles that are contained in each row
        int xStartOffset = 40; // offsets the entire field to the right
        int yStartOffset = 40; // offsets the entire fiels downwards
//        TILE_COORDINATES
//        for (int[] tileCoordinate : TILE_COORDINATES) {
//            int x = tileCoordinate[0];
//            int y = tileCoordinate[1];
//            double xCoord = 1.5 * x * TILE_WIDTH + xStartOffset;
//            double yCoord = yStartOffset + y * TILE_WIDTH * V2;
//            Polygon tile = new TileView(8, ResourceType.BRICK, xCoord, yCoord);
//            tileMap.getChildren().add(tile);
//        }
        for (int y = 0; y < rowCount; y++) {
                double yCoordInit = yStartOffset + y * TILE_WIDTH * V2;
            double yCoord = yCoordInit;
            for (int x = 0; x < tilesPerRow; x++) {
                double xCoord = 1.5 * x * TILE_WIDTH + xStartOffset;
                Polygon tile = new TileView(8, ResourceType.BRICK, xCoord, yCoord);
                tileMap.getChildren().add(tile);
                yCoord = yCoord == yCoordInit ? yCoord + TILE_HEIGHT * V : yCoordInit;
            }
        }
        primaryStage.show();
    }


}