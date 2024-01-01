package com.group1.frontend.controllers;

import com.group1.frontend.dto.httpDto.GameRoom_PlayerDto;
import com.group1.frontend.dto.httpDto.PlayerDto;
import com.group1.frontend.dto.websocketDto.JoinLobbyDto;
import com.group1.frontend.dto.websocketDto.KickPlayerDto;
import com.group1.frontend.dto.websocketDto.LeaveGameDto;
import com.group1.frontend.dto.websocketDto.WebSocketDto;
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
        //TODO: handle other types of messages, getting class type is too ugly
        if (dto.getClass().equals(JoinLobbyDto.class)) {
            LobbyPlayer lobbyPlayer = ((JoinLobbyDto) dto).getPlayer();
            if(lobbyPlayer.getCpu()){
                CPU_NAMES.replace(lobbyPlayer.getName(), false);
            }
            service.getGameRoom().addPlayer(lobbyPlayer);
            addPlayerToTable(lobbyPlayer);
            lobbyTable.refresh();
        }
        else if(dto.getClass().equals(LeaveGameDto.class)){
            LeaveGameDto leaveGameDto = (LeaveGameDto) dto;
            LobbyPlayer lobbyPlayer = leaveGameDto.getPlayer();
            service.getGameRoom().removePlayer(lobbyPlayer.getName());
            removeFromLobbyTable(lobbyPlayer.getName());
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

    }

    @FXML
    protected void onAddCpuPlayerButtonClick() {
        if (service.getGameRoom().getPlayers().size() >= 4) {
            //TODO: disable Add CPU button (UI disable)
            return;
        }
        LobbyPlayer lobbyPlayer = new LobbyPlayer(
                getRandomAvailableColor(),
                getRandomAvailableCpuName(),
                true,
                true);
        service.getGameRoom().addPlayer(lobbyPlayer);
        lobbyTable.getItems().add(lobbyPlayer);
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
    }
    private void removeFromLobbyTable(String playerName){
        lobbyTable.getItems().removeIf(player -> player.getName().equals(playerName));
        lobbyTable.refresh();
    }
}
