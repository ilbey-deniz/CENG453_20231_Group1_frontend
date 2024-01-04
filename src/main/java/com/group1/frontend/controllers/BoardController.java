package com.group1.frontend.controllers;

import com.group1.frontend.components.*;
import com.group1.frontend.dto.httpDto.ScoreDto;
import com.group1.frontend.dto.websocketDto.DiceRollDto;
import com.group1.frontend.dto.websocketDto.GameDto;
import com.group1.frontend.dto.websocketDto.JoinLobbyDto;
import com.group1.frontend.dto.websocketDto.WebSocketDto;
import com.group1.frontend.enums.BuildingType;
import com.group1.frontend.enums.ResourceType;
import com.group1.frontend.enums.TradeViewType;
import com.group1.frontend.events.TimeEvent;
import com.group1.frontend.utils.BoardUtilityFunctions;
import com.group1.frontend.utils.IntegerPair;
import com.group1.frontend.utils.LobbyPlayer;
import com.group1.frontend.view.elements.BoardView;
import com.group1.frontend.events.*;
import com.group1.frontend.utils.Timer;

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


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

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

    @FXML
    private AnchorPane tradeInitAnchorPane;

    @FXML
    private AnchorPane tradeOfferAnchorPane;

    private final HashSet<Edge> highlightedEdges;
    private final HashSet<Corner> highlightedCorners;
    private final HashSet<Corner> highlightedBuildings;
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
        service.setWebsocketHandler(this::boardMessageHandler);
        try{
            //game is null when the player is host
            if(service.getGame() == null){
                service.setGame(new Game());
                Board board = new Board();
                board.generateRandomBoard();
                service.getGame().setBoard(board);
                boardView = new BoardView(board);
                //add players from lobby to game
                List<LobbyPlayer> lobbyPlayers = service.getGameRoom().getPlayersAsList();
                lobbyPlayers.forEach(lobbyPlayer -> {
                    Player player = new Player(lobbyPlayer.getColor(), lobbyPlayer.getName(), lobbyPlayer.getCpu());
                    service.getGame().addPlayer(player);
                });
                service.getGame().setCurrentPlayer(service.getGame().getRandomNonCpuPlayer());
                //place random settlements and one road for each player
                service.getGame().createInitialBuildings();
                service.getGame().getPlayers().forEach(player -> {
                    player.addResource(ResourceType.GRAIN, 10);
                    player.addResource(ResourceType.LUMBER, 10);
                    player.addResource(ResourceType.WOOL, 10);
                    player.addResource(ResourceType.BRICK, 10);
                    player.addResource(ResourceType.ORE, 10);
                });
                GameDto gameDto = new GameDto();
                gameDto.setGame(service.getGame());
                String message = service.objectToJson(gameDto);
                service.sendWebsocketMessage(message);
            }
            //game is not null when the player is guest, since it is created in LobbyController
            else {
                boardView = new BoardView(service.getGame().getBoard());
            }
            loadPlayerInfos();
            for(Player player : service.getGame().getPlayers()){
                player.getRoads().forEach(road -> boardView.getEdgeView(
                        road.getEdge()).occupyEdge(player.getColor()));
                player.getBuildings().forEach(building -> boardView.getCornerView(
                        building.getCorner()).occupyCorner(player.getColor(), building.getBuildingType()));
            }
            loadTradeViews();
            highlightPlayerInfo(service.getGame().getCurrentPlayer());

            timer = new Timer(TURN_TIME);

            hexagonPane.getChildren().add(boardView);
            hexagonPane.getChildren().add(timer);
            hexagonPane.getChildren().add(service.getGame());

            hexagonPane.addEventHandler(CornerClickedEvent.CORNER_CLICKED, this::handleCornerClickEvent);
            hexagonPane.addEventHandler(EdgeClickedEvent.EDGE_CLICKED, this::handleEdgeClickEvent);
            hexagonPane.addEventHandler(TimeEvent.TIMES_UP, this::handleTimesUpEvent);
            hexagonPane.addEventHandler(TimeEvent.ONE_TICK, this::handleOneTickEvent);
            hexagonPane.addEventHandler(DiceRolledEvent.DICE_ROLLED, this::handleDiceRolledEvent);
            hexagonPane.addEventHandler(DiceRolledEvent.CPU_ROLLED_DICE, this::handleDiceRolledEvent);
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
            timer.start();

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void boardMessageHandler(String message) {
        WebSocketDto dto = (WebSocketDto) service.jsonToObject(message, WebSocketDto.class);
        if (dto.getClass().equals(DiceRollDto.class)) {
            DiceRollDto diceRollDto = (DiceRollDto) dto;
            DiceRolledEvent diceRolledEvent = new DiceRolledEvent(DiceRolledEvent.DICE_ROLLED, new IntegerPair(diceRollDto.getDice1(), diceRollDto.getDice2()));
            hexagonPane.fireEvent(diceRolledEvent);
        }

        else {
            throw new RuntimeException("Invalid message type");
        }

        writeToGameUpdates(message);
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
            hexagonPane.fireEvent(new BuildingPlacedEvent(event.getCorner(), BuildingType.SETTLEMENT, service.getGame().getCurrentPlayer()));

            //unselect the settlementToggleButton
            settlementToggleButton.setSelected(false);
            //update resource labels
            updatePlayerInfo(service.getGame().getCurrentPlayer());
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
            hexagonPane.fireEvent(new BuildingPlacedEvent(event.getCorner(), BuildingType.CITY, service.getGame().getCurrentPlayer()));
            //unselect the cityToggleButton
            cityToggleButton.setSelected(false);
            //update resource labels
            updatePlayerInfo(service.getGame().getCurrentPlayer());
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
            hexagonPane.fireEvent(new BuildingPlacedEvent(event.getEdge(), BuildingType.ROAD, service.getGame().getCurrentPlayer()));

            //unselect the roadToggleButton
            roadToggleButton.setSelected(false);
            //update resource labels
            updatePlayerInfo(service.getGame().getCurrentPlayer());
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

        service.getGame().distributeResources(event.getDiceRoll().getFirst() + event.getDiceRoll().getSecond());


        if(event.getEventType() == DiceRolledEvent.CPU_ROLLED_DICE){
            DiceRollDto diceRollDto = new DiceRollDto();
            diceRollDto.setDice1(event.getDiceRoll().getFirst());
            diceRollDto.setDice2(event.getDiceRoll().getSecond());
            service.sendWebsocketMessage(service.objectToJson(diceRollDto));
        }

        try {
            firstDiceImage.setImage(BoardUtilityFunctions.getDiceImage(event.getDiceRoll().getFirst()));
            secondDiceImage.setImage(BoardUtilityFunctions.getDiceImage(event.getDiceRoll().getSecond()));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        //statusLabel.setText("Dice rolled: " + (event.getDiceRoll().getKey() + event.getDiceRoll().getValue()));
        statusLabel.setText(service.getGame().getCurrentPlayer().getName() + " rolled: " + (event.getDiceRoll().getFirst() + event.getDiceRoll().getSecond()));
        writeToGameUpdates(service.getGame().getCurrentPlayer().getName() + " rolled: " + (event.getDiceRoll().getFirst() + event.getDiceRoll().getSecond()));

        updateAllPlayerInfos();
    }

    public void handleTurnEndedEvent(TurnEndedEvent event) {

        timer.stop();
        hexagonPane.getChildren().remove(timer);
        timer = new Timer(TURN_TIME);
        hexagonPane.getChildren().add(timer);
        leftTimeLabel.setStyle("-fx-text-fill: black");
        timer.start();

        removeHighlight(); //unhighlight all highlighted edges, corners
        unhighlightPlayerInfo(service.getGame().getCurrentPlayer()); //unhighlight current player info

        service.getGame().endTurn();
        //send TurnEnded message

        if(service.getGame().checkWinner()!=null){
            hexagonPane.fireEvent(new GameWonEvent(service.getGame().checkWinner()));
            return;
        }

        highlightPlayerInfo(service.getGame().getCurrentPlayer());

        statusLabel.setText(service.getGame().getCurrentPlayer().getName() + "'s turn");
        writeToGameUpdates(service.getGame().getCurrentPlayer().getName() + "'s turn");
        if(service.getGame().equals(service.getGameRoom().getHostName())){
            service.getGame().autoPlayCpuPlayer();
        }
    }

    private void handleBuildingPlacedEvent(BuildingPlacedEvent buildingPlacedEvent) {
        //send BuildingPlaced message
        if(buildingPlacedEvent.getEventType() == BuildingPlacedEvent.SETTLEMENT_PLACED){
            service.getGame().placeSettlement((Corner) buildingPlacedEvent.getPlacement());
            statusLabel.setText("Settlement built");
            writeToGameUpdates(buildingPlacedEvent.getPlayer().getName() + " built a settlement");
            boardView.getCornerView((Corner) buildingPlacedEvent.getPlacement()).occupyCorner(buildingPlacedEvent.getPlayer().getColor(), BuildingType.SETTLEMENT);
        }
        else if(buildingPlacedEvent.getEventType() == BuildingPlacedEvent.CITY_PLACED){
            service.getGame().placeCity((Corner) buildingPlacedEvent.getPlacement());
            statusLabel.setText("City built");
            writeToGameUpdates(buildingPlacedEvent.getPlayer().getName() + " built a city");
            boardView.getCornerView((Corner) buildingPlacedEvent.getPlacement()).occupyCorner(buildingPlacedEvent.getPlayer().getColor(), BuildingType.CITY);
        }
        else{
            service.getGame().placeRoad((Edge) buildingPlacedEvent.getPlacement());
            statusLabel.setText("Road built");
            writeToGameUpdates(buildingPlacedEvent.getPlayer().getName() + " built a road");
            boardView.getEdgeView((Edge) buildingPlacedEvent.getPlacement()).occupyEdge(buildingPlacedEvent.getPlayer().getColor());
            service.getGame().fireLongestRoadEventInNeed(service.getGame().getCurrentPlayer().getLongestRoad());
            service.getGame().updateAllVictoryPoints();
        }
        updatePlayerInfo(buildingPlacedEvent.getPlayer());
    }

    private void handleLongestRoadEvent(LongestRoadEvent event) {
        for (Player player : service.getGame().getPlayers()) {
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
        //TODO: send LeaveGame message
        service.setGame(null);
        service.setGameRoom(null);
        service.disconnectFromGameRoom();
        sceneSwitch.switchToScene(stage, service, "menu-view.fxml");
    }

    public void onDiceButtonClick(){
        DiceRolledEvent diceRolledEvent;
        if(service.getGame().getCurrentDiceRoll()!=null){
            statusLabel.setText("You already rolled the dice");
        }

        else{
            IntegerPair dicePair = service.getGame().rollDice();
            DiceRollDto diceRollDto = new DiceRollDto();
            diceRollDto.setDice1(dicePair.getFirst());
            diceRollDto.setDice2(dicePair.getSecond());
            service.sendWebsocketMessage(service.objectToJson(diceRollDto));
            diceRolledEvent = new DiceRolledEvent(DiceRolledEvent.DICE_ROLLED, dicePair);
            hexagonPane.fireEvent(diceRolledEvent);
        }
    }
    public void onEndTourButtonClick(){
        hexagonPane.fireEvent(new TurnEndedEvent(TurnEndedEvent.TURN_ENDED));
    }
    public void onTradeButtonClick(){
        removeHighlight();
        tradeInitAnchorPane.setVisible(!tradeInitAnchorPane.isVisible());
        tradeOfferAnchorPane.setVisible(!tradeOfferAnchorPane.isVisible());
    }
    public void onSettlementButtonClick(){
        removeHighlight();
        if(!service.getGame().getCurrentPlayer().hasEnoughResources(BuildingType.SETTLEMENT)){
            statusLabel.setText("Not enough resources. You need 1 brick, 1 lumber, 1 wool, 1 grain");
            settlementToggleButton.setSelected(false);
            return;
        }

        statusLabel.setText("Select a corner to build a settlement");
        //highlight all corners that are available to build a settlement
        List<Corner> availableCorners = service.getGame().getAvailableCorners();
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
    public void onCityButtonClick(){
        removeHighlight();
        if(!service.getGame().getCurrentPlayer().hasEnoughResources(BuildingType.CITY)){
            statusLabel.setText("Not enough resources. You need 3 ore, 2 grain");
            cityToggleButton.setSelected(false);
            return;
        }
        statusLabel.setText("Select a settlement to build a city on it");
        //highlight all corners that are available to build a city
        List<Building> buildings = service.getGame().getCurrentPlayer().getBuildings();
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
    public void onRoadButtonClick(){
        removeHighlight();
        if(!service.getGame().getCurrentPlayer().hasEnoughResources(BuildingType.ROAD)){
            statusLabel.setText("Not enough resources. You need 1 brick, 1 lumber");
            roadToggleButton.setSelected(false);
            return;
        }
        statusLabel.setText("Select an edge to build a road");
        //highlight all edges that are available to build a road
        List<Edge> availableEdges = service.getGame().getAvailableEdges();
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
    public void loadPlayerInfos() {
        for (Player player : service.getGame().getPlayers()) {
            FXMLLoader loader = getSceneLoader("player-info-view.fxml");
            AnchorPane playerInfoAnchorPane;
            try {
                assert loader != null;
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
        for (Player player : service.getGame().getPlayers()) {
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
        for (Player player : service.getGame().getPlayers()) {
            if (player.isCpu()) {
                continue;
            }
            service.makeRequestWithToken("/saveScore",
                    "POST",
                    new ScoreDto(player.getName(), player.getVictoryPoint())
            );
        }
    }
    public void loadTradeViews(){
        tradeInitAnchorPane.setVisible(false);
        tradeOfferAnchorPane.setVisible(false);
        //trade init
        FXMLLoader loader = getSceneLoader("trade-view.fxml");
        try {
            assert loader != null;
            AnchorPane tradeAnchorPane = loader.load();
            TradeController tradeController = loader.getController();
            tradeController.setTradeViewType(TradeViewType.TRADE_INIT);
            tradeAnchorPane.getProperties().put("controller", tradeController);
            tradeInitAnchorPane.getChildren().clear();
            tradeInitAnchorPane.getChildren().add(tradeAnchorPane);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //trade offer
        loader = getSceneLoader("trade-view.fxml");
        try {
            assert loader != null;
            AnchorPane tradeAnchorPane = loader.load();
            TradeController tradeController = loader.getController();
            tradeController.setTradeViewType(TradeViewType.TRADE_OFFER);
            tradeAnchorPane.getProperties().put("controller", tradeController);
            tradeOfferAnchorPane.getChildren().clear();
            tradeOfferAnchorPane.getChildren().add(tradeAnchorPane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}