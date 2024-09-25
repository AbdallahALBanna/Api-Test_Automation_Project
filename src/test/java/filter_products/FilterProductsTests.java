package filter_products;

import base.BaseTests;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;
import java.util.Set;

import static io.restassured.RestAssured.given;

public class FilterProductsTests extends BaseTests {

    @Test
    public void filterProductsByTitle(){

        Response response=given().spec(requestSpecification).param("title","Silver")
                .when().get(productsResource)
                .then().extract().response();

        SoftAssert softAssert=new SoftAssert();
        softAssert.assertEquals(response.statusCode(),200);
        softAssert.assertTrue(response.time()<=2000L,"Products_Filter Response time longer that 1 seconds: " + response.time() + "ms");

        List<String> title=response.path("title");
        boolean flag_contains_title= true;
        for(String e:title){
            if(!e.contains("Silver")){
                flag_contains_title=false;
                break;
            }
        }
        softAssert.assertTrue(flag_contains_title,"There is a problem in filter functionality(the title does not contain the keyword)");
        softAssert.assertAll();
    }

    @Test
    public void filterProductsByPrice(){

        Response response=given().spec(requestSpecification).param("price",44)
                .when().get(productsResource)
                .then().extract().response();

        SoftAssert softAssert=new SoftAssert();
        softAssert.assertEquals(response.statusCode(),200);
        softAssert.assertTrue(response.time()<=3000L,"Products_Filter Response time longer that 1 seconds: " + response.time() + "ms");

        List<Integer> price=response.path("price");
        boolean flag_contains_price= !price.isEmpty() ? true:false;
        for(int e:price){
            if(!(e ==44)){
                flag_contains_price=false;
                break;
            }
        }
        softAssert.assertTrue(flag_contains_price,"There is a problem in filter functionality(the price does not contain the keyword)");
        softAssert.assertAll();
    }


    @Test
    public void filterProductsByPriceRange(){

        int price_min=1;
        int price_max=1000;

        Response response=given().spec(requestSpecification).params("price_min",price_min,"price_max",price_max)
                .when().get(productsResource)
                .then().extract().response();

        SoftAssert softAssert=new SoftAssert();
        softAssert.assertEquals(response.statusCode(),200);
        softAssert.assertTrue(response.time()<=3000L,"Products_Filter Response time longer that 1 seconds: " + response.time() + "ms");

        List<Integer> price=response.path("price");
        boolean flag_contains_price= !price.isEmpty() ? true:false;
        for(int e:price){
            if(e > price_max||e<price_min){
                flag_contains_price=false;
                break;
            }
        }
        softAssert.assertTrue(flag_contains_price,"the price is not in the price range");
        softAssert.assertAll();
    }

    @Test
    public void filterProductsByCategoryId(){

        int categoryId=1;

        Response response=given().spec(requestSpecification).param("categoryId",categoryId)
                .when().get(productsResource)
                .then().extract().response();

        SoftAssert softAssert=new SoftAssert();
        softAssert.assertEquals(response.statusCode(),200);
        softAssert.assertTrue(response.time()<=3000L,"Products_Filter Response time longer that 1 seconds: " + response.time() + "ms");

        List<Integer> listCategoryId=response.path("category.id");
        boolean flag_contains_price= !listCategoryId.isEmpty() ? true:false;
        for(int e:listCategoryId){
            if(e != categoryId){
                flag_contains_price=false;
                break;
            }
        }
        softAssert.assertTrue(flag_contains_price,"the Category is not correct");
        softAssert.assertAll();
    }



}


