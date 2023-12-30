package com.group1.frontend.controllers;
import com.group1.frontend.dto.httpDto.JoinGameDto;
import com.group1.frontend.dto.httpDto.PlayerDto;
import com.group1.frontend.enums.PlayerColor;
import com.group1.frontend.utils.LobbyPlayer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.http.HttpResponse;

import static com.group1.frontend.constants.LobbyPlayerConstants.LOBBY_PLAYER_COLORS;
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
            LobbyPlayer lobbyPlayer = new LobbyPlayer(
                    playerDto.getColor(),
                    playerDto.getName(),
                    playerDto.isHost(),
                    playerDto.isCpu(),
                    playerDto.isReady());
            service.getGameRoom().addPlayer(lobbyPlayer);
            service.getGameRoom().setHost(service.getUsername());

            sceneSwitch.switchToScene(stage, service, "host-lobby-view.fxml");
        }
        else {
            statusLabel.setText("Error creating game: " + response.body());
        }


    }

    @FXML
    protected void onJoinGameButtonClick() {
//        PlayerColor color = getRandomAvailableColor();
//        PlayerDto playerDto = new PlayerDto(
//                service.getUsername(),
//                color,
//                false,
//                true,
//                false);
        if(roomCodeTextField.getText().isEmpty()) {
            statusLabel.setText("Please enter a room code");
            return;
        }
        PlayerColor color = getRandomAvailableColor();
        PlayerDto playerDto = new PlayerDto(
                service.getUsername(),
                color,
                false,
                true,
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
            service.connectToGameRoom(this::joinLobbyHandler);
            System.out.println("Joined game");

        }
        else {
            statusLabel.setText("Error joining game: " + response.body());
        }

//        HttpResponse<String> response = service.makeRequestWithToken(
//                "/game/join",
//                "POST",
//                );

//        sceneSwitch.switchToScene(stage, service, "guest-lobby-view.fxml");
    }

    private void joinLobbyHandler(String s) {
        System.out.println("Joined lobby");
        sceneSwitch.switchToScene(stage, service, "guest-lobby-view.fxml");
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
