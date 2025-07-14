package com.propertymonitor.tests;


import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import com.aventstack.extentreports.Status;
import com.propertymonitor.pages.RentalIndexPage;
import com.propertymonitor.base.BaseTest;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
//import java.util.concurrent.TimeoutException;



public class RentalIndexTest extends BaseTest {

    private RentalIndexPage rentalIndexPage;
    private static final String PAGE_NAME = "Rental Index Page";

    @BeforeClass
    public void setupRentalIndexPage() {
        driver.get(baseUrl + "indices/rental-index?limit=10&page=1");
        rentalIndexPage = new RentalIndexPage(driver);
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

        extentTest = extent.createTest("📄 " + PAGE_NAME + " [" + BaseTest.browserName + "]")
                .assignCategory(BaseTest.browserName)
                .info("🔍 Test Case: " + testDescription);
    }

    @AfterMethod
    public void logTestResult(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            extentTest.fail("❌ The test failed: " + result.getThrowable());
        } else if (result.getStatus() == ITestResult.SKIP) {
            extentTest.skip("⚠️ This test was skipped.");
        } else {
            extentTest.pass("✅ The test passed successfully.");
        }
    }
    
    @Test(priority = 1, description = "Verifies that user can open the Rental Yield page")
    public void test1_VerifyRentalIndexPageOpen() {
        try {
            extentTest.info("🚀 Navigating to Rental Index Page.");
            rentalIndexPage.openRentalIndexPage(baseUrl);

            extentTest.info("⏳ Waiting for loading spinner to disappear...");
            rentalIndexPage.waitForSpinnerToDisappear();

            extentTest.info("🔍 Verifying presence of Rental Index chart element.");
            boolean isChartPresent = rentalIndexPage.isRentalYieldChartVisible();

            Assert.assertTrue(isChartPresent, "❌ Rental Index chart not found!");
            extentTest.pass("🎯 Rental Index Page opened and chart verified successfully.");

        } catch (Exception e) {
            extentTest.log(Status.FAIL, "❌ Failed to open Rental Index Page or locate chart: " + e.getMessage());
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }
    }

    @Test(priority = 2, description = "Verifies that at least one KPI value is updated after selecting location 'Damac Hills'")
    public void test2_VerifyKPIDataUpdatesOnLocationSearch() {

        try {
            extentTest.info("📊 Capturing KPI data before selecting location...");
            Map<String, String> beforeValues = rentalIndexPage.getKpiValues();
            beforeValues.forEach((key, value) ->
                extentTest.info("Before - " + key + ": " + value)
            );

            extentTest.info("🧭 Searching and selecting location 'Damac Hills'...");
            rentalIndexPage.searchAndSelectLocation("Damac Hills", false);

            boolean kpiChanged = false;
            Map<String, String> afterValues = new HashMap<>();

            // Retry up to 4 times, waiting between each try
            for (int attempt = 1; attempt <= 4; attempt++) {
                extentTest.info("🔁 Attempt " + attempt + ": Checking KPI values after location selection...");
                afterValues = rentalIndexPage.getKpiValues();

                for (String key : beforeValues.keySet()) {
                    String before = beforeValues.get(key);
                    String after = afterValues.getOrDefault(key, "");

                    if (!before.equalsIgnoreCase(after)) {
                        extentTest.pass("✅ " + key + " changed from '" + before + "' to '" + after + "'");
                        kpiChanged = true;
                    } else {
                        extentTest.info("ℹ️ " + key + " remains unchanged at '" + after + "'");
                    }
                }

                if (kpiChanged) break;

                // Wait 3 seconds before retrying
                Thread.sleep(3000);
            }

            // Final reporting
            afterValues.forEach((key, value) ->
                extentTest.info("After - " + key + ": " + value)
            );

            if (kpiChanged) {
                extentTest.pass("✅ The test passed successfully.");
            } else {
                extentTest.fail("❌ None of the KPI values changed after location selection!");
            }

            Assert.assertTrue(kpiChanged, "❌ None of the KPI values changed after location selection!");

        } catch (Exception e) {
            extentTest.fail("❌ Test failed due to exception: " + e.getMessage());
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }
    }
    
    @Test(priority = 3, description = "Verifies that KPI values are updated for each dropdown selection on Rental Index page.")
    public void test3_VerifyKPIDataUpdatesOnDropdownSelections() {
        try {
            // Capture KPI before any dropdown interaction
            extentTest.info("📊 Capturing KPI values before dropdown interactions...");
            Map<String, String> beforeValues = rentalIndexPage.getKpiValuesForDropdownValidation();
            beforeValues.forEach((key, value) ->
                extentTest.info("Before - " + key + ": " + value)
            );

            // Define dropdown interactions
            Map<String, Runnable> dropdownActions = new LinkedHashMap<>();
            dropdownActions.put("Property Type: Apartment", () -> rentalIndexPage.selectPropertyType("Apartment"));
            dropdownActions.put("Index Type: New", () -> rentalIndexPage.selectIndexType("New"));
            dropdownActions.put("Calendar: Mar 2025", () -> rentalIndexPage.selectCalendarMonth("Mar 2025"));

            for (Map.Entry<String, Runnable> entry : dropdownActions.entrySet()) {
                String dropdownLabel = entry.getKey();
                extentTest.info("🔽 Selecting option -> " + dropdownLabel);

                entry.getValue().run();
                Thread.sleep(1000); // Allow slight delay for UI to respond (use explicit waits if needed)

                Map<String, String> afterValues = rentalIndexPage.getKpiValuesForDropdownValidation();

                boolean isChanged = false;
                for (String key : beforeValues.keySet()) {
                    String before = beforeValues.get(key);
                    String after = afterValues.get(key);
                    if (!before.equalsIgnoreCase(after)) {
                        extentTest.pass("✅ " + key + " changed after " + dropdownLabel + ": '" + before + "' ➜ '" + after + "'");
                        isChanged = true;
                    } else {
                        extentTest.info("ℹ️ " + key + " unchanged after " + dropdownLabel + ": '" + before + "'");
                    }
                }

                Assert.assertTrue(isChanged, "❌ None of the KPI values changed after " + dropdownLabel + " selection!");

                // Store updated values for next iteration comparison
                beforeValues = afterValues;
            }

        } catch (Exception e) {
            extentTest.fail("❌ Test failed due to exception: " + e.getMessage());
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }
    }
    
    @Test(priority = 4, description = "Verifies that the Rental Index table is visible on the page")
    public void test4_VerifyRentalIndexTableVisible() {
        try {
            extentTest.info("🚀 Navigating to Rental Index Page.");
            rentalIndexPage.openRentalIndexPage(baseUrl);

            extentTest.info("⏳ Waiting for loading spinner to disappear...");
            rentalIndexPage.waitForSpinnerToDisappear();

            extentTest.info("🔍 Verifying presence of Rental Index table.");
            boolean isTablePresent = rentalIndexPage.isRentalYieldTableVisible();

            Assert.assertTrue(isTablePresent, "❌ Rental Index table not found!");
            extentTest.pass("✅ Rental Index table is visible on the page.");

        } catch (Exception e) {
            extentTest.log(Status.FAIL, "❌ Failed to verify Rental Index table: " + e.getMessage());
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }
    }
    
    @Test(priority = 5, description = "Verifies that table row range text updates when selecting page sizes: 50 and All")
    public void test5_VerifyTableRangeUpdatesOnPageSizeChange() {
        try {
            extentTest.info("🚀 Navigating to Rental Index Page.");
            rentalIndexPage.openRentalIndexPage(baseUrl);

            extentTest.info("⏳ Waiting for loading spinner to disappear...");
            rentalIndexPage.waitForSpinnerToDisappear();

            extentTest.info("📊 Getting initial table row range...");
            String initialRange = rentalIndexPage.getTableRowRangeText();
            extentTest.info("Initial table range: " + initialRange);

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(rentalIndexPage.pageSizeDropdown));
            rentalIndexPage.waitFor(1000); // scroll buffer

            // Select 50
            extentTest.info("🔽 Selecting page size: 50");
            driver.findElement(rentalIndexPage.pageSizeDropdown).click();
            driver.findElement(rentalIndexPage.option50).click();
            rentalIndexPage.waitForSpinnerToDisappear();

            String rangeAfter50 = rentalIndexPage.getTableRowRangeText();
            extentTest.info("Table range after selecting 50: " + rangeAfter50);
            Assert.assertNotEquals(rangeAfter50, initialRange, "❌ Table range did not update after selecting 50.");
            extentTest.pass("✅ Table range updated after selecting 50.");

            // Select All
            extentTest.info("🔽 Selecting page size: All");
            driver.findElement(rentalIndexPage.pageSizeDropdown).click();
            driver.findElement(rentalIndexPage.optionAll).click();
            rentalIndexPage.waitForSpinnerToDisappear();

            String rangeAfterAll = rentalIndexPage.getTableRowRangeText();
            extentTest.info("Table range after selecting All: " + rangeAfterAll);
            Assert.assertNotEquals(rangeAfterAll, rangeAfter50, "❌ Table range did not update after selecting All.");
            extentTest.pass("✅ Table range updated after selecting All.");

        } catch (Exception e) {
            extentTest.log(Status.FAIL, "❌ Test failed: " + e.getMessage());
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }
    }
    
    @Test(priority = 6, description = "Verifies pagination works and table count updates accordingly.")
    public void test6_VerifyPaginationTableUpdates() {
        try {
            extentTest.info("🚀 Navigating to Rental Index Page.");
            rentalIndexPage.openRentalIndexPage(baseUrl);

            extentTest.info("⏳ Waiting for loading spinner to disappear...");
            rentalIndexPage.waitForSpinnerToDisappear();

            extentTest.info("🔁 Clicking through all pagination buttons to verify table count updates.");
            boolean updated = rentalIndexPage.clickPaginationAndVerifyTableUpdates();

            Assert.assertTrue(updated, "❌ Table row count did not update correctly on pagination.");
            extentTest.pass("✅ Pagination verified successfully, table count updated on each page.");

        } catch (Exception e) {
            extentTest.fail("❌ Exception during pagination test: " + e.getMessage());
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }
    }
    
    @Test(priority = 3, description = "Verifies that at least one KPI value is updated after selecting 'Rental Price'")
    public void test3_VerifyKPIDataUpdatesOnRentalPriceSelection() {
        try {
            extentTest.info("📊 Capturing KPI data before selecting Rental Price...");
            Map<String, String> beforeValues = rentalIndexPage.getKpiValues();
            beforeValues.forEach((key, value) ->
                extentTest.info("Before - " + key + ": " + value)
            );

            extentTest.info("🏷️ Selecting 'Rental Price' from dropdown...");
            Map<String, String> afterValues = rentalIndexPage.selectRentalPrice(beforeValues);

            afterValues.forEach((key, value) ->
                extentTest.info("After - " + key + ": " + value)
            );

            boolean atLeastOneChanged = false;
            for (String key : beforeValues.keySet()) {
                String before = beforeValues.get(key);
                String after = afterValues.getOrDefault(key, "");

                if (!before.equalsIgnoreCase(after)) {
                    extentTest.pass("✅ " + key + " changed from '" + before + "' to '" + after + "'");
                    atLeastOneChanged = true;
                } else {
                    extentTest.info("ℹ️ " + key + " remains unchanged at '" + before + "'");
                }
            }

            Assert.assertTrue(atLeastOneChanged, "❌ None of the KPI values changed after selecting Rental Price!");

        } catch (Exception e) {
            extentTest.fail("❌ Test failed due to exception: " + e.getMessage());
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }
    }
    
//  @Test(priority = 8, description = "Verifies that submitting the feedback form displays a successful thank-you message.")
//  public void test8_SubmitFeedbackForm() {
//      String answer1 = "This is a test feedback message for automated testing.";
//      String answer2 = "Second part of test feedback.";
//
//      extentTest.info("Clicking the 'Feedback' button and entering test messages.");
//      rentalYieldPage.submitFeedback(answer1, answer2);
//
//      boolean isThankYouShown = rentalYieldPage.isThankYouNodePresent();
//
//      if (isThankYouShown) {
//          extentTest.pass("✅ Feedback submitted successfully. A thank-you message appeared.");
//      } else {
//          extentTest.fail("❌ Feedback submission failed. The thank-you message was not found.");
//      }
//      Assert.assertTrue(isThankYouShown, "❌ The feedback form did not show the thank-you message.");
//  }
    
}
