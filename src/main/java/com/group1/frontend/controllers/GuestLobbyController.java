package com.group1.frontend.controllers;

import com.group1.frontend.events.PlayerKickedEvent;
import com.group1.frontend.utils.LobbyPlayer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;


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
    private Button readyButton;
    @FXML
    private Button backButton;
    @FXML
    private Label hostNameLabel;
    @FXML
    private Label roomCodeLabel;

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
        lobbyTable.addEventHandler(PlayerKickedEvent.PLAYER_KICKED, this::handlePlayerKickedEvent);
        service.connectToGameRoom(this::guestLobbyMessageHandler);
    }

    private void guestLobbyMessageHandler(String string) {
    }

    private void handlePlayerKickedEvent(PlayerKickedEvent playerKickedEvent) {
    }

    @FXML
    protected void onReadyButtonClick() {

    }
    @FXML
    protected void onBackButtonClick() {
        service.disconnectFromGameRoom();
        sceneSwitch.switchToScene(stage, service, "menu-view.fxml");
    }


}
