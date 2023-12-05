package com.group1.frontend.view.elements;

import com.group1.frontend.components.Edge;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import static com.group1.frontend.constants.BoardConstants.*;
import static com.group1.frontend.utils.BoardUtilityFunctions.getRandomColor;


public class EdgeView extends Polygon {

    double XScale = TILE_HEIGHT*V2/4*1.10;
    double YScale = TILE_HEIGHT*1/4*1.10;
    double rectangleWidth = TILE_HEIGHT*V2/4*0.075;

    private Edge edge;

    public EdgeView(Edge edge) {
        this.edge = edge;
        double firstXCoordinate = edge.getFirstXCoordinate() * XScale  + xStartOffset;
        double firstYCoordinate = edge.getFirstYCoordinate() * YScale + yStartOffset;
        double secondXCoordinate = edge.getSecondXCoordinate() * XScale  + xStartOffset;
        double secondYCoordinate = edge.getSecondYCoordinate() * YScale + yStartOffset;
        // TODO: X Coordinate check is not a good way to check if the edge is vertical or horizontal.
        //  With precision error, it may not work
        if (firstXCoordinate == secondXCoordinate) {
            getPoints().addAll(
                    firstXCoordinate - rectangleWidth*0.9, firstYCoordinate,
                    firstXCoordinate + rectangleWidth*0.9, firstYCoordinate,
                    secondXCoordinate + rectangleWidth*0.9, secondYCoordinate,
                    secondXCoordinate - rectangleWidth*0.9, secondYCoordinate
                    );
        }
        else{
            getPoints().addAll(
                    firstXCoordinate, firstYCoordinate - rectangleWidth,
                    firstXCoordinate, firstYCoordinate + rectangleWidth,
                    secondXCoordinate, secondYCoordinate + rectangleWidth,
                    secondXCoordinate, secondYCoordinate - rectangleWidth
            );
        }
        setFill(Color.TRANSPARENT);

        setOnMouseClicked(e -> occupyEdge());

    }

    public void occupyEdge() {
        setFill(Color.valueOf(getRandomColor()));
        setStroke(Color.BLACK);
        setStrokeWidth(1);
        edge.setIsOccupied(true);
    }


}
