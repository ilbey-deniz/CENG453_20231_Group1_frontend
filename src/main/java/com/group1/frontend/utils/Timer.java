package com.group1.frontend.utils;

import com.group1.frontend.events.TimeEvent;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static java.time.Duration.ofSeconds;

import javafx.scene.layout.AnchorPane;

public class Timer extends AnchorPane{
    ObjectProperty<Duration> remainingSeconds;
    Timeline leftTime;

    public Timer(int seconds) {
        remainingSeconds = new SimpleObjectProperty<>(ofSeconds(seconds));
        javafx.util.Duration oneSecond = javafx.util.Duration.seconds(1);
        leftTime = new Timeline(new KeyFrame(oneSecond, (ActionEvent event) -> {
            remainingSeconds.setValue(remainingSeconds.get().minus(1, ChronoUnit.SECONDS));
            TimeEvent oneTickEvent = new TimeEvent((int) remainingSeconds.get().getSeconds());
            getParent().fireEvent(oneTickEvent);
        }));
        leftTime.setCycleCount((int) remainingSeconds.get().getSeconds());
        leftTime.setOnFinished((ActionEvent event) -> {
            TimeEvent timesUpEvent = new TimeEvent();
            getParent().fireEvent(timesUpEvent);
            leftTime.stop();
        });

    }
    public void start() {
        leftTime.play();
    }

}
