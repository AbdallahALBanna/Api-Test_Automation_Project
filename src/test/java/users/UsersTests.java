package users;

import base.BaseTests;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.Collections;
import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class UsersTests extends BaseTests {

    int createdUserID;

    @Test
    public void getAllUsers(){
        Response res = given().
                            spec(requestSpecification).
                        when().
                            get(usersResource).
                        then().
                            log().all().
                            extract().response();

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(res.statusCode(), 200, "Users_Get_All users are not found successfully. status code: " + res.statusCode());
        softAssert.assertTrue(res.time() <= 3000L, "Users_Get_All Response time longer that 3 seconds: " + res.time() + "ms");
        softAssert.assertTrue(res.contentType().contains("json"), "Users_Get_All Content type is not json");
        softAssert.assertAll();
    }

    @Test
    public void getUserByID(){
        int userID = 2;
        Response res = given().
                            spec(requestSpecification).
                        when().
                            get(usersResource + userID).
                        then().
                            log().body().
                            extract().response();

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(res.statusCode(), 200, "Users_Get_One user is not found successfully. status code: " + res.statusCode());
        softAssert.assertEquals((int)res.path("id"), userID, "Users_Get_One user ID is not correct. user ID: " + res.path("id"));
        softAssert.assertTrue(res.time() <= 3000L, "Users_Get_One Response time longer that 3 seconds: " + res.time() + "ms");
        softAssert.assertTrue(res.contentType().contains("json"), "Users_Get_One Content type is not json");
        softAssert.assertAll();
    }

    @Test
    public void createUser(){
        int expectedStatusCode = 201;
        HashMap<String, Object> requestBody = buildUserBody("moaz", "moaz@mail.com", "1234", "https://picsum.photos/800");

        Response res = given().
                            spec(requestSpecification).
                            contentType(ContentType.JSON).
                            body(requestBody).
                        when().
                            post(usersResource).
                        then().
//                            assertThat().statusCode(201).
//                            log().body().
                            extract().response();

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(res.statusCode(), expectedStatusCode, "Users_Create User is not created successfully. status code: " + res.statusCode());
        softAssert.assertTrue(res.time() <= 3000L, "Users_Create Response time is longer than 3 second: " + res.time() + "ms");
        softAssert.assertAll();
        System.out.println((int)res.path("id"));
        createdUserID = res.path("id");
    }

    @Test
    public void updateUser(){
        int expectedStatusCode = 200;
//        int updatedUserID = createdUserID;
        int updatedUserID = 44;
        String newMail = "newEmail@mail.com";
        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("email", newMail);

        Response res = given().
                            spec(requestSpecification).
                            contentType(ContentType.JSON).
                            body(requestBody).
                        when().
                            put(usersResource + updatedUserID).
                        then().
                            log().body().
                            assertThat().statusCode(expectedStatusCode).
                            extract().response();
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(res.statusCode(), expectedStatusCode, "Update_User User is not updated successfully. status code: " + res.statusCode());
        softAssert.assertEquals(res.path("email"), newMail, "Update_User User is not updated correctly. status code: " + res.statusCode());
        softAssert.assertTrue(res.time() <= 3000L, "Update_User Response time is longer than 3 second: " + res.time() + "ms");
        softAssert.assertAll();
    }

    @Test
    public void checkEmailIsAvailable(){
        int expectedStatusCode = 201;
        String checkEmailAvailabilityResource = "is-available";
        String mailToCheck = "newMail@mail.com";
        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("email", mailToCheck);
        Response res = given().
                            spec(requestSpecification).
                            contentType(ContentType.JSON).
                            body(requestBody).
                        when().
                            post(usersResource + checkEmailAvailabilityResource).
                        then().
                            log().body().
                            assertThat().statusCode(expectedStatusCode).
                            extract().response();
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(res.statusCode(), expectedStatusCode, "Check_Email_Availability Check is not done successfully. status code: " + res.statusCode());
        softAssert.assertTrue(res.time() <= 3000L, "Check_Email_Availability Response time is longer than 3 second: " + res.time() + "ms");
        softAssert.assertAll();
    }

    private HashMap<String, Object> buildUserBody(String name, String email, String password, String avatar){
        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", name);
        requestBody.put("email", email);
        requestBody.put("password", password);
        requestBody.put("avatar", avatar);
        return requestBody;
    }
}
