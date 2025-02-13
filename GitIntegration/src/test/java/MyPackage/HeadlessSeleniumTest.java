package MyPackage;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

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
	static Properties properties;
    
    @BeforeTest
    public void beforeTest() throws IOException {
        // Use WebDriverManager to set up ChromeDriver automatically
        WebDriverManager.chromedriver().setup();
    	//System.setProperty("webdriver.chrome.driver", "C:/Users/swami/Downloads/chromedriver-win64/chromedriver.exe");


        // Set Chrome options
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Uncomment for headless mod
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--remote-allow-origins=*");

        // Initialize WebDriver
         driver = new ChromeDriver(options);

         
         properties = new Properties();
         FileInputStream fileInputStream = new FileInputStream("C:/Users/swami/Downloads/configuration.properties");
         properties.load(fileInputStream);
    }
    
    @Test
    public void loginTest() {
    	
    	String userID = getUser();
        String pwd = getPassword();
        // Open the login page
        driver.get("https://practicetestautomation.com/practice-test-login/");

        // Find username field and enter value
        WebElement username = driver.findElement(By.id("username"));
       // username.sendKeys("student"); // Valid username
        username.sendKeys(userID);

        // Find password field and enter value
        WebElement password = driver.findElement(By.id("password"));
        //password.sendKeys("Password123"); // Valid password
        password.sendKeys(pwd); // Valid password

        // Click the login button
        WebElement loginButton = driver.findElement(By.id("submit"));
        loginButton.click();
        
        

        // Verify successful login
        WebElement successMessage = driver.findElement(By.xpath("//h1[contains(text(),'Logged In Successfully')]"));
        Assert.assertTrue(successMessage.isDisplayed(), "Login failed!");

        // Print success message
        System.out.println("User ID & Password"+userID+":"+pwd);
        System.out.println("Login Successful! Page Title: " + driver.getTitle());
    }

    @AfterTest
    public void teardown() {
        // Close the browser
        if (driver != null) {
            driver.quit();
        }
    }

        public static String getUser() {
            // Read from GitHub Secrets if available, otherwise use local properties
            return System.getenv("SELENIUM_USERNAME") != null ? System.getenv("SELENIUM_USERNAME") : properties.getProperty("loginID");
        }

        public String getPassword() {
            return System.getenv("SELENIUM_PASSWORD") != null ? System.getenv("SELENIUM_PASSWORD") : properties.getProperty("loginPWD");
        }

}