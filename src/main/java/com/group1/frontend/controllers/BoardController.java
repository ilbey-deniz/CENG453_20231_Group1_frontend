package com.group1.frontend.controllers;

import com.group1.frontend.components.*;
import com.group1.frontend.dto.httpDto.RoomCodeDto;
import com.group1.frontend.dto.httpDto.ScoreDto;
import com.group1.frontend.dto.websocketDto.*;
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
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static com.group1.frontend.constants.BoardConstants.TURN_TIME;
import static com.group1.frontend.utils.BoardUtilityFunctions.secondsToTime;
import static com.group1.frontend.utils.BoardUtilityFunctions.tradeResourcesToString;
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
                service.getGame().setCurrentPlayer(service.getGame().getPlayerByName(service.getUsername()));
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

            if(service.getGame().getCurrentPlayer().getName().equals(service.getUsername())){
                enableButtons();
                statusLabel.setText("Your turn");
            }
            else{
                disableButtons();
                statusLabel.setText(service.getGame().getCurrentPlayer().getName() + "'s turn");
            }

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
            hexagonPane.addEventHandler(TurnEndedEvent.CPU_TURN_ENDED, this::handleTurnEndedEvent);
            hexagonPane.addEventHandler(BuildingPlacedEvent.ROAD_PLACED, this::handleBuildingPlacedEvent);
            hexagonPane.addEventHandler(BuildingPlacedEvent.SETTLEMENT_PLACED, this::handleBuildingPlacedEvent);
            hexagonPane.addEventHandler(BuildingPlacedEvent.CITY_PLACED, this::handleBuildingPlacedEvent);
            hexagonPane.addEventHandler(BuildingPlacedEvent.CPU_ROAD_PLACED, this::handleBuildingPlacedEvent);
            hexagonPane.addEventHandler(BuildingPlacedEvent.CPU_SETTLEMENT_PLACED, this::handleBuildingPlacedEvent);
            hexagonPane.addEventHandler(BuildingPlacedEvent.CPU_CITY_PLACED, this::handleBuildingPlacedEvent);
            hexagonPane.addEventHandler(LongestRoadEvent.LONGEST_ROAD, this::handleLongestRoadEvent);
            hexagonPane.addEventHandler(GameWonEvent.GAME_WON, this::handleGameWonEvent);
            hexagonPane.addEventHandler(TradeButtonEvent.TRADE_INIT_ACCEPT, this::handleTradeAcceptCancelButtonEvent);
            hexagonPane.addEventHandler(TradeButtonEvent.TRADE_INIT_CANCEL, this::handleTradeAcceptCancelButtonEvent);
            hexagonPane.addEventHandler(TradeButtonEvent.TRADE_OFFER_ACCEPT, this::handleTradeAcceptCancelButtonEvent);
            hexagonPane.addEventHandler(TradeButtonEvent.TRADE_OFFER_CANCEL, this::handleTradeAcceptCancelButtonEvent);

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
    private void boardMessageHandler(String message) {
        WebSocketDto dto = (WebSocketDto) service.jsonToObject(message, WebSocketDto.class);
        if (dto.getClass().equals(DiceRollDto.class)) {
            DiceRollDto diceRollDto = (DiceRollDto) dto;
            DiceRolledEvent diceRolledEvent = new DiceRolledEvent(DiceRolledEvent.DICE_ROLLED, new IntegerPair(diceRollDto.getDice1(), diceRollDto.getDice2()));
            hexagonPane.fireEvent(diceRolledEvent);
        }
        else if (dto.getClass().equals(TradeInitDto.class)) {
            TradeInitDto tradeInitDto = (TradeInitDto) dto;
            TradeController tradeOfferController = getTradeController(TradeViewType.TRADE_OFFER);
            tradeOfferController.zeroizeLabels();
            tradeOfferController.setResourceLabels(tradeInitDto.getOfferedResources(), tradeInitDto.getRequestedResources());
            tradeOfferController.setTraderNameLabel(tradeInitDto.getTraderName());
            tradeOfferAnchorPane.setVisible(true);
        }
        else if (dto.getClass().equals(TradeAcceptDto.class)) {
            TradeAcceptDto tradeAcceptDto = (TradeAcceptDto) dto;
            service.getGame().tradeResources(
                    tradeAcceptDto.getTraderName(),
                    tradeAcceptDto.getTradeeName(),
                    tradeAcceptDto.getOfferedResources(),
                    tradeAcceptDto.getRequestedResources());
            updateAllPlayerInfos();
            writeToGameUpdates(
                    tradeAcceptDto.getTraderName() +
                    " traded with " +
                    tradeAcceptDto.getTradeeName() +
                    " : offered " +
                    tradeResourcesToString(tradeAcceptDto.getOfferedResources()) +
                    " and received " +
                    tradeResourcesToString(tradeAcceptDto.getRequestedResources())
            );

        }
        else if(dto.getClass().equals(EndTurnDto.class)){
            hexagonPane.fireEvent(new TurnEndedEvent(TurnEndedEvent.TURN_ENDED));
        }
        else if (dto.getClass().equals(TextMessageDto.class)) {

        }
        else if (dto.getClass().equals(PlaceBuildingDto.class)) {
            PlaceBuildingDto placeBuildingDto = (PlaceBuildingDto) dto;
            Player player = service.getGame().getPlayerByName(placeBuildingDto.getPlayerName());
            List<Double> coordinates = placeBuildingDto.getCoordinates();
            Object placementObject;
            if(coordinates.size() == 2){
                placementObject = service.getGame().getBoard().getCornersAsMap().get(coordinates);
            }
            else {
                placementObject = service.getGame().getBoard().getEdgesAsMap().get(coordinates);
            }
            BuildingPlacedEvent buildingPlacedEvent = new BuildingPlacedEvent(
                    placeBuildingDto.getBuildingType() == BuildingType.ROAD ? BuildingPlacedEvent.ROAD_PLACED :
                            placeBuildingDto.getBuildingType() == BuildingType.CITY ? BuildingPlacedEvent.CITY_PLACED :
                                    BuildingPlacedEvent.SETTLEMENT_PLACED,
                    placementObject,
                    player);
            hexagonPane.fireEvent(buildingPlacedEvent);
        }
        else {
            throw new RuntimeException("Invalid message type");
        }
    }
    public void handleTradeAcceptCancelButtonEvent(TradeButtonEvent event) {
        if (event.getEventType() == TradeButtonEvent.TRADE_INIT_ACCEPT) {
            TradeController tradeController = getTradeController(TradeViewType.TRADE_INIT);
            HashMap<ResourceType, Integer> offeredResources = tradeController.getOutResources();
            HashMap<ResourceType, Integer> requestedResources = tradeController.getInResources();
            if (offeredResources.isEmpty() || requestedResources.isEmpty()) {
                statusLabel.setText("You must offer and request at least one resource");
                tradeToggleButton.setSelected(false);
                return;
            }
            //if the player doesn't have enough resources to offer
            if(!service.getGame().getCurrentPlayer().hasEnoughResourcesToTrade(offeredResources)){
                statusLabel.setText("You don't have enough resources to offer");
                tradeToggleButton.setSelected(false);
                return;
            }
            HttpResponse<String> response = service.makeRequestWithToken("/game/tradeInit",
                    "POST",
                    new RoomCodeDto(service.getGameRoom().getRoomCode())
            );
            if(response.statusCode() == 200){
                tradeController.zeroizeLabels();
                tradeInitAnchorPane.setVisible(false);
                statusLabel.setText("Trade offer sent");
                //Sending Trade message
                TradeInitDto tradeInitDto = new TradeInitDto();
                tradeInitDto.setTraderName(service.getUsername());
                tradeInitDto.setOfferedResources(offeredResources);
                tradeInitDto.setRequestedResources(requestedResources);
                String message = service.objectToJson(tradeInitDto);
                service.sendWebsocketMessage(message);
            }
            else{
                statusLabel.setText("Trade failed");
            }


        }
        else if (event.getEventType() == TradeButtonEvent.TRADE_INIT_CANCEL) {
            TradeController tradeController = getTradeController(TradeViewType.TRADE_INIT);
            tradeController.zeroizeLabels();
            tradeInitAnchorPane.setVisible(false);
        }
        else if (event.getEventType() == TradeButtonEvent.TRADE_OFFER_ACCEPT) {
            TradeController tradeController = getTradeController(TradeViewType.TRADE_OFFER);
            HashMap<ResourceType, Integer> inResources = tradeController.getInResources();
            HashMap<ResourceType, Integer> outResources = tradeController.getOutResources();
            //check if the player has enough resources to accept the trade
            if(!service.getGame().getCurrentPlayer().hasEnoughResourcesToTrade(outResources)){
                statusLabel.setText("You don't have enough resources to accept the trade");
                tradeToggleButton.setSelected(false);
                return;
            }
            HttpResponse<String> response = service.makeRequestWithToken("/game/tradeAccept",
                    "POST",
                    new RoomCodeDto(service.getGameRoom().getRoomCode())
            );
            if(response.statusCode() == 200) {
                statusLabel.setText("Trade accepted");
                tradeOfferAnchorPane.setVisible(false);
                Player trader = service.getGame().getPlayerByName(tradeController.getTraderNameLabel().getText());
                Player tradee = service.getGame().getPlayerByName(service.getUsername());
                service.getGame().tradeResources(
                        tradeController.getTraderNameLabel().getText(),
                        service.getUsername(),
                        inResources,
                        outResources);
                //Sending Trade message
                TradeAcceptDto tradeAcceptDto = new TradeAcceptDto();
                tradeAcceptDto.setTraderName(trader.getName());
                tradeAcceptDto.setTradeeName(tradee.getName());
                tradeAcceptDto.setOfferedResources(inResources);
                tradeAcceptDto.setRequestedResources(outResources);
                String message = service.objectToJson(tradeAcceptDto);
                service.sendWebsocketMessage(message);
                updateAllPlayerInfos();
                writeToGameUpdates(
                        tradeAcceptDto.getTraderName() +
                                " traded with " +
                                tradeAcceptDto.getTradeeName() +
                                " : offered " +
                                tradeResourcesToString(tradeAcceptDto.getOfferedResources()) +
                                " and received " +
                                tradeResourcesToString(tradeAcceptDto.getRequestedResources())
                );
            }
            else{
                statusLabel.setText("Trade failed");
            }
            tradeController.zeroizeLabels();
            tradeOfferAnchorPane.setVisible(false);

        }
        else if (event.getEventType() == TradeButtonEvent.TRADE_OFFER_CANCEL) {
            tradeOfferAnchorPane.setVisible(false);
            TradeController tradeController = getTradeController(TradeViewType.TRADE_OFFER);
            tradeController.zeroizeLabels();
        }
        tradeToggleButton.setSelected(false);

    }
    private void handleCornerClickEvent(CornerClickedEvent event) {
        //check settlementToggleButton is selected
        if(settlementToggleButton.isSelected()){
            //check if the corner is available to build a settlement
            if(!highlightedCorners.contains(event.getCorner())){
                statusLabel.setText("You can't build a settlement here");
                return;
            }

            //unhighlight all corners
            removeHighlight();

            BuildingPlacedEvent buildingPlacedEvent = new BuildingPlacedEvent(
                    BuildingPlacedEvent.SETTLEMENT_PLACED ,event.getCorner(), service.getGame().getCurrentPlayer());
            //place the settlement
            hexagonPane.fireEvent(buildingPlacedEvent);

            // send building placed message
            PlaceBuildingDto placeBuildingDto = new PlaceBuildingDto();
            placeBuildingDto.setBuildingType(BuildingType.SETTLEMENT);
            placeBuildingDto.setCoordinates(List.of(event.getCorner().getXCoordinate(), event.getCorner().getYCoordinate()));
            placeBuildingDto.setPlayerName(buildingPlacedEvent.getPlayer().getName());
            service.sendWebsocketMessage(service.objectToJson(placeBuildingDto));

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

            BuildingPlacedEvent buildingPlacedEvent = new BuildingPlacedEvent(
                    BuildingPlacedEvent.CITY_PLACED, event.getCorner(), service.getGame().getCurrentPlayer());
            //place the city
            hexagonPane.fireEvent(buildingPlacedEvent);
            // send building placed message
            PlaceBuildingDto placeBuildingDto = new PlaceBuildingDto();
            placeBuildingDto.setBuildingType(BuildingType.CITY);
            placeBuildingDto.setCoordinates(List.of(event.getCorner().getXCoordinate(), event.getCorner().getYCoordinate()));
            placeBuildingDto.setPlayerName(buildingPlacedEvent.getPlayer().getName());
            service.sendWebsocketMessage(service.objectToJson(placeBuildingDto));
            //unselect the cityToggleButton
            cityToggleButton.setSelected(false);
            //update resource labels
            updatePlayerInfo(service.getGame().getCurrentPlayer());
        }

    }
    private void handleEdgeClickEvent(EdgeClickedEvent event) {
        //chech roadToggleButton is selected
        if(roadToggleButton.isSelected()){
            //check if the edge is available to build a road
            if(!highlightedEdges.contains(event.getEdge())){
                statusLabel.setText("You can't build a road here");
                return;
            }

            //unhighlight all edges
            removeHighlight();
            BuildingPlacedEvent buildingPlacedEvent = new BuildingPlacedEvent(
                    BuildingPlacedEvent.ROAD_PLACED ,event.getEdge(), service.getGame().getCurrentPlayer());
            //place the road
            hexagonPane.fireEvent(buildingPlacedEvent);
            // send building placed message
            PlaceBuildingDto placeBuildingDto = new PlaceBuildingDto();
            placeBuildingDto.setBuildingType(BuildingType.ROAD);
            placeBuildingDto.setCoordinates(List.of(event.getEdge().getFirstXCoordinate(),
                    event.getEdge().getFirstYCoordinate(),
                    event.getEdge().getSecondXCoordinate(),
                    event.getEdge().getSecondYCoordinate()));
            placeBuildingDto.setPlayerName(buildingPlacedEvent.getPlayer().getName());
            service.sendWebsocketMessage(service.objectToJson(placeBuildingDto));

            //unselect the roadToggleButton
            roadToggleButton.setSelected(false);
            //update resource labels
            updatePlayerInfo(service.getGame().getCurrentPlayer());
        }
    }

    private void handleOneTickEvent(TimeEvent event) {
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

    private void handleDiceRolledEvent(DiceRolledEvent event) {

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

    private void handleTurnEndedEvent(TurnEndedEvent event) {

        timer.stop();
        hexagonPane.getChildren().remove(timer);
        timer = new Timer(TURN_TIME);
        hexagonPane.getChildren().add(timer);
        leftTimeLabel.setStyle("-fx-text-fill: black");
        timer.start();

        removeHighlight(); //unhighlight all highlighted edges, corners
        unhighlightPlayerInfo(service.getGame().getCurrentPlayer()); //unhighlight current player info

        service.getGame().endTurn();
        if(service.getGame().getCurrentPlayer().getName().equals(service.getUsername())){
            statusLabel.setText("Your turn");
            enableButtons();
        }
        else{
            statusLabel.setText(service.getGame().getCurrentPlayer().getName() + "'s turn");
        }

        if(event.getEventType().equals(TurnEndedEvent.CPU_TURN_ENDED)){
            EndTurnDto endTurnDto = new EndTurnDto();
            service.sendWebsocketMessage(service.objectToJson(endTurnDto));
        }

        //TODO: service winner should be set
        if(service.getGame().checkWinner()!=null){
            hexagonPane.fireEvent(new GameWonEvent(service.getGame().checkWinner()));
            return;
        }

        highlightPlayerInfo(service.getGame().getCurrentPlayer());

        writeToGameUpdates(service.getGame().getCurrentPlayer().getName() + "'s turn");
        if(service.getUsername().equals(service.getGameRoom().getHostName())){
            service.getGame().autoPlayCpuPlayer();
        }
    }
    public void handleBuildingPlacedEvent(BuildingPlacedEvent buildingPlacedEvent) {

        //send BuildingPlaced message
        if(buildingPlacedEvent.getEventType() == BuildingPlacedEvent.SETTLEMENT_PLACED
                || buildingPlacedEvent.getEventType() == BuildingPlacedEvent.CPU_SETTLEMENT_PLACED){
            service.getGame().placeSettlement(buildingPlacedEvent.getCorner());
            statusLabel.setText("Settlement built");
            writeToGameUpdates(buildingPlacedEvent.getPlayer().getName() + " built a settlement");
            boardView.getCornerView(buildingPlacedEvent.getCorner()).occupyCorner(buildingPlacedEvent.getPlayer().getColor(), BuildingType.SETTLEMENT);
        }
        else if(buildingPlacedEvent.getEventType() == BuildingPlacedEvent.CITY_PLACED
                || buildingPlacedEvent.getEventType() == BuildingPlacedEvent.CPU_CITY_PLACED){
            service.getGame().placeCity(buildingPlacedEvent.getCorner());
            statusLabel.setText("City built");
            writeToGameUpdates(buildingPlacedEvent.getPlayer().getName() + " built a city");
            boardView.getCornerView(buildingPlacedEvent.getCorner()).occupyCorner(buildingPlacedEvent.getPlayer().getColor(), BuildingType.CITY);
        }
        else if(buildingPlacedEvent.getEventType() == BuildingPlacedEvent.ROAD_PLACED
                || buildingPlacedEvent.getEventType() == BuildingPlacedEvent.CPU_ROAD_PLACED){
            service.getGame().placeRoad(buildingPlacedEvent.getEdge());
            statusLabel.setText("Road built");
            writeToGameUpdates(buildingPlacedEvent.getPlayer().getName() + " built a road");
            boardView.getEdgeView(buildingPlacedEvent.getEdge()).occupyEdge(buildingPlacedEvent.getPlayer().getColor());
            service.getGame().fireLongestRoadEventInNeed(service.getGame().getCurrentPlayer().getLongestRoad());
            service.getGame().updateAllVictoryPoints();
        }
        // handle bot
        if (buildingPlacedEvent.getEventType() == BuildingPlacedEvent.CPU_ROAD_PLACED
                || buildingPlacedEvent.getEventType() == BuildingPlacedEvent.CPU_CITY_PLACED
                || buildingPlacedEvent.getEventType() == BuildingPlacedEvent.CPU_SETTLEMENT_PLACED) {
            // send building placed message
            PlaceBuildingDto placeBuildingDto = new PlaceBuildingDto();
            placeBuildingDto.setBuildingType(buildingPlacedEvent.getEventType() == BuildingPlacedEvent.CPU_ROAD_PLACED ?
                    BuildingType.ROAD : buildingPlacedEvent.getEventType() == BuildingPlacedEvent.CPU_CITY_PLACED ?
                    BuildingType.CITY : BuildingType.SETTLEMENT);
            if (buildingPlacedEvent.getEventType() == BuildingPlacedEvent.CPU_ROAD_PLACED) {
                placeBuildingDto.setCoordinates(List.of(buildingPlacedEvent.getEdge().getFirstXCoordinate(),
                        buildingPlacedEvent.getEdge().getFirstYCoordinate(),
                        buildingPlacedEvent.getEdge().getSecondXCoordinate(),
                        buildingPlacedEvent.getEdge().getSecondYCoordinate()));
            } else {
                placeBuildingDto.setCoordinates(List.of(buildingPlacedEvent.getCorner().getXCoordinate(),
                        buildingPlacedEvent.getCorner().getYCoordinate()));
            }
            placeBuildingDto.setPlayerName(buildingPlacedEvent.getPlayer().getName());
            service.sendWebsocketMessage(service.objectToJson(placeBuildingDto));
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
        disableButtons();
        EndTurnDto endTurnDto = new EndTurnDto();
        service.sendWebsocketMessage(service.objectToJson(endTurnDto));
        hexagonPane.fireEvent(new TurnEndedEvent(TurnEndedEvent.TURN_ENDED));
    }
    public void onTradeButtonClick(){
        removeHighlight();
        tradeInitAnchorPane.setVisible(!tradeInitAnchorPane.isVisible());
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
            if(player.getName().equals(service.getUsername())){
                playerInfoController.highlightPlayerNameLabel();
            }

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
            tradeController.setParent(hexagonPane);
            tradeController.getTraderNameAnchorPane().setVisible(false);
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
            tradeController.setParent(hexagonPane);
            tradeController.getTraderNameAnchorPane().setVisible(true);
            tradeAnchorPane.getProperties().put("controller", tradeController);
            tradeOfferAnchorPane.getChildren().clear();
            tradeOfferAnchorPane.getChildren().add(tradeAnchorPane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public TradeController getTradeController(TradeViewType tradeViewType){
        if(tradeViewType == TradeViewType.TRADE_INIT){
            return (TradeController) tradeInitAnchorPane.getChildren().getFirst().getProperties().get("controller");
        }
        else if(tradeViewType == TradeViewType.TRADE_OFFER){
            return (TradeController) tradeOfferAnchorPane.getChildren().getFirst().getProperties().get("controller");
        }
        return null;
    }

    private void disableButtons(){
        boardView.setDisable(true);
        tradeToggleButton.setDisable(true);
        settlementToggleButton.setDisable(true);
        cityToggleButton.setDisable(true);
        roadToggleButton.setDisable(true);
        firstDiceButton.setDisable(true);
        secondDiceButton.setDisable(true);
        endTourButton.setDisable(true);
    }

    private void enableButtons(){
        boardView.setDisable(false);
        tradeToggleButton.setDisable(false);
        settlementToggleButton.setDisable(false);
        cityToggleButton.setDisable(false);
        roadToggleButton.setDisable(false);
        firstDiceButton.setDisable(false);
        secondDiceButton.setDisable(false);
        endTourButton.setDisable(false);
    }
}