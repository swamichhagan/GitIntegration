package MyPackage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class HeadlessSeleniumTest {

	WebDriver driver;
    @BeforeTest
    public void beforeTest() {
        // Use WebDriverManager to set up ChromeDriver automatically
        WebDriverManager.chromedriver().setup();
    	//System.setProperty("webdriver.chrome.driver", "C:/Users/swami/Downloads/chromedriver-win64/chromedriver.exe");


        // Set Chrome options
        ChromeOptions options = new ChromeOptions();
        // options.addArguments("--headless"); // Uncomment for headless mode
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--remote-allow-origins=*");

        // Initialize WebDriver
         driver = new ChromeDriver(options);

       
    }
    
    @Test
    public void loginTest() {
        // Open the login page
        driver.get("https://practicetestautomation.com/practice-test-login/");

        // Find username field and enter value
        WebElement username = driver.findElement(By.id("username"));
        username.sendKeys("student"); // Valid username

        // Find password field and enter value
        WebElement password = driver.findElement(By.id("password"));
        password.sendKeys("Password123"); // Valid password

        // Click the login button
        WebElement loginButton = driver.findElement(By.id("submit"));
        loginButton.click();

        // Verify successful login
        WebElement successMessage = driver.findElement(By.xpath("//h1[contains(text(),'Logged In Successfully')]"));
        Assert.assertTrue(successMessage.isDisplayed(), "Login failed!");

        // Print success message
        System.out.println("Login Successful! Page Title: " + driver.getTitle());
    }

    @AfterTest
    public void teardown() {
        // Close the browser
        if (driver != null) {
            driver.quit();
        }

}
}