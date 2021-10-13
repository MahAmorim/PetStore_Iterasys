package petstore;

import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class Store {
    String uri = "https://petstore.swagger.io/v2/store/order";

    @Test(priority = 1)
    public String lerJson(String caminhoJson) throws IOException {
        return new String(Files.readAllBytes(Paths.get(caminhoJson)));
    }

    @Test(priority = 1)
    public void incluirPedido() throws IOException {
        String jsonBody = lerJson("db/order_1.json");

        given().contentType("application/json").log().all().body(jsonBody).when().post(uri)

                .then().log().all().statusCode(200).body("id", is(2021)).body("petId", is(1104)).body("quantity", is(1))
                .body("shipDate", is("2021-10-04T20:55:34.987+0000")).body("status", is("placed")).body("complete", is(true));

    }

    @Test(priority = 2)
    public void consultarPedido() throws IOException {
        String jsonBody = lerJson("db/order_2.json");
        String orderId = "10";

        given().contentType("application/json").log().all().body(jsonBody).when().get(uri + "/" + orderId).then().log()
                .all().statusCode(200).body("petId", is(10)).body("quantity", is(10))
                .body("shipDate", is("1970-01-01T00:00:00.001+0000")).body("status", is("placed';start-sleep -s 15"))
                .body("complete", is(true));
        ;

    }

    @Test(priority = 3)
    public void listarInventario() {

        given().contentType("application/json").log().all()

                .when().get("https://petstore.swagger.io/v2/store/inventory").then().log().all().statusCode(200);
    }

    @Test(priority = 5)
    public void excluirPedido() {
        String orderId = "2021";
        given().contentType("application/json").log().all().when().delete(uri + "/" + orderId).then().log().all()
                .statusCode(200).body("code", is(200)).body("type", is("unknown")).body("message", is(orderId));
    }

}
