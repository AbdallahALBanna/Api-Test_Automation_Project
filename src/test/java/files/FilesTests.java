package files;

import base.BaseTests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.File;

import static io.restassured.RestAssured.given;


public class FilesTests extends BaseTests {



    public String fileNameOnCloud ;
    public String filePathOnCloud ;

    @Test
    public void createFileUpload(){

        Response fileUploadResponse =
                given()
                .spec(requestSpecification)
                .multiPart( new File("testFile.jpeg"))
                .when().post(fileUploadeResource)
                .then().extract().response();


        SoftAssert softAssert=new SoftAssert();
        softAssert.assertEquals(fileUploadResponse.statusCode(),201);
        softAssert.assertTrue(fileUploadResponse.time()<=2000L,"fileUploadResponse Response time longer that 2 seconds: " + fileUploadResponse.time() + "ms");
        softAssert.assertAll();





        System.out.println("Response Body: " + fileUploadResponse.getBody().asString());



         fileNameOnCloud = fileUploadResponse.path("filename");
         filePathOnCloud = fileUploadResponse.path("location");

        System.out.println("Output File name : " + fileNameOnCloud);
        System.out.println("Output File path : " + filePathOnCloud);



    }







    @Test(dependsOnMethods = "createFileUpload")
    public void gitFileByName(){


        Response gitFileByNameResponse =
                given()
                        .when()
                        .get(filePathOnCloud)
                        .then()
                        .extract()
                        .response();

        int statusCode = gitFileByNameResponse.statusCode();
        System.out.println("gitFileByNameResponse Status Code: " + statusCode);
        System.out.println("aaResponse Body: " + gitFileByNameResponse.getBody().asString());

        SoftAssert softAssert=new SoftAssert();
        softAssert.assertEquals(gitFileByNameResponse.statusCode(),200);
        softAssert.assertTrue(gitFileByNameResponse.time()<=2000L,"gitFileByNameResponse Response time longer that 2 seconds: " + gitFileByNameResponse.time() + "ms");
        softAssert.assertAll();


        // Optional: Save the image to a local file
        try {
            byte[] imageData = gitFileByNameResponse.asByteArray(); // Get the image as a byte array
            java.nio.file.Files.write(java.nio.file.Paths.get("gitFileByName_downloadedImage.jpg"), imageData); // Save to local file
            System.out.println("Image saved successfully as 'gitFileByName_downloadedImage.jpg'");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }




}
