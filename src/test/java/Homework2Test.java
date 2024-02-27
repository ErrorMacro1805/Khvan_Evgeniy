import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Condition.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.testng.Assert.assertEquals;

import static com.codeborne.selenide.Selenide.*;

import java.time.Duration;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$$;

public class Homework2Test {
    @Test
    public void firstTest() {
        Configuration.baseUrl = "https://the-internet.herokuapp.com";
        Configuration.timeout = Duration.ofSeconds(10).toMillis();
        Configuration.browser = "firefox";
        SelenideElement inputField = $x("//input[@type='text']");
        open("/dynamic_controls");
        inputField.shouldNot(be(enabled));
        $x("//button[contains(. ,'Enable')]").click();
        inputField.shouldBe(enabled);
        inputField.setValue("Test string");
        String inputValue = inputField.getValue();
        assertEquals("Test string", inputValue);
        $x("//button[contains(. ,'Disable')]").click();
        inputField.shouldNot(be(enabled));
        $x("//*[@id='message']").shouldHave(text("It's disabled!"));
    }

    @Test
    public void secondTest() {
        Configuration.baseUrl = "https://the-internet.herokuapp.com";
        Configuration.timeout = Duration.ofSeconds(10).toMillis();
        WebDriver driver = new ChromeDriver();
        WebDriverRunner.setWebDriver(driver);
        open("//dynamic_controls");
        SelenideElement checkbox = $x("//input[@type='checkbox']");
        checkbox.shouldBe(exist).shouldBe(enabled);
        checkbox.shouldNot(be(checked));
        checkbox.click();
        checkbox.should(be(checked));
        $x("//button[contains(., 'Remove')]").click();
        $x("//div[@id='loading']").should(disappear);
        checkbox.shouldNot(exist);
        $x("//button[contains(., 'Add')]").click();
        $x("//div[@id='loading']").should(disappear);
        checkbox.should(exist);
        $x("//*[@id='message']").shouldHave(text("It's back!"));
        driver.quit();
    }

    @Test
    public void thirdTest() {
        Configuration.browser = SpecificDriver.class.getName();
        Configuration.baseUrl = "https://the-internet.herokuapp.com";
        open("/javascript_alerts");
        $$(byText("Click for JS Alert")).shouldHave(size(1)).first().click();
        String confirmText = confirm();
        assertThat(confirmText).isEqualTo("I am a JS Alert");
        assertThat($x("//*[@id='result']").text()).isEqualTo("You successfully clicked an alert");
    }
}
