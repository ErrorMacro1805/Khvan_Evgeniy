import com.microsoft.playwright.*;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static com.microsoft.playwright.options.ElementState.*;
import static org.testng.Assert.*;
import static org.testng.AssertJUnit.assertEquals;

public class Homework5Test {
    static Playwright playwright;
    static Page page;
    static Browser browser;

    @BeforeClass
    public void setUp() {
        playwright = Playwright.create();
    }

    @AfterClass
    public void afterClass() {
        playwright.close();
    }

    @AfterMethod
    public void closePage() {
        page.close();
    }

    @Test
    public void firstTest() {
        Browser.NewPageOptions options = new Browser.NewPageOptions().setBaseURL("https://the-internet.herokuapp.com/");
        Browser browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
        page = browser.newPage(options);
        page.navigate("/dynamic_controls");
        ElementHandle inputField = page.querySelector("input[type='text']");
        assertTrue(inputField.isDisabled());
        page.click("button[onclick='swapInput()']");
        inputField.waitForElementState(ENABLED);
        String inputText = "Test string";
        inputField.fill(inputText);
        String inputData = inputField.inputValue();
        assertEquals(inputText, inputData);
        page.click("button[onclick='swapInput()']");
        inputField.waitForElementState(DISABLED);
        assertTrue(page.isVisible("text='It's disabled!'"));
    }

    @Test
    public void secondTest() {
        browser = playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(false));
        Browser.NewPageOptions options = new Browser.NewPageOptions().setBaseURL("https://the-internet.herokuapp.com/");
        page = browser.newPage(options);
        page.navigate("/dynamic_controls");
        ElementHandle checkbox = page.querySelector("#checkbox-example input");
        assertTrue(checkbox.isEnabled());
        assertFalse(checkbox.isChecked());
        checkbox.click();
        assertTrue(checkbox.isChecked());
        page.click("button[onclick='swapCheckbox()']");
        checkbox.waitForElementState(HIDDEN);
        assertFalse(checkbox.isVisible());
        page.click("button[onclick='swapCheckbox()']");
        page.waitForTimeout((Duration.ofSeconds(10).toMillis()));
        checkbox = page.querySelector("#checkbox-example input");
        assertTrue(checkbox.isEnabled());
        assertTrue(page.isVisible("text='It's back!'"));
    }

    @Test
    public void thirdTest() {
        AtomicReference<String> message = new AtomicReference<>();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setArgs(List.of("--start-maximized")));
        Browser.NewPageOptions options = new Browser.NewPageOptions().setBaseURL("https://the-internet.herokuapp.com/");
        page = browser.newPage(options);
        page.navigate("javascript_alerts");
        page.onceDialog(dialog -> {
            message.set(dialog.message());
            dialog.accept();
        });
        page.locator("button").getByText("Click for JS Alert").click();
        Assert.assertEquals(message.get(), "I am a JS Alert");
        assertThat(page.locator("#result")).containsText("You successfully clicked an alert");
    }
}


