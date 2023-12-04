package com.group1.frontend.view.elements;

import com.group1.frontend.components.Edge;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

import static com.group1.frontend.constants.BoardConstants.*;

public class EdgeView extends Polygon {

    double XScale = TILE_HEIGHT*V2/4*1.10;
    double YScale = TILE_HEIGHT*1/4*1.10;

    public EdgeView(Edge edge) {
        double firstXCoordinate = edge.getFirstXCoordinate() * XScale  + xStartOffset;
        double firstYCoordinate = edge.getFirstYCoordinate() * YScale + yStartOffset;
        double secondXCoordinate = edge.getSecondXCoordinate() * XScale  + xStartOffset;
        double secondYCoordinate = edge.getSecondYCoordinate() * YScale + yStartOffset;
        // TODO: X Coordinate check is not a good way to check if the edge is vertical or horizontal.
        //  With precision error, it may not work
        if (firstXCoordinate == secondXCoordinate) {
            getPoints().addAll(
                    firstXCoordinate - 4.8, firstYCoordinate,
                    firstXCoordinate + 4.8, firstYCoordinate,
                    secondXCoordinate + 4.8, secondYCoordinate,
                    secondXCoordinate - 4.8, secondYCoordinate
                    );
        }
        else{
            getPoints().addAll(
                    firstXCoordinate, firstYCoordinate - 5,
                    firstXCoordinate, firstYCoordinate + 5,
                    secondXCoordinate, secondYCoordinate + 5,
                    secondXCoordinate, secondYCoordinate - 5
            );
        }
        setFill(Color.PURPLE);
        setOnMouseClicked(e -> System.out.println("Clicked: " + this));

    }

}
