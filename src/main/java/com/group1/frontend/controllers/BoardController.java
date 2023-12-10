package com.group1.frontend.controllers;

import com.group1.frontend.components.*;
import com.group1.frontend.enums.BuildingType;
import com.group1.frontend.enums.ResourceType;
import com.group1.frontend.events.TimeEvent;
import com.group1.frontend.exceptions.DiceAlreadyRolledException;
import com.group1.frontend.utils.BoardUtilityFunctions;
import com.group1.frontend.view.elements.BoardView;
import com.group1.frontend.events.CornerClickedEvent;
import com.group1.frontend.events.EdgeClickedEvent;
import com.group1.frontend.utils.Timer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Pair;


import java.util.HashSet;
import java.util.List;

import static com.group1.frontend.constants.BoardConstants.PLAYER_COLORS;
import static com.group1.frontend.constants.BoardConstants.TURN_TIME;
import static com.group1.frontend.utils.BoardUtilityFunctions.secondsToTime;


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

    @FXML
    private Label leftTimeLabel;

    @FXML
    private Button endTourButton;

    @FXML
    private ToggleButton tradeToggleButton;

    @FXML
    private ToggleButton settlementToggleButton;

    @FXML
    private ToggleButton cityToggleButton;

    @FXML
    private ToggleButton roadToggleButton;


    private HashSet<Edge> highlightedEdges;
    private HashSet<Corner> highlightedCorners;

    private Game game;
    private Timer timer;

    BoardView boardView;

    public BoardController(){
        highlightedEdges = new HashSet<>();
        highlightedCorners = new HashSet<>();
    }

    public void init() {
        try {
            game = new Game();
            Board board = new Board();
            game.setBoard(board);
            boardView = new BoardView(board);
            //populate with mock players
            Player p1 = new Player("red", "iplikçi nedim", true);
            Player p2 = new Player("yellow", "laz ziya", true);
            Player p3 = new Player("green", "tombalacı mehmet", true);
            Player p4 = new Player("blue", "karahanlı", false);

            game.addPlayer(p1);
            game.addPlayer(p2);
            game.addPlayer(p3);
            game.addPlayer(p4);
            game.setCurrentPlayer(p4);

            game.getCurrentPlayer().addResource(ResourceType.GRAIN, 10);
            game.getCurrentPlayer().addResource(ResourceType.LUMBER, 10);
            game.getCurrentPlayer().addResource(ResourceType.WOOL, 10);
            game.getCurrentPlayer().addResource(ResourceType.BRICK, 10);
            game.getCurrentPlayer().addResource(ResourceType.ORE, 10);

            setResourceLabels(game.getCurrentPlayer());

            game.createInitialBuildings();

            game.getPlayers().forEach(player -> {
                //place random settlements and one road for each player
                player.getRoads().forEach(road -> boardView.getEdgeView(
                        road.getEdge()).occupyEdge(PLAYER_COLORS.get(player.getColor())));
                player.getBuildings().forEach(building -> boardView.getCornerView(
                        building.getCorner()).occupyCorner(player.getColor(), building.getBuildingType()));
                player.addResource(ResourceType.GRAIN, 10);
                player.addResource(ResourceType.LUMBER, 10);
                player.addResource(ResourceType.WOOL, 10);
                player.addResource(ResourceType.BRICK, 10);
                player.addResource(ResourceType.ORE, 20);
            });
            setResourceLabels(game.getCurrentPlayer());

            timer = new Timer(TURN_TIME);

            hexagonPane.getChildren().add(boardView);
            hexagonPane.getChildren().add(timer);
            hexagonPane.getChildren().add(game);

            hexagonPane.addEventHandler(CornerClickedEvent.CORNER_CLICKED, this::handleCornerClickEvent);
            hexagonPane.addEventHandler(EdgeClickedEvent.EDGE_CLICKED, this::handleEdgeClickEvent);
            hexagonPane.addEventHandler(TimeEvent.TIMES_UP, this::handleTimesUpEvent);
            hexagonPane.addEventHandler(TimeEvent.ONE_TICK, this::handleOneTickEvent);

            for(int i = 0; i < 19; i++){
                writeToGameUpdates("");
            }
            writeToGameUpdates("Welcome to Catan!");
            timer.start();

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
        //TODO: highlight adjacent tiles, inefficient

//        List<Tile> adjacentTiles = game.getBoard().getAdjacentTilesOfCorner(event.getCorner());
//        boardView.getChildren().forEach(child -> {
//            if(child instanceof TileView){
//                TileView tileView = (TileView) child;
//                if(adjacentTiles.contains(tileView.getTile())){
//                    tileView.highlight();
//                }
//            }
//        });

        //check settlementToggleButton is selected
        if(settlementToggleButton.isSelected()){
            //check if the corner is available to build a settlement
            if(!highlightedCorners.contains(event.getCorner())){
                statusLabel.setText("You can't build a settlement here");
                return;
            }

            //unhighlight all corners
            removeHighlight();

            //build the settlement
            game.placeSettlement(event.getCorner());
            statusLabel.setText("Settlement built");
            boardView.getCornerView(event.getCorner()).occupyCorner(game.getCurrentPlayer().getColor(), BuildingType.SETTLEMENT);

            //unselect the settlementToggleButton
            settlementToggleButton.setSelected(false);
            //update resource labels
            setResourceLabels(game.getCurrentPlayer());
        }

    }
    public void handleEdgeClickEvent(EdgeClickedEvent event) {
        //chech roadToggleButton is selected
        if(roadToggleButton.isSelected()){
            //check if the edge is available to build a road
            if(!highlightedEdges.contains(event.getEdge())){
                statusLabel.setText("You can't build a road here");
                return;
            }

            //unhighlight all edges
            removeHighlight();

            //build the road
            game.placeRoad(event.getEdge());
            statusLabel.setText("Road built");
            boardView.getEdgeView(event.getEdge()).occupyEdge(PLAYER_COLORS.get(game.getCurrentPlayer().getColor()));

            //unselect the roadToggleButton
            roadToggleButton.setSelected(false);
            //update resource labels
            setResourceLabels(game.getCurrentPlayer());
        }
    }

    public void handleOneTickEvent(TimeEvent event) {
        leftTimeLabel.setText(secondsToTime(event.getRemainingSeconds()));
        if(event.getRemainingSeconds() <= 10){
            leftTimeLabel.setStyle("-fx-text-fill: red");
        }
    }
    public void handleTimesUpEvent(TimeEvent event) {
        statusLabel.setText("Time's up!");
        writeToGameUpdates("Time's up!");

        hexagonPane.getChildren().remove(timer);
        timer = new Timer(TURN_TIME);
        hexagonPane.getChildren().add(timer);
        leftTimeLabel.setStyle("-fx-text-fill: black");
        timer.start();

    }


    public void onDiceImageClick(){
        try {
            Pair<Integer, Integer> dicePair = game.rollDice();
            firstDiceImage.setImage(BoardUtilityFunctions.getDiceImage(dicePair.getKey()));
            secondDiceImage.setImage(BoardUtilityFunctions.getDiceImage(dicePair.getValue()));
            statusLabel.setText("Dice rolled: " + (dicePair.getKey() + dicePair.getValue()));
            writeToGameUpdates("Dice rolled: " + (dicePair.getKey() + dicePair.getValue()));
            game.distributeResources(dicePair.getKey() + dicePair.getValue());
            setResourceLabels(game.getCurrentPlayer());
        } catch (Exception e) {
            statusLabel.setText(e.getMessage());
        }

    }
    public void onEndTourButtonClick(ActionEvent event) throws DiceAlreadyRolledException {
        game.endTurn();
        statusLabel.setText("Turn ended");
        writeToGameUpdates("Turn ended");
    }

    private void writeToGameUpdates(String message) {
        gameUpdatesTextFlow.getChildren().add(new Text(message + "\n"));
        gameUpdatesScrollPane.setVvalue(1.0);
    }

    public void onTradeButtonClick(){

    }
    public void onSettlementButtonClick(ActionEvent event){
        removeHighlight();
        if(!game.getCurrentPlayer().hasEnoughResources(BuildingType.SETTLEMENT)){
            statusLabel.setText("Not enough resources. You need 1 brick, 1 lumber, 1 wool, 1 grain");
            settlementToggleButton.setSelected(false);
            return;
        }

        statusLabel.setText("Select a corner to build a settlement");
        //highlight all corners that are available to build a settlement
        List<Corner> availableCorners = game.getAvailableCorners();
        if(availableCorners.isEmpty()){
            statusLabel.setText("There is no valid corner to build a settlement");
            settlementToggleButton.setSelected(false);
            return;
        }
        availableCorners.forEach(corner -> {
            try {
                boardView.getCornerView(corner).highlight();
                highlightedCorners.add(corner);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });


    }
    public void onCityButtonClick(ActionEvent event){

    }
    public void onRoadButtonClick(ActionEvent event){
        removeHighlight();
        if(!game.getCurrentPlayer().hasEnoughResources(BuildingType.ROAD)){
            statusLabel.setText("Not enough resources. You need 1 brick, 1 lumber");
            roadToggleButton.setSelected(false);
            return;
        }
        statusLabel.setText("Select an edge to build a road");
        //highlight all edges that are available to build a road
        List<Edge> availableEdges = game.getAvailableEdges();
        if(availableEdges.isEmpty()){
            statusLabel.setText("There is no valid edge to build a road");
            roadToggleButton.setSelected(false);
            return;
        }
        availableEdges.forEach(edge -> {
            boardView.getEdgeView(edge).highlight();
            highlightedEdges.add(edge);
        });

    }
    public void removeHighlight(){
        highlightedEdges.forEach(edge -> {
            try {
                boardView.getEdgeView(edge).unhighlight();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        highlightedCorners.forEach(corner -> {
            try {
                boardView.getCornerView(corner).unhighlight();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        highlightedEdges.clear();
        highlightedCorners.clear();
    }

    public void setResourceLabels(Player player){
        player.getResources().forEach((resourceType, integer) -> {
            switch (resourceType){
                case GRAIN:
                    grainLabel.setText(integer.toString());
                    break;
                case LUMBER:
                    lumberLabel.setText(integer.toString());
                    break;
                case WOOL:
                    woolLabel.setText(integer.toString());
                    break;
                case BRICK:
                    brickLabel.setText(integer.toString());
                    break;
                case ORE:
                    oreLabel.setText(integer.toString());
                    break;
            }
        });
    }

}