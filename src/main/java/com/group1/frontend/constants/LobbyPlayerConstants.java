package com.group1.frontend.constants;

import com.group1.frontend.enums.PlayerColor;

import java.util.HashMap;
import java.util.Random;
import java.util.stream.Stream;

public class LobbyPlayerConstants {
    public static HashMap<String, Boolean> CPU_NAMES =  Stream.of(
            "AlphaZero", "LeelaZero", "Stockfish", "Komodo", "Houdini",
            "Fritz", "Rybka", "DeepFritz", "Shredder", "Firebird"
    ).collect(HashMap::new, (m, v) -> m.put(v, false), HashMap::putAll);

    public static HashMap<PlayerColor, Boolean> LOBBY_PLAYER_COLORS = Stream.of(
            PlayerColor.red, PlayerColor.blue, PlayerColor.yellow, PlayerColor.green
    ).collect(HashMap::new, (m, v) -> m.put(v, false), HashMap::putAll);

    public static PlayerColor getRandomAvailableColor() {
        Random random = new Random();
        PlayerColor color = null;
        for (int i = 0; i < 10; i++){
            color = PlayerColor.values()[random.nextInt(PlayerColor.values().length)];
            if (!LOBBY_PLAYER_COLORS.get(color)) {
                LOBBY_PLAYER_COLORS.replace(color, true);
                return color;
            }
        }
        return color;
    }

    public static String getRandomAvailableCpuName() {
        Random random = new Random();
        String name = null;
        for (int i = 0; i < CPU_NAMES.size(); i++) {
            name = CPU_NAMES.keySet().toArray()[random.nextInt(CPU_NAMES.size())].toString();
            if (!CPU_NAMES.get(name)) {
                CPU_NAMES.replace(name, true);
                return name;
            }
        }
        return name;
    }
}
