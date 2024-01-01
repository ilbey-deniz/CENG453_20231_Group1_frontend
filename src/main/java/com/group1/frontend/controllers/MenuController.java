package com.group1.frontend.controllers;
import com.group1.frontend.components.Game;
import com.group1.frontend.dto.httpDto.JoinGameDto;
import com.group1.frontend.dto.httpDto.PlayerDto;
import com.group1.frontend.enums.PlayerColor;
import com.group1.frontend.utils.GameRoom;
import com.group1.frontend.utils.LobbyPlayer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.http.HttpResponse;

import static com.group1.frontend.constants.LobbyPlayerConstants.getRandomAvailableColor;


public class MenuController extends Controller{

    @FXML
    private TextField roomCodeTextField;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label statusLabel;

    @FXML
    protected void onCreateGameButtonClick() {
        // it picks random unpicked color
        PlayerColor color = getRandomAvailableColor();
        PlayerDto playerDto = new PlayerDto(
                service.getUsername(),
                color,
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
            LobbyPlayer lobbyPlayer = new LobbyPlayer(playerDto);
            service.getGameRoom().addPlayer(lobbyPlayer);
            service.getGameRoom().setHostName(service.getUsername());

            sceneSwitch.switchToScene(stage, service, "host-lobby-view.fxml");
        }
        else {
            statusLabel.setText("Error creating game: " + response.body());
        }


    }

    @FXML
    protected void onJoinGameButtonClick() {

        if(roomCodeTextField.getText().isEmpty()) {
            statusLabel.setText("Please enter a room code");
            return;
        }

        //this check needs to be done in backend, guest doesn't know about existing colors
        PlayerDto playerDto = new PlayerDto(
                service.getUsername(),
                null,
                false,
                false,
                false);
        JoinGameDto joinGameDto = new JoinGameDto(
                roomCodeTextField.getText(),
                playerDto
        );
        HttpResponse<String> response = service.makeRequestWithToken(
                "/game/join",
                "POST",
                joinGameDto);
        if(response.statusCode() == 200) {
            GameRoom gameRoom = (GameRoom) service.jsonToObject(response.body(), GameRoom.class);
            service.setGameRoom(gameRoom);
            sceneSwitch.switchToScene(stage, service, "guest-lobby-view.fxml");
        }
        else {
            statusLabel.setText("Error joining game: " + response.body());
        }
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
