package com.group1.frontend.dto.httpDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class TopScoreUserDto implements HttpRequestDto{
    private String name;
    private BigDecimal totalScore;
}
