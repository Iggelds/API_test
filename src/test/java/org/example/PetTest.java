package org.example;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.example.log.Log;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;

public class PetTest {

    private static final String BASE_URL = "https://petstore.swagger.io/v2/";

    @Test
    public void postPetTest() {
        Pet pet = new Pet("Toby");

        Response response = request().body(pet).expect().statusCode(SC_OK).log().ifError()
                .when().post(BASE_URL + "pet");

        Pet createdPet = response.jsonPath().getObject("", Pet.class);
        Assert.assertTrue(createdPet.getId() > 0, "Expected id exists");
        Assert.assertEquals(createdPet.getName(), "Toby", "Expected valid name");
    }

    @Test
    public void postWithFormPetTest() {
        Pet pet = new Pet("Toby");

        Response response = request().body(pet).expect().statusCode(SC_OK).log().ifError()
                .when().post(BASE_URL + "pet");

        Pet createdPet = response.jsonPath().getObject("", Pet.class);
        RequestSpecification requestSpecification = request().header("Content-Type", "application/x-www-form-urlencoded");
        Map<String, String> params = new HashMap<>();
        params.put("name", "Barsik");

        requestSpecification.formParams(params).expect().statusCode(SC_OK).log().ifError()
                .when().post(BASE_URL + "pet/" + createdPet.getId());

        response = request().expect().statusCode(SC_OK).log().ifError()
                .when().get(BASE_URL + "pet/" + createdPet.getId());
        Pet newPet = response.jsonPath().getObject("", Pet.class);
        Assert.assertTrue(newPet.getId() > 0, "Expected id exists");
        Assert.assertEquals(newPet.getName(), "Barsik", "Expected valid name");
    }

    @Test
    public void putPetTest() {
        Pet pet = new Pet("Toby");

        Response response = request().body(pet).expect().statusCode(SC_OK).log().ifError()
                .when().post(BASE_URL + "pet");

        Pet createdPet = response.jsonPath().getObject("", Pet.class);
        createdPet.setName("Barsik");
        response = request().body(createdPet).expect().statusCode(SC_OK).log().ifError()
                .when().put(BASE_URL + "pet");
        Pet newPet = response.jsonPath().getObject("", Pet.class);
        Assert.assertEquals(newPet.getName(), "Barsik", "Expected valid name");
    }

    @Test
    public void deletePetTest() {
        Pet pet = new Pet("Toby");

        Response response = request().body(pet).expect().statusCode(SC_OK).log().ifError()
                .when().post(BASE_URL + "pet");

        Pet createdPet = response.jsonPath().getObject("", Pet.class);
        String petUrl = BASE_URL + "pet/" + createdPet.getId();
        request().when().delete(petUrl).then().statusCode(SC_OK).log().ifError();
        request().when().get(petUrl).then().statusCode(SC_NOT_FOUND).log().ifError();
        Log.info("Response status code is " + response.statusCode());
        Log.info("Response body is " + response.asPrettyString());
    }

    private static RequestSpecification request() {
        RequestSpecification requestSpecification = RestAssured.given();
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        headers.put("api-key", "key");
        return requestSpecification.headers(headers);
    }
}
