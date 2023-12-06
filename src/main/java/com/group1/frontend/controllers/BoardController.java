package com.group1.frontend.controllers;

import com.group1.frontend.components.Board;
import com.group1.frontend.components.Game;
import com.group1.frontend.utils.BoardUtilityFunctions;
import com.group1.frontend.view.elements.BoardView;
import com.group1.frontend.events.CornerClickedEvent;
import com.group1.frontend.events.EdgeClickedEvent;
import com.group1.frontend.utils.BoardUtilityFunctions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.TextFlow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Pair;


import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class BoardController extends Controller{
    @FXML
    private AnchorPane hexagonPane;

    @FXML
    private TextFlow gameUpdatesTextFlow;

    @FXML
    private TextField chatTextField;

    @FXML
    private ImageView firstDiceImage;

    @FXML
    private ImageView secondDiceImage;

    private Game game;

    BoardView boardView;

    public BoardController() throws FileNotFoundException {
    }

    public void initialize() {
        try {
            game = new Game();
            game.setBoard(new Board());
            boardView = new BoardView(game.getBoard());
            hexagonPane.getChildren().add(boardView);
            hexagonPane.addEventHandler(CornerClickedEvent.CORNER_CLICKED, this::handleCornerClickEvent);
            hexagonPane.addEventHandler(EdgeClickedEvent.EDGE_CLICKED, this::handleEdgeClickEvent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onSendButtonClick() {
        String message = chatTextField.getText();
        gameUpdatesTextFlow.getChildren().add(new javafx.scene.text.Text(message + "\n"));
        chatTextField.clear();
    }

    public void onEnter(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER){
            onSendButtonClick();
        }
    }

    public void onLeaveButtonClick() {
        sceneSwitch.switchToScene(stage, service, "menu-view.fxml");
    }

    public void handleCornerClickEvent(CornerClickedEvent event) {
        System.out.println("EDGE COUNT: " + game.getBoard().getEdges().size());
        System.out.println("Corner clicked: " + event.getCorner().getXCoordinate() + ", " + event.getCorner().getYCoordinate());
            game.getBoard().getAdjacentEdgesOfCorner(event.getCorner()).forEach(edge -> {
                boardView.getEdgeView(edge).occupyEdge();

            });
    }
    public void handleEdgeClickEvent(EdgeClickedEvent event) {
        System.out.println("Edge clicked: " + event.getEdge().toString());
        game.getBoard().getAdjacentCornersOfEdge(event.getEdge()).forEach(corner -> {
            try {
                boardView.getCornerView(corner).occupyCorner();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    public void onDiceImageClick(MouseEvent event) throws FileNotFoundException, InterruptedException {
        Pair<Integer, Integer> dicePair = game.rollDice();
        firstDiceImage.setImage(BoardUtilityFunctions.getDiceImage(dicePair.getKey()));
        secondDiceImage.setImage(BoardUtilityFunctions.getDiceImage(dicePair.getValue()));
    }

}