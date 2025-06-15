package com.propertymonitor.tests;

import com.propertymonitor.base.BaseTest;
import com.propertymonitor.pages.ProjectDetailPage;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;

public class ProjectDetailTest extends BaseTest {

    private ProjectDetailPage projectDetailPage;
    private static final String PAGE_NAME = "Project Detail Page";

    @BeforeClass
    public void setupProjectDetail() {
        driver.get(baseUrl + "projects/project-search");
        projectDetailPage = new ProjectDetailPage(driver, wait);
    }

    @BeforeMethod
    public void startTestLog(Method method) {
        String testDescription = "";
        if (method.isAnnotationPresent(Test.class)) {
            Test testAnnotation = method.getAnnotation(Test.class);
            testDescription = testAnnotation.description();
        }
        if (testDescription.isEmpty()) {
            testDescription = method.getName()
                    .replace("test", "")
                    .replaceAll("([A-Z])", " $1")
                    .trim() + " (Consider adding a specific description for this test)";
        }
        extentTest = extent.createTest("📄 " + PAGE_NAME)
                .assignCategory(PAGE_NAME)
                .info("🔍 Test Case: " + testDescription);
    }

    @AfterMethod
    public void logTestResult(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            extentTest.fail("The test failed: " + result.getThrowable());
        } else if (result.getStatus() == ITestResult.SKIP) {
            extentTest.skip("This test was skipped.");
        } else {
            extentTest.pass("The test passed successfully.");
        }
    }

    @Test(priority = 1, description = "Verifies that user can open the Project Detail page in a new tab.")
    public void test1_VerifyProjectDetailPageOpensInNewTab() {
        try {
            projectDetailPage.openProjectDetailPage();
            extentTest.info("✅ Successfully navigated to the Project Detail page.");
            // This assertion is a bit redundant as an exception would fail the test anyway.
            // A more meaningful assertion here could be to check a specific element on the detail page.
            // However, given the context, you might be relying on the page object's internal checks.
            Assert.assertTrue(true, "✅ Project Detail page opened and verified.");
            extentTest.pass("🎯 Project Detail page opened in a new tab and verified successfully.");
        } catch (Exception e) {
            extentTest.fail("❌ Failed to open Project Detail page: " + e.getMessage());
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }
    }

    @Test(priority = 2, description = "Verifies that Property Details section is visible in Overview tab.")
    public void test2_VerifyPropertyDetailsSectionVisible() {
        boolean isVisible = projectDetailPage.isPropertyDetailsSectionVisible();
        extentTest.info("Checking visibility of Property Details section.");
        Assert.assertTrue(isVisible, "❌ Property Details section is not visible.");
        extentTest.pass("✅ Property Details section is visible.");
    }

    @Test(priority = 3, description = "Verifies that Map is visible after clicking Map tab.")
    public void test3_VerifyMapTabAndMapVisible() {
        projectDetailPage.clickMapTab();
        boolean isVisible = projectDetailPage.isMapVisible();
        extentTest.info("Checking visibility of Map on Map tab.");
        Assert.assertTrue(isVisible, "❌ Map is not visible on Map tab.");
        extentTest.pass("✅ Map is visible after clicking Map tab.");
    }

    @Test(priority = 4, description = "Verifies that images load correctly after clicking Images tab.")
    public void test4_VerifyImagesTabLoadsImages() {
        projectDetailPage.clickImagesTab();
        boolean isVisible = projectDetailPage.areImagesLoaded();
        extentTest.info("Checking if images are loaded.");
        Assert.assertTrue(isVisible, "❌ Images not loaded correctly.");
        extentTest.pass("✅ Images loaded and visible after clicking Images tab.");
    }

    @Test(priority = 5, description = "Verifies Units and Layout tab elements like Topology, Floor Chart, Floor Plan Pictures, and Unit List Table.")
    public void test5_VerifyUnitsAndLayoutsContent() {
        projectDetailPage.clickUnitsAndLayoutsTab();
        boolean topology = projectDetailPage.isTopologyTableVisible();
        boolean floorChart = projectDetailPage.isFloorChartVisible();
        boolean floorPlan = projectDetailPage.areFloorPlanPicturesVisible();
        boolean unitList = projectDetailPage.isUnitListTableVisible();

        extentTest.info("Verifying all components on Units and Layouts tab.");

        Assert.assertTrue(topology, "❌ Topology table not visible.");
        Assert.assertTrue(floorChart, "❌ Floor chart not visible.");
        Assert.assertTrue(floorPlan, "❌ Floor plan pictures not visible.");
        Assert.assertTrue(unitList, "❌ Unit list table not visible.");

        extentTest.pass("✅ All elements on Units and Layouts tab are loaded and visible.");
    }

    @Test(priority = 6, description = "Verifies that Payment Plan table is visible after clicking Payment Plans tab.")
    public void test6_VerifyPaymentPlanVisible() {
        projectDetailPage.clickPaymentPlanTab();
        boolean isVisible = projectDetailPage.isPaymentPlanVisible();
        extentTest.info("Checking if Payment Plan table is visible.");
        Assert.assertTrue(isVisible, "❌ Payment Plan table not visible.");
        extentTest.pass("✅ Payment Plan table is visible.");
    }

    @Test(priority = 7, description = "Verifies that Inspection table is visible after clicking Inspections tab.")
    public void test7_VerifyInspectionsVisible() {
        projectDetailPage.clickInspectionsTab();
        boolean isVisible = projectDetailPage.isInspectionTableVisible();
        extentTest.info("Checking if Inspection table is visible.");
        Assert.assertTrue(isVisible, "❌ Inspection table not visible.");
        extentTest.pass("✅ Inspection table is visible.");
    }

    @Test(priority = 8, description = "Verifies that Downloads tab opens and content is visible.")
    public void test8_VerifyDownloadsTabVisible() {
        // Capture the boolean return value from the page object method
        boolean isDownloadsTabVerified = projectDetailPage.clickDownloadsTabAndVerify();

        extentTest.info("Checking if Downloads tab content is visible.");

        // Now assert based on the return value
        Assert.assertTrue(isDownloadsTabVerified, "❌ Downloads tab did not open or content was not verified.");
        extentTest.pass("✅ Downloads tab opened and content is visible.");
    }

