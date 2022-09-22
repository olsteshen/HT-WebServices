package desktop.pages;

import abstractclasses.page.AbstractPage;
import driver.SingletonDriver;
import io.cucumber.datatable.DataTable;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.Map;

import static com.codeborne.selenide.Selenide.*;
import static constants.Constants.*;
import static java.lang.String.format;

public class SearchResultsPage extends AbstractPage {

private static final String BOOK_ITEM_TITLE = "//div[@class='book-item'][.//h3/a[contains(text(),'%s')]]//a[contains(@class,'add-to-basket')]";

    public SearchResultsPage(WebDriver driver){
        super(driver);
        SingletonDriver.getInstance();
    }

    public boolean isSearchResultsPresent() {
        return ! $$(By.xpath("//*[@class='book-item']")).isEmpty();
    }

    public void checkSearchPageURL(){
        Assertions.assertTrue(SingletonDriver.getInstance().getCurrentUrl().contains(SEARCH_RESULT_PAGE_URL), "Wrong Search page url" );
    }

    public void applyFilters(DataTable filtersData){
        Map<String,String> data = filtersData.asMap(String.class, String.class);
        $(By.xpath("//select[@name='price']")).selectOption(data.get("Price range"));
        $(By.xpath("//select[@name='availability']")).selectOption(data.get("Availability"));
        $(By.xpath("//select[@name='searchLang']")).selectOption(data.get("Language"));
        $(By.xpath("//select[@name='format']")).selectOption(data.get("Format"));
        $(By.xpath("//button[@class='btn btn-primary'][contains(text(),'Refine results')]")).click();
    }

    public void addProductToBasket(String name){
        $(By.xpath(format(BOOK_ITEM_TITLE, name))).click();
    }


    public BasketPage clickButtonContinue(){
        $(By.xpath("//a[contains(@class,'continue-to-basket')]")).click();
        return new BasketPage(driver);
    }
}