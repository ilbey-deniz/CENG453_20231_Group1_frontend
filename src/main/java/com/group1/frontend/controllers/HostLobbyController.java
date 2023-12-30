package com.group1.frontend.controllers;

import com.group1.frontend.dto.DestroyGameDto;
import com.group1.frontend.events.PlayerKickedEvent;
import com.group1.frontend.utils.LobbyPlayer;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import lombok.Getter;

import java.net.http.HttpResponse;

public class HostLobbyController extends Controller{
    @Getter
    @FXML
    private TableView<LobbyPlayer> lobbyTable;
    @FXML
    private TableColumn<LobbyPlayer, String> usernameColumn;
    @FXML
    private TableColumn<LobbyPlayer, Boolean> hostColumn;
    @FXML
    private TableColumn<LobbyPlayer, ImageView> colorColumn;
    @FXML
    private TableColumn<LobbyPlayer, Boolean> readyColumn;
    @FXML
    private TableColumn<LobbyPlayer, LobbyPlayer> kickColumn;

    @FXML
    private Label statusLabel;
    @FXML
    private Label roomCodeLabel;

    public void init() {
        colorColumn.setCellValueFactory(
                new PropertyValueFactory<LobbyPlayer, ImageView>("colorImage")
        );
        usernameColumn.setCellValueFactory(
                new PropertyValueFactory<LobbyPlayer, String>("username")
        );
        hostColumn.setCellValueFactory(
                new PropertyValueFactory<LobbyPlayer, Boolean>("host")
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
                        event -> onKickButtonClick(this)
                );


                if (lobbyPlayer.getUsername().equals(service.getUsername())) {
                    kickButton.setDisable(true);
                }
            }

        });

        service.getGameRoom().getPlayers().forEach((username, lobbyPlayer) -> {
            addPlayerToTable(lobbyPlayer);
        });

        //roomCodeLabel.setText(service.getRoomCode());
        statusLabel.setText("Waiting for players...");
        roomCodeLabel.setText(service.getGameRoom().getRoomCode());
        lobbyTable.addEventHandler(PlayerKickedEvent.PLAYER_KICKED, this::handlePlayerKickedEvent);

        service.connectToGameRoom(this::hostLobbyMessageHandler);


    }
    public void hostLobbyMessageHandler(String message) {
        statusLabel.setText(message);
    }


    protected void handlePlayerKickedEvent(PlayerKickedEvent event) {
        LobbyPlayer lobbyPlayer = event.getTableCell().getTableRow().getItem();
        lobbyTable.getItems().remove(lobbyPlayer);
        lobbyTable.refresh();
    }

    protected void onKickButtonClick(TableCell<LobbyPlayer, LobbyPlayer> cell) {
        lobbyTable.fireEvent(new PlayerKickedEvent(cell));
    }

    @FXML
    protected void onReadyButtonClick() {
        lobbyTable.getItems().forEach(lobbyPlayer -> {
            if (lobbyPlayer.getUsername().equals(service.getUsername())) {
                lobbyPlayer.setReady(true);
            }
        });

    }

    @FXML
    protected void onAddCpuPlayerButtonClick() {
        //select a random color and nickname from non selected ones
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
