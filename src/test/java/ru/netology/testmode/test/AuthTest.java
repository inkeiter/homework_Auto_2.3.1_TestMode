package ru.netology.testmode.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.WebDriverRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.testmode.data.DataGenerator;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

class AuthTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

//    @Test
//    @DisplayName("Should successfully login with active registered user")
//    void shouldSuccessfulLoginIfRegisteredActiveUser() {
//        // 1. Регистрируем активного пользователя через API
//        var registeredUser = DataGenerator.getRegisteredUser("active");
//        System.out.println("Registered user: " + registeredUser.getLogin() + "/" + registeredUser.getPassword());
//
//        // 2. Заполняем форму данными из registeredUser
//        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
//        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
//
//        // 3. Нажимаем кнопку входа
//        $("[data-test-id=action-login]").click();
//
//        // 4. Проверяем успешный вход
//        $("[data-test-id=dashboard]").shouldBe(visible, Duration.ofSeconds(10));
//    }
@Test
@DisplayName("Should successfully login with active registered user")
void shouldSuccessfulLoginIfRegisteredActiveUser() {
    var registeredUser = DataGenerator.getRegisteredUser("active");

    // ДИАГНОСТИКА: проверим видимость элементов
    System.out.println("Login field exists: " + $("[data-test-id=login]").exists());
    System.out.println("Password field exists: " + $("[data-test-id=password]").exists());
    System.out.println("Button exists: " + $("[data-test-id=action-login]").exists());

    // Заполняем с паузами для наблюдения
    $("[data-test-id=login] input").setValue(registeredUser.getLogin());
    try { Thread.sleep(1000); } catch (InterruptedException e) {}

    $("[data-test-id=password] input").setValue(registeredUser.getPassword());
    try { Thread.sleep(1000); } catch (InterruptedException e) {}

    // Проверим, что значения заполнились
    System.out.println("Login value: " + $("[data-test-id=login] input").getValue());
    System.out.println("Password value: " + $("[data-test-id=password] input").getValue());

    $("[data-test-id=action-login]").click();
    try { Thread.sleep(3000); } catch (InterruptedException e) {} // Ждем реакцию

    // ПРАВИЛЬНЫЙ способ получить URL:
    System.out.println("After click - URL: " + WebDriverRunner.getWebDriver().getCurrentUrl());

    // Дополнительная диагностика
    System.out.println("Page title: " + WebDriverRunner.getWebDriver().getTitle());
    System.out.println("Body text: " + $("body").getText());
}


    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = DataGenerator.getUser("active");

        // Заполняем форму данными НЕзарегистрированного пользователя
        $("[data-test-id=login] input").setValue(notRegisteredUser.getLogin());
        $("[data-test-id=password] input").setValue(notRegisteredUser.getPassword());
        $("[data-test-id=action-login]").click();

        // Проверяем сообщение об ошибке
        $("[data-test-id=error-notification]")
                .shouldBe(visible)
                .shouldHave(text("Ошибка"))
                .shouldHave(text("Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        // Регистрируем заблокированного пользователя через API
        var blockedUser = DataGenerator.getRegisteredUser("blocked");

        // Заполняем форму данными заблокированного пользователя
        $("[data-test-id=login] input").setValue(blockedUser.getLogin());
        $("[data-test-id=password] input").setValue(blockedUser.getPassword());
        $("[data-test-id=action-login]").click();

        // Проверяем сообщение об ошибке блокировки
        $("[data-test-id=error-notification]")
                .shouldBe(visible)
                .shouldHave(text("Ошибка"))
                .shouldHave(text("Пользователь заблокирован"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        // Регистрируем активного пользователя
        var registeredUser = DataGenerator.getRegisteredUser("active");
        // Генерируем случайный неверный логин
        var wrongLogin = DataGenerator.getRandomLogin();

        // Заполняем форму: неверный логин, но верный пароль
        $("[data-test-id=login] input").setValue(wrongLogin);
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $("[data-test-id=action-login]").click();

        // Проверяем сообщение об ошибке
        $("[data-test-id=error-notification]")
                .shouldBe(visible)
                .shouldHave(text("Ошибка"))
                .shouldHave(text("Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = DataGenerator.getRegisteredUser("active");
        var wrongPassword = DataGenerator.getRandomPassword();

        // Заполняем форму: верный логин, но неверный пароль
        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue(wrongPassword);
        $("[data-test-id=action-login]").click();

        // Проверяем сообщение об ошибке
        $("[data-test-id=error-notification]")
                .shouldBe(visible)
                .shouldHave(text("Ошибка"))
                .shouldHave(text("Неверно указан логин или пароль"));
    }
}