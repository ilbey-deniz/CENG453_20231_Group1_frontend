package com.group1.frontend.controllers;

import com.group1.frontend.components.Board;
import com.group1.frontend.view.elements.BoardView;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.TextFlow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


import java.io.FileNotFoundException;

public class BoardController extends Controller{
    @FXML
    private AnchorPane hexagonPane;

    @FXML
    private TextFlow gameUpdatesTextFlow;

    @FXML
    private TextField chatTextField;


    @Override
    public void init() {
        try {
            hexagonPane.getChildren().add(new BoardView(new Board()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void onSendButtonClick() {
        String message = chatTextField.getText();
        gameUpdatesTextFlow.getChildren().add(new javafx.scene.text.Text(message + "\n"));
        chatTextField.clear();
    }

    public void onEnter(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER){
            onSendButtonClick();
        }
    }

    public void onLeaveButtonClick() {
        sceneSwitch.switchToScene(stage, service, "menu-view.fxml");
    }

}