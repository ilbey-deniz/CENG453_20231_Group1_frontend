package com.group1.frontend.utils;

import java.util.Map;
import java.util.Random;

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

    public static String mapIntToNumberAsset(Integer number){
        return "src/main/resources/assets/" + number.toString() + ".png";
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

        return "src/main/resources/assets/" + buildingType + getRandomKey(PLAYER_COLORS) + ".png";
    }
}
