package com.group1.frontend.dto.websocketDto;

import com.group1.frontend.utils.LobbyPlayer;
import lombok.Data;


@Data
public class KickPlayerDto {
    private LobbyPlayer player;

}
