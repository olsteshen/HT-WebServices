package abstractclasses.page;


import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.WebDriver;


public abstract class AbstractPage {
    protected WebDriver driver;
    private String pageUrl;
    private String pageUrlPattern;

    public AbstractPage(WebDriver driver) {
        this.driver = driver;
    }

    public String getPageUrl() {
        return WebDriverRunner.getWebDriver().getCurrentUrl();
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String setPageUrlPattern(String pageUrlPattern) {
        return this.pageUrlPattern = pageUrlPattern;
    }

    public String getPageUrlPattern() {
        return pageUrlPattern;
    }
}