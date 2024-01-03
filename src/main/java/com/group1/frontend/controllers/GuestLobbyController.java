package com.group1.frontend.controllers;

import com.group1.frontend.components.Game;
import com.group1.frontend.dto.httpDto.GameRoom_PlayerDto;
import com.group1.frontend.dto.httpDto.PlayerDto;
import com.group1.frontend.dto.websocketDto.*;
import com.group1.frontend.utils.LobbyPlayer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

import java.net.http.HttpResponse;


public class GuestLobbyController extends Controller{
    @FXML
    private TableView<LobbyPlayer> lobbyTable;
    @FXML
    private TableColumn<LobbyPlayer, String> usernameColumn;
    @FXML
    private TableColumn<LobbyPlayer, ImageView> colorColumn;
    @FXML
    private TableColumn<LobbyPlayer, String> readyColumn;
    
    @FXML
    private Label hostNameLabel;
    @FXML
    private Label roomCodeLabel;
    @FXML
    private Label statusLabel;

    public void init() {
        colorColumn.setCellValueFactory(
                new PropertyValueFactory<LobbyPlayer, ImageView>("colorImage")
        );
        usernameColumn.setCellValueFactory(
                new PropertyValueFactory<LobbyPlayer, String>("name")
        );
        readyColumn.setCellValueFactory(
                new PropertyValueFactory<LobbyPlayer, String>("ready")
        );
        roomCodeLabel.setText(service.getGameRoom().getRoomCode());
        hostNameLabel.setText(service.getGameRoom().getHostName());
        service.getGameRoom().getPlayersAsList().forEach(
                player -> lobbyTable.getItems().add(player)
        );

        service.connectToGameRoom(this::guestLobbyMessageHandler);
        while(!service.getClient().isOpen()){
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        LobbyPlayer clientPlayer = service.getGameRoom().getPlayers().get(service.getUsername());
        JoinLobbyDto joinLobbyDto = new JoinLobbyDto();
        joinLobbyDto.setPlayer(clientPlayer);
        service.sendWebsocketMessage(service.objectToJson(joinLobbyDto));
    }

    private void guestLobbyMessageHandler(String message) {
        WebSocketDto dto = (WebSocketDto) service.jsonToObject(message, WebSocketDto.class);
        //TODO: handle other types of messages, getting class type is too ugly

        if(dto.getClass().equals(JoinLobbyDto.class)){
            LobbyPlayer lobbyPlayer = ((JoinLobbyDto) dto).getPlayer();
            service.getGameRoom().addPlayer(lobbyPlayer);
            lobbyTable.getItems().add(lobbyPlayer);
            lobbyTable.refresh();
        }
        else if (dto.getClass().equals(KickPlayerDto.class)){
            LobbyPlayer player = ((KickPlayerDto) dto).getPlayer();
            if (player.getName().equals(service.getUsername())) {
                //TODO: show some sort of "you were kicked" message
                service.disconnectFromGameRoom();
                service.setGameRoom(null);
                sceneSwitch.switchToScene(stage, service, "menu-view.fxml");
            }
            else{
                service.getGameRoom().removePlayer(player.getName());
                removeFromLobbyTable(player.getName());

            }
        }
        else if (dto.getClass().equals(LeaveGameDto.class)){
            LobbyPlayer player = ((LeaveGameDto) dto).getPlayer();
            //if the player is the host, delete the room
            if(service.getGameRoom().getHostName().equals(player.getName())){
                //TODO: show some sort of "host left" message
                service.disconnectFromGameRoom();
                service.setGameRoom(null);
                sceneSwitch.switchToScene(stage, service, "menu-view.fxml");
            }
            else{
                service.getGameRoom().removePlayer(player.getName());
                removeFromLobbyTable(player.getName());
            }
        }
        else if(dto.getClass().equals(PlayerReadyDto.class)){
            PlayerReadyDto playerReadyDto = (PlayerReadyDto) dto;
            LobbyPlayer lobbyPlayer = playerReadyDto.getPlayer();
            service.getGameRoom().getPlayers().put(lobbyPlayer.getName(), lobbyPlayer);
            lobbyTable.getItems().forEach(player -> {
                if(player.getName().equals(lobbyPlayer.getName())){
                    player.setReady(lobbyPlayer.getReady());
                }
            });
            lobbyTable.refresh();
        }
        else if (dto.getClass().equals(StartGameDto.class)){
            statusLabel.setText("Starting game...");
        }
        else if (dto.getClass().equals(GameDto.class)){
            GameDto gameDto = (GameDto) dto;
            Game game = new Game(gameDto);
            game.setOwnerOfBuildings();
            //TODO: set the players with longest road
            service.setGame(game);
            sceneSwitch.switchToScene(stage, service, "board-view.fxml");
        }

    }

    @FXML
    protected void onReadyButtonClick() {
        LobbyPlayer lobbyPlayer = service.getGameRoom().getPlayers().get(service.getUsername());
        HttpResponse<String> response = service.makeRequestWithToken(
                "/game/playerReady",
                "POST",
                new GameRoom_PlayerDto(
                        service.getGameRoom().getRoomCode(),
                        new PlayerDto(
                                lobbyPlayer.getName(),
                                lobbyPlayer.getColor(),
                                !lobbyPlayer.getReady(),
                                false,
                                false
                        )
                )
        );
        if(response.statusCode() == 200){
            lobbyPlayer.setReady(!lobbyPlayer.getReady());
            service.getGameRoom().getPlayers().put(lobbyPlayer.getName(), lobbyPlayer);
            PlayerReadyDto playerReadyDto = new PlayerReadyDto();
            playerReadyDto.setPlayer(lobbyPlayer);
            String message = service.objectToJson(playerReadyDto);
            service.sendWebsocketMessage(message);
            //find the player in the table and update the ready column
            lobbyTable.getItems().forEach(player -> {
                if(player.getName().equals(lobbyPlayer.getName())){
                    player.setReady(lobbyPlayer.getReady());
                }
            });
            lobbyTable.refresh();
        }
        else{
            statusLabel.setText(response.body());
        }
    }
    @FXML
    protected void onBackButtonClick() {
        HttpResponse<String> response = service.makeRequestWithToken(
                "/game/playerLeft",
                "POST",
                new GameRoom_PlayerDto(
                        service.getGameRoom().getRoomCode(),
                        new PlayerDto(
                                service.getUsername(),
                                null,
                                false,
                                false,
                                false
                        )
        ));
        if(response.statusCode() == 200){
            LeaveGameDto leaveGameDto = new LeaveGameDto();
            leaveGameDto.setPlayer(service.getGameRoom().getPlayers().get(service.getUsername()));
            service.sendWebsocketMessage(service.objectToJson(leaveGameDto));
            service.disconnectFromGameRoom();
            service.setGameRoom(null);
            sceneSwitch.switchToScene(stage, service, "menu-view.fxml");
        }
        else {
            statusLabel.setText(response.body());
        }
    }

    public void removeFromLobbyTable(String playerName) {
        lobbyTable.getItems().removeIf(player -> player.getName().equals(playerName));
        lobbyTable.refresh();
    }


}
