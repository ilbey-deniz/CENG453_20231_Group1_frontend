package com.group1.frontend.controllers;

import com.group1.frontend.utils.SceneSwitch;
import com.group1.frontend.utils.Service;
import javafx.stage.Stage;
import lombok.Setter;

public class Controller {
    @Setter
    public Service service;
    @Setter
    protected Stage stage;
    protected SceneSwitch sceneSwitch = new SceneSwitch();

    public void construct(Stage stage, Service service) {
        this.stage = stage;
        this.service = service;
    }
    //override this method in each controller, works as a custom initializer
    public void init() {}
}
