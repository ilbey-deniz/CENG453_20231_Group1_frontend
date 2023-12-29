package com.group1.frontend.controllers;

import com.group1.frontend.components.*;
import com.group1.frontend.dto.ScoreDto;
import com.group1.frontend.enums.BuildingType;
import com.group1.frontend.enums.PlayerColor;
import com.group1.frontend.enums.ResourceType;
import com.group1.frontend.events.TimeEvent;
import com.group1.frontend.utils.BoardUtilityFunctions;
import com.group1.frontend.view.elements.BoardView;
import com.group1.frontend.events.*;
import com.group1.frontend.utils.Timer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Pair;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static com.group1.frontend.constants.BoardConstants.PLAYER_COLORS;
import static com.group1.frontend.constants.BoardConstants.TURN_TIME;
import static com.group1.frontend.utils.BoardUtilityFunctions.secondsToTime;
import static com.group1.frontend.utils.SceneSwitch.getSceneLoader;


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
    private Button firstDiceButton;

    @FXML
    private Button secondDiceButton;

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

    @FXML
    private VBox playerInfoVBox;

    private final HashSet<Edge> highlightedEdges;
    private final HashSet<Corner> highlightedCorners;
    private final HashSet<Corner> highlightedBuildings;

    private Game game;
    private Timer timer;
    private BoardView boardView;

    //stores player and its index in playerInfoVBox
    private final HashMap<Player, Integer> playerInfoMap;

    public BoardController(){
        this.highlightedBuildings = new HashSet<>();
        highlightedEdges = new HashSet<>();
        highlightedCorners = new HashSet<>();
        playerInfoMap = new HashMap<>();
    }

    public void init() {
        try {
            game = new Game();
            Board board = new Board();
            game.setBoard(board);
            boardView = new BoardView(board);
            //populate with mock players
            Player p1 = new Player(PlayerColor.red, service.getUsername(), false);
            Player p2 = new Player(PlayerColor.yellow, "laz ziya", true);
            Player p3 = new Player(PlayerColor.green, "tombalacı mehmet", true);
            Player p4 = new Player(PlayerColor.blue, "karahanlı", true);

            game.addPlayer(p1);
            game.addPlayer(p2);
            game.addPlayer(p3);
            game.addPlayer(p4);
            game.setCurrentPlayer(game.getPlayers().get(0));

            game.createInitialBuildings();

            game.getPlayers().forEach(player -> {
                //place random settlements and one road for each player
                player.getRoads().forEach(road -> boardView.getEdgeView(
                        road.getEdge()).occupyEdge(player.getColor()));
                player.getBuildings().forEach(building -> boardView.getCornerView(
                        building.getCorner()).occupyCorner(player.getColor(), building.getBuildingType()));
                player.addResource(ResourceType.GRAIN, 10);
                player.addResource(ResourceType.LUMBER, 10);
                player.addResource(ResourceType.WOOL, 10);
                player.addResource(ResourceType.BRICK, 10);
                player.addResource(ResourceType.ORE, 10);
            });

            loadPlayerInfos();
            highlightPlayerInfo(game.getCurrentPlayer());

            timer = new Timer(TURN_TIME);

            hexagonPane.getChildren().add(boardView);
            hexagonPane.getChildren().add(timer);
            hexagonPane.getChildren().add(game);

            hexagonPane.addEventHandler(CornerClickedEvent.CORNER_CLICKED, this::handleCornerClickEvent);
            hexagonPane.addEventHandler(EdgeClickedEvent.EDGE_CLICKED, this::handleEdgeClickEvent);
            hexagonPane.addEventHandler(TimeEvent.TIMES_UP, this::handleTimesUpEvent);
            hexagonPane.addEventHandler(TimeEvent.ONE_TICK, this::handleOneTickEvent);
            hexagonPane.addEventHandler(DiceRolledEvent.DICE_ROLLED, this::handleDiceRolledEvent);
            hexagonPane.addEventHandler(DiceRolledEvent.DICE_ROLLED_ALREADY, this::handleDiceRolledEvent);
            hexagonPane.addEventHandler(TurnEndedEvent.TURN_ENDED, this::handleTurnEndedEvent);
            hexagonPane.addEventHandler(BuildingPlacedEvent.ROAD_PLACED, this::handleBuildingPlacedEvent);
            hexagonPane.addEventHandler(BuildingPlacedEvent.SETTLEMENT_PLACED, this::handleBuildingPlacedEvent);
            hexagonPane.addEventHandler(BuildingPlacedEvent.CITY_PLACED, this::handleBuildingPlacedEvent);
            hexagonPane.addEventHandler(LongestRoadEvent.LONGEST_ROAD, this::handleLongestRoadEvent);
            hexagonPane.addEventHandler(GameWonEvent.GAME_WON, this::handleGameWonEvent);

            for(int i = 0; i < 19; i++){
                writeToGameUpdates("");
            }
            writeToGameUpdates("Welcome to Catan!");

            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(game.getPlayers());
            System.out.println(json);

            timer.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void handleCornerClickEvent(CornerClickedEvent event) {
        //check settlementToggleButton is selected
        if(settlementToggleButton.isSelected()){
            //check if the corner is available to build a settlement
            if(!highlightedCorners.contains(event.getCorner())){
                statusLabel.setText("You can't build a settlement here");
                return;
            }

            //unhighlight all corners
            removeHighlight();

            //place the settlement
            hexagonPane.fireEvent(new BuildingPlacedEvent(event.getCorner(), BuildingType.SETTLEMENT, game.getCurrentPlayer()));

            //unselect the settlementToggleButton
            settlementToggleButton.setSelected(false);
            //update resource labels
            updatePlayerInfo(game.getCurrentPlayer());
        }

        if(cityToggleButton.isSelected()){
            //check if the corner is available to build a city
            if(!highlightedBuildings.contains(event.getCorner())){
                statusLabel.setText("You can't build a city here");
                return;
            }

            //unhighlight all corners
            removeHighlight();

            //place the city
            hexagonPane.fireEvent(new BuildingPlacedEvent(event.getCorner(), BuildingType.CITY, game.getCurrentPlayer()));
            //unselect the cityToggleButton
            cityToggleButton.setSelected(false);
            //update resource labels
            updatePlayerInfo(game.getCurrentPlayer());
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
            //place the road
            hexagonPane.fireEvent(new BuildingPlacedEvent(event.getEdge(), BuildingType.ROAD, game.getCurrentPlayer()));

            //unselect the roadToggleButton
            roadToggleButton.setSelected(false);
            //update resource labels
            updatePlayerInfo(game.getCurrentPlayer());
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
        hexagonPane.fireEvent(new TurnEndedEvent(TurnEndedEvent.TURN_ENDED));
    }

    public void handleDiceRolledEvent(DiceRolledEvent event) {
        if(event.getEventType() == DiceRolledEvent.DICE_ROLLED_ALREADY){
            statusLabel.setText("The dice is already rolled");
            return;
        }
        else if (event.getEventType() == DiceRolledEvent.DICE_ROLLED){
            Pair<Integer, Integer> dicePair = game.rollDice();
            game.distributeResources(dicePair.getKey() + dicePair.getValue());

            try {
                firstDiceImage.setImage(BoardUtilityFunctions.getDiceImage(dicePair.getKey()));
                secondDiceImage.setImage(BoardUtilityFunctions.getDiceImage(dicePair.getValue()));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            //statusLabel.setText("Dice rolled: " + (dicePair.getKey() + dicePair.getValue()));
            statusLabel.setText(game.getCurrentPlayer().getName() + " rolled: " + (dicePair.getKey() + dicePair.getValue()));
            writeToGameUpdates(game.getCurrentPlayer().getName() + " rolled: " + (dicePair.getKey() + dicePair.getValue()));

            updateAllPlayerInfos();
        }
        else{
            throw new RuntimeException("Invalid event type");
        }
    }

    public void handleTurnEndedEvent(TurnEndedEvent event) {

        timer.stop();
        hexagonPane.getChildren().remove(timer);
        timer = new Timer(TURN_TIME);
        hexagonPane.getChildren().add(timer);
        leftTimeLabel.setStyle("-fx-text-fill: black");
        timer.start();

        removeHighlight(); //unhighlight all highlighted edges, corners
        unhighlightPlayerInfo(game.getCurrentPlayer()); //unhighlight current player info

        game.endTurn();

        if(game.checkWinner()!=null){
            hexagonPane.fireEvent(new GameWonEvent(game.checkWinner()));
            return;
        }

        highlightPlayerInfo(game.getCurrentPlayer());

        statusLabel.setText(game.getCurrentPlayer().getName() + "'s turn");
        writeToGameUpdates(game.getCurrentPlayer().getName() + "'s turn");

        game.autoPlayCpuPlayer();
    }

    private void handleBuildingPlacedEvent(BuildingPlacedEvent buildingPlacedEvent) {
        if(buildingPlacedEvent.getEventType() == BuildingPlacedEvent.SETTLEMENT_PLACED){
            game.placeSettlement((Corner) buildingPlacedEvent.getPlacement());
            statusLabel.setText("Settlement built");
            writeToGameUpdates(buildingPlacedEvent.getPlayer().getName() + " built a settlement");
            boardView.getCornerView((Corner) buildingPlacedEvent.getPlacement()).occupyCorner(buildingPlacedEvent.getPlayer().getColor(), BuildingType.SETTLEMENT);
        }
        else if(buildingPlacedEvent.getEventType() == BuildingPlacedEvent.CITY_PLACED){
            game.placeCity((Corner) buildingPlacedEvent.getPlacement());
            statusLabel.setText("City built");
            writeToGameUpdates(buildingPlacedEvent.getPlayer().getName() + " built a city");
            boardView.getCornerView((Corner) buildingPlacedEvent.getPlacement()).occupyCorner(buildingPlacedEvent.getPlayer().getColor(), BuildingType.CITY);
        }
        else{
            game.placeRoad((Edge) buildingPlacedEvent.getPlacement());
            statusLabel.setText("Road built");
            writeToGameUpdates(buildingPlacedEvent.getPlayer().getName() + " built a road");
            boardView.getEdgeView((Edge) buildingPlacedEvent.getPlacement()).occupyEdge(buildingPlacedEvent.getPlayer().getColor());
            game.fireLongestRoadEventInNeed(game.getCurrentPlayer().getLongestRoad());
            game.updateAllVictoryPoints();
        }
        updatePlayerInfo(buildingPlacedEvent.getPlayer());
    }

    private void handleLongestRoadEvent(LongestRoadEvent event) {
        for (Player player : game.getPlayers()) {
            getPlayerInfoController(player).unhighlightLongestRoadLabel();
        }
        //for each player, highlight the longestRoadImage if it is in the playersWithLongestRoad list
        event.getPlayersWithLongestRoad().forEach(player -> {
            getPlayerInfoController(player).highlightLongestRoadLabel();
            writeToGameUpdates(player.getName() + " has the longest road");
        });
    }

    private void handleGameWonEvent(GameWonEvent event) {
        writeToGameUpdates(event.getWinner().getName() + " won the game!");
        statusLabel.setText(event.getWinner().getName() + " won the game!");
        timer.stop();
        endTourButton.setDisable(true);
        tradeToggleButton.setDisable(true);
        settlementToggleButton.setDisable(true);
        cityToggleButton.setDisable(true);
        roadToggleButton.setDisable(true);
        firstDiceButton.setDisable(true);
        secondDiceButton.setDisable(true);
        savePlayerScores();
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

    public void onDiceButtonClick(){
        DiceRolledEvent diceRolledEvent;
        if(game.getCurrentDiceRoll()!=null){
            diceRolledEvent = new DiceRolledEvent(DiceRolledEvent.DICE_ROLLED_ALREADY);
        }
        else{
            diceRolledEvent = new DiceRolledEvent(DiceRolledEvent.DICE_ROLLED);
        }
        hexagonPane.fireEvent(diceRolledEvent);
    }
    public void onEndTourButtonClick(ActionEvent event){
        hexagonPane.fireEvent(new TurnEndedEvent(TurnEndedEvent.TURN_ENDED));
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
        removeHighlight();
        if(!game.getCurrentPlayer().hasEnoughResources(BuildingType.CITY)){
            statusLabel.setText("Not enough resources. You need 3 ore, 2 grain");
            cityToggleButton.setSelected(false);
            return;
        }
        statusLabel.setText("Select a settlement to build a city on it");
        //highlight all corners that are available to build a city
        List<Building> buildings = game.getCurrentPlayer().getBuildings();
        if(buildings.isEmpty()){
            statusLabel.setText("There is no valid corner to build a city");
            cityToggleButton.setSelected(false);
            return;
        }
        buildings.forEach(building -> {
            if(building.getBuildingType() == BuildingType.SETTLEMENT){
                try {
                    boardView.getCornerView(building.getCorner()).highlightOccupied();
                    highlightedBuildings.add(building.getCorner());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
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
    private void writeToGameUpdates(String message) {
        gameUpdatesTextFlow.getChildren().add(new Text(message + "\n"));
        gameUpdatesScrollPane.applyCss();
        gameUpdatesScrollPane.layout();
        gameUpdatesScrollPane.setVvalue(1.0);
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
        highlightedBuildings.forEach(corner -> {
            try {
                boardView.getCornerView(corner).unhighlightOccupied();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        highlightedEdges.clear();
        highlightedCorners.clear();
        highlightedBuildings.clear();
    }

    //TODO: move these to another class
    public void loadPlayerInfos() {
        for (Player player : game.getPlayers()) {
            FXMLLoader loader = getSceneLoader("player-info-view.fxml");
            AnchorPane playerInfoAnchorPane = null;
            try {
                playerInfoAnchorPane = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            PlayerInfoController playerInfoController = loader.getController();
            playerInfoController.initPlayerInfo(player);

            playerInfoAnchorPane.getProperties().put("controller", playerInfoController);
            playerInfoMap.put(player, playerInfoVBox.getChildren().size());
            playerInfoVBox.getChildren().add(playerInfoAnchorPane);

        }
    }
    public void updateAllPlayerInfos() {
        for (Player player : game.getPlayers()) {
            getPlayerInfoController(player).setPlayerInfo(player);
        }
    }
    public void updatePlayerInfo(Player player) {
        getPlayerInfoController(player).setPlayerInfo(player);
    }
    public void highlightPlayerInfo(Player player) {
        getPlayerInfoController(player).highlight();
    }
    public void unhighlightPlayerInfo(Player player) {
        getPlayerInfoController(player).unhighlight();
    }
    public PlayerInfoController getPlayerInfoController(Player player){
        return (PlayerInfoController) playerInfoVBox.getChildren().get(playerInfoMap.get(player)).getProperties().get("controller");
    }
    public void savePlayerScores() {
        for (Player player : game.getPlayers()) {
            if (player.isCpu()) {
                continue;
            }
            service.makeRequestWithToken("/saveScore",
                    "POST",
                    new ScoreDto(player.getName(), player.getVictoryPoint())
            );

        }
    }

    private void restartTimer() {
        hexagonPane.getChildren().remove(timer);
        timer = new Timer(TURN_TIME);
        hexagonPane.getChildren().add(timer);
        leftTimeLabel.setStyle("-fx-text-fill: black");
        timer.start();
    }
}