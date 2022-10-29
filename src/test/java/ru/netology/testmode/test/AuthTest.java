package ru.netology.testmode.test;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.testmode.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.testmode.data.DataGenerator.Registration.getUser;
import static ru.netology.testmode.data.DataGenerator.getRandomLogin;
import static ru.netology.testmode.data.DataGenerator.getRandomPassword;

class AuthTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        $x(".//span[@data-test-id='login']//child::input").val(registeredUser.getLogin());
        $x(".//span[@data-test-id='password']//child::input").val(registeredUser.getPassword());
        $x(".//button[@data-test-id='action-login']").click();
        $x("//h2").should(text("Личный кабинет"));
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        $x(".//span[@data-test-id='login']//child::input").val(notRegisteredUser.getLogin());
        $x(".//span[@data-test-id='password']//child::input").val(notRegisteredUser.getPassword());
        $x(".//button[@data-test-id='action-login']").click();
        $x(".//div[@data-test-id='error-notification']")
                .shouldBe(visible, Duration.ofSeconds(15))
                .should(text("Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        Configuration.holdBrowserOpen = true;
        $x(".//span[@data-test-id='login']//child::input").val(blockedUser.getLogin());
        $x(".//span[@data-test-id='password']//child::input").val(blockedUser.getPassword());
        $x(".//button[@data-test-id='action-login']").click();
        $x(".//div[@data-test-id='error-notification']")
                .shouldBe(visible, Duration.ofSeconds(15))
                .should(text("Пользователь заблокирован"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $x(".//span[@data-test-id='login']//child::input").val(wrongLogin);
        $x(".//span[@data-test-id='password']//child::input").val(registeredUser.getPassword());
        $x(".//button[@data-test-id='action-login']").click();
        $x(".//div[@data-test-id='error-notification']")
                .shouldBe(visible, Duration.ofSeconds(15))
                .should(text("Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $x(".//span[@data-test-id='login']//child::input").val(registeredUser.getLogin());
        $x(".//span[@data-test-id='password']//child::input").val(wrongPassword);
        $x(".//button[@data-test-id='action-login']").click();
        $x(".//div[@data-test-id='error-notification']")
                .shouldBe(visible, Duration.ofSeconds(15))
                .should(text("Неверно указан логин или пароль"));
    }
}
