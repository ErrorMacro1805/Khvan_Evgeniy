import com.github.javafaker.Faker;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.ScreenshotType;
import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Date;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.testng.AssertJUnit.assertTrue;

public class Homework6Test {
    private static Page page;
    private static Browser browser;
    private static Playwright playwright;
    private static Video video;


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
        if (page != null) {
            page.close();
        }
    }

    @DataProvider(name = "browsers")
    public Object[][] browsers() {
        return new Object[][]{
                {"chromium"},
                {"firefox"},
                {"webkit"},};
    }

    @Test
    public void firstTest() throws IOException {
        File testFile = new File("My_Test_File_" + new Date().getTime() + ".txt");
        Faker faker = new Faker();
        String testText = faker.dune().character();
        FileUtils.writeStringToFile(testFile, testText, Charset.defaultCharset());
        Browser.NewPageOptions options = new Browser.NewPageOptions().setBaseURL("https://the-internet.herokuapp.com/");
        Browser browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setTimeout(Duration.ofSeconds(10).toMillis()).setHeadless(false));
        page = browser.newPage(options);
        page.navigate("/upload");
        FileChooser fileChooser = page.waitForFileChooser(() -> {
            page.locator("#file-upload").click();
        });
        fileChooser.setFiles(testFile.toPath());
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Upload")).click();
        assertThat(page.locator("#uploaded-files")).containsText(testFile.getName());
        page.navigate("/download");
        Download download = page.waitForDownload(() -> {
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(testFile.getName())).click();
        });
        download.saveAs(Paths.get(download.suggestedFilename() + "downloaded"));
        org.assertj.core.api.Assertions.assertThat(testFile).hasSameTextualContentAs(new File(download.suggestedFilename() + "downloaded"));
    }

    @Test
    public void secondTest() {
        Browser.NewPageOptions options = new Browser.NewPageOptions().setBaseURL("https://the-internet.herokuapp.com/");
        Browser browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
        page = browser.newPage(options);
        page.navigate("/drag_and_drop");
        page.dragAndDrop("#column-a", "#column-b");
        String columnBText = page.innerText("#column-b");
        Assert.assertEquals(columnBText, "A");
        page.dragAndDrop("#column-b", "#column-a");
        String columnAText = page.innerText("#column-b");
        Assert.assertEquals(columnAText, "B");
    }

    @Test(dataProvider = "browsers")
    public void thirdTest(String browserType) throws IOException {
        BrowserType type;
        switch (browserType) {
            case "chromium":
                type = playwright.chromium();
                break;
            case "firefox":
                type = playwright.firefox();
                break;
            case "webkit":
                type = playwright.webkit();
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser type: " + browserType);
        }

        BrowserContext browserContext = type.launch(new BrowserType.LaunchOptions().setHeadless(false)).
                newContext(new Browser.NewContextOptions().setRecordVideoDir(Path.of("video")));
        Page page = browserContext.newPage();
        page.navigate("https://google.com");
        page.fill("[name=q]", "Hillel IT School");
        page.press("[name=q]", "Enter");
        page.waitForTimeout(3000);
        byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setType(ScreenshotType.PNG).setFullPage(true));
        File screenshotFile = new File("img/google_" + browserType + ".png");
        FileUtils.writeByteArrayToFile(screenshotFile, screenshot);
        Path videoPath = Paths.get("video" + browserType + ".webm");
        page.close();
        page.video().saveAs(videoPath);
        browserContext.close();
        Assert.assertTrue(screenshotFile.exists());
        Assert.assertTrue(screenshotFile.length() > 0);
        assert Files.exists(videoPath);
        assert Files.size(videoPath) > 0;
    }
}

