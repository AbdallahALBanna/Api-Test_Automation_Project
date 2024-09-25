package auth;

import base.BaseTests;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import java.io.File;
import static io.restassured.RestAssured.given;

public class AuthenticationTests extends BaseTests {

    String accessToken;

    @Test
    public void createLoginAuth() {

        File loginCr = new File("src/test/java/auth/LoginCredintials.json");

        Response response = given()
                .spec(requestSpecification)
                .header("Content-Type", "application/json")
                .body(loginCr)
                .when().post(authenticationLoginResource)
                .then().extract().response();
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(response.statusCode(), 201, "Status code is not 201");
        softAssert.assertTrue(response.time() < 2000L, "Response time is greater than 2000ms: " + response.time() + "ms");
        accessToken = response.path("access_token").toString();
        softAssert.assertNotNull(accessToken, "Access Token is null");
        softAssert.assertFalse(accessToken.isEmpty(), "Access Token is empty");
        softAssert.assertAll();
        System.out.println("Test1 Access Token: " + accessToken);
    }

    @Test(dependsOnMethods = {"createLoginAuth"})
    public void getUserProfileAuth() {

        SoftAssert softAssert = new SoftAssert();


        softAssert.assertNotNull(accessToken, "Access Token is not available, login might have failed.");
        System.out.println("Test2 Access Token: " + accessToken);


        Response response =
                given()
                .spec(requestSpecification)
                .auth().oauth2(accessToken)
                .when().get(authenticationProfileResource)
                .then().extract().response();

        softAssert.assertEquals(response.statusCode(), 200, "Status code is not 200");
        softAssert.assertTrue(response.time() < 2000L, "Response time is greater than 2000ms: " + response.time() + "ms");
        softAssert.assertAll();
        System.out.println("Response Body: " + response.getBody().asString());
    }

}
