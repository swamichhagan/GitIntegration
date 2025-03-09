package MyPackage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;

import Configuration.ExtentManager;

public class HeadlessSeleniumTest {

    WebDriver driver;
    static Properties properties;
    ExtentReports extent;
    ExtentTest test;

    @BeforeTest
    public void beforeTest() throws IOException {
        // Use WebDriverManager to set up ChromeDriver automatically
        WebDriverManager.chromedriver().setup();

        // Set Chrome options
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--remote-allow-origins=*");

        // Initialize WebDriver
        URL hubURL = new URL(getHubURL());
        driver = new RemoteWebDriver(hubURL, options);
        extent = ExtentManager.getInstance();
        test = extent.createTest("Verify Google Title");
    }

    @Test
    public void loginTest() throws IOException {
        String userID = getUser();
        String pwd = getPassword();

        // Open the login page
        driver.get("https://practicetestautomation.com/practice-test-login/");
        test.log(Status.INFO, "Launching Google");

        // Find username field and enter value
        WebElement username = driver.findElement(By.id("username"));
        username.sendKeys(userID);

        // Find password field and enter value
        WebElement password = driver.findElement(By.id("password"));
        password.sendKeys(pwd);

        // Click the login button
        WebElement loginButton = driver.findElement(By.id("submit"));
        loginButton.click();

        // Verify successful login
        WebElement successMessage = driver.findElement(By.xpath("//h1[contains(text(),'Logged In Successfully')]"));
        Assert.assertTrue(successMessage.isDisplayed(), "Login failed!");

        // Capture screenshot after successful login
        String screenshotPath = captureScreenshot("successfulLogin");
        test.pass("Login Successful", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());

        // Print success message
        System.out.println("User ID & Password" + userID + ":" + pwd);
        System.out.println("Login Successful! Page Title: " + driver.getTitle());
        test.pass("Test Passed Successfully");
    }

    @AfterTest
    public void teardown() {
        // Close the browser
        if (driver != null) {
            driver.quit();
        }
        test.log(Status.INFO, "Browser Closed");
        extent.flush();  // Save the report
    }

    public static String getUser() throws IOException {
        if (isRunningOnGitHub()) {
            return System.getenv("SELENIUM_USERNAME");
        } else {
            properties = new Properties();
            FileInputStream fileInputStream = new FileInputStream("C:/Users/swami/Downloads/configuration.properties");
            properties.load(fileInputStream);
            return properties.getProperty("loginID");
        }
    }

    public String getPassword() throws IOException {
        if (isRunningOnGitHub()) {
            return System.getenv("SELENIUM_PASSWORD");
        } else {
            properties = new Properties();
            FileInputStream fileInputStream = new FileInputStream("C:/Users/swami/Downloads/configuration.properties");
            properties.load(fileInputStream);
            return properties.getProperty("loginPWD");
        }
    }

    public String getHubURL() throws IOException {
        /*if (isRunningOnGitHub()) {
            return System.getenv("SELENIUM_GRID_URL");
        } else {
            return "http://localhost:4444/wd/hub";
        }*/
        
         return "http://localhost:4444/wd/hub";
    }

    public static boolean isRunningOnGitHub() {
        return "true".equalsIgnoreCase(System.getenv("GITHUB_ACTIONS"));
    }

    // Method to capture screenshot and save it to a file
    private String captureScreenshot(String screenshotName) throws IOException {
        // Capture screenshot
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        // Define the directory and file path where the screenshot will be saved
        String destDir = System.getProperty("user.dir") + "/test-output/screenshots/";
        Path destinationPath = Paths.get(destDir + screenshotName + ".png");

        // Create directories if they don't exist
        Files.createDirectories(destinationPath.getParent());

        // Copy the screenshot to the desired location
        File destFile = destinationPath.toFile();
        Files.copy(srcFile.toPath(), destFile.toPath());

        // Return the absolute path of the screenshot
        return destFile.getAbsolutePath();
    }
}
