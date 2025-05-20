package com.propertymonitor.tests;

import com.propertymonitor.base.BaseTest;
import com.propertymonitor.pages.ProjectSearchPage;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;

public class ProjectSearchTest extends BaseTest {

    private ProjectSearchPage projectSearchPage;

    @BeforeClass
    public void setupProjectSearch() {
        // Initialize the ProjectSearchPage
        projectSearchPage = new ProjectSearchPage();
        // No URL or login verification needed here as user is already logged in and on the page
    }

    @BeforeMethod
    public void startTestLog(Method method) {
        extentTest = createTest("ProjectSearchTest - " + method.getName());
    }

    @AfterMethod
    public void logTestResult(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            extentTest.fail(result.getThrowable());
        } else if (result.getStatus() == ITestResult.SKIP) {
            extentTest.skip("Test skipped");
        } else {
            extentTest.pass("Test passed");
        }
    }

    @Test(priority = 1, dependsOnMethods = {"com.propertymonitor.tests.LoginPageTest.testValidLoginLandsOnProjectSearch"})
    public void testSearchLocationUpdatesProjectCount() {
        extentTest = extent.createTest("🔍 Test Case 3: Verify project count updates after location search");

        String before = projectSearchPage.getProjectCount();
        extentTest.info("Project count before search: " + before);

        projectSearchPage.searchLocation("Jumeirah Beach");

        String after = projectSearchPage.getProjectCount();
        extentTest.info("Project count after search: " + after);

        Assert.assertNotEquals(after, before, "❌ Project count did not change after search.");
        extentTest.pass("✅ Project count successfully updated after searching location.");
    }

    @Test(priority = 2, dependsOnMethods = {"testSearchLocationUpdatesProjectCount"})
    public void testClearLocationUpdatesProjectCount() throws InterruptedException {
        extentTest = extent.createTest("🧹 Test Case 4: Verify project count reverts after clearing location");

        String before = projectSearchPage.getProjectCount();
        extentTest.info("Project count before clearing location: " + before);

        Thread.sleep(3000);
        projectSearchPage.clearLocation();

        String after = projectSearchPage.getProjectCount();
        extentTest.info("Project count after clearing location: " + after);

        Assert.assertNotEquals(after, before, "❌ Project count did not change after clearing location.");
        extentTest.pass("✅ Project count updated after clearing location.");
    }

    @Test(priority = 3, dependsOnMethods = {"testClearLocationUpdatesProjectCount"})
    public void testSelectApartmentUnitType() throws InterruptedException {
        extentTest = extent.createTest("🏢 Test Case 5: Verify project count after selecting 'Apartment' unit type");

        String before = projectSearchPage.getProjectCount();
        extentTest.info("Project count before selecting Apartment: " + before);

        projectSearchPage.toggleApartmentCheckbox();
        Thread.sleep(3000);

        String after = projectSearchPage.getProjectCount();
        extentTest.info("Project count after selecting Apartment: " + after);

        Assert.assertNotEquals(after, before, "❌ No change in project count after selecting 'Apartment'.");
        extentTest.pass("✅ Project count updated after selecting 'Apartment'.");
    }

    @Test(priority = 4, dependsOnMethods = {"testSelectApartmentUnitType"})
    public void testUnselectApartmentUnitType() throws InterruptedException {
        extentTest = extent.createTest("🏢 Test Case 6: Verify project count after unselecting 'Apartment' unit type");

        String before = projectSearchPage.getProjectCount();
        extentTest.info("Project count before unselecting Apartment: " + before);

        projectSearchPage.toggleApartmentCheckbox();
        Thread.sleep(3000);

        String after = projectSearchPage.getProjectCount();
        extentTest.info("Project count after unselecting Apartment: " + after);

        Assert.assertNotEquals(after, before, "❌ No change in project count after unselecting 'Apartment'.");
        extentTest.pass("✅ Project count updated after unselecting 'Apartment'.");
    }

    @Test(priority = 5, dependsOnMethods = {"testUnselectApartmentUnitType"})
    public void testUncheckUpcomingSaleStatus() throws InterruptedException {
        extentTest = extent.createTest("📉 Test Case 7: Verify project count after unchecking 'Upcoming' sale status");

        String before = projectSearchPage.getProjectCount();
        extentTest.info("Project count before unchecking 'Upcoming': " + before);

        projectSearchPage.toggleUpcomingSaleStatus();
        Thread.sleep(3000);

        String after = projectSearchPage.getProjectCount();
        extentTest.info("Project count after unchecking 'Upcoming': " + after);

        Assert.assertNotEquals(after, before, "❌ No change in project count after unchecking 'Upcoming'.");
        extentTest.pass("✅ Project count updated after unchecking 'Upcoming'.");
    }

    @Test(priority = 6, dependsOnMethods = {"testUncheckUpcomingSaleStatus"})
    public void testSelectUpcomingSaleStatus() {
        extentTest = extent.createTest("📈 Test Case 8: Verify project count after selecting back 'Upcoming' sale status");

        String before = projectSearchPage.getProjectCount();
        extentTest.info("Project count before selecting 'Upcoming': " + before);

        projectSearchPage.toggleUpcomingSaleStatus();

        String after = projectSearchPage.getProjectCount();
        extentTest.info("Project count after selecting 'Upcoming': " + after);

        Assert.assertNotEquals(after, before, "❌ No change in project count after selecting 'Upcoming'.");
        extentTest.pass("✅ Project count updated after selecting 'Upcoming'.");
    }
}
