package petstore;

import Utils.Util;
import org.json.JSONObject;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class User {
    String uri = "https://petstore.swagger.io/v2/user";

    @Test(priority = 1)
    public String lerJson(String caminhoJson) throws IOException {
        return new String(Files.readAllBytes(Paths.get(caminhoJson)));
    }

    String caminho = "db/user.json";
    JSONObject jsonObj = new JSONObject(Util.readJson(caminho));

    @Test(priority = 1)
    public void incluirUser() throws IOException {
        String jsonBody = lerJson("db/user_1.json");
        String userId = "555";

        given().contentType("application/json").log().all().body(jsonBody.toString()).when().post(uri).then().log()
                .all().statusCode(200).body("code", equalTo(200)).body("type", equalTo("unknown"))
                .body("message", equalTo(userId));
    }

    @Test(priority = 2)
    public void consultarUserPorNome() {

        String username = "user1";
        String token =

                given().contentType("application/json").log().all().when().get(uri + "/" + username).then().log().all()
                        .statusCode(200).body("id", is(555)).body("username", is("user1")).body("firstName", is("meno"))
                        .body("lastName", is("priezvisko")).body("email", is("email@qbsw.sk"))
                        .body("password", is("abcd")).body("phone", is("+421")).body("userStatus", is(0))

                        .extract().path("username");
        System.out.println("Token: " + token);

    }

    @Test(priority = 4)
    public void alterarUserName() throws IOException {
        String userId = "9223372036854775807";
        String username = "charlie";
        String jsonBody = lerJson("db/user_2.json");
        given().contentType("application/json").log().all().body(username).body(jsonBody).when()
                .put(uri + "/" + username).then().log().all().statusCode(200).body("type", is("unknown"))
                .body("message", is(userId));
    }

    @Test(priority = 5)
    public void excluiUser() {
        String username = "charlie";
        given().contentType("application/json").log().all().when().delete(uri + "/" + username).then().log().all()
                .statusCode(200).body("code", is(200)).body("type", is("unknown")).body("message", is(username));
    }
}
