package com.group1.frontend.controllers;
import com.group1.frontend.components.Player;
import com.group1.frontend.enums.ResourceType;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

public class PlayerInfoController extends Controller{
    @FXML
    private Label playerNameLabel;
    @FXML
    private ImageView playerImageView;
    @FXML
    private Label longestRoadLabel;
    @FXML
    private Label victoryPointsLabel;
    @FXML
    private Label woolLabel;
    @FXML
    private Label lumberLabel;
    @FXML
    private Label grainLabel;
    @FXML
    private Label brickLabel;
    @FXML
    private Label oreLabel;
    @FXML
    private ImageView victoryPointsImageView;
    @FXML
    private ImageView longestRoadImageView;
    @FXML
    private AnchorPane playerInfoAnchorPane;

    public void setResourceLabels(Map<ResourceType, Integer> resources) {
        this.woolLabel.setText(resources.get(ResourceType.WOOL).toString());
        this.lumberLabel.setText(resources.get(ResourceType.LUMBER).toString());
        this.grainLabel.setText(resources.get(ResourceType.GRAIN).toString());
        this.brickLabel.setText(resources.get(ResourceType.BRICK).toString());
        this.oreLabel.setText(resources.get(ResourceType.ORE).toString());

    }

    public void initPlayerInfo(Player player) {
        try {
            this.playerImageView.setImage(new Image(new FileInputStream("src/main/resources/assets/" + player.getColor() + ".png")));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        this.setPlayerInfo(player);
    }
    public void setPlayerInfo(Player player) {
        this.playerNameLabel.setText(player.getName());
        this.longestRoadLabel.setText(player.getLongestRoad().toString());
        this.victoryPointsLabel.setText(player.getVictoryPoint().toString());
        this.setResourceLabels(player.getResources());
    }

    public void highlight() {
        this.playerInfoAnchorPane.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: red; "
                        + "-fx-border-width: 3px; "
        );
    }
    public void unhighlight() {
        this.playerInfoAnchorPane.setStyle(
                "-fx-background-color: white; " +
                        "-fx-border-color: transparent; " +
                        "-fx-border-width: 3px; "
        );
    }
    public String getPlayerName() {
        return this.playerNameLabel.getText();
    }

    public void unhighlightLongestRoadLabel() {
        this.longestRoadLabel.setTextFill(javafx.scene.paint.Color.BLACK);
    }
    public void highlightLongestRoadLabel() {
        this.longestRoadLabel.setTextFill(javafx.scene.paint.Color.RED);
    }
}
