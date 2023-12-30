package com.group1.frontend.controllers;


import com.group1.frontend.dto.httpDto.ForgotPasswordDto;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.http.HttpResponse;


public class ForgotController extends Controller {
    @FXML
    private Label statusLabel;
    @FXML
    private TextField emailField;

    @FXML
    protected void onSendButtonClick() {
        statusLabel.setText("Send button pressed!");
        if(emailField.getText().isEmpty()) {
            statusLabel.setText("Please enter an email");
        }
        else {
            statusLabel.setText("Sending...");
            HttpResponse<String> response = service.makeRequest(
                    "/forgotPassword",
                    "POST",
                    new ForgotPasswordDto(emailField.getText()));

            if(response.statusCode() == 200) {
                statusLabel.setText("Email sent!");
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

    @FXML
    protected void onRegisterButtonClick() {
        sceneSwitch.switchToScene(stage, service, "register-view.fxml");
    }
}
