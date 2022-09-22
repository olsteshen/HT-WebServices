package desktop.pages;

import abstractclasses.page.AbstractPage;
import driver.SingletonDriver;
import io.cucumber.datatable.DataTable;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.Map;

import static com.codeborne.selenide.Selenide.$;
import static constants.Constants.*;


public class BasketPage extends AbstractPage {
    public BasketPage(WebDriver driver) {
        super(driver);
        SingletonDriver.getInstance();
    }

    public void checkBasketPageURL(){
        assert(getPageUrl()).equals(BASKET_PAGE_URL);
    }

    public CheckoutPage buttonCheckoutOnBasket(){
       $(By.xpath("//*[contains(@class,'checkout-btn')]")).click();
       return new CheckoutPage(driver);
    }

    public void checkBasketOrderSummary(DataTable orderSummary){
        List<Map<String,String>> data = orderSummary.asMaps(String.class, String.class);
        Assertions.assertAll("Error on basket",
                () -> Assertions.assertEquals(data.get(0).get("Delivery cost"), $(By.xpath("//div[@class='basket-totals']//dl[@class='delivery-text']/dd")).getText()),
                () -> Assertions.assertEquals(data.get(0).get("Total"), $(By.xpath("//div[@class='basket-totals']//dl[@class='total']/dd")).getText())
        );
    }
}
