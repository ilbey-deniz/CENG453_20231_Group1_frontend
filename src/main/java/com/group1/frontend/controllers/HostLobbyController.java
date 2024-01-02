package com.group1.frontend.controllers;

import com.group1.frontend.dto.httpDto.GameRoom_PlayerDto;
import com.group1.frontend.dto.httpDto.PlayerDto;
import com.group1.frontend.dto.websocketDto.*;
import com.group1.frontend.utils.GameRoom;
import com.group1.frontend.utils.LobbyPlayer;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import lombok.Getter;

import java.net.http.HttpResponse;

import static com.group1.frontend.constants.LobbyPlayerConstants.*;

public class HostLobbyController extends Controller{
    @Getter
    @FXML
    private TableView<LobbyPlayer> lobbyTable;
    @FXML
    private TableColumn<LobbyPlayer, String> usernameColumn;
    @FXML
    private TableColumn<LobbyPlayer, ImageView> colorColumn;
    @FXML
    private TableColumn<LobbyPlayer, Boolean> readyColumn;
    @FXML
    private TableColumn<LobbyPlayer, LobbyPlayer> kickColumn;

    @FXML
    private Label roomCodeLabel;
    @FXML
    private Label hostNameLabel;

    public void init() {
        colorColumn.setCellValueFactory(
                new PropertyValueFactory<LobbyPlayer, ImageView>("colorImage")
        );
        usernameColumn.setCellValueFactory(
                new PropertyValueFactory<LobbyPlayer, String>("name")
        );
        readyColumn.setCellValueFactory(
                new PropertyValueFactory<LobbyPlayer, Boolean>("ready")
        );
        kickColumn.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        kickColumn.setCellFactory(param -> new TableCell<LobbyPlayer, LobbyPlayer>(){
            private final Button kickButton = new Button("Kick");
            @Override
            protected void updateItem(LobbyPlayer lobbyPlayer, boolean empty) {
                super.updateItem(lobbyPlayer, empty);

                if (lobbyPlayer == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(kickButton);
                kickButton.setOnAction(
                        event -> onKickButtonClick(lobbyPlayer)
                );

                if (lobbyPlayer.getName().equals(service.getUsername())) {
                    kickButton.setDisable(true);
                }
            }

        });

        service.getGameRoom().getPlayers().forEach((username, lobbyPlayer) -> {
            addPlayerToTable(lobbyPlayer);
        });
        hostNameLabel.setText(service.getUsername());
        roomCodeLabel.setText(service.getGameRoom().getRoomCode());
        service.connectToGameRoom(this::hostLobbyMessageHandler);

    }


    public void hostLobbyMessageHandler(String message) {
        WebSocketDto dto = (WebSocketDto) service.jsonToObject(message, WebSocketDto.class);
        //TODO: find a different way to handle message types, class type is too ugly
        if (dto.getClass().equals(JoinLobbyDto.class)) {
            LobbyPlayer lobbyPlayer = ((JoinLobbyDto) dto).getPlayer();
            if(lobbyPlayer.getCpu()){
                CPU_NAMES.replace(lobbyPlayer.getName(), false);
            }
            service.getGameRoom().addPlayer(lobbyPlayer);
            addPlayerToTable(lobbyPlayer);
        }
        else if(dto.getClass().equals(LeaveGameDto.class)){
            LeaveGameDto leaveGameDto = (LeaveGameDto) dto;
            LobbyPlayer lobbyPlayer = leaveGameDto.getPlayer();
            service.getGameRoom().removePlayer(lobbyPlayer.getName());
            removeFromLobbyTable(lobbyPlayer.getName());
        }
        else if(dto.getClass().equals(PlayerReadyDto.class)){
            PlayerReadyDto playerReadyDto = (PlayerReadyDto) dto;
            LobbyPlayer lobbyPlayer = playerReadyDto.getPlayer();
            service.getGameRoom().getPlayers().put(lobbyPlayer.getName(), lobbyPlayer);
            //find the player in the table and update the ready column
            lobbyTable.getItems().forEach(player -> {
                if(player.getName().equals(lobbyPlayer.getName())){
                    player.setReady(lobbyPlayer.getReady());
                }
            });
            lobbyTable.refresh();
        }
    }

    protected void onKickButtonClick(LobbyPlayer lobbyPlayer) {
        String roomCode = service.getGameRoom().getRoomCode();
        PlayerDto playerDto = new PlayerDto(
                lobbyPlayer.getName(),
                lobbyPlayer.getColor(),
                lobbyPlayer.getReady(),
                false,
                lobbyPlayer.getCpu()
        );
        GameRoom_PlayerDto gameRoomPlayerDto = new GameRoom_PlayerDto(
                roomCode,
                playerDto
        );

        HttpResponse<String> response = service.makeRequestWithToken(
                "/game/playerKicked",
                "POST",
                gameRoomPlayerDto
        );
        if(response.statusCode() == 200){
            KickPlayerDto kickPlayerJson = new KickPlayerDto();
            kickPlayerJson.setPlayer(lobbyPlayer);
            String message = service.objectToJson(kickPlayerJson);
            service.sendWebsocketMessage(message);

            if(lobbyPlayer.getCpu()){
                CPU_NAMES.replace(lobbyPlayer.getName(), false);
            }
            service.getGameRoom().removePlayer(lobbyPlayer.getName());
            LOBBY_PLAYER_COLORS.replace(lobbyPlayer.getColor(), false);
            removeFromLobbyTable(lobbyPlayer.getName());
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
                                true,
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
            lobbyTable.getItems().forEach(player -> {
                if(player.getName().equals(lobbyPlayer.getName())){
                    player.setReady(lobbyPlayer.getReady());
                }
            });
            lobbyTable.refresh();
        }

    }

    @FXML
    protected void onAddCpuPlayerButtonClick() {
        PlayerDto playerDto = new PlayerDto(
                CPU_NAMES.entrySet().stream().filter(entry -> !entry.getValue()).findFirst().get().getKey(),
                null,
                true,
                false,
                true
        );
        HttpResponse<String> response = service.makeRequestWithToken(
                "/game/join",
                "POST",
                new GameRoom_PlayerDto(
                        service.getGameRoom().getRoomCode(),
                        playerDto

                )
        );
        if(response.statusCode() == 200){
            CPU_NAMES.replace(playerDto.getName(), true);
            GameRoom gameRoom = (GameRoom) service.jsonToObject(response.body(), GameRoom.class);
            service.setGameRoom(gameRoom);
            JoinLobbyDto joinLobbyDto = new JoinLobbyDto();
            joinLobbyDto.setPlayer(gameRoom.getPlayers().get(playerDto.getName()));
            String message = service.objectToJson(joinLobbyDto);
            service.sendWebsocketMessage(message);
            addPlayerToTable(gameRoom.getPlayers().get(playerDto.getName()));
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
                                true,
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
    }
    @FXML
    protected void onStartGameButtonClick() {
        sceneSwitch.switchToScene(stage, service, "board-view.fxml");
    }

    private void addPlayerToTable(LobbyPlayer lobbyPlayer) {
        lobbyTable.getItems().add(lobbyPlayer);
        lobbyTable.refresh();
    }
    private void removeFromLobbyTable(String playerName){
        lobbyTable.getItems().removeIf(player -> player.getName().equals(playerName));
        lobbyTable.refresh();
    }
}
