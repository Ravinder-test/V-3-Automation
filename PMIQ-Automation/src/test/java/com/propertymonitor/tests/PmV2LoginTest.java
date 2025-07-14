package com.propertymonitor.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class PmV2LoginTest {

    static String loginUrl = "https://stage.propertymonitor.ae/v2/no-access.php";
    static String expectedUrlFragment = "/v2/developers-dashboard.php";
    static String email = "ravinder@oktopi.ai";
    static String password = "Ravi123!";

    public static void main(String[] args) {
        WebDriverManager.firefoxdriver().setup();

        System.out.println("🔍 Running test in NON-headless mode...");
        runTest(false);

        System.out.println("\n🔍 Running test in HEADLESS mode...");
        runTest(true);
    }

    public static void runTest(boolean isHeadless) {
        FirefoxOptions options = new FirefoxOptions();

        if (isHeadless) {
            options.addArguments("-headless");
        }

        WebDriver driver = new FirefoxDriver(options);
        driver.manage().window().maximize();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        try {
            driver.get(loginUrl);

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='username']")))
                .sendKeys(email);
            driver.findElement(By.xpath("//input[@id='pword']")).sendKeys(password);

            // Wait for Cloudflare spinner to disappear
            WebDriverWait spinnerWait = new WebDriverWait(driver, Duration.ofSeconds(30));
            spinnerWait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".se-pre-con")));

            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='login']"))).click();

            Thread.sleep(4000);

            String currentUrl = driver.getCurrentUrl();
            System.out.println("🔎 Current URL: " + currentUrl);

            if (currentUrl.contains(expectedUrlFragment)) {
                System.out.println("✅ Test Passed: Successfully logged in and landed on expected page.");
            } else {
                System.out.println("❌ Test Failed: Login did not reach expected dashboard.");
            }

        } catch (Exception e) {
            System.out.println("❗ Exception occurred: " + e.getMessage());
        } finally {
          //  driver.quit();
        }
    }
}