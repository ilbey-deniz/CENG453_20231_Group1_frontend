package com.group1.frontend.view.elements;

import com.group1.frontend.components.Tile;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;



import java.io.FileNotFoundException;
import java.net.URL;

import static com.group1.frontend.constants.BoardConstants.*;
import static com.group1.frontend.constants.BoardConstants.xStartOffset;

public class TileView extends Polygon {

    private double boardXCoordinate;
    private double boardYCoordinate;

    Tile tile;

    public TileView(Tile tile) throws FileNotFoundException {
        this.tile = tile;
        double xCoordinate = tile.getXCoordinate() * TILE_HEIGHT*V2/2 * 1.10 + xStartOffset;
        double yCoordinate = tile.getYCoordinate() * TILE_HEIGHT*3/4 * 1.10 + yStartOffset;
        this.boardXCoordinate = xCoordinate;
        this.boardYCoordinate = yCoordinate;
        getPoints().addAll(
                xCoordinate - (V * TILE_HEIGHT / 2), yCoordinate - (TILE_HEIGHT / 4),
                xCoordinate - (V * TILE_HEIGHT / 2), yCoordinate + (TILE_HEIGHT / 4),
                xCoordinate, yCoordinate + (TILE_HEIGHT / 2),
                xCoordinate + (V * TILE_HEIGHT / 2), yCoordinate + (TILE_HEIGHT / 4),
                xCoordinate + (V * TILE_HEIGHT / 2), yCoordinate - (TILE_HEIGHT / 4),
                xCoordinate, yCoordinate - (TILE_HEIGHT / 2)
        );
        // set up the visuals and a click listener for the tile

        if(tile.getTileType() != null){
            URL url = tile.getTileType().getImagePath();
            setFill(new ImagePattern(new Image(url.toString())));
        }
        setStrokeWidth(2);
        setStroke(Color.BLACK);
        setEventHandler(javafx.scene.input.MouseEvent.MOUSE_ENTERED, e -> setStroke(Color.RED));
        setEventHandler(javafx.scene.input.MouseEvent.MOUSE_EXITED, e -> setStroke(Color.BLACK));
    }

    public double getBoardXCoordinate() {
        return boardXCoordinate;
    }

    public double getBoardYCoordinate() {
        return boardYCoordinate;
    }

    public void setBoardXCoordinate(double boardXCoordinate) {
        this.boardXCoordinate = boardXCoordinate;
    }

    public void setBoardYCoordinate(double boardYCoordinate) {
        this.boardYCoordinate = boardYCoordinate;
    }

    public Tile getTile() {
        return tile;
    }

}
