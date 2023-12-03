package com.group1.frontend.view.elements;

import com.group1.frontend.components.Board;
import com.group1.frontend.components.Tile;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.FileNotFoundException;
import java.util.List;

public class BoardView extends AnchorPane {

    public BoardView(Board board) throws FileNotFoundException {
        board.generateRandomBoard();

        List<Tile> tiles = board.getTiles();
        for (Tile tile : tiles) {
            TileView tileView = new TileView(tile);
            getChildren().add(tileView);
            getChildren().add(new Text(tileView.getBoardXCoordinate(), tileView.getBoardYCoordinate(),
                    String.valueOf(tile.getDiceNumber())));
        }

        setBackground(Background.fill(Color.LIGHTSKYBLUE));
    }
}
