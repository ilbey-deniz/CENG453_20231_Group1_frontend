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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import static com.group1.frontend.constants.BoardConstants.TILE_HEIGHT;
import static com.group1.frontend.utils.BoardUtilityFunctions.mapIntToNumberAsset;

public class BoardView extends AnchorPane {

    public BoardView(Board board) throws FileNotFoundException {
        board.generateRandomBoard();

        List<Tile> tiles = board.getTiles();
        for (Tile tile : tiles) {
            TileView tileView = new TileView(tile);
            getChildren().add(tileView);
            Image image = new Image(new FileInputStream(mapIntToNumberAsset(tile.getDiceNumber())));
            Circle c = new Circle(tileView.getBoardXCoordinate(), tileView.getBoardYCoordinate(), TILE_HEIGHT/6);
            c.setFill(new ImagePattern(image));
            getChildren().add(c);
        }

        for(Corner point : board.getPoints()){
            CornerView cornerView = new CornerView(point);
            getChildren().add(cornerView);
        }

        for(Edge edge : board.getEdges()){
            EdgeView edgeView = new EdgeView(edge);
            getChildren().add(edgeView);
        }

        setBackground(Background.fill(Color.valueOf("#4fa6eb")));
    }
}
