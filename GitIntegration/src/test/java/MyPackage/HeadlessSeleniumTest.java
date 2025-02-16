package MyPackage;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
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
    	//System.setProperty("webdriver.chrome.driver", "C:/Users/swami/Downloads/chromedriver-win64/chromedriver.exe");


        // Set Chrome options
        ChromeOptions options = new ChromeOptions();
        //options.addArguments("--headless"); // Uncomment for headless mod
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--remote-allow-origins=*");

        // Initialize WebDriver
         //driver = new ChromeDriver(options);
        //URL hubUrl = new URL("http://192.168.1.80:4444/wd/hub");
        URL hubURL = new URL(getHubURL());
         driver = new RemoteWebDriver(hubURL,options);
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
        	
        	if(isRunningOnGitHub())
            { 
              return System.getenv("SELENIUM_USERNAME");
            }
        	else
        	{
        		properties = new Properties();
                FileInputStream fileInputStream = new FileInputStream("C:/Users/swami/Downloads/configuration.properties");
                properties.load(fileInputStream);
        	  return properties.getProperty("loginID");	
        	}
         }

        public String getPassword() throws IOException {
        	if(isRunningOnGitHub())
            { 
              return System.getenv("SELENIUM_PASSWORD");
            }
        	else
        	{
        		properties = new Properties();
                FileInputStream fileInputStream = new FileInputStream("C:/Users/swami/Downloads/configuration.properties");
                properties.load(fileInputStream);
        	  return properties.getProperty("loginPWD");	
        	}}
        
        public String getHubURL() throws IOException {
        	if(isRunningOnGitHub())
            { 
              return System.getenv("SELENIUM_GRID_URL");
            }
        	else
        	{	
        	  return "http://localhost:4444/wd/hub";	
        	}}
        
        
        
        public static boolean isRunningOnGitHub() {
            return "true".equalsIgnoreCase(System.getenv("GITHUB_ACTIONS"));
        }

}