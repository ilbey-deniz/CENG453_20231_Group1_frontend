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

    List<EdgeView> edgeViews = new java.util.ArrayList<>();
    List<CornerView> cornerViews = new java.util.ArrayList<>();
    List<TileView> tileViews = new java.util.ArrayList<>();

    public BoardView(Board board) throws FileNotFoundException {

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
            if(tile.getDiceNumber() == 7){
                continue;
            }
            Image image = new Image(mapIntToNumberAsset(tile.getDiceNumber()).toString());
            Circle c = new Circle(tileView.getBoardXCoordinate(), tileView.getBoardYCoordinate(), TILE_HEIGHT/6);
            c.setFill(new ImagePattern(image));
            getChildren().add(c);
        }

        for(Edge edge : board.getEdges()){
            EdgeView edgeView = new EdgeView(edge);
            edgeViews.add(edgeView);
            getChildren().add(edgeView);
        }

        for(Corner point : board.getCorners()){
            CornerView cornerView = new CornerView(point);
            cornerViews.add(cornerView);
            getChildren().add(cornerView);
        }




        setBackground(Background.fill(Color.valueOf("#4fa6eb")));

//        setOnMouseClicked(e -> {
//           board.printAllCornerOfIsOccupied();
//        });
    }

    public EdgeView getEdgeView(Edge edge) {
        for (EdgeView edgeView : edgeViews) {
            if (edgeView.getEdge().equals(edge)) {
                return edgeView;
            }
        }
        return null;
    }

    public TileView getTileView(Tile tile) {
        for (TileView tileView : tileViews) {
            if (tileView.getTile().equals(tile)) {
                return tileView;
            }
        }
        return null;
    }

    public CornerView getCornerView(Corner corner) {
        for (CornerView cornerView : cornerViews) {
            if (cornerView.getCorner().equals(corner)) {
                return cornerView;
            }
        }
        return null;
    }

}
