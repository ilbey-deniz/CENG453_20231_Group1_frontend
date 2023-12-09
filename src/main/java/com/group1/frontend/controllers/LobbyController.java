package com.group1.frontend.controllers;
import com.group1.frontend.utils.LobbyPlayer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;


public class LobbyController extends Controller{
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
    private Button addCpuPlayerButton;
    @FXML
    private Button backButton;

    @FXML
    private Label statusLabel;
    @FXML
    private Label roomCodeLabel;

    public void init() {
        colorColumn.setCellValueFactory(
                new PropertyValueFactory<LobbyPlayer, ImageView>("color")
        );
        usernameColumn.setCellValueFactory(
                new PropertyValueFactory<LobbyPlayer, String>("username")
        );
        readyColumn.setCellValueFactory(
                new PropertyValueFactory<LobbyPlayer, String>("ready")
        );
        //roomCodeLabel.setText(service.getRoomCode());
        statusLabel.setText("Waiting for players...");

        //populate table with players
        lobbyTable.getItems().add(new LobbyPlayer("red", "iplikçi nedim", "Not Ready"));
        lobbyTable.getItems().add(new LobbyPlayer("yellow", "laz ziya", "Not Ready"));
        lobbyTable.getItems().add(new LobbyPlayer("green", "tombalacı mehmet", "Ready"));
        lobbyTable.getItems().add(new LobbyPlayer("blue", "karahanlı", "Not Ready"));
    }


}
