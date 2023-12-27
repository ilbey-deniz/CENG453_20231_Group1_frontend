package com.group1.frontend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class ChangePasswordDto implements HttpRequestDto {
    private String name;
    private String oldPassword;
    private String newPassword;
}
