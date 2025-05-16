package com.propertymonitor.pages;

import com.propertymonitor.base.BaseTest; // Import the BaseTest class

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
// import org.testng.annotations.AfterClass; // Keep commented or remove
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Properties;
import java.util.Set;


// Extend the BaseTest class
public class LoginPage extends BaseTest { // CHANGE: Extend BaseTest

    private static final String CONFIG_FILE = "config.properties";
    // ENV_CONFIG_FILE constant is not strictly needed here if getTargetEnvironmentHelper is in BaseTest
    private static final String COOKIES_FILE = "cookies.data";
    // REMOVED: private String loginUrl; // Now in BaseTest
    // REMOVED: private String projectSearchUrl; // Now in BaseTest
    // REMOVED: private String email; // Now in BaseTest (consider security implications)
    // REMOVED: private String password; // Now in BaseTest (consider security implications)
    // REMOVED: private WebDriver driver; // Driver is now in BaseTest
    // REMOVED: private WebDriverWait wait; // Wait is now in BaseTest

    // Locators for login page elements
    private By emailField = By.xpath("//input[@data-test-id='email']");
    private By passwordField = By.xpath("//input[@data-test-id='password']");
    private By loginButton = By.xpath("//html/body/div[1]/div[2]/div/div/div/button");
    // private By loginErrorMessage = By.xpath("//div[@class='error-message']"); // Locator for error message if needed

    /**
     * Sets up the configuration, loads cookies, and performs login if necessary
     * using the WebDriver initialized in the BaseTest.
     *
     * @throws IOException if configuration files cannot be read.
     */
    @BeforeClass
    public void setUp() throws IOException {
        System.out.println("--- LoginPage @BeforeClass: Starting setup ---");
        // No need to initialize driver here, it's done in @BeforeSuite in BaseTest

        // Get target environment using the helper from BaseTest
        String targetEnvironment = getTargetEnvironmentHelper(); // Corrected: Use helper from BaseTest
        System.out.println("LoginPage @BeforeClass: Target environment identified as '" + targetEnvironment + "'");

        loadConfig(targetEnvironment); // Load config into BaseTest static variables

        // driver and wait are available from BaseTest

        System.out.println("LoginPage @BeforeClass: Navigating to initial Login URL: " + loginUrl); // Enhanced Logging
        driver.get(loginUrl); // Use the shared static loginUrl from BaseTest
        System.out.println("LoginPage @BeforeClass: Finished initial navigation. Current URL: " + driver.getCurrentUrl()); // Enhanced Logging

        loadCookies(); // loadCookies method will use the shared static 'driver'
        System.out.println("LoginPage @BeforeClass: Cookie loading attempted. Current URL: " + driver.getCurrentUrl()); // Enhanced Logging

        if (!driver.manage().getCookies().isEmpty()) {
            System.out.println("LoginPage @BeforeClass: Cookies found and loaded. Attempting navigation to Project Search URL: " + projectSearchUrl); // Enhanced Logging
            driver.get(projectSearchUrl); // Attempt to navigate to a page requiring login using static projectSearchUrl
            System.out.println("LoginPage @BeforeClass: Finished attempting navigation with cookies. Current URL: " + driver.getCurrentUrl()); // Enhanced Logging

            // Wait for a short time to allow redirection
            try {
                System.out.println("LoginPage @BeforeClass: Sleeping for 3 seconds after cookie navigation attempt."); // Enhanced Logging
                Thread.sleep(3000); // Adjust time as needed
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                 System.err.println("LoginPage @BeforeClass: Thread interrupted during sleep."); // Enhanced Logging
            }
             System.out.println("LoginPage @BeforeClass: Sleep finished. Current URL: " + driver.getCurrentUrl()); // Enhanced Logging

            // Check if we are NOT on the login page after attempting to load the project search page
            if (!driver.getCurrentUrl().contains(loginUrl)) {
                System.out.println("LoginPage @BeforeClass: Using stored cookies. User is likely logged in.");
            } else {
                System.out.println("LoginPage @BeforeClass: Stored cookies are invalid or expired. Performing login.");
                performLogin(); // performLogin uses static variables and navigates
                System.out.println("LoginPage @BeforeClass: performLogin finished."); // Enhanced Logging
                saveCookies(); // saveCookies uses static driver
                 System.out.println("LoginPage @BeforeClass: Cookies saved after login."); // Enhanced Logging
            }
        } else {
            System.out.println("LoginPage @BeforeClass: No cookies found. Performing login."); // Enhanced Logging
            performLogin(); // performLogin uses static variables and navigates
            System.out.println("LoginPage @BeforeClass: performLogin finished."); // Enhanced Logging
            saveCookies(); // saveCookies uses static driver
             System.out.println("LoginPage @BeforeClass: Cookies saved after login."); // Enhanced Logging
        }
         System.out.println("--- LoginPage @BeforeClass finished ALL steps. Final URL: " + driver.getCurrentUrl() + " ---"); // Enhanced Logging
    }

