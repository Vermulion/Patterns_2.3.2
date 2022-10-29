package ru.netology.testmode.data;


import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class RegistrationInfo {
    private final String login;
    private final String password;
    private final String status;
}
