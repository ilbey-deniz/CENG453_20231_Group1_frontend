package com.group1.frontend.utils;

import java.util.Map;
import java.util.Random;

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
}
