package com.group1.frontend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class DestroyGameDto implements HttpRequestDto{
    String roomCode;
}
