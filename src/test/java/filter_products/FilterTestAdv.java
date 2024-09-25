package filter_products;

import base.BaseTests;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.HashMap;
import java.util.List;

import static io.restassured.RestAssured.given;

public class FilterTestAdv extends BaseTests {

    private static final long MAX_TITLE_RESPONSE_TIME = 2000L; // 2 seconds
    private static final long MAX_PRICE_RESPONSE_TIME = 3000L; // 3 seconds
    private static final String TITLE_FILTER = "Silver";

    private void validateResponseTime(Response response, long expectedTime, SoftAssert softAssert) {
        softAssert.assertTrue(response.time() <= expectedTime,
                "Response time longer than expected: " + response.time() + "ms");
    }

    private void validateStatusCode(Response response, int expectedStatus, SoftAssert softAssert) {
        softAssert.assertEquals(response.statusCode(), expectedStatus,
                "Status code mismatch. Expected: " + expectedStatus + ", but got: " + response.statusCode());
    }

    private void validateTitleContainsKeyword(List<String> titles, String keyword, SoftAssert softAssert) {
        boolean allTitlesContainKeyword = titles.stream().allMatch(title -> title.contains(keyword));
        softAssert.assertTrue(allTitlesContainKeyword, "Not all titles contain the keyword: " + keyword);
    }

    private void validatePriceExactMatch(List<Integer> prices, int expectedPrice, SoftAssert softAssert) {
        boolean allPricesMatch = prices.stream().allMatch(price -> price == expectedPrice);
        softAssert.assertTrue(allPricesMatch, "Not all prices match the expected price: " + expectedPrice);
    }

    private void validatePriceRange(List<Integer> prices, int minPrice, int maxPrice, SoftAssert softAssert) {
        boolean pricesWithinRange = prices.stream().allMatch(price -> price >= minPrice && price <= maxPrice);
        softAssert.assertTrue(pricesWithinRange, "Prices are not within the expected range: " + minPrice + " - " + maxPrice);
    }

    private void validateCategoryId(List<Integer> categoryIds, int expectedCategoryId, SoftAssert softAssert) {
        boolean allCategoryIdsMatch = categoryIds.stream().allMatch(id -> id == expectedCategoryId);
        softAssert.assertTrue(allCategoryIdsMatch, "Category ID mismatch. Expected: " + expectedCategoryId);
    }

    @Test
    public void filterProductsByTitle() {
        Response response = given().spec(requestSpecification).param("title", TITLE_FILTER)
                .when().get(productsResource)
                .then().extract().response();

        SoftAssert softAssert = new SoftAssert();
        validateStatusCode(response, 200, softAssert);
        validateResponseTime(response, MAX_TITLE_RESPONSE_TIME, softAssert);

        List<String> titles = response.path("title");
        validateTitleContainsKeyword(titles, TITLE_FILTER, softAssert);

        softAssert.assertAll();
    }

    @Test
    public void filterProductsByPrice() {
        int priceFilter = 44;
        Response response = given().spec(requestSpecification).param("price", priceFilter)
                .when().get(productsResource)
                .then().extract().response();

        SoftAssert softAssert = new SoftAssert();
        validateStatusCode(response, 200, softAssert);
        validateResponseTime(response, MAX_PRICE_RESPONSE_TIME, softAssert);

        List<Integer> prices = response.path("price");
        validatePriceExactMatch(prices, priceFilter, softAssert);

        softAssert.assertAll();
    }

    @Test
    public void filterProductsByPriceRange() {
        int priceMin = 1;
        int priceMax = 1000;

        Response response = given().spec(requestSpecification)
                .params("price_min", priceMin, "price_max", priceMax)
                .when().get(productsResource)
                .then().extract().response();

        SoftAssert softAssert = new SoftAssert();
        validateStatusCode(response, 200, softAssert);
        validateResponseTime(response, MAX_PRICE_RESPONSE_TIME, softAssert);

        List<Integer> prices = response.path("price");
        validatePriceRange(prices, priceMin, priceMax, softAssert);

        softAssert.assertAll();
    }

    @Test
    public void filterProductsByCategoryId() {
        int categoryId = 1;

        Response response = given().spec(requestSpecification).param("categoryId", categoryId)
                .when().get(productsResource)
                .then().extract().response();

        SoftAssert softAssert = new SoftAssert();
        validateStatusCode(response, 200, softAssert);
        validateResponseTime(response, MAX_PRICE_RESPONSE_TIME, softAssert);

        List<Integer> categoryIds = response.path("category.id");
        validateCategoryId(categoryIds, categoryId, softAssert);

        softAssert.assertAll();
    }

    @Test
    public void joinedFilterProducts() {
        int priceMin = 1;
        int priceMax = 1000;
        int categoryId = 2;
        String title = "Earbud";

        HashMap<String, Object> requestParams = new HashMap<>();
        requestParams.put("title", title);
        requestParams.put("price_min", priceMin);
        requestParams.put("price_max", priceMax);
        requestParams.put("categoryId", categoryId);

        Response response = given().spec(requestSpecification).params(requestParams)
                .when().get(productsResource)
                .then().extract().response();

        SoftAssert softAssert = new SoftAssert();
        validateStatusCode(response, 200, softAssert);
        validateResponseTime(response, MAX_PRICE_RESPONSE_TIME, softAssert);

        List<Integer> prices = response.path("price");
        validatePriceRange(prices, priceMin, priceMax, softAssert);

        List<String> titles = response.path("title");
        validateTitleContainsKeyword(titles, title, softAssert);

        List<Integer> categoryIds = response.path("category.id");
        validateCategoryId(categoryIds, categoryId, softAssert);

        softAssert.assertAll();
    }
}
