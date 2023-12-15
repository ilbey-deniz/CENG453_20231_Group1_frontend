package com.group1.frontend.utils;

import com.group1.frontend.MainApplication;
import javafx.scene.image.Image;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Map;
import java.util.Random;
import java.io.FileInputStream;

import static com.group1.frontend.constants.BoardConstants.PLAYER_COLORS;

public class BoardUtilityFunctions {

    public static <K, V> K getRandomKey(Map<K, V> map) {
        if (map.isEmpty()) {
            return null; // Handle the case when the map is empty
        }

        // Convert the keys to an array
        Object[] keysArray = map.keySet().toArray();

        // Get a random index
        int randomIndex = new Random().nextInt(keysArray.length);

        // Retrieve the random key
        return (K) keysArray[randomIndex];
    }

    public static URL mapIntToNumberAsset(Integer number){
        return MainApplication.class.getResource("/assets/"+ number +".png");
    }

    public static String getRandomColor(){
        return PLAYER_COLORS.get(getRandomKey(PLAYER_COLORS));
    }

    public static String getRandomBuildingAsset(){
        Random random = new Random();
        String buildingType;
        if(random.nextBoolean()){
            buildingType = "house_";
        }
        else{
            buildingType = "city_";
        }
        return MainApplication.class.getResource("/assets/" + buildingType + getRandomKey(PLAYER_COLORS) + ".png").toString();
    }
    public static URL getSettlementAsset(String color){
        return MainApplication.class.getResource("/assets/house_" + color + ".png");
    }
    public static String getCityAsset(String color){
        return MainApplication.class.getResource("/assets/city_" + color + ".png").toString();
    }

    public static Image getDiceImage(int diceRoll) throws FileNotFoundException {
        String diceType = "dice_" + diceRoll;
        diceType = MainApplication.class.getResource("/assets/" + diceType + ".png").toString();
        return new Image(diceType);
    }

    public static String secondsToTime(int seconds){
        int minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }
}
