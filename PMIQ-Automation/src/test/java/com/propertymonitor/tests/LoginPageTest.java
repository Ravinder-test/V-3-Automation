package com.propertymonitor.tests;

import com.propertymonitor.base.BaseTest;
import com.propertymonitor.pages.LoginPage;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;

public class LoginPageTest extends BaseTest {

    private LoginPage loginPage;
    private static final String PAGE_NAME = "Login Page"; // Consistent naming for clearer reports

    @BeforeClass
    public void setup() {
        loginPage = new LoginPage(driver, wait);
        // Removed extentTest.info() here to prevent NullPointerException as extentTest is set in @BeforeMethod.
        // If you need class-level setup logging, consider the separate 'classSetupTest' approach from previous response.
        loginPage.setupAndLoginIfNeeded(); // This still performs the login setup.
    }

    @BeforeMethod
    public void startExtent(Method method) {
        String testDescription = "";
        if (method.isAnnotationPresent(Test.class)) {
            Test testAnnotation = method.getAnnotation(Test.class);
            testDescription = testAnnotation.description();
        }

        if (testDescription.isEmpty()) {
            testDescription = method.getName()
                                .replace("test", "")
                                .replaceAll("([A-Z])", " $1")
                                .trim() + " (Consider adding a specific description for this test)"; // Improved placeholder
        }

        extentTest = extent.createTest("📄 " + PAGE_NAME)
                .assignCategory(PAGE_NAME)
                .info("🔍 Test Case: " + testDescription);
    }

    @AfterMethod
    public void captureResult(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            extentTest.fail("The test failed: " + result.getThrowable()); // More descriptive failure message
        } else if (result.getStatus() == ITestResult.SKIP) {
            extentTest.skip("This test was skipped."); // Clearer skip message
        } else {
            extentTest.pass("The test passed successfully."); // Clearer pass message
        }
    }

    @Test(priority = 1, description = "Verifies that a user with valid login details successfully logs in and lands on the correct page.")
    public void testValidLoginLandsOnProjectSearch() {
        String expectedUrlBase = loginPage.getProjectSearchUrl().split("\\?")[0];
        extentTest.info("Checking if the user is redirected to the Project Search page after logging in.");

        String actualCurrentUrl = driver.getCurrentUrl();
        boolean result = actualCurrentUrl.contains(expectedUrlBase);
        extentTest.info("Current page URL is: " + actualCurrentUrl);

        if (result) {
            extentTest.pass("✅ Login successful. The user landed on the Project Search page as expected.");
        } else {
            extentTest.fail("❌ Login failed. The user did not land on the Project Search page. Current URL: " + actualCurrentUrl);
        }
        Assert.assertTrue(result, "❌ The user was not on the expected Project Search page after valid login.");
    }

    @Test(priority = 2, description = "Verifies that the Project Search page URL is correct after successful login.")
    public void verifyProjectSearchUrlLoaded() {
        String expectedUrl = loginPage.getProjectSearchUrl().split("\\?")[0];
        extentTest.info("Confirming the Project Search page URL is correct.");

        String actualCurrentUrl = driver.getCurrentUrl();
        boolean result = actualCurrentUrl.contains(expectedUrl);
        extentTest.info("Current page URL is: " + actualCurrentUrl);

        if (result) {
            extentTest.pass("✅ The current URL matches the expected Project Search page URL.");
        } else {
            extentTest.fail("❌ The current URL is incorrect for the Project Search page. Expected to contain: " + expectedUrl + ", but found: " + actualCurrentUrl);
        }
        Assert.assertTrue(result, "❌ The Project Search page URL is incorrect.");
    }
}