package base;

import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;

import static io.restassured.RestAssured.given;

public class BaseTests {

    public RequestSpecification requestSpecification;
    public final String productsResource = "products/";
    public final String authenticationLoginResource = "auth/login";
    public final String authenticationProfileResource = "auth/profile";
    public final String fileUploadeResource = "files/upload";
    public final String fileGetByNameResource = "https://api.escuelajs.co/api/files/";
    public final String categoryResource = "categories/";
    public final String usersResource = "users/";

    @BeforeClass
    public void setUp(){
        String version = "v1/";
        String baseUri = "https://api.escuelajs.co/api/";
        requestSpecification = given().baseUri(baseUri + version);
    }
}
