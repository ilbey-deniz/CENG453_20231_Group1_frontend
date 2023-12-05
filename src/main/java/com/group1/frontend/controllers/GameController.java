package com.group1.frontend.controllers;

import com.group1.frontend.components.Board;
import com.group1.frontend.view.elements.BoardView;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

import java.io.FileNotFoundException;

public class GameController extends Controller{
    @FXML
    private AnchorPane hexagonPane;


    @Override
    public void init() {
        try {
            hexagonPane.getChildren().add(new BoardView(new Board()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}