package ru.netology.test;

import org.junit.jupiter.api.*;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.data.DataGenerator.Registration.getUser;
import static ru.netology.data.DataGenerator.getRandomLogin;
import static ru.netology.data.DataGenerator.getRandomPassword;

class AuthTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");

        $("[data-test-id = login] input").setValue(registeredUser.getLogin());
        $("[data-test-id = password] input").setValue(registeredUser.getPassword());
        $("form button").click();

        $("h2").shouldBe(visible, Duration.ofSeconds(10)).shouldHave(text("Личный кабинет"));
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");

        $("[data-test-id = login] input").setValue(notRegisteredUser.getLogin());
        $("[data-test-id = password] input").setValue(notRegisteredUser.getPassword());
        $("form button").click();

        $("[data-test-id = error-notification]").shouldBe(visible, Duration.ofSeconds(10)).shouldHave(text("Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");

        $("[data-test-id = login] input").setValue(blockedUser.getLogin());
        $("[data-test-id = password] input").setValue(blockedUser.getPassword());
        $("form button").click();

        $("[data-test-id = error-notification]").shouldBe(visible, Duration.ofSeconds(10)).shouldHave(text("Пользователь заблокирован"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();

        $("[data-test-id = login] input").setValue(wrongLogin);
        $("[data-test-id = password] input").setValue(registeredUser.getPassword());
        $("form button").click();

        $("[data-test-id = error-notification]").shouldBe(visible, Duration.ofSeconds(10)).shouldHave(text("Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();

        $("[data-test-id = login] input").setValue(registeredUser.getLogin());
        $("[data-test-id = password] input").setValue(wrongPassword);
        $("form button").click();

        $("[data-test-id = error-notification]").shouldBe(visible, Duration.ofSeconds(10)).shouldHave(text("Неверно указан логин или пароль"));
    }
}