    // REMOVED: getTargetEnvironment() method - now in BaseTest

    /**
     * Loads the application configuration (URLs, email, password)
     * from config.properties based on the specified environment
     * and stores them in static variables in BaseTest.
     *
     * @param environment The target environment (e.g., "stage", "prod").
     * @throws IOException if the configuration file cannot be read.
     * @throws IllegalArgumentException if required configuration properties are missing.
     */
    private void loadConfig(String environment) throws IOException {
        Properties config = new Properties();
        ClassLoader classLoader = getClass().getClassLoader(); // Use LoginPage's class loader
        System.out.println("LoginPage loadConfig: Attempting to load config from '" + CONFIG_FILE + "' for environment '" + environment + "'"); // Enhanced Logging
        try (java.io.InputStream inputStream = classLoader.getResourceAsStream(CONFIG_FILE)) {
            if (inputStream == null) {
                throw new IOException("Could not find " + CONFIG_FILE + " in the classpath");
            }
            config.load(inputStream);
            String prefix = environment + ".";

            // Populate static variables in BaseTest
            BaseTest.loginUrl = config.getProperty(prefix + "baseUrl");
             if (BaseTest.loginUrl != null && !BaseTest.loginUrl.endsWith("/")) {
                BaseTest.loginUrl += "/"; // Ensure base URL ends with a slash
            }
            BaseTest.loginUrl += "auth/login"; // Append the /auth/login endpoint

            BaseTest.projectSearchUrl = config.getProperty(prefix + "projectSearchUrl");
            BaseTest.email = config.getProperty(prefix + "email");
            BaseTest.password = config.getProperty(prefix + "password"); // Note: Storing password statically is a security risk

            if (BaseTest.loginUrl == null || BaseTest.projectSearchUrl == null || BaseTest.email == null || BaseTest.password == null) {
                throw new IllegalArgumentException("Configuration not found for environment: " + environment + ". Ensure "
                        + prefix + "baseUrl, " + prefix + "projectSearchUrl, " + prefix + "email, and " + prefix + "password are set in " + CONFIG_FILE);
            }
            System.out.println("LoginPage loadConfig: Loaded configuration successfully."); // Enhanced Logging
            System.out.println("  Login URL: " + BaseTest.loginUrl);
            System.out.println("  Project Search URL: " + BaseTest.projectSearchUrl);
            // Note: Avoid printing password in logs
        } catch (IOException e) {
            System.err.println("LoginPage loadConfig: Error loading config: " + e.getMessage()); // Enhanced Logging
            throw e;
        } catch (IllegalArgumentException e) {
             System.err.println("LoginPage loadConfig: Configuration missing: " + e.getMessage()); // Enhanced Logging
             throw e;
        }
    }


    /**
     * Loads cookies from a file to restore a previous session.
     * If successful, adds the cookies to the current WebDriver instance.
     */
    @SuppressWarnings("unchecked")
    private void loadCookies() {
        System.out.println("LoginPage loadCookies: Attempting to load cookies from '" + COOKIES_FILE + "'"); // Enhanced Logging
        try (FileInputStream fileIn = new FileInputStream(COOKIES_FILE)) {
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            Set<org.openqa.selenium.Cookie> cookies = (Set<org.openqa.selenium.Cookie>) objectIn.readObject();
            driver.manage().deleteAllCookies(); // Use static driver from BaseTest
            System.out.println("LoginPage loadCookies: Existing cookies cleared."); // Enhanced Logging
            int addedCount = 0;
            for (org.openqa.selenium.Cookie cookie : cookies) {
                 try {
                      // More robust domain check
                      String currentDomain = driver.getCurrentUrl().replaceFirst("https?://", "").split("/")[0];
                      String cookieDomain = cookie.getDomain().replaceFirst("^\\.", ""); // Remove leading dot if present

                      if (currentDomain.equals(cookieDomain) || currentDomain.endsWith("." + cookieDomain) || cookieDomain.equals("localhost")) {
                         driver.manage().addCookie(cookie); // Use static driver from BaseTest
                         // System.out.println("  Added cookie: " + cookie.getName() + " for domain " + cookie.getDomain()); // Detailed logging
                         addedCount++;
                     } else {
                         // System.out.println("  Skipped cookie (domain mismatch): " + cookie.getName() + " for domain " + cookie.getDomain() + " on current domain " + currentDomain); // Detailed logging
                     }
                 } catch (Exception e) {
                      System.err.println("LoginPage loadCookies: Error adding cookie " + cookie.getName() + ": " + e.getMessage()); // Enhanced Logging
                 }
            }
            System.out.println("LoginPage loadCookies: Successfully loaded and added " + addedCount + " cookies from " + COOKIES_FILE); // Enhanced Logging
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("LoginPage loadCookies: Warning: Could not load cookies from " + COOKIES_FILE + ". File might not exist or is corrupt. " + e.getMessage()); // Enhanced Logging
            // e.printStackTrace(); // Uncomment for detailed exception trace
        }
    }

