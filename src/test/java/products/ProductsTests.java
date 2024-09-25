package products;

import base.BaseTests;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.lessThan;

public class ProductsTests extends BaseTests {

    public int[] allProductsIDs;
    public int createdProductID;

    @Test(priority = 1)
    public void getAllProducts(){
        Response res = given().
                            spec(requestSpecification).
                        when().
                            get(productsResource).
                        then().
//                            log().all().
//                            assertThat().time(lessThan(1000L)).
//                            assertThat().statusCode(200).
//                            assertThat().contentType(ContentType.JSON).
                            extract().response();
        allProductsIDs = res.jsonPath().getObject("id", int[].class);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(res.statusCode(), 200, "Products_Get_All Products is not found successfully. status code: " + res.statusCode());
        softAssert.assertTrue(res.time() <= 1000L, "Products_Get_All Response time longer that 1 seconds: " + res.time() + "ms");
        softAssert.assertTrue(res.contentType().contains("json"), "Products_Get_All Content type is not json");
        softAssert.assertAll();
    }

    @Test(priority = 2)
    public void getProductByID(){
        int productID = allProductsIDs[new Random().nextInt(allProductsIDs.length)];
//        int productID = 175;
        Response res = given().
                            spec(requestSpecification).
                        when().
                            get(productsResource + productID).
                        then().
//                            log().body().
                            extract().response();
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(res.statusCode(), 200, "Products_Get_One Product is not found successfully. status code: " + res.statusCode());
        softAssert.assertTrue(res.time() <= 1000L, "Products_Get_One Response time longer that 1 second: " + res.time() + "ms");
        softAssert.assertTrue(res.contentType().contains("json"), "Products_Get_One Content type is not json");
        softAssert.assertEquals((int)res.path("id"), productID, "Products_Get_One response product ID is not correct");
        softAssert.assertAll();
    }

    @Test(priority = 3)
    public void createProduct(){
        int expectedStatusCode = 201;
        HashMap<String, Object> requestBody = buildProductBody("battle star galactica", 100, "battle star galactica amazing hoodie", 1, "https://placeimg.com/640/480/any");

        Response res = given().
                            spec(requestSpecification).
                            contentType(ContentType.JSON).
                            body(requestBody).
                        when().
                            post(productsResource).
                        then().
//                            assertThat().statusCode(201).
                            extract().response();

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(res.statusCode(), expectedStatusCode, "Products_Create Product is not created successfully. status code: " + res.statusCode());
        softAssert.assertTrue(res.time() <= 1000L, "Products_Create Response time is less than 1 second: " + res.time() + "ms");
        softAssert.assertAll();

        createdProductID = res.path("id");
        System.out.println("created: " + createdProductID);
    }

    @Test(priority = 4)
    public void updateProduct(){
        int expectedStatusCode = 200;
        int updatedProductID = createdProductID;

        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("price", 555);
        Response res = given().
                            spec(requestSpecification).
                            contentType(ContentType.JSON).
                            body(requestBody).
                        when().
                            put(productsResource + updatedProductID).
                        then().
                            assertThat().statusCode(expectedStatusCode).
                            extract().response();
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(res.statusCode(), expectedStatusCode, "Update_Product Product is not updated successfully. status code: " + res.statusCode());
        softAssert.assertTrue(res.time() <= 1000L, "Update_Product Response time is less than 1 second: " + res.time() + "ms");
        softAssert.assertAll();
        System.out.println(res.asString());
    }

    @Test(priority = 5)
    public void deleteProduct(){
        int expectedStatusCode = 200;
        int deletedProductID = createdProductID;
        System.out.println("delete: " + deletedProductID);
        Response res = given().
                            spec(requestSpecification).
                        when().
                            delete(productsResource + deletedProductID).
                        then().
                            extract().response();
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(res.statusCode(), expectedStatusCode, "Delete_Product Product is not deleted successfully. status code: " + res.statusCode());
        softAssert.assertTrue(res.time() <= 1000L, "Delete_Product Response time is less than 1 second: " + res.time() + "ms");
        softAssert.assertAll();
    }

    private HashMap<String, Object> buildProductBody(String title, int price, String description, int categoryId, String url){
        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("title", title);
        requestBody.put("price", price);
        requestBody.put("description", description);
        requestBody.put("categoryId", categoryId);
        requestBody.put("images", Collections.singletonList(url));
        return requestBody;
    }
}
