package com.group1.frontend.controllers;
import com.group1.frontend.utils.Score;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import org.json.*;

import java.net.http.HttpResponse;

public class LeaderboardController extends Controller{
    @FXML
    private TableView<Score> leaderboardTable;
    @FXML
    private TableColumn<Score, String> usernameColumn;
    @FXML
    private TableColumn<Score, String> scoreColumn;

    @FXML
    private ToggleButton allButton;
    @FXML
    private ToggleButton weeklyButton;
    @FXML
    private ToggleButton monthlyButton;


    @Override
    public void init() {

        usernameColumn.setCellValueFactory(
                new PropertyValueFactory<Score, String>("username")
        );
        scoreColumn.setCellValueFactory(
                new PropertyValueFactory<Score, String>("score")
        );
        HttpResponse<String> response = service.makeRequestWithToken(
                "/get/topScorers/all",
                "GET",
                "");

            if(response.statusCode() == 200) {
                JSONArray scores = new JSONArray(response.body());
                addJsonToTable(scores);
            }
            else {
                System.out.println(response.body());
            }
    }

    @FXML
    protected void onBackButtonClick() {
        sceneSwitch.switchToScene(stage, service, "menu-view.fxml");
    }

    @FXML
    protected void onAllButtonClick() {
        weeklyButton.setSelected(false);
        monthlyButton.setSelected(false);
        leaderboardTable.getItems().clear();
        HttpResponse<String> response = service.makeRequestWithToken(
                "/get/topScorers/all",
                "GET",
                "");

        if(response.statusCode() == 200) {
            JSONArray scores = new JSONArray(response.body());
            addJsonToTable(scores);
        }
        else {
            System.out.println(response.body());
            sceneSwitch.switchToScene(stage, service, "login-view.fxml");
        }
    }
    @FXML
    protected void onWeeklyButtonClick() {
        allButton.setSelected(false);
        monthlyButton.setSelected(false);
        leaderboardTable.getItems().clear();
        HttpResponse<String> response = service.makeRequestWithToken(
                "/get/topScorers/weekly",
                "GET",
                "");
        if(response.statusCode() == 200) {
            JSONArray scores = new JSONArray(response.body());
            addJsonToTable(scores);
        }
        else {
            System.out.println(response.body());
            sceneSwitch.switchToScene(stage, service, "login-view.fxml");
        }
    }
    @FXML
    protected void onMonthlyButtonClick() {
        allButton.setSelected(false);
        weeklyButton.setSelected(false);
        leaderboardTable.getItems().clear();
        HttpResponse<String> response = service.makeRequestWithToken(
                "/get/topScorers/monthly",
                "GET",
                "");
        if(response.statusCode() == 200) {
            JSONArray scores = new JSONArray(response.body());
            addJsonToTable(scores);
        }
        else {
            System.out.println(response.body());
            sceneSwitch.switchToScene(stage, service, "login-view.fxml");
        }
    }

    private void addJsonToTable(JSONArray scores) {
        for(int i = 0; i < scores.length(); i++) {
            JSONObject jsonScore = scores.getJSONObject(i);
            String username = jsonScore.getString("name");
            int scoreValue = jsonScore.getInt("totalScore");
            Score score = new Score(username, scoreValue);
            leaderboardTable.getItems().add(score);
        }
    }
}
