package com.group1.frontend.utils;

import com.group1.frontend.constants.ApplicationConstants;
import com.group1.frontend.controllers.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static com.group1.frontend.constants.BoardConstants.*;


public class SceneSwitch {

    public void switchToScene(Stage stage, Service service, String sceneName) {
        try {
            Class<?> c = Class.forName(ApplicationConstants.MAIN_CLASS);
            FXMLLoader loader = new FXMLLoader(c.getResource(sceneName));
            Scene scene;
            if(sceneName.equals("board-view.fxml")) {
                scene = new Scene(loader.load(), WINDOW_WIDTH, WINDOW_HEIGHT);
                stage.setMaximized(true);
            }
            else {
                scene = new Scene(loader.load(), DEFAULT_WIDTH, DEFAULT_HEIGHT);
                stage.setMaximized(false);
            }

            Controller controller = loader.getController();
            controller.construct(stage, service);
            controller.init();

            stage.setScene(scene);
            stage.show();
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
