package com.propertymonitor.tests;

import com.propertymonitor.base.BaseTest;
import com.propertymonitor.pages.LoginPage;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;

public class LoginPageTest extends BaseTest {

    private LoginPage loginPage;

    @BeforeClass
    public void setup() {
        loginPage = new LoginPage(driver, wait);
        // Perform login once here if needed (optional)
        loginPage.setupAndLoginIfNeeded();
    }

    @BeforeMethod
    public void startExtent(Method method) {
        extentTest = createTest("LoginTest - " + method.getName());
        extentTest.info("🔧 Starting test: " + method.getName());
    }

    @AfterMethod
    public void captureResult(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            extentTest.fail(result.getThrowable());
        } else if (result.getStatus() == ITestResult.SKIP) {
            extentTest.skip("Test skipped.");
        } else {
            extentTest.pass("Test passed.");
        }
    }

    @Test(priority = 1)
    public void testValidLoginLandsOnProjectSearch() {
        extentTest = extent.createTest("🧪 Test Case 1: Verify user is logged in with valid login details");

        String expectedUrlBase = loginPage.getProjectSearchUrl().split("\\?")[0];
        boolean result = driver.getCurrentUrl().contains(expectedUrlBase);

        if (result) {
            extentTest.pass("✅ Login successful. User successfully logged in and landed on Project Search page.");
        } else {
            extentTest.fail("❌ Login failed. Current URL: " + driver.getCurrentUrl());
        }

        assert result;
    }

    @Test(priority = 2)
    public void verifyProjectSearchUrlLoaded() {
        extentTest = extent.createTest("🧪 Test Case 2: Verify Project Search page is loaded correctly");

        String expectedUrl = loginPage.getProjectSearchUrl().split("\\?")[0];
        boolean result = driver.getCurrentUrl().contains(expectedUrl);

        if (result) {
            extentTest.pass("✅ User is on the correct Project Search URL.");
        } else {
            extentTest.fail("❌ Incorrect Project Search URL. Current: " + driver.getCurrentUrl());
        }

        assert result;
    }
}
