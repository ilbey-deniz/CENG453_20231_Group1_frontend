package com.group1.frontend.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.http.HttpResponse;


public class RegisterController extends Controller{
    @FXML
    private Label statusLabel;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    protected void onRegisterButtonClick() {
        statusLabel.setText("Register button pressed!");
        if(usernameField.getText().isEmpty() || passwordField.getText().isEmpty() || confirmPasswordField.getText().isEmpty()) {
            statusLabel.setText("Please enter a username and password");
        }
        else if(!passwordField.getText().equals(confirmPasswordField.getText())) {
            statusLabel.setText("Passwords do not match");
        }
        else {
            statusLabel.setText("Registering...");
            HttpResponse<String> response = service.makeRequest(
                    "/register",
                    "POST",
                    "{\"name\":\"" + usernameField.getText() +
                            "\",\"email\":\"" + emailField.getText() +
                            "\",\"password\":\"" + passwordField.getText() +
                            "\"}");

            if(response.statusCode() == 200) {
                statusLabel.setText("Registration successful!");
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
    protected void onLoginButtonClick() {
        sceneSwitch.switchToScene(stage, service, "login-view.fxml");
    }

}
