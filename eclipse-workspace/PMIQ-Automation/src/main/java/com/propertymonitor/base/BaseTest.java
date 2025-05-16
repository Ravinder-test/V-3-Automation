package com.propertymonitor.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.ProfilesIni;
import org.openqa.selenium.support.ui.WebDriverWait;
//import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

public class BaseTest {

    protected static WebDriver driver;
    protected static WebDriverWait wait;
    private static final Duration DEFAULT_WAIT_TIMEOUT = Duration.ofSeconds(10);

    // Add static variables to hold configuration properties
    protected static String loginUrl;
    protected static String projectSearchUrl;
    protected static String email; // Consider if email/password should really be shared or just used in LoginPage
    protected static String password; // <-- Security risk: Avoid making password static and accessible globally if possible

    /**
     * Initializes the WebDriver before the test suite starts.
     */
    @BeforeSuite
    public void initializeDriver() {
        System.out.println("Initializing WebDriver for the test suite...");
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();

        // Configure Firefox profile
        ProfilesIni profileIni = new ProfilesIni();
        String profileToUseName = "default"; // The name of the profile you want to use
        FirefoxProfile profile = profileIni.getProfile(profileToUseName);

        if (profile == null) {
            profile = new FirefoxProfile();
            System.out.println("Named Firefox profile '" + profileToUseName + "' not found. Creating a new, unnamed profile.");
        } else {
            System.out.println("Using named Firefox profile: '" + profileToUseName + "'");
        }

        options.setProfile(profile);

        driver = new FirefoxDriver(options);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, DEFAULT_WAIT_TIMEOUT);
        System.out.println("WebDriver initialized.");
    }

    /**
     * Quits the WebDriver after the test suite finishes.
     */
//    @AfterSuite
//    public void quitDriver() {
//        if (driver != null) {
//            driver.quit();
//            System.out.println("WebDriver quit by BaseTest @AfterSuite.");
//        }
//    }

     // Helper method to get target environment - put it here so all pages can use it
    protected static String getTargetEnvironmentHelper() throws IOException {
         Properties envProps = new Properties();
        ClassLoader classLoader = BaseTest.class.getClassLoader(); // Use BaseTest class loader
        try (java.io.InputStream inputStream = classLoader.getResourceAsStream("test-environment.properties")) {
             if (inputStream == null) {
                 System.out.println("Could not find test-environment.properties in the classpath. Defaulting to 'stage'.");
                 return "stage";
            }
            envProps.load(inputStream);
            String environment = envProps.getProperty("environment");
            return (environment != null && !environment.isEmpty()) ? environment : "stage";
         } catch (IOException e) {
            System.err.println("Error reading test-environment.properties: " + e.getMessage());
            throw e; // Re-throw as loading environment is crucial
        }
    }

    // You can add helper methods here that test classes might need
}