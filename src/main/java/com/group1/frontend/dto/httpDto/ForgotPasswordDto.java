package com.group1.frontend.dto.httpDto;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class ForgotPasswordDto implements HttpRequestDto{
    private String email;
}
