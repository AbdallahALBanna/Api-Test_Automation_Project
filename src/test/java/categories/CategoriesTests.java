package categories;

import base.BaseTests;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.File;

import static io.restassured.RestAssured.given;

public class CategoriesTests extends BaseTests {

    @Test
    public void getAllCategories(){
        Response response= given().spec(requestSpecification)
                .when().get(categoryResource)
                .then().extract().response();
        SoftAssert softAssert=new SoftAssert();
        softAssert.assertEquals(response.statusCode(),200,"Status code incorrect");
        softAssert.assertTrue(response.time()<=3000L,"Categories Response time longer that 3 seconds: " + response.time() + "ms");
        softAssert.assertAll();
    }

    @Test
    public void getSingleCategories(){
        int categoryId=1;
        Response response= given().spec(requestSpecification)
                .when().get(categoryResource+categoryId)
                .then().extract().response();
        SoftAssert softAssert=new SoftAssert();
        softAssert.assertEquals((int)response.path("id"),categoryId,"Incorrect functionality");
        softAssert.assertEquals(response.statusCode(),200,"Status code incorrect");
        softAssert.assertTrue(response.time()<=3000L,"Categories Response time longer that 3 seconds: " + response.time() + "ms");
        softAssert.assertAll();
    }

    @Test
    public void postCreateCategory(){
        File data=new File("src/test/java/categories/postCategoryData.json");
        Response response= given().spec(requestSpecification).contentType(ContentType.JSON).body(data)
                .when().post(categoryResource)
                .then().extract().response();
        SoftAssert softAssert=new SoftAssert();
        softAssert.assertEquals(response.statusCode(),201,"Status code incorrect");
        softAssert.assertTrue(response.time()<=3000L,"Categories Response time longer that 3 seconds: " + response.time() + "ms");
        softAssert.assertAll();
    }

    @Test
    public void putUpdateCategory(){
        int categoryId=25;
        File data=new File("src/test/java/categories/putCategoryData.json");
        Response response= given().spec(requestSpecification).contentType(ContentType.JSON).body(data)
                .when().put(categoryResource+categoryId)
                .then().extract().response();
        SoftAssert softAssert=new SoftAssert();
        softAssert.assertEquals(response.statusCode(),200,"Status code incorrect");
        softAssert.assertTrue(response.time()<=3000L,"Products_Filter Response time longer that 3 seconds: " + response.time() + "ms");
        response= given().spec(requestSpecification)
                .when().get(categoryResource+categoryId)
                .then().extract().response();
        softAssert.assertEquals(response.path("name"),"nuevo","No update happen");
        softAssert.assertAll();
    }

    @Test
    public void deleteCategory(){
        int categoryId=26;
        Response response= given().spec(requestSpecification)
                .when().delete(categoryResource+categoryId)
                .then().extract().response();
        SoftAssert softAssert=new SoftAssert();
        softAssert.assertEquals(response.statusCode(),200,"Status code incorrect");
        softAssert.assertTrue(response.time()<=3000L,"Categories Response time longer that 3 seconds: " + response.time() + "ms");
        response= given().spec(requestSpecification)
                .when().get(categoryResource+categoryId)
                .then().extract().response();
        softAssert.assertEquals(response.path("name"),"EntityNotFoundError","Delete not happen");
        softAssert.assertEquals(response.statusCode(),400,"Delete not happen");
        softAssert.assertAll();
    }

}
