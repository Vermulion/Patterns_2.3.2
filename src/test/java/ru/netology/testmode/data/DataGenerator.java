package ru.netology.testmode.data;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;
import lombok.val;

import java.util.Locale;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

public class DataGenerator {
    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();
    private static final Faker faker = new Faker(new Locale("en"));

    private DataGenerator() {
    }

    private static void sendRequest(RegistrationInfo user) {
        given()
                .spec(requestSpec)
                .body(new RegistrationInfo(
                        user.getLogin(),
                        user.getPassword(),
                        user.getStatus()
                ))
                .when()
                    .post("/api/system/users")
                .then()
                    .statusCode(200);
    }

    public static String getRandomLogin() {
        Faker faker = new Faker();
        String login = faker.name().username();
        return login;
    }

    public static String getRandomPassword() {
        Faker faker = new Faker();
        String password = faker.internet().password();
        return password;
    }

    public static class Registration {
        private Registration() {
        }

        public static RegistrationInfo getUser(String status) {
           return new RegistrationInfo(getRandomLogin(), getRandomPassword(), status);
        }

        public static RegistrationInfo getRegisteredUser(String status) {
            RegistrationInfo registeredUser = getUser(status);
            sendRequest(registeredUser);
            return registeredUser;
        }
    }
}
