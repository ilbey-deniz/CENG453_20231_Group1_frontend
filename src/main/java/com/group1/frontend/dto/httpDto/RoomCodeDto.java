package com.group1.frontend.dto.httpDto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Data
public class RoomCodeDto implements HttpRequestDto{
    String roomCode;
}
