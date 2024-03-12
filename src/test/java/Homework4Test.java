import com.microsoft.playwright.*;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.testng.Assert.assertEquals;

public class Homework4Test {

    private static Page page;
    private static Browser browser;
    private static Playwright playwright;


    @BeforeClass
    public static void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setTimeout(Duration.ofSeconds(10).toMillis()).setHeadless(false));
        Browser.NewPageOptions options = new Browser.NewPageOptions().setBaseURL("https://the-internet.herokuapp.com/");
        page = browser.newPage(options);
    }

    @BeforeMethod
    public static void navigateToUrl() {
        page.navigate("javascript_alerts");
    }

    @AfterClass
    public void tearDown() {
        browser.close();
        playwright.close();
    }

    @Test
    public void firstTest() {
        AtomicReference<String> message = new AtomicReference<>();

        page.onceDialog(dialog -> {
            message.set(dialog.message());
            dialog.accept();
        });
        page.click("button:has-text('Click for JS Alert')");
        Assert.assertEquals(message.get(), "I am a JS Alert");
        assertThat(page.locator("#result")).containsText("You successfully clicked an alert");
    }

    @Test
    public void secondTest() {
        AtomicReference<String> message = new AtomicReference<>();
        page.onceDialog(dialog -> {
            message.set(dialog.message());
            dialog.dismiss();
        });
        page.click("button:has-text('Click for JS Confirm')");
        Assert.assertEquals(message.get(), "I am a JS Confirm");
        assertThat(page.locator("#result")).containsText("You clicked: Cancel");
    }

    @Test
    public void thirdTest() {
        AtomicReference<String> message = new AtomicReference<>();
        String inputText = "Test message";
        page.onceDialog(dialog -> {
            message.set(dialog.message());
            dialog.accept(inputText);
        });
        page.click("button:has-text('Click for JS Prompt')");
        Assert.assertEquals(message.get(), "I am a JS prompt");
        assertThat(page.locator("#result")).containsText(inputText);
    }

    @Test
    public void forthTest() {
        AtomicReference<String> message = new AtomicReference<>();
        page.onceDialog(dialog -> {
            message.set(dialog.message());
            dialog.accept();
        });
        page.evaluate("() => { " +
                "   document.querySelector(\"button[onclick='jsConfirm()']\").click(); " +
                "}");
        Assert.assertEquals(message.get(), "I am a JS Confirm");
        assertThat(page.locator("#result")).containsText("You clicked: Ok");
    }

    @Test
    public void fifthTest() {
        AtomicReference<String> message = new AtomicReference<>();
        page.onceDialog(dialog -> {
            message.set(dialog.message());
            dialog.accept();
        });
        page.evaluate("() => { " +
                "   document.querySelector(\"button[onclick='jsConfirm()']\").click(); " +
                "}");
        assertEquals(message.get(), "I am a JS Confirm");
        String resultText = (String) page.evaluate("() => document.querySelector('#result').innerText");
        assertEquals(resultText, "You clicked: Ok");
    }
}




