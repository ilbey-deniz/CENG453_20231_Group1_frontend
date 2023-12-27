package com.group1.frontend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class ScoreDto implements HttpRequestDto{
    private String name;
    private int score;
}
