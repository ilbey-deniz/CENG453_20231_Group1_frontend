package com.group1.frontend.controllers;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


import java.net.http.HttpResponse;

public class ChangePasswordController extends Controller{
    @FXML
    private Label statusLabel;
    @FXML
    private PasswordField oldPasswordField;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    protected void onChangePasswordButtonClick(){
        statusLabel.setText("Changing password...");
        if(oldPasswordField.getText().isEmpty() || newPasswordField.getText().isEmpty() || confirmPasswordField.getText().isEmpty()) {
            statusLabel.setText("Please enter your old password and a new password");
        }
        else if(!newPasswordField.getText().equals(confirmPasswordField.getText())) {
            statusLabel.setText("New passwords do not match");
        }
        else {
            HttpResponse<String> response = service.makeRequestWithToken(
                    "/changePassword",
                    "POST",
                    "{\"name\":\"" + service.getUsername() +
                            "\",\"oldPassword\":\"" + oldPasswordField.getText() +
                            "\",\"newPassword\":\"" + newPasswordField.getText() +
                            "\"}");

            if(response.statusCode() == 200) {
                statusLabel.setText("Password changed successfully!");
            }
            else {
                statusLabel.setText(response.body());
                sceneSwitch.switchToScene(stage, service, "login-view.fxml");
            }
        }

    }

    @FXML
    protected void onBackButtonClick() {
        sceneSwitch.switchToScene(stage, service, "menu-view.fxml");
    }

    @FXML
    protected void onEnter(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.ENTER){
            onChangePasswordButtonClick();
        }
    }

}
