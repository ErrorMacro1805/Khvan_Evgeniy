import com.microsoft.playwright.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

import static com.microsoft.playwright.options.ElementState.DISABLED;
import static com.microsoft.playwright.options.ElementState.ENABLED;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;

public class Homework5 {
    private static Page page;
    private static Browser browser;
    private static Playwright playwright;

    @BeforeClass
    public static void setUp() {
        playwright = Playwright.create();
        browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setTimeout(Duration.ofSeconds(10).toMillis()).setHeadless(false));
        Browser.NewPageOptions options = new Browser.NewPageOptions().setBaseURL("https://the-internet.herokuapp.com/");
        page = browser.newPage(options);
    }

    @Test
    public void firstTest() {
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
        assertTrue(page.isVisible("text='It's disabled!'"),"the text 'It's disabled!' is not visible");
    }
}
