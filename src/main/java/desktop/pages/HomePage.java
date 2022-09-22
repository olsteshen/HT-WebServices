package desktop.pages;

import abstractclasses.page.AbstractPage;
import driver.SingletonDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static constants.Constants.INITIAL_HOME_PAGE;
import static com.codeborne.selenide.Selenide.*;

public class HomePage extends AbstractPage {

    public HomePage(WebDriver driver) {
        super(driver);
        SingletonDriver.getInstance().get(INITIAL_HOME_PAGE);
    }

    public SearchResultsPage enterSearchTerm(String searchTerm) {
        $(By.name("searchTerm")).setValue(searchTerm).pressEnter();
        return new SearchResultsPage(driver);
    }

}