    /**
     * Saves the current WebDriver cookies to a file for future sessions.
     */
    private void saveCookies() {
        System.out.println("LoginPage saveCookies: Attempting to save cookies to '" + COOKIES_FILE + "'"); // Enhanced Logging
        try (FileOutputStream fileOut = new FileOutputStream(COOKIES_FILE);
             ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {
            objectOut.writeObject(driver.manage().getCookies()); // Use static driver from BaseTest
            System.out.println("LoginPage saveCookies: Cookies saved successfully to " + COOKIES_FILE + "."); // Enhanced Logging
        } catch (IOException e) {
            System.err.println("LoginPage saveCookies: Error saving cookies: " + e.getMessage()); // Enhanced Logging
            // e.printStackTrace(); // Uncomment for detailed exception trace
        }
    }


    /**
     * Performs the user login process by entering credentials and clicking the login button.
     * Waits for the project search page URL to load after login.
     */
    private void performLogin() {
        System.out.println("LoginPage performLogin: Starting user login with email: " + email + "..."); // Use static email from BaseTest
        try {
            // Ensure we are on the login page before attempting to log in
            System.out.println("LoginPage performLogin: Waiting for URL to contain login URL: " + loginUrl + ". Current URL: " + driver.getCurrentUrl()); // Enhanced Logging
            wait.until(ExpectedConditions.urlContains(loginUrl)); // Use static loginUrl from BaseTest
            System.out.println("LoginPage performLogin: Confirmed URL contains login URL. Proceeding with login."); // Enhanced Logging


            System.out.println("LoginPage performLogin: Entering credentials and clicking login."); // Enhanced Logging
            wait.until(ExpectedConditions.visibilityOfElementLocated(emailField));
            driver.findElement(emailField).sendKeys(email); // Use static driver and email
            wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField));
            driver.findElement(passwordField).sendKeys(password); // Use static driver and password
             wait.until(ExpectedConditions.elementToBeClickable(loginButton));
            driver.findElement(loginButton).click();
             System.out.println("LoginPage performLogin: Login button clicked. Waiting for redirection..."); // Enhanced Logging

            // Wait for the URL to change to the project search page
            String targetUrl = projectSearchUrl.split("\\?")[0]; // Use static projectSearchUrl from BaseTest
            System.out.println("LoginPage performLogin: Waiting for URL to contain project search base URL: " + targetUrl); // Enhanced Logging
            wait.until(ExpectedConditions.urlContains(targetUrl)); // Use static wait
            System.out.println("LoginPage performLogin: Login successful. Redirected to " + driver.getCurrentUrl()); // Enhanced Logging
        } catch (Exception e) {
             System.err.println("LoginPage performLogin: Login process failed: " + e.getMessage()); // Enhanced Logging
             Assert.fail("Login process failed: " + e.getMessage()); // This should stop the test suite configuration
        }
    }


    @Test(priority = 1)
    public void testValidLogin() {
         System.out.println("--- Running Test Case 1: testValidLogin ---"); // Enhanced Logging
         String targetUrl = projectSearchUrl.split("\\?")[0]; // Use static projectSearchUrl
        Assert.assertTrue(driver.getCurrentUrl().contains(targetUrl), "Login failed: User not redirected to Project Search page. Current URL: " + driver.getCurrentUrl()); // Use static driver
        System.out.println("--- Test Case 1: Valid login successful. User is on the Project Search page. ---"); // Enhanced Logging
    }

    @Test(priority = 2)
    public void verifyProjectSearchPageURL() {
        System.out.println("--- Running Test Case 2: verifyProjectSearchPageURL ---"); // Enhanced Logging
         String targetUrl = projectSearchUrl.split("\\?")[0]; // Use static projectSearchUrl
        Assert.assertTrue(driver.getCurrentUrl().contains(targetUrl), "Test Failed: Current URL does not contain " + targetUrl + ". Current URL: " + driver.getCurrentUrl()); // Use static driver
        System.out.println("--- Test Case 2: Verified user is on the Project Search page. URL contains " + targetUrl + " ---"); // Enhanced Logging
    }

}