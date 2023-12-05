package com.group1.frontend.controllers;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class MenuController extends Controller{
    @FXML
    private Label usernameLabel;

    @FXML
    protected void onCreateGameButtonClick() {
        sceneSwitch.switchToScene(stage, service, "game-view.fxml");

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
