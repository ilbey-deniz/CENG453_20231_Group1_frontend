package com.group1.frontend.controllers;

import com.group1.frontend.dto.httpDto.DestroyGameDto;
import com.group1.frontend.events.PlayerKickedEvent;
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
                        event -> onKickButtonClick(lobbyPlayer.getName())
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

        //roomCodeLabel.setText(service.getRoomCode());
        roomCodeLabel.setText(service.getGameRoom().getRoomCode());
        lobbyTable.addEventHandler(PlayerKickedEvent.PLAYER_KICKED, this::handlePlayerKickedEvent);

        service.connectToGameRoom(this::hostLobbyMessageHandler);


    }
    public void hostLobbyMessageHandler(String message) {

    }


    protected void handlePlayerKickedEvent(PlayerKickedEvent event) {
        LobbyPlayer lobbyPlayer = event.getName() == null ? null : lobbyTable.getItems().stream()
                .filter(player -> player.getName().equals(event.getName()))
                .findFirst()
                .orElse(null);
        if (lobbyPlayer == null) {
            return;
        }
        if(lobbyPlayer.getCpu()){
            CPU_NAMES.replace(lobbyPlayer.getName(), false);
        }
        service.getGameRoom().removePlayer(lobbyPlayer.getName());
        LOBBY_PLAYER_COLORS.replace(lobbyPlayer.getColor(), false);
        lobbyTable.getItems().remove(lobbyPlayer);
        lobbyTable.refresh();
    }

    protected void onKickButtonClick(String name) {
        lobbyTable.fireEvent(new PlayerKickedEvent(name));
    }

    @FXML
    protected void onReadyButtonClick() {
        lobbyTable.getItems().forEach(lobbyPlayer -> {
            if (lobbyPlayer.getName().equals(service.getUsername())) {
                lobbyPlayer.setReady(true);
            }
        });

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
        //TODO: room should be destroyed and players should be notified
        //alternatively, the host can be kicked and the host can be transferred to another non-CPU player
        //if there are no non-CPU players left, the room should be destroyed
        //In any case, some sort of HostLeftEvent should be fired
        service.disconnectFromGameRoom();
        HttpResponse<String> response = service.makeRequestWithToken(
                "/game/destroy",
                "POST",
                new DestroyGameDto(service.getGameRoom().getRoomCode())
        );
        sceneSwitch.switchToScene(stage, service, "menu-view.fxml");
    }
    @FXML
    protected void onStartGameButtonClick() {
        sceneSwitch.switchToScene(stage, service, "board-view.fxml");
    }

    private void addPlayerToTable(LobbyPlayer lobbyPlayer) {
        lobbyTable.getItems().add(lobbyPlayer);
    }
}
