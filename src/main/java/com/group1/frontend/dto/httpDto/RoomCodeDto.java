package com.group1.frontend.dto.httpDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RoomCodeDto implements HttpRequestDto{
    String roomCode;
}
