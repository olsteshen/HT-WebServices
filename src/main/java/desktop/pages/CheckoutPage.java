package desktop.pages;

import abstractclasses.page.AbstractPage;
import driver.SingletonDriver;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.Map;

import static com.codeborne.selenide.Selenide.$;
import static constants.Constants.*;

public class CheckoutPage extends AbstractPage {
    public CheckoutPage(WebDriver driver) {
        super(driver);
        SingletonDriver.getInstance();
    }

    public void checkCheckoutPageURL(){
        Assertions.assertEquals(CHECKOUT_PAGE_URL, SingletonDriver.getInstance().getCurrentUrl(), "Wrong Checkout page url" );
    }

    public void buyNowButtonclick(){
        $(By.xpath("//button[@type='submit']")).click();
    }

    public void fillInUserEmail(String email){
        $(By.xpath("//input[@name='emailAddress']")).sendKeys(email);
    }

    public void fillAddressFields(Map<String, String> deliveryAddress){
        $(By.xpath("//input[@name='delivery-fullName']")).sendKeys(deliveryAddress.get("Full name"));
        $(By.xpath("//*[@id='deliveryCountryDropdown']//select[@id='delivery-CountryDropdown']")).selectOption(deliveryAddress.get("Delivery country"));
        $(By.xpath("//input[@name='delivery-addressLine1']")).sendKeys(deliveryAddress.get("Address line 1"));
        $(By.xpath("//input[@name='delivery-addressLine2']")).sendKeys(deliveryAddress.get("Address line 2"));
        $(By.xpath("//input[@name='delivery-city']")).sendKeys(deliveryAddress.get("Town/City"));
        $(By.xpath("//input[@name='delivery-county']")).sendKeys(deliveryAddress.get("County/State"));
        $(By.name("delivery-postCode")).sendKeys(deliveryAddress.get("Postcode"));
        $(By.xpath("//div[@class='block-wrap delivery-address']")).click();
    }

    public void enterCardDetails(Map<String, String> cardDetails){
        $(By.xpath("//input[@id='credit-card-number']")).sendKeys(cardDetails.get("cardNumber"));
        $(By.xpath("//iframe[@id='braintree-hosted-field-expirationDate']")).sendKeys(cardDetails.get("Expiry date"));
        $(By.xpath("//iframe[@id='braintree-hosted-field-cvv']")).sendKeys(cardDetails.get("Cvv"));
    }

    public void checkOrderSummary(Map<String, String> orderDetails){
        Assertions.assertAll("Error on checkout",
                () -> Assertions.assertEquals(orderDetails.get("Sub-total"), $(By.xpath("//div[@class='wrapper']/div[2]/dl/dd")).getText()),
                () -> Assertions.assertEquals(orderDetails.get("Delivery"), $(By.xpath("//div[@class='wrapper']/div[3]/dl/dd")).getText()),
                () -> Assertions.assertEquals(orderDetails.get("VAT"), $(By.xpath("//dd[@class='text-right total-tax']")).getText()),
                () -> Assertions.assertEquals(orderDetails.get("Total"), $(By.xpath("//dd[@class='text-right total-price']")).getText()));
    }

    public void checkErrorMessage(List<Map<String, String>> expectedErrors){
        Assertions.assertAll("Error on checkout",
                () -> Assertions.assertEquals(expectedErrors.get(0).get("validaton error message"), $(By.xpath("//div[@id='emailAddress']//div[@class='error-block']")).getText()),
                () -> Assertions.assertEquals(expectedErrors.get(1).get("validaton error message"), $(By.xpath("//div[@id='delivery-fullName-errors']")).getText()),
                () -> Assertions.assertEquals(expectedErrors.get(2).get("validaton error message"), $(By.xpath("//div[@id='delivery-addressLine1-errors']")).getText()),
                () -> Assertions.assertEquals(expectedErrors.get(3).get("validaton error message"), $(By.xpath("//div[@id='delivery-city-errors']")).getText()),
                () -> Assertions.assertEquals(expectedErrors.get(4).get("validaton error message"), $(By.xpath("//div[@id='delivery-postCode-errors']")).getText())
        );

    }
}
