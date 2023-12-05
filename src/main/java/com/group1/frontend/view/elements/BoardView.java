package com.group1.frontend.view.elements;

import com.group1.frontend.components.Board;
import com.group1.frontend.components.Corner;
import com.group1.frontend.components.Edge;
import com.group1.frontend.components.Tile;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import static com.group1.frontend.constants.BoardConstants.*;
import static com.group1.frontend.constants.BoardConstants.V;
import static com.group1.frontend.constants.BoardConstants.BACKGROUND_ISLAND_COLOR;
import static com.group1.frontend.utils.BoardUtilityFunctions.mapIntToNumberAsset;

public class BoardView extends AnchorPane {

    private Board board;
    public BoardView(Board board) throws FileNotFoundException {
        this.board = board;
        board.generateRandomBoard();

        Polygon backgroundIsland = new Polygon();
        double backgroundScaleConstant = TILE_HEIGHT*2.75;
        backgroundIsland.getPoints().addAll(
                xStartOffset - (backgroundScaleConstant/2), yStartOffset - (V * backgroundScaleConstant),
                xStartOffset - backgroundScaleConstant, yStartOffset,
                xStartOffset - (backgroundScaleConstant/2), yStartOffset + (V * backgroundScaleConstant),
                xStartOffset + (backgroundScaleConstant/2), yStartOffset + (V * backgroundScaleConstant),
                xStartOffset + backgroundScaleConstant, yStartOffset,
                xStartOffset + (backgroundScaleConstant/2), yStartOffset - (V * backgroundScaleConstant)
        );
        backgroundIsland.setFill(Color.valueOf(BACKGROUND_ISLAND_COLOR));
        getChildren().add(backgroundIsland);

        List<Tile> tiles = board.getTiles();
        for (Tile tile : tiles) {
            TileView tileView = new TileView(tile);
            getChildren().add(tileView);
            Image image = new Image(new FileInputStream(mapIntToNumberAsset(tile.getDiceNumber())));
            Circle c = new Circle(tileView.getBoardXCoordinate(), tileView.getBoardYCoordinate(), TILE_HEIGHT/6);
            c.setFill(new ImagePattern(image));
            getChildren().add(c);
        }

        for(Edge edge : board.getEdges()){
            EdgeView edgeView = new EdgeView(edge);
            getChildren().add(edgeView);
        }

        for(Corner point : board.getPoints()){
            CornerView cornerView = new CornerView(point);
            getChildren().add(cornerView);
        }



        setBackground(Background.fill(Color.valueOf("#4fa6eb")));

//        setOnMouseClicked(e -> {
//           board.printAllCornerOfIsOccupied();
//        });
    }
}
