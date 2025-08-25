package com.propertymonitor.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
import java.util.Properties;
import java.util.Set;

public class LoginPage {

    private WebDriver driver;
    private WebDriverWait wait;

    private static final String CONFIG_FILE = "config.properties";
    private static final String COOKIES_FILE = "cookies.data";

    private String loginUrl;
    private String projectSearchUrl;
    private String email;
    private String password;

    private By emailField = By.xpath("//input[@data-test-id='email']");
    private By passwordField = By.xpath("//input[@data-test-id='password']");
    private By loginButton = By.xpath("//button[text()='Sign In']");
    private By postLoginElement = By.cssSelector("[data-test-id='list-scroll-wrapper']");

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void setupAndLoginIfNeeded() {
        try {
            String environment = getTargetEnvironmentHelper();
            loadConfig(environment);

            driver.get(loginUrl);
            loadCookies();

            // Refresh to check if login persists
            driver.navigate().refresh();
            Thread.sleep(2000);

            if (!driver.getCurrentUrl().contains("auth/login")) {
                System.out.println("Already logged in via cookies.");
                return;
            }

            login(email, password);
            saveCookies();

        } catch (Exception e) {
            System.err.println("Login setup failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void login(String email, String password) throws InterruptedException {
        wait.until(ExpectedConditions.urlContains("auth/login"));

        Thread.sleep(9000);
        
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailField)).clear();
        driver.findElement(emailField).sendKeys(email);

        Thread.sleep(9000);
        
        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField)).clear();
        driver.findElement(passwordField).sendKeys(password);

        Thread.sleep(9000);
        
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();

        // Wait for post-login confirmation (projects page or specific element)
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("/projects/project-search"),
                ExpectedConditions.visibilityOfElementLocated(postLoginElement)
        ));

        System.out.println("Login successful. Current URL: " + driver.getCurrentUrl());

        if (driver.getCurrentUrl().contains("/auth/login")) {
            throw new IllegalStateException("Login failed or redirected back to login page.");
        }
    }

    private void loadConfig(String environment) throws IOException {
        Properties config = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (inputStream == null) throw new IOException("Config file not found.");
            config.load(inputStream);

            String prefix = environment + ".";

            String baseUrl = config.getProperty(prefix + "baseUrl");
            if (baseUrl == null || baseUrl.isEmpty()) {
                throw new IllegalArgumentException("baseUrl is missing in config for environment: " + environment);
            }

            if (!baseUrl.endsWith("/")) {
                baseUrl += "/";
            }

            loginUrl = baseUrl + "auth/login";
            projectSearchUrl = config.getProperty(prefix + "projectSearchUrl");
            email = config.getProperty(prefix + "email");
            password = config.getProperty(prefix + "password");

            if (email == null || password == null || projectSearchUrl == null) {
                throw new IllegalArgumentException("Missing login credentials or projectSearchUrl in config.");
            }
        }
    }

    private void loadCookies() {
        try (FileInputStream fileIn = new FileInputStream(COOKIES_FILE);
             ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {

            @SuppressWarnings("unchecked")
			Set<Cookie> cookies = (Set<Cookie>) objectIn.readObject();
            driver.manage().deleteAllCookies();
            for (Cookie cookie : cookies) {
                driver.manage().addCookie(cookie);
            }
            System.out.println("Cookies loaded.");
        } catch (Exception e) {
            System.out.println("No cookies loaded: " + e.getMessage());
        }
    }

    private void saveCookies() {
        try (FileOutputStream fileOut = new FileOutputStream(COOKIES_FILE);
             ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {
            objectOut.writeObject(driver.manage().getCookies());
            System.out.println("Cookies saved.");
        } catch (IOException e) {
            System.out.println("Failed to save cookies: " + e.getMessage());
        }
    }

    private String getTargetEnvironmentHelper() throws IOException {
        Properties envProps = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test-environment.properties")) {
            if (inputStream == null) {
                System.out.println("test-environment.properties not found. Defaulting to 'stage'.");
                return "stage";
            }
            envProps.load(inputStream);
            String environment = envProps.getProperty("environment");
            return (environment != null && !environment.isEmpty()) ? environment : "stage";
        }
    }

    // Corrected getter method (capital P in Project)
    public String getProjectSearchUrl() {
        return this.projectSearchUrl;
    }
}