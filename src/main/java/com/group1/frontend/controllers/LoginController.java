package com.group1.frontend.controllers;

import com.group1.frontend.dto.httpDto.LoginDto;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.http.HttpResponse;


public class LoginController extends Controller {
    @FXML
    private Label statusLabel;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;


    @FXML
    protected void onLoginClick() {
        statusLabel.setText("Logging in...");
        if(usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            statusLabel.setText("Please enter a username and password");
        }
        else {
            HttpResponse<String> response = service.makeRequest(
                    "/login",
                    "POST",
                    new LoginDto(usernameField.getText(), passwordField.getText()));

            if(response.statusCode() == 200) {
                service.setToken(response.body());
                service.setUsername(usernameField.getText());
                sceneSwitch.switchToScene(stage, service, "menu-view.fxml");
            }
            else {
                statusLabel.setText(response.body());
            }
        }

    }
    @FXML
    protected void onForgotLinkClick() {
        sceneSwitch.switchToScene(stage, service,"forgot-view.fxml");
    }
    @FXML
    protected void onRegisterButtonClick() {
        sceneSwitch.switchToScene(stage, service, "register-view.fxml");
    }

    @FXML
    protected void onEnter(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER) {
            onLoginClick();
        }
    }

}
