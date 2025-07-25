package com.propertymonitor.base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.propertymonitor.pages.LoginPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.*;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.testng.annotations.*;

import java.io.*;
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
    public static String browserName; // ✅ Used for tagging tests by browser
    private static final Duration DEFAULT_WAIT_TIMEOUT = Duration.ofSeconds(10);

    @BeforeTest(alwaysRun = true)
    @Parameters({ "browser", "environment" })
    public void initializeDriverAndReports(@Optional("firefox") String browser, @Optional("stage") String env) throws IOException {
        browserName = browser; // ✅ Capture current test browser
        environment = env;
        loadEnvironmentConfig(environment);

        // Setup driver per browser
        if (browser.equalsIgnoreCase("firefox")) {
            WebDriverManager.firefoxdriver().setup();
            FirefoxOptions options = new FirefoxOptions();
            options.setProfile(new FirefoxProfile());
            driver = new FirefoxDriver(options);
        } else if (browser.equalsIgnoreCase("safari")) {
            driver = new SafariDriver();
        } else {
            throw new IllegalArgumentException("Unsupported browser: " + browser);
        }

        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, DEFAULT_WAIT_TIMEOUT);

        // Setup Extent report once
        if (extent == null) {
            String reportPath = System.getProperty("user.dir") + "/test-output/ExtentReport.html";
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setReportName("Property Monitor Test Suite");
            sparkReporter.config().setDocumentTitle("Automation Report");
            sparkReporter.config().setTheme(Theme.STANDARD);

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
            extent.setSystemInfo("Tester", "Ravinder Singh");
        }

        extent.setSystemInfo("Browser", browserName); // ✅ Set browser info per <test>

        performLoginOnce();
    }

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

            wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("div[data-test-id='list-scroll-wrapper']")
            ));

            System.out.println("Login successful. Current URL: " + driver.getCurrentUrl());

        } catch (Exception e) {
            System.err.println("Initial login failed: " + e.getMessage());
            throw new RuntimeException("Login failed, stopping test run.", e);
        }
    }

    @AfterTest(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("✅ WebDriver quit for this browser: " + browserName);
        }
    }

    @AfterSuite(alwaysRun = true)
    public void flushReportsAndUpload() {
        try {
            if (extent != null) {
                extent.flush();
                System.out.println("✅ ExtentReports flushed. Report generated.");
            }

            // Upload to GitHub
            String os = System.getProperty("os.name").toLowerCase();
            String bashCommand = os.contains("win") ? "C:\\Program Files\\Git\\bin\\bash.exe" : "bash";

            System.out.println("🔄 Uploading Extent HTML report to GitHub...");
            ProcessBuilder pb = new ProcessBuilder(bashCommand, "upload-report.sh");
            pb.directory(new File(System.getProperty("user.dir")));
            Process process = pb.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                 BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {

                String line;
                while ((line = reader.readLine()) != null) System.out.println(line);
                while ((line = errorReader.readLine()) != null) System.err.println(line);
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("✅ Report uploaded successfully to GitHub Pages.");
            } else {
                System.err.println("❌ Report upload failed with exit code: " + exitCode);
            }

        } catch (IOException | InterruptedException e) {
            System.err.println("❌ Error in AfterSuite: " + e.getMessage());
            e.printStackTrace();
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