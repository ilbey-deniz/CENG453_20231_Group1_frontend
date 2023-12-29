package com.group1.frontend.dto;


import com.group1.frontend.enums.PlayerColor;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlayerDto implements HttpRequestDto {
    String name;
    PlayerColor color;
    boolean ready;
    boolean host;
    boolean cpu;
}
