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
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
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

    @FXML
    private Label grainLabel;

    @FXML
    private Label lumberLabel;

    @FXML
    private Label woolLabel;

    @FXML
    private Label brickLabel;

    @FXML
    private Label oreLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private ScrollPane gameUpdatesScrollPane;


    private Game game;

    BoardView boardView;

    public BoardController() throws FileNotFoundException {
    }

    public void init() {
        try {
            game = new Game();
            game.setBoard(new Board());
            boardView = new BoardView(game.getBoard());
            hexagonPane.getChildren().add(boardView);
            hexagonPane.addEventHandler(CornerClickedEvent.CORNER_CLICKED, this::handleCornerClickEvent);
            hexagonPane.addEventHandler(EdgeClickedEvent.EDGE_CLICKED, this::handleEdgeClickEvent);
            for(int i = 0; i < 19; i++){
                writeToGameUpdates("");
            }
            gameUpdatesTextFlow.getChildren().add(new Text("Welcome to Catan!\n"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onSendButtonClick() {
        String message = chatTextField.getText();
        writeToGameUpdates(message);
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
            game.getBoard().getAdjacentTilesOfCorner(event.getCorner()).forEach(tile -> {
                boardView.getTileView(tile);
//                boardView.getTileView(tile).highlight();

                System.out.println("Resource Type of tile: " + tile.getResourceType().toString());
            });
    }
    public void handleEdgeClickEvent(EdgeClickedEvent event) {
        System.out.println("Edge clicked: " + event.getEdge().toString());
        game.getBoard().getAdjacentEdgesOfEdge(event.getEdge()).forEach(edge -> {
            boardView.getEdgeView(edge).occupyEdge();
        });
    }

    public void onDiceImageClick() throws FileNotFoundException{
        try {
            Pair<Integer, Integer> dicePair = game.rollDice();
            firstDiceImage.setImage(BoardUtilityFunctions.getDiceImage(dicePair.getKey()));
            secondDiceImage.setImage(BoardUtilityFunctions.getDiceImage(dicePair.getValue()));
            statusLabel.setText("Dice rolled: " + (dicePair.getKey() + dicePair.getValue()));
            writeToGameUpdates("Dice rolled: " + (dicePair.getKey() + dicePair.getValue()));
        } catch (Exception e) {
            statusLabel.setText(e.getMessage());
        }
    }
    public void onEndTourButtonClick(ActionEvent event){
        game.endTurn();
        statusLabel.setText("Turn ended");
        writeToGameUpdates("Turn ended");
    }

    private void writeToGameUpdates(String message) {
        gameUpdatesTextFlow.getChildren().add(new Text(message + "\n"));
        gameUpdatesScrollPane.setVvalue(1.0);
    }

}