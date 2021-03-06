// 1 - Pacote
package petstore;

// 2 - Bibliotecas
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;



// 3 - Classe
public class Pet {
    // 3.1 - Atributos
    String uri = "https://petstore.swagger.io/v2/pet"; //uri ou url: endereço da interface da entidade Pet

    // 3.2 - Métodos e Funções

    @Test(priority = 1)  // Anotação que identifica o teste
    public String lerJson(String caminhoJson) throws IOException {
        return new String(Files.readAllBytes(Paths.get(caminhoJson)));
    }

    //Incluir - Create - Post: Registro
    @Test(priority = 1)  // Anotacao que identifica como teste para o TestNG
    public void incluirPet() throws IOException {
        String jsonBody = lerJson("db/pet_1.json");

        //Sintaxe Gherkin: comunicação estruturada = Dado/Given - Quando/When - Entao/Then

        given() // Dado
                .contentType("application/json")
                .log().all()
                .body(jsonBody)
        .when() // Quando
                .post(uri)
        .then() // Então
                .log().all()
                .statusCode(200)
                .body("name", is("tommy"))
                .body("status", is("available"))
                .body("category.name", is("tommy"))
                .body("tags.name", contains("tommy"));
    }

    @Test(priority = 2)
    public void consultarPet() {
        String petId = "1104";

        String token =
        given()
                .contentType("application/json")
                .log().all()
        .when()
                .get(uri + "/" + petId)
        .then()
                .log().all().statusCode(200).body("name", is("tommy"))
                .body("category.name", is("tommy"))
                .body("status", is("available"))
        .extract()
                .path("category.name");
                System.out.println("Token: " + token);

    }

    @Test(priority = 3)
    public void alterarPet() throws IOException {
        String jsonBody = lerJson("db/pet_2.json");

        given()
                .contentType("application/json")
                .log().all()
                .body(jsonBody)
        .when()
                .put(uri)
        .then()
                .log().all()
                .statusCode(200)
                .body("name", is("Timmy"))
                .body("status", is("sold"));
    }

    @Test(priority = 4)
    public void excluirPet() {
        String petId = "1104";

        given()
                .contentType("application/json")
                .log().all()
        .when()
                .delete(uri + "/" + petId)
        .then()
                .log().all()
                .statusCode(200)
                .body("code", is(200))
                .body("type", is("unknown"))
                .body("message", is(petId));
    }

    @Test
    public void consultarPetPorStatus() {
        String status = "available";

        given()
                .contentType("application/json")
                .log().all()
                .when()
                .get(uri + "/findByStatus?status=" + status)
                .then()
                .log().all()
                .statusCode(200)
                .body("name[]", everyItem(equalTo("tommy")));

    }
}
