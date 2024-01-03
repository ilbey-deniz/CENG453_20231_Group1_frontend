package com.group1.frontend.dto.websocketDto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.group1.frontend.components.Board;
import com.group1.frontend.components.Building;
import com.group1.frontend.components.Game;
import com.group1.frontend.components.Player;
import com.group1.frontend.utils.IntegerPair;
import lombok.Data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Data
@JsonTypeName("INITIAL_GAME")
public class GameDto implements WebSocketDto{
    private List<Player> players;
    private Board board;
    private Player currentPlayer;
    private int turnNumber;
    private IntegerPair currentDiceRoll;
}
