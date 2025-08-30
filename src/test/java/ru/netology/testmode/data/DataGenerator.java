package ru.netology.testmode.data;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {

    @Value
    public static class RegistrationDto {
        String login;
        String password;
        String status;
    }

    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();
    private static final Faker faker = new Faker(new Locale("ru"));

    private DataGenerator() {
    }

    private static void sendRequest(RegistrationDto user) {
        given() // "дано"
                .spec(requestSpec) // используем подготовленную спецификацию
                .body(user)        // передаем в теле объект user
                .when() // "когда"
                .post("/api/system/users") // отправляем POST-запрос
                .then() // "тогда"
                .statusCode(200); // проверяем, что сервер вернул 200 OK
    }

    public static String getRandomLogin() {
        String login = faker.name().username(); // Генерирует логин на русском
        return login;
    }

    public static String getRandomPassword() {
        String password = faker.internet().password();
        return password;
    }

        public static RegistrationDto getUser(String status) {
            RegistrationDto user = new RegistrationDto(getRandomLogin(), getRandomPassword(), status);
            return user;
        }

        public static RegistrationDto getRegisteredUser(String status) {
            // Объявляем переменную registeredUser и присваиваем ей значение возвращённое getUser(status)
            RegistrationDto registeredUser = getUser(status);

            // Посылаем запрос на регистрацию пользователя
            sendRequest(registeredUser);

            return registeredUser;
        }
    }