package com.propertymonitor.base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.propertymonitor.utils.TokenLoginUtil;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.safari.SafariDriver;
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

    protected static String environment;
    public static String browserName;
    protected String baseUrl;
    private static final Duration DEFAULT_WAIT_TIMEOUT = Duration.ofSeconds(10);
    private static Properties configProps;

    @BeforeSuite(alwaysRun = true)
    @Parameters({"browser", "environment"})
    public void initializeDriverAndReports(@Optional("firefox") String browser, @Optional("stage") String env) throws IOException  {
        browserName = browser;
        environment = env.toLowerCase();

        // Load config.properties once
        configProps = new Properties();
        try (InputStream input = BaseTest.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new IOException("Configuration file config.properties not found in resources.");
            }
            configProps.load(input);
        }

        // Setup driver
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

        // Initialize baseUrl so subclasses can access it
        baseUrl = getEnvProperty("baseUrl");

        // Setup Extent report
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
        extent.setSystemInfo("Browser", browserName);

        performApiLoginOnce();
    }

    private void performApiLoginOnce() {
        try {
            System.out.println("Performing API token login via cookies...");

            String projectSearchUrl = getEnvProperty("projectSearchUrl");
            String apiUrl = getEnvProperty("apiURL");
            String email = getEnvProperty("email");
            String password = getEnvProperty("password");
            String captchaToken = getEnvProperty("CaptchaTestToken");
            String deviceId = getEnvProperty("deviceId");

            TokenLoginUtil.loginAndInject(driver, baseUrl, projectSearchUrl, apiUrl, email, password, deviceId, captchaToken);

            System.out.println("✅ API login successful, now at: " + driver.getCurrentUrl());
        } catch (Exception e) {
            System.err.println("❌ API login failed: " + e.getMessage());
            throw new RuntimeException("API login failed, stopping test run.", e);
        }
    }

    private String getEnvProperty(String key) {
        String fullKey = environment + "." + key;
        String value = configProps.getProperty(fullKey);
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Missing property: " + fullKey);
        }
        return value;
    }

    @AfterClass(alwaysRun = true)
    public void afterClassDelay() {
        try {
            System.out.println("Waiting 5 seconds before next test class...");
            Thread.sleep(5000);  // 5 seconds delay
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @AfterTest(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
         //   driver.quit();
       //     System.out.println("✅ WebDriver quit for this browser: " + browserName);
        }
    }

    @AfterSuite(alwaysRun = true)
    public void flushReportsAndUpload() {
        try {
            if (extent != null) {
                extent.flush();
                System.out.println("✅ ExtentReports flushed. Report generated.");
            }

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

    public static WebDriver getDriver() {
        return driver;
    }
}