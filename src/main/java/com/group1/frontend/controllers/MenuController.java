package com.group1.frontend.controllers;
import com.group1.frontend.dto.PlayerDto;
import com.group1.frontend.enums.PlayerColor;
import com.group1.frontend.utils.LobbyPlayer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.net.http.HttpResponse;


public class MenuController extends Controller{
    @FXML
    private Label usernameLabel;
    @FXML
    private Label statusLabel;

    @FXML
    protected void onCreateGameButtonClick() {
        PlayerDto playerDto = new PlayerDto(
                service.getUsername(),
                PlayerColor.getRandomColor(),
                false,
                true,
                false);

        HttpResponse<String> response = service.makeRequestWithToken(
                "/game/create",
                "POST",
                playerDto);

        if(response.statusCode() == 200) {
            service.createGameRoom();
            service.getGameRoom().setRoomCode(response.body());
            LobbyPlayer lobbyPlayer = new LobbyPlayer(
                    playerDto.getColor(),
                    playerDto.getName(),
                    playerDto.isHost(),
                    playerDto.isCpu(),
                    playerDto.isReady());
            service.getGameRoom().addPlayer(lobbyPlayer);
            service.getGameRoom().setHost(service.getUsername());
            service.connectToGameRoom();

            sceneSwitch.switchToScene(stage, service, "host-lobby-view.fxml");
        }
        else {
            statusLabel.setText("Error creating game: " + response.body());
        }


    }

    @FXML
    protected void onJoinGameButtonClick() {

    }

    @FXML
    protected void onLeaderboardButtonClick() {
        sceneSwitch.switchToScene(stage, service, "leaderboard-view.fxml");
    }

    @FXML
    protected void onLogoutButtonClick() {
        service.setUsername(null);
        service.setToken(null);
        sceneSwitch.switchToScene(stage, service, "login-view.fxml");
    }

    @FXML
    protected void onQuitButtonClick() {
        Platform.exit();
    }

    @FXML
    protected void onChangePasswordLinkClick() {
        sceneSwitch.switchToScene(stage, service, "change-password-view.fxml");
    }
    @Override
    public void init() {
        usernameLabel.setText(service.getUsername());
    }
}
