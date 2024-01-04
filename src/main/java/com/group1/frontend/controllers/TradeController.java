package com.group1.frontend.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import com.group1.frontend.enums.TradeViewType;
import lombok.Getter;
import lombok.Setter;

//TODO: onButtonClicks
public class TradeController extends Controller{
    @FXML
    private Button outGrainPlusButton;
    @FXML
    private Button outGrainMinusButton;
    @FXML
    private Button outOrePlusButton;
    @FXML
    private Button outOreMinusButton;
    @FXML
    private Button outWoolPlusButton;
    @FXML
    private Button outWoolMinusButton;
    @FXML
    private Button outBrickPlusButton;
    @FXML
    private Button outBrickMinusButton;
    @FXML
    private Button outLumberPlusButton;
    @FXML
    private Button outLumberMinusButton;
    @FXML
    private Button inGrainPlusButton;
    @FXML
    private Button inGrainMinusButton;
    @FXML
    private Button inOrePlusButton;
    @FXML
    private Button inOreMinusButton;
    @FXML
    private Button inWoolPlusButton;
    @FXML
    private Button inWoolMinusButton;
    @FXML
    private Button inBrickPlusButton;
    @FXML
    private Button inBrickMinusButton;
    @FXML
    private Button inLumberPlusButton;
    @FXML
    private Button inLumberMinusButton;
    @FXML
    private Label outGrainLabel;
    @FXML
    private Label outOreLabel;
    @FXML
    private Label outWoolLabel;
    @FXML
    private Label outBrickLabel;
    @FXML
    private Label outLumberLabel;
    @FXML
    private Label inGrainLabel;
    @FXML
    private Label inOreLabel;
    @FXML
    private Label inWoolLabel;
    @FXML
    private Label inBrickLabel;
    @FXML
    private Label inLumberLabel;
    @FXML
    private Button acceptButton;
    @FXML
    private Button cancelButton;

    @Setter
    @Getter
    private TradeViewType tradeViewType;


    public void hidePlusMinusButtons(){
        outGrainPlusButton.setVisible(false);
        outGrainMinusButton.setVisible(false);
        outOrePlusButton.setVisible(false);
        outOreMinusButton.setVisible(false);
        outWoolPlusButton.setVisible(false);
        outWoolMinusButton.setVisible(false);
        outBrickPlusButton.setVisible(false);
        outBrickMinusButton.setVisible(false);
        outLumberPlusButton.setVisible(false);
        outLumberMinusButton.setVisible(false);
        inGrainPlusButton.setVisible(false);
        inGrainMinusButton.setVisible(false);
        inOrePlusButton.setVisible(false);
        inOreMinusButton.setVisible(false);
        inWoolPlusButton.setVisible(false);
        inWoolMinusButton.setVisible(false);
        inBrickPlusButton.setVisible(false);
        inBrickMinusButton.setVisible(false);
        inLumberPlusButton.setVisible(false);
        inLumberMinusButton.setVisible(false);
    }
    public void showPlusMinusButtons(){
        outGrainPlusButton.setVisible(true);
        outGrainMinusButton.setVisible(true);
        outOrePlusButton.setVisible(true);
        outOreMinusButton.setVisible(true);
        outWoolPlusButton.setVisible(true);
        outWoolMinusButton.setVisible(true);
        outBrickPlusButton.setVisible(true);
        outBrickMinusButton.setVisible(true);
        outLumberPlusButton.setVisible(true);
        outLumberMinusButton.setVisible(true);
        inGrainPlusButton.setVisible(true);
        inGrainMinusButton.setVisible(true);
        inOrePlusButton.setVisible(true);
        inOreMinusButton.setVisible(true);
        inWoolPlusButton.setVisible(true);
        inWoolMinusButton.setVisible(true);
        inBrickPlusButton.setVisible(true);
        inBrickMinusButton.setVisible(true);
        inLumberPlusButton.setVisible(true);
        inLumberMinusButton.setVisible(true);
    }
}
