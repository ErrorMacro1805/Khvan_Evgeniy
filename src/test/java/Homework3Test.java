import Commands.CheckAllCheckboxCommand;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

import static Commands.CustomCommand.checkAllCheckboxes;
import static Conditions.CustomCondition.allChecked;
import static com.codeborne.selenide.Selenide.open;

public class Homework3Test {
    @BeforeClass
    public static void setUp() {
        Configuration.baseUrl = "https://the-internet.herokuapp.com";
        Configuration.timeout = Duration.ofSeconds(10).toMillis();
        WebDriver driver = new ChromeDriver();
        WebDriverRunner.setWebDriver(driver);
    }

    @Test
    public void testCustomCommand() {
        open("/checkboxes");
        SelenideElement element = Selenide.$(By.cssSelector("body"));
        element.execute(checkAllCheckboxes);
        element.shouldBe(allChecked);
    }

    @AfterClass
    public static void closeDriver() {
        WebDriverRunner.getWebDriver().quit();
    }
}