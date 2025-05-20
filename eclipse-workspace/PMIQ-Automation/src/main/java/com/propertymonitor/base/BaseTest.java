package com.propertymonitor.base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.propertymonitor.pages.LoginPage;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.testng.annotations.*;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;

public class BaseTest {

    protected static WebDriver driver;
    protected static WebDriverWait wait;
    protected static ExtentReports extent;
    protected static ExtentTest extentTest;

    protected static String baseUrl;
    protected static String email;
    protected static String password;
    protected static String environment;

    private static final Duration DEFAULT_WAIT_TIMEOUT = Duration.ofSeconds(10);

    @BeforeSuite
    public void initializeDriverAndReports() throws IOException {
        System.out.println("Initializing WebDriver and ExtentReports...");

        environment = getTargetEnvironmentHelper();
        loadEnvironmentConfig(environment);

        WebDriverManager.firefoxdriver().setup();

        FirefoxOptions options = new FirefoxOptions();
        options.setProfile(new FirefoxProfile()); // Clean profile

        driver = new FirefoxDriver(options);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, DEFAULT_WAIT_TIMEOUT);

        // Extent Report setup
        String reportPath = System.getProperty("user.dir") + "/test-output/ExtentReport.html";
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
        sparkReporter.config().setReportName("Property Monitor Test Suite");
        sparkReporter.config().setDocumentTitle("Automation Report");
        sparkReporter.config().setTheme(Theme.STANDARD);

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        extent.setSystemInfo("Tester", "QA Automation");
        extent.setSystemInfo("Browser", "Firefox");

        System.out.println("WebDriver and ExtentReports initialized.");

        // ✅ Do login only once after driver is initialized
        performLoginOnce();
    }

    /**
     * Login once for the entire suite. Skips if already on project search page.
     */
    private void performLoginOnce() {
        try {
            System.out.println("Performing initial login...");

            if (!baseUrl.endsWith("/")) {
                baseUrl += "/";
            }

            driver.get(baseUrl + "auth/login");

            String currentUrl = driver.getCurrentUrl();
            if (currentUrl.contains("/projects/project-search")) {
                System.out.println("Already logged in. Skipping login.");
                return;
            }

            LoginPage loginPage = new LoginPage(driver, wait);
            loginPage.login(email, password);

            // Wait for element that confirms page is loaded
            wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("div[data-test-id='list-scroll-wrapper']")
            ));

            System.out.println("Login successful. Current URL: " + driver.getCurrentUrl());

        } catch (Exception e) {
            System.err.println("Initial login failed: " + e.getMessage());
            throw new RuntimeException("Login failed, stopping test run.", e);
        }
    }

    @AfterSuite
    public void quitDriverAndFlushReports() {
        if (driver != null) {
            driver.quit();
            System.out.println("WebDriver quit by BaseTest @AfterSuite.");
        }

        if (extent != null) {
            extent.flush();
            System.out.println("ExtentReports flushed. Report generated.");
        }
    }

    protected static ExtentTest createTest(String testName) {
        if (extent == null) {
            throw new IllegalStateException("ExtentReports is not initialized.");
        }
        return extent.createTest(testName);
    }

    protected static String getTargetEnvironmentHelper() throws IOException {
        Properties envProps = new Properties();
        try (InputStream input = BaseTest.class.getClassLoader().getResourceAsStream("test-environment.properties")) {
            if (input == null) {
                System.out.println("test-environment.properties not found. Defaulting to 'stage'.");
                return "stage";
            }
            envProps.load(input);
            return envProps.getProperty("environment", "stage");
        }
    }

    protected static void loadEnvironmentConfig(String env) throws IOException {
        Properties configProps = new Properties();
        String filename = "config.properties";

        try (InputStream input = BaseTest.class.getClassLoader().getResourceAsStream(filename)) {
            if (input == null) {
                throw new IOException("Configuration file " + filename + " not found.");
            }
            configProps.load(input);

            baseUrl = configProps.getProperty(env + ".baseUrl");
            email = configProps.getProperty(env + ".email");
            password = configProps.getProperty(env + ".password");

            if (baseUrl == null || email == null || password == null) {
                throw new IllegalArgumentException("Missing required properties in " + filename + " for environment: " + env);
            }

            System.out.println("Loaded configuration for environment: " + env);
        }
    }

    public static WebDriver getDriver() {
        return driver;
    }
}
