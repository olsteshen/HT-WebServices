package kruidvat;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.github.fge.jsonschema.examples.Utils;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.io.IOUtils;
import org.apache.commons.text.StringSubstitutor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.codeborne.selenide.Selenide.$;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;


public class KruidvatTest {
    Map<String, String> sessionStorage = new HashMap<>();


    @BeforeAll
    static void setUpRequestSpecification() {
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setBaseUri("https://www.kruidvat.nl")
                .setContentType(ContentType.JSON)
                .build();
        Configuration.baseUrl = "https://www.kruidvat.nl";
    }

    @Test
    public void verifyProductInCart() {
        createCart();
        addProductToCart();
        Selenide.open("/cart");
        $(By.id("onetrust-accept-btn-handler")).click();
        setAuthHeaders();
        $(By.id("onetrust-accept-btn-handler")).click();
        $(By.className("product-summary__description-name")).shouldHave(Condition.text("Kruidvat Sensitive Handzeep Navulling"));
        $(By.className("select__selected-option")).shouldHave(Condition.text("1"));
    }

    private void createCart() {
        Response response = given().post("/api/v2/kvn/users/anonymous/carts");
        String guid = response.jsonPath().get("guid");
        String code = response.jsonPath().get("code");
        sessionStorage.put("guid", guid);
        sessionStorage.put("code", code);
        response.then().assertThat().body(matchesJsonSchemaInClasspath("cart_create_response_schema.json"));
    }

    private void setAuthHeaders() {
        Selenide.clearBrowserCookies();
        WebDriverRunner.getWebDriver().manage().addCookie(new Cookie("kvn-cart", sessionStorage.get("guid")));
        Selenide.refresh();
    }

    private void addProductToCart() {
        Map<String, Object> values = new HashMap<>();
        values.put("code", "2876350");
        values.put("quantity", 1);
        String payload = StringSubstitutor.replace(getTemplate("post_cart.txt"), values, "{{", "}}");
        Response response = given()
                .body(payload)
                .when()
                .post(getEndPoint())
                .then()
                .extract()
                .response();
        response.then().assertThat().body((matchesJsonSchemaInClasspath("add_to_cart_response_schema.json")));
        Assertions.assertAll("Response is not as expected",
                () -> Assertions.assertEquals("1", response.jsonPath().getString("quantity")),
                () -> Assertions.assertEquals("2876350", response.jsonPath().getString("entry.product.code"))
        );
    }

    public String getEndPoint() {
        return "/api/v2/kvn/users/anonymous/carts/" + sessionStorage.get("guid") + "/entries?lang=nl";
    }

    public static String getTemplate(String path) {
        try {
            ClassLoader classLoader = Utils.class.getClassLoader();
            return IOUtils.toString(Objects.requireNonNull(classLoader.getResourceAsStream(path)), Charset.defaultCharset());
        } catch (IOException exception) {
            throw new RuntimeException("Cannot read file from path", exception);
        }
    }
}
