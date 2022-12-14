package one.digitalinnovation.parking.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import one.digitalinnovation.parking.model.dto.ParkingDTO;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;


import java.awt.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ParkingControllerTest extends AbstractContainerBase {
    @LocalServerPort
    private int randomPort;

    @BeforeEach
    public void setUpTest() {
        RestAssured.port = randomPort;
    }
    @Test
    void whenCreateThenCheckResult() {
        ParkingDTO parkingDTO = new ParkingDTO();
        parkingDTO.setColor("Vermelho");
        parkingDTO.setLicense("GPS-5454S");
        parkingDTO.setModel("FIESTA SEDAN 2015");
        parkingDTO.setState("RJ");

        RestAssured.given()
                .auth().preemptive().basic("user","123")
                .contentType(ContentType.JSON)
                .header("Authorization","Basic dXNlcjoxMjM=")
                .body(parkingDTO)
                .when()
                .post("/parking")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("license",Matchers.equalTo("GPS-5454S"))
                .body("model",Matchers.equalTo("FIESTA SEDAN 2015"))
                .extract().response().body().prettyPrint();
    }
    @Test
    void whenFindAllThenCheckResult() {
        RestAssured.given()
                .when()
                .auth()
                .basic("user","123")
                .get("/parking")
                .then()
                .statusCode(HttpStatus.OK.value())
                //.body("license[0]", Matchers.equalTo("WWW-GSCADE"))
                .extract().response().body().prettyPrint();
    }

}
