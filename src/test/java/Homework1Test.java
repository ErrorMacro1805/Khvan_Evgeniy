import com.codeborne.selenide.ClickOptions;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.exactText;
import static org.assertj.core.api.Assertions.assertThat;


public class Homework1Test {
    @BeforeClass
    public static void setUp() {
        Configuration.baseUrl = "https://the-internet.herokuapp.com";
        Configuration.timeout = Duration.ofSeconds(10).toMillis();
        WebDriver driver = new ChromeDriver();
        WebDriverRunner.setWebDriver(driver);
    }

    @Test
    public void firstTest() {
        open("/javascript_alerts");
        $x("//button[contains(text(),'Alert')]").click();
        String confirmText = confirm();
        assertThat(confirmText).isEqualTo("I am a JS Alert");
        assertThat($x("//*[@id='result']").text()).isEqualTo("You successfully clicked an alert");

    }

    @Test
    public void secondTest() {
        open("/javascript_alerts");
        $x("//button[contains(text(),'Confirm')]").click();
        String alertText = switchTo().alert().getText();
        assertThat(alertText).isEqualTo("I am a JS Confirm");
        switchTo().alert().dismiss();
        assertThat($x("//*[@id='result']").text()).isEqualTo("You clicked: Cancel");
    }

    @Test
    public void thirdTest() {
        open("/javascript_alerts");
        $x("//button[contains(text(),'Prompt')]").click();
        switchTo().alert().sendKeys("Test message");
        String alertText = switchTo().alert().getText();
        assertThat(alertText).isEqualTo("I am a JS prompt");
        switchTo().alert().accept();
        assertThat($("#result").text()).isEqualTo("You entered: Test message");
    }

    @Test
    public void fourthTest() {
        open("/javascript_alerts");
        $x("//button[contains(text(),'Confirm')]").click(ClickOptions.usingJavaScript());
        String alertText = switchTo().alert().getText();
        assertThat(alertText).isEqualTo("I am a JS Confirm");
        confirm();
        assertThat($x("//*[@id='result']").text()).isEqualTo("You clicked: Ok");
    }

    @Test
    public void fifthTest() {
        open("/javascript_alerts");
        SelenideElement button = $x("//button[contains(text(),'Confirm')]");
        executeJavaScript("arguments[0].click();", button);
        String alertText = switchTo().alert().getText();
        assertThat(alertText).isEqualTo("I am a JS Confirm");
        confirm();
        assertThat($x("//*[@id='result']").text()).isEqualTo("You clicked: Ok");
    }

    @AfterClass
    public static void closeDriver() {
        WebDriverRunner.getWebDriver().quit();
    }
}
