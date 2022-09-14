package org.example;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;

public class AppTest {

    @Test
    public void getAllUsersTest() {
        RequestSpecification requestSpecification = RestAssured.given();
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        requestSpecification.headers(headers);

        Response response = requestSpecification.expect().statusCode(SC_OK)
                .when().get("https://gorest.co.in/public/v2/users");

        List<User> users = response.jsonPath().getList("", User.class);
        Assert.assertEquals(users.size(), 10, "Expected size is not as actual");
    }

    @Test
    public void createUserTest() {
        RequestSpecification requestSpecification =
                RestAssured.given().auth().oauth2("0d5c390179585173234b56a80ea701f295cb0c8460847698c3263d14e63360c8");
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        requestSpecification.headers(headers);

        User exptectedUser = createUser();
        Response createdUserResponse = requestSpecification.body(exptectedUser).expect().statusCode(SC_CREATED)
                .when().post("https://gorest.co.in/public/v2/users");
        User actualUser = createdUserResponse.as(User.class);
        Assert.assertEquals(actualUser.getName(), exptectedUser.getName(),
                "Expected user doesn't have correct name");
    }

    private User createUser() {
        Random random = new Random();
        User user = new User();
        user.setName("test" + random.nextInt());
        user.setEmail("testEmail" + random.nextInt() + "@gmail.com");
        user.setGender("Male");
        user.setStatus("active");

        return user;
    }
}
