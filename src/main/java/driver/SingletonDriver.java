package driver;

import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;
import java.util.Optional;


public class SingletonDriver {
    private static WebDriver driver;
    private static final int IMPLICIT_WAIT_TIMEOUT = 5;
    private static final int PAGE_LOAD_TIMEOUT = 10;
    private static ThreadLocal<WebDriver> webDriverThreadLocal = new ThreadLocal<>();

    private SingletonDriver() {
        driver = new ChromeDriver();
        WebDriverRunner.setWebDriver(driver);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICIT_WAIT_TIMEOUT));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(PAGE_LOAD_TIMEOUT));
    }

    public static WebDriver getInstance() {
        if (driver == null) {
            new SingletonDriver();
        }
        return driver;
    }

    public static void quitDriver(){
        Optional.ofNullable(getInstance()).ifPresent(webDriver -> {
            webDriver.quit();
            webDriverThreadLocal.remove();
        });
    }
}