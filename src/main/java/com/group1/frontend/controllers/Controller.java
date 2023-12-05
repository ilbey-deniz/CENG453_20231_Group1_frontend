package com.group1.frontend.controllers;

import com.group1.frontend.utils.SceneSwitch;
import com.group1.frontend.utils.Service;
import javafx.stage.Stage;

public class Controller {
    public Service service;
    protected Stage stage;
    protected SceneSwitch sceneSwitch = new SceneSwitch();

    public void setService(Service service) {
        this.service = service;
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    public void construct(Stage stage, Service service) {
        this.stage = stage;
        this.service = service;
    }
    //override this method in each controller, works as a custom initializer
    public void init() {}
}