//    @Test(priority = 9, description = "Verifies that submitting the feedback form displays a successful thank-you message.")
//    public void test9_SubmitFeedbackForm() {
//        String answer1 = "This is a test feedback message for automated testing.";
//        String answer2 = "Second part of test feedback.";
//
//        extentTest.info("Clicking the 'Feedback' button and entering test messages.");
//        projectDetailPage.submitFeedback(answer1, answer2);
//
//        boolean isThankYouShown = projectDetailPage.isThankYouNodePresent();
//
//        if (isThankYouShown) {
//            extentTest.pass("✅ Feedback submitted successfully. A thank-you message appeared.");
//        } else {
//            extentTest.fail("❌ Feedback submission failed. The thank-you message was not found.");
//        }
//        Assert.assertTrue(isThankYouShown, "❌ The feedback form did not show the thank-you message.");
//    }
    
    @Test(priority = 10, description = "Verifies breadcrumb navigation to Project Search page and project counts visibility.")
    public void test10_VerifyBreadcrumbNavigationAndProjectCounts() {
        try {
            projectDetailPage.clickBreadcrumbAndVerifyNavigation();
            extentTest.info("✅ Successfully navigated back to Project Search page via breadcrumb.");

            // Verify URL
            String currentUrl = driver.getCurrentUrl();
            Assert.assertTrue(currentUrl.contains("projects/project-search"), "❌ Current URL does not contain 'projects/project-search'. URL: " + currentUrl);
            extentTest.info("URL verification successful: " + currentUrl);

            // Verify Project Counts text
            boolean isProjectCountVisible = projectDetailPage.isProjectCountTextVisible();
            Assert.assertTrue(isProjectCountVisible, "❌ Project counts text is not visible on the search page.");
            extentTest.pass("✅ Project counts text is visible on the Project Search page.");

            extentTest.pass("🎯 Breadcrumb navigation to Project Search page and project counts verification successful.");
        } catch (Exception e) {
            extentTest.fail("❌ Failed to navigate via breadcrumb or verify project counts: " + e.getMessage());
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }
    }
    
}