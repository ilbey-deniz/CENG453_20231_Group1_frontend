package com.group1.frontend.controllers;

import javafx.event.ActionEvent;
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
    @Getter
    private TradeViewType tradeViewType;
    @FXML
    public void onPlusButtonClick(ActionEvent event){
        if(event.getSource() == outGrainPlusButton){
            outGrainLabel.setText(String.valueOf(Integer.parseInt(outGrainLabel.getText()) + 1));
        }
        else if(event.getSource() == outOrePlusButton){
            outOreLabel.setText(String.valueOf(Integer.parseInt(outOreLabel.getText()) + 1));
        }
        else if(event.getSource() == outWoolPlusButton){
            outWoolLabel.setText(String.valueOf(Integer.parseInt(outWoolLabel.getText()) + 1));
        }
        else if(event.getSource() == outBrickPlusButton){
            outBrickLabel.setText(String.valueOf(Integer.parseInt(outBrickLabel.getText()) + 1));
        }
        else if(event.getSource() == outLumberPlusButton){
            outLumberLabel.setText(String.valueOf(Integer.parseInt(outLumberLabel.getText()) + 1));
        }
        else if(event.getSource() == inGrainPlusButton){
            inGrainLabel.setText(String.valueOf(Integer.parseInt(inGrainLabel.getText()) + 1));
        }
        else if(event.getSource() == inOrePlusButton){
            inOreLabel.setText(String.valueOf(Integer.parseInt(inOreLabel.getText()) + 1));
        }
        else if(event.getSource() == inWoolPlusButton){
            inWoolLabel.setText(String.valueOf(Integer.parseInt(inWoolLabel.getText()) + 1));
        }
        else if(event.getSource() == inBrickPlusButton){
            inBrickLabel.setText(String.valueOf(Integer.parseInt(inBrickLabel.getText()) + 1));
        }
        else if(event.getSource() == inLumberPlusButton){
            inLumberLabel.setText(String.valueOf(Integer.parseInt(inLumberLabel.getText()) + 1));
        }

    }
    @FXML
    public void onMinusButtonClick(ActionEvent event){
        if(event.getSource() == outGrainMinusButton){
            if(Integer.parseInt(outGrainLabel.getText()) > 0){
                outGrainLabel.setText(String.valueOf(Integer.parseInt(outGrainLabel.getText()) - 1));
            }
        }
        else if(event.getSource() == outOreMinusButton){
            if(Integer.parseInt(outOreLabel.getText()) > 0){
                outOreLabel.setText(String.valueOf(Integer.parseInt(outOreLabel.getText()) - 1));
            }
        }
        else if(event.getSource() == outWoolMinusButton){
            if(Integer.parseInt(outWoolLabel.getText()) > 0){
                outWoolLabel.setText(String.valueOf(Integer.parseInt(outWoolLabel.getText()) - 1));
            }
        }
        else if(event.getSource() == outBrickMinusButton){
            if(Integer.parseInt(outBrickLabel.getText()) > 0){
                outBrickLabel.setText(String.valueOf(Integer.parseInt(outBrickLabel.getText()) - 1));
            }
        }
        else if(event.getSource() == outLumberMinusButton){
            if(Integer.parseInt(outLumberLabel.getText()) > 0){
                outLumberLabel.setText(String.valueOf(Integer.parseInt(outLumberLabel.getText()) - 1));
            }
        }
        else if(event.getSource() == inGrainMinusButton){
            if(Integer.parseInt(inGrainLabel.getText()) > 0){
                inGrainLabel.setText(String.valueOf(Integer.parseInt(inGrainLabel.getText()) - 1));
            }
        }
        else if(event.getSource() == inOreMinusButton){
            if(Integer.parseInt(inOreLabel.getText()) > 0){
                inOreLabel.setText(String.valueOf(Integer.parseInt(inOreLabel.getText()) - 1));
            }
        }
        else if(event.getSource() == inWoolMinusButton){
            if(Integer.parseInt(inWoolLabel.getText()) > 0){
                inWoolLabel.setText(String.valueOf(Integer.parseInt(inWoolLabel.getText()) - 1));
            }
        }
        else if(event.getSource() == inBrickMinusButton){
            if(Integer.parseInt(inBrickLabel.getText()) > 0){
                inBrickLabel.setText(String.valueOf(Integer.parseInt(inBrickLabel.getText()) - 1));
            }
        }
        else if(event.getSource() == inLumberMinusButton){
            if(Integer.parseInt(inLumberLabel.getText()) > 0){
                inLumberLabel.setText(String.valueOf(Integer.parseInt(inLumberLabel.getText()) - 1));
            }
        }
    }
    @FXML
    public void onAcceptButtonClick(ActionEvent event){

    }
    @FXML
    public void onCancelButtonClick(ActionEvent event){

    }
    public void setTradeViewType(TradeViewType tradeViewType){
        this.tradeViewType = tradeViewType;
        if(tradeViewType == TradeViewType.TRADE_OFFER){
            hidePlusMinusButtons();
        }
        else if(tradeViewType == TradeViewType.TRADE_INIT){
            showPlusMinusButtons();
        }
    }
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
    public void zeroizeLabels(){
        outGrainLabel.setText("0");
        outOreLabel.setText("0");
        outWoolLabel.setText("0");
        outBrickLabel.setText("0");
        outLumberLabel.setText("0");
        inGrainLabel.setText("0");
        inOreLabel.setText("0");
        inWoolLabel.setText("0");
        inBrickLabel.setText("0");
        inLumberLabel.setText("0");
    }
}
