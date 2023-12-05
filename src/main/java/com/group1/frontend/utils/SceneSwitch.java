package com.group1.frontend.utils;

import com.group1.frontend.controllers.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static com.group1.frontend.constants.BoardConstants.*;


public class SceneSwitch {

    public void switchToScene(Stage stage, Service service, String sceneName) {
        try {
            //TODO: change main class name
            Class<?> c = Class.forName("com.group1.frontend.HelloApplication");
            FXMLLoader loader = new FXMLLoader(c.getResource(sceneName));
            Scene scene = new Scene(loader.load(), WINDOW_WIDTH, WINDOW_HEIGHT);

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
