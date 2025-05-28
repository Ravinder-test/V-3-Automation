package com.propertymonitor.tests;

import com.propertymonitor.base.BaseTest;
import com.propertymonitor.pages.ProjectSearchPage;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ProjectSearchTest extends BaseTest {

    private ProjectSearchPage projectSearchPage;
    private static final String PAGE_NAME = "Project Search Page"; // Changed for clearer report title

    @BeforeClass
    public void setupProjectSearch() {
        driver.get(baseUrl + "projects/project-search");
        projectSearchPage = new ProjectSearchPage(driver, wait); // Pass driver and wait
        // No extentTest.info() here to avoid NullPointerException in @BeforeClass
    }

    @BeforeMethod
    public void startTestLog(Method method) {
        String testDescription = "";
        if (method.isAnnotationPresent(Test.class)) {
            Test testAnnotation = method.getAnnotation(Test.class);
            testDescription = testAnnotation.description();
        }

        if (testDescription.isEmpty()) {
            // Generate a readable description from method name if @Test description is missing
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
    public void logTestResult(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            extentTest.fail("The test failed: " + result.getThrowable()); // More descriptive failure message
        } else if (result.getStatus() == ITestResult.SKIP) {
            extentTest.skip("This test was skipped."); // Clearer skip message
        } else {
            extentTest.pass("The test passed successfully."); // Clearer pass message
        }
    }

    // Search for Jumeriah location and click on it, Verify project results updated or not
    @Test(priority = 1, description = "Verifies that searching for a specific location updates the displayed project count.")
    public void test1_SearchLocationUpdatesProjectCount() {
        String before = projectSearchPage.getProjectCount();
        extentTest.info("Initial project count before searching: " + before);
        projectSearchPage.searchLocation("Jumeirah Beach");
        projectSearchPage.waitFor(3000); // Allow time for results to update

        String after = projectSearchPage.getProjectCount();
        extentTest.info("Project count after searching for 'Jumeirah Beach': " + after);

        Assert.assertNotEquals(after, before, "❌ The project count did not change after searching for the location.");
        extentTest.pass("✅ The project count successfully updated after searching for 'Jumeirah Beach'.");
    }

    // Clear the Jumeriah location and verify Projects count updated or not
    @Test(priority = 2, description = "Verifies that clearing the location filter reverts the project count.")
    public void test2_ClearLocationUpdatesProjectCount() {
        // Assume a location has been searched before this test runs, so 'before' count is from a filtered state
        String before = projectSearchPage.getProjectCount();
        extentTest.info("Project count before clearing the location filter: " + before);

        projectSearchPage.clearLocation();
        projectSearchPage.waitFor(3000); // Allow time for results to update

        String after = projectSearchPage.getProjectCount();
        extentTest.info("Project count after clearing the location filter: " + after);

        Assert.assertNotEquals(after, before, "❌ The project count did not change after clearing the location filter.");
        extentTest.pass("✅ The project count successfully updated after clearing the location filter.");
    }

    // Click and select Apartment Unit type and verify projects list updated or not
    @Test(priority = 3, description = "Verifies that selecting 'Apartment' as a unit type filters the project list correctly.")
    public void test3_SelectApartmentUnitType() {
        String before = projectSearchPage.getProjectCount();
        extentTest.info("Project count before selecting 'Apartment': " + before);

        projectSearchPage.toggleApartmentCheckbox(); // Selects Apartment and clicks outside
        projectSearchPage.waitFor(3000);

        String after = projectSearchPage.getProjectCount();
        extentTest.info("Project count after selecting 'Apartment': " + after);

        Assert.assertNotEquals(after, before, "❌ The project count did not change after selecting 'Apartment'.");
        extentTest.pass("✅ The project count updated after selecting 'Apartment' unit type.");
    }

    // Click and Uncheck Aparment unit type and verify result are updated or not.
    @Test(priority = 4, description = "Verifies that unselecting 'Apartment' unit type updates the project list.")
    public void test4_UnselectApartmentUnitType() {
        // Assume 'Apartment' is selected from a previous test or a setup step
        String before = projectSearchPage.getProjectCount();
        extentTest.info("Project count before unselecting 'Apartment': " + before);

        projectSearchPage.toggleApartmentCheckbox(); // Unselects Apartment and clicks outside
        projectSearchPage.waitFor(3000);

        String after = projectSearchPage.getProjectCount();
        extentTest.info("Project count after unselecting 'Apartment': " + after);

        Assert.assertNotEquals(after, before, "❌ The project count did not change after unselecting 'Apartment'.");
        extentTest.pass("✅ The project count updated after unselecting 'Apartment' unit type.");
    }

    // Click and Unselect upcoming sale status and verify results are updated or not.
    @Test(priority = 5, description = "Verifies that unchecking 'Upcoming' sale status filters the project list.")
    public void test5_UncheckUpcomingSaleStatus() {
        String before = projectSearchPage.getProjectCount();
        extentTest.info("Project count before unchecking 'Upcoming' status: " + before);

        projectSearchPage.toggleUpcomingSaleStatus(); // Unchecks Upcoming and clicks outside
        projectSearchPage.waitFor(3000);

        String after = projectSearchPage.getProjectCount();
        extentTest.info("Project count after unchecking 'Upcoming': " + after);

        Assert.assertNotEquals(after, before, "❌ The project count did not change after unchecking 'Upcoming' sale status.");
        extentTest.pass("✅ The project count updated after unchecking 'Upcoming' status.");
    }

    // Click and Select back upcoming sale status and verify results are updated or not.
    @Test(priority = 6, description = "Verifies that re-selecting 'Upcoming' sale status updates the project list.")
    public void test6_SelectUpcomingSaleStatus() {
        // Assume 'Upcoming' is unchecked from a previous test or setup step
        String before = projectSearchPage.getProjectCount();
        extentTest.info("Project count before re-selecting 'Upcoming' status: " + before);

        projectSearchPage.toggleUpcomingSaleStatus(); // Selects Upcoming and clicks outside
        projectSearchPage.waitFor(3000);

        String after = projectSearchPage.getProjectCount();
        extentTest.info("Project count after re-selecting 'Upcoming': " + after);

        Assert.assertNotEquals(after, before, "❌ The project count did not change after re-selecting 'Upcoming'.");
        extentTest.pass("✅ The project count updated after re-selecting 'Upcoming' status.");
    }

    // Click on the developer and search for "Aark Developer" and select it, then verify project is updated or not
    @Test(priority = 7, description = "Verifies that selecting and then unselecting a developer changes the project count correctly.")
    public void test7_SearchDeveloperProjectCount() {
        // Part 1: Select Developer and Verify
        String beforeSelection = projectSearchPage.getProjectCount();
        extentTest.info("Project count before selecting a developer: " + beforeSelection);

        projectSearchPage.searchDeveloper(); // Searches and selects "Aark Developer" and clicks outside
        projectSearchPage.waitFor(3000);

        String afterSelection = projectSearchPage.getProjectCount();
        extentTest.info("Project count after selecting 'Aark Developer': " + afterSelection);

        Assert.assertNotEquals(afterSelection, beforeSelection, "❌ The project count did not change after selecting 'Aark Developer'.");
        extentTest.pass("✅ The project count successfully updated after selecting 'Aark Developer'.");

        // Part 2: Unselect Developer and Verify
        extentTest.info("Now attempting to unselect 'Aark Developer' and verify the project count changes again.");
        String beforeUnselection = projectSearchPage.getProjectCount(); // This should be 'afterSelection'
        
        projectSearchPage.searchDeveloper(); // Clicks again to unselect "Aark Developer" and clicks outside
        projectSearchPage.waitFor(2000);

        String afterUnselection = projectSearchPage.getProjectCount();
        extentTest.info("Project count after unselecting 'Aark Developer': " + afterUnselection);

        Assert.assertNotEquals(afterUnselection, beforeUnselection, "❌ The project count did not change after unselecting 'Aark Developer'.");
        extentTest.pass("✅ The project count successfully updated after unselecting 'Aark Developer'.");
    }

    // Click on the Advance filter and Verify Project count is showing.
    @Test(priority = 8, description = "Verifies that clicking 'Advance Filter' opens the advanced filter section.")
    public void test8_ClickAdvanceFilter() {
        projectSearchPage.clickAdvanceFilter();
        projectSearchPage.waitFor(1000); // Allow time for the drawer to appear

        Assert.assertTrue(projectSearchPage.isAdvanceFilterDrawerVisible(), "❌ The Advance Filter section is not visible.");
        extentTest.pass("✅ The Advance Filter section is visible as expected.");
    }

    // Click on the Construction Status filter and select Completed filter then verify project count updated or not
    @Test(priority = 9, description = "Verifies that selecting 'Completed' for construction status filters the project list.")
    public void test9_SelectConstructionStatusCompleted() {
        String before = projectSearchPage.getProjectCount();
        extentTest.info("Project count before selecting 'Completed' status: " + before);

        projectSearchPage.toggleConstructionStatusCompleted(); // Selects "Completed" and clicks outside
        projectSearchPage.waitFor(3000);

        String after = projectSearchPage.getProjectCount();
        extentTest.info("Project count after selecting 'Completed' status: " + after);

        Assert.assertNotEquals(after, before, "❌ The project count did not change after selecting 'Completed' construction status.");
        extentTest.pass("✅ The project count updated after selecting 'Completed' construction status.");
    }

    // Unselect Construction Status (Completed) for next test
    @Test(priority = 10, description = "Verifies that unselecting 'Completed' for construction status updates the project list.")
    public void test10_UnselectConstructionStatusCompleted() {
        // Assume 'Completed' is selected from a previous test or setup step
        String before = projectSearchPage.getProjectCount();
        extentTest.info("Project count before unselecting 'Completed' status: " + before);

        projectSearchPage.toggleConstructionStatusCompleted(); // Unselects "Completed" and clicks outside
        projectSearchPage.waitFor(3000);

        String after = projectSearchPage.getProjectCount();
        extentTest.info("Project count after unselecting 'Completed' status: " + after);

        Assert.assertNotEquals(after, before, "❌ The project count did not change after unselecting 'Completed' construction status.");
        extentTest.pass("✅ The project count updated after unselecting 'Completed' construction status.");
    }

    // Click on the calendar filter and select 2025 upcoming option
    @Test(priority = 11, description = "Verifies that selecting '2025 (upcoming)' as a completion date filters the project list.")
    public void test11_SelectCompletionDate2025Upcoming() {
        String before = projectSearchPage.getProjectCount();
        extentTest.info("Project count before selecting a completion date: " + before);

        projectSearchPage.selectCompletionDate2025Upcoming(); // Selects 2025 (upcoming) and clicks outside
        projectSearchPage.waitFor(3000);

        String after = projectSearchPage.getProjectCount();
        extentTest.info("Project count after selecting '2025 (upcoming)': " + after);

        Assert.assertNotEquals(after, before, "❌ The project count did not change after selecting '2025 (upcoming)' option.");
        extentTest.pass("✅ The project count updated after selecting '2025 (upcoming)' option.");
    }

    // Click on the Close icon to Reset the Selected Completion date and verify project count
    @Test(priority = 12, description = "Verifies that clearing the selected completion date updates the project list.")
    public void test12_ClearCompletionDate() {
        // Assume a completion date is selected from a previous test or setup step
        String before = projectSearchPage.getProjectCount();
        extentTest.info("Project count before clearing the completion date: " + before);

        projectSearchPage.clearCompletionDate();
        projectSearchPage.waitFor(2000);

        String after = projectSearchPage.getProjectCount();
        extentTest.info("Project count after clearing the completion date: " + after);

        Assert.assertNotEquals(after, before, "❌ The project count did not change after clearing the completion date.");
        extentTest.pass("✅ The project count updated after clearing the completion date.");
    }

    //Click on the Sales start date field and select the last 90 days option and verify project count
    @Test(priority = 13, description = "Verifies that selecting 'Last 90 days' for sales start date filters the project list.")
    public void test13_SelectSalesStartDateLast90Days() {
        String before = projectSearchPage.getProjectCount();
        extentTest.info("Project count before selecting sales start date: " + before);

        projectSearchPage.selectSalesStartDateLast90Days(); // Selects Last 90 Days and clicks outside
        projectSearchPage.waitFor(3000);

        String after = projectSearchPage.getProjectCount();
        extentTest.info("Project count after selecting 'Last 90 days' sales start date: " + after);

        Assert.assertNotEquals(after, before, "❌ The project count did not change after selecting 'Last 90 days' sales start date.");
        extentTest.pass("✅ The project count updated after selecting 'Last 90 days' sales start date.");
    }

    // Click on the close icon to Reset the sales start date and verify project count.
    @Test(priority = 14, description = "Verifies that clearing the sales start date updates the project list.")
    public void test14_ClearSalesStartDate() {
        // Assume a sales start date is selected from a previous test or setup step
        String before = projectSearchPage.getProjectCount();
        extentTest.info("Project count before clearing the sales start date: " + before);

        projectSearchPage.clearSalesStartDate();
        projectSearchPage.waitFor(2000);

        String after = projectSearchPage.getProjectCount();
        extentTest.info("Project count after clearing the sales start date: " + after);

        Assert.assertNotEquals(after, before, "❌ The project count did not change after clearing the sales start date.");
        extentTest.pass("✅ The project count updated after clearing the sales start date.");
    }

    // On the Completion Percentage input Min and Max values and Verify the Updated project count
    @Test(priority = 15, description = "Verifies that entering minimum and maximum completion percentage values filters the project list.")
    public void test15_InputMinMaxCompletionPercentage() {
        String before = projectSearchPage.getProjectCount();
        extentTest.info("Project count before setting completion percentage range: " + before);

        projectSearchPage.setCompletionPercentageRange("5", "90");
        projectSearchPage.waitFor(2000);

        String after = projectSearchPage.getProjectCount();
        extentTest.info("Project count after setting completion percentage range: " + after);

        Assert.assertNotEquals(after, before, "❌ The project count did not change after setting min/max completion percentage.");
        extentTest.pass("✅ The project count updated after setting min/max completion percentage.");
    }

    // Reset the Min and Max Completion percentage count
    @Test(priority = 16, description = "Verifies that resetting the completion percentage range updates the project list.")
    public void test16_ResetMinMaxCompletionPercentage() {
        // Assume completion percentage is set from a previous test or setup step
        String before = projectSearchPage.getProjectCount();
        extentTest.info("Project count before resetting completion percentage: " + before);

        projectSearchPage.setCompletionPercentageRange("", ""); // Clear fields
        projectSearchPage.waitFor(2000);

        String after = projectSearchPage.getProjectCount();
        extentTest.info("Project count after resetting completion percentage: " + after);

        Assert.assertNotEquals(after, before, "❌ The project count did not change after resetting min/max completion percentage.");
        extentTest.pass("✅ The project count updated after resetting min/max completion percentage.");
    }

    // Select Bedroom and Verify projects count updated or not
    @Test(priority = 17, description = "Verifies that selecting '5 Bedrooms' filters the project list.")
    public void test17_SelectBedroomLabel5() {
        String before = projectSearchPage.getProjectCount();
        extentTest.info("Project count before selecting '5 Bedrooms': " + before);

        projectSearchPage.toggleBedroom5();
        projectSearchPage.waitFor(2000);

        String after = projectSearchPage.getProjectCount();
        extentTest.info("Project count after selecting '5 Bedrooms': " + after);

        Assert.assertNotEquals(after, before, "❌ The project count did not change after selecting '5 Bedrooms'.");
        extentTest.pass("✅ The project count updated after selecting '5 Bedrooms'.");
    }

    // unselect Bedroom label and verify projects count updated or not.
    @Test(priority = 18, description = "Verifies that unselecting '5 Bedrooms' updates the project list.")
    public void test18_UnselectBedroomLabel5() {
        // Assume '5 Bedrooms' is selected from a previous test or setup step
        String before = projectSearchPage.getProjectCount();
        extentTest.info("Project count before unselecting '5 Bedrooms': " + before);

        projectSearchPage.toggleBedroom5(); // Clicks again to unselect
        projectSearchPage.waitFor(2000);

        String after = projectSearchPage.getProjectCount();
        extentTest.info("Project count after unselecting '5 Bedrooms': " + after);

        Assert.assertNotEquals(after, before, "❌ The project count did not change after unselecting '5 Bedrooms'.");
        extentTest.pass("✅ The project count updated after unselecting '5 Bedrooms'.");
    }

    // User should input Min and Max value on the Built up area and verify Projects Count updated or not.
    @Test(priority = 19, description = "Verifies that entering minimum and maximum built-up area values filters the project list.")
    public void test19_InputBuiltupAreaValue() {
        String before = projectSearchPage.getProjectCount();
        extentTest.info("Project count before setting built-up area range: " + before);

        projectSearchPage.setBuiltupAreaRange("3000", "5000");
        projectSearchPage.waitFor(2000);

        String after = projectSearchPage.getProjectCount();
        extentTest.info("Project count after setting built-up area range: " + after);

        Assert.assertNotEquals(after, before, "❌ The project count did not change after setting min/max built-up area.");
        extentTest.pass("✅ The project count updated after setting min/max built-up area.");
    }

    // user should reset the builtup area and verify projects count are updated or not
    @Test(priority = 20, description = "Verifies that resetting the built-up area range updates the project list.")
    public void test20_ResetBuiltupAreaValue() {
        // Assume built-up area is set from a previous test or setup step
        String before = projectSearchPage.getProjectCount();
        extentTest.info("Project count before resetting built-up area: " + before);

        projectSearchPage.setBuiltupAreaRange("", ""); // Clear fields
        projectSearchPage.waitFor(2000);

        String after = projectSearchPage.getProjectCount();
        extentTest.info("Project count after resetting built-up area: " + after);

        Assert.assertNotEquals(after, before, "❌ The project count did not change after resetting built-up area.");
        extentTest.pass("✅ The project count updated after resetting built-up area.");
    }

    // User should input the Min and max values on the plot area and verify projects count are updated or not.
    @Test(priority = 21, description = "Verifies that entering minimum and maximum plot area values filters the project list.")
    public void test21_InputPlotAreaValue() {
        String before = projectSearchPage.getProjectCount();
        extentTest.info("Project count before setting plot area range: " + before);

        projectSearchPage.setPlotSizeRange("3000", "5000");
        projectSearchPage.waitFor(2000);

        String after = projectSearchPage.getProjectCount();
        extentTest.info("Project count after setting plot area range: " + after);

        Assert.assertNotEquals(after, before, "❌ The project count did not change after setting min/max plot area.");
        extentTest.pass("✅ The project count updated after setting min/max plot area.");
    }

    // user should reset the Plot area and verify projects count are updated or not
    @Test(priority = 22, description = "Verifies that resetting the plot area range updates the project list.")
    public void test22_ResetPlotAreaValue() {
        // Assume plot area is set from a previous test or setup step
        String before = projectSearchPage.getProjectCount();
        extentTest.info("Project count before resetting plot area: " + before);

        projectSearchPage.setPlotSizeRange("", ""); // Clear fields
        projectSearchPage.waitFor(2000);

        String after = projectSearchPage.getProjectCount();
        extentTest.info("Project count after resetting plot area: " + after);

        Assert.assertNotEquals(after, before, "❌ The project count did not change after resetting plot area.");
        extentTest.pass("✅ The project count updated after resetting plot area.");
    }

    // User should enter the Min and Max down payment percentage and verify project count updated or not
    @Test(priority = 23, description = "Verifies that entering minimum and maximum down payment percentage values filters the project list.")
    public void test23_InputDownPaymentValue() {
        String before = projectSearchPage.getProjectCount();
        extentTest.info("Project count before setting down payment percentage: " + before);

        projectSearchPage.setDownpaymentPercentageRange("5", "10");
        projectSearchPage.waitFor(2000);

        String after = projectSearchPage.getProjectCount();
        extentTest.info("Project count after setting down payment percentage: " + after);

        Assert.assertNotEquals(after, before, "❌ The project count did not change after setting min/max down payment percentage.");
        extentTest.pass("✅ The project count updated after setting min/max down payment percentage.");
    }

    // User should Reset the Min and Max down payment percentage and verify project count updated or not
    @Test(priority = 24, description = "Verifies that resetting the down payment percentage range updates the project list.")
    public void test24_ResetDownPaymentValue() {
        // Assume down payment percentage is set from a previous test or setup step
        String before = projectSearchPage.getProjectCount();
        extentTest.info("Project count before resetting down payment percentage: " + before);

        projectSearchPage.setDownpaymentPercentageRange("", ""); // Clear fields
        projectSearchPage.waitFor(2000);

        String after = projectSearchPage.getProjectCount();
        extentTest.info("Project count after resetting down payment percentage: " + after);

        Assert.assertNotEquals(after, before, "❌ The project count did not change after resetting down payment percentage.");
        extentTest.pass("✅ The project count updated after resetting down payment percentage.");
    }

    // User should click on the Payment plan drop-down and select the 'On Completion' option and verify Results are updated or not
    @Test(priority = 25, description = "Verifies that selecting 'On Completion' payment plan filters the project list.")
    public void test25_SelectPaymentPlanOnCompletion() {
        String before = projectSearchPage.getProjectCount();
        extentTest.info("Project count before selecting payment plan: " + before);

        projectSearchPage.togglePaymentPlanOnCompletion(); // Selects On Completion and clicks outside
        projectSearchPage.waitFor(2000);

        String after = projectSearchPage.getProjectCount();
        extentTest.info("Project count after selecting 'On Completion' payment plan: " + after);

        Assert.assertNotEquals(after, before, "❌ The project count did not change after selecting 'On Completion' payment plan.");
        extentTest.pass("✅ The project count updated after selecting 'On Completion' payment plan.");
    }

    // User should click on the Payment plan drop-down and Unselect the 'On Completion' option and verify Results are updated or not
    @Test(priority = 26, description = "Verifies that unselecting 'On Completion' payment plan updates the project list.")
    public void test26_UnselectPaymentPlanOnCompletion() {
        // Assume 'On Completion' payment plan is selected from a previous test or setup step
        String before = projectSearchPage.getProjectCount();
        extentTest.info("Project count before unselecting payment plan: " + before);

        projectSearchPage.togglePaymentPlanOnCompletion(); // Unselects On Completion and clicks outside
        projectSearchPage.waitFor(2000);

        String after = projectSearchPage.getProjectCount();
        extentTest.info("Project count after unselecting payment plan: " + after);

        Assert.assertNotEquals(after, before, "❌ The project count did not change after unselecting 'On Completion' payment plan.");
        extentTest.pass("✅ The project count updated after unselecting 'On Completion' payment plan.");
    }

    // User should click and select the DLD transfer Fee of 50% and verify projects count Updated or not.
    @Test(priority = 27, description = "Verifies that selecting '50%' DLD transfer fee filters the project list.")
    public void test27_SelectDldTransfer50Percent() {
        String before = projectSearchPage.getProjectCount();
        extentTest.info("Project count before selecting DLD transfer fee: " + before);

        projectSearchPage.toggleDldTransfer50Percent(); // Selects 50% DLD transfer and clicks outside
        projectSearchPage.waitFor(2000);

        String after = projectSearchPage.getProjectCount();
        extentTest.info("Project count after selecting '50%' DLD transfer fee: " + after);

        Assert.assertNotEquals(after, before, "❌ The project count did not change after selecting '50%' DLD transfer fee.");
        extentTest.pass("✅ The project count updated after selecting '50%' DLD transfer fee.");
    }

    // User should click and unselect the DLD transfer Fee of 50% and verify projects count Updated or not.
    @Test(priority = 28, description = "Verifies that unselecting '50%' DLD transfer fee updates the project list.")
    public void test28_UnselectDldTransfer50Percent() {
        // Assume '50%' DLD transfer fee is selected from a previous test or setup step
        String before = projectSearchPage.getProjectCount();
        extentTest.info("Project count before unselecting DLD transfer fee: " + before);

        projectSearchPage.toggleDldTransfer50Percent(); // Unselects 50% DLD transfer and clicks outside
        projectSearchPage.waitFor(2000);

        String after = projectSearchPage.getProjectCount();
        extentTest.info("Project count after unselecting DLD transfer fee: " + after);

        Assert.assertNotEquals(after, before, "❌ The project count did not change after unselecting '50%' DLD transfer fee.");
        extentTest.pass("✅ The project count updated after unselecting '50%' DLD transfer fee.");
    }

    // User should enter the Min and Max broker commission and verify project count updated or not
    @Test(priority = 29, description = "Verifies that entering minimum and maximum broker commission values filters the project list.")
    public void test29_InputBrokerCommissionValue() {
        String before = projectSearchPage.getProjectCount();
        extentTest.info("Project count before setting broker commission: " + before);

        projectSearchPage.setBrokerCommissionRange("2", "4");
        projectSearchPage.waitFor(2000);

        String after = projectSearchPage.getProjectCount();
        extentTest.info("Project count after setting broker commission: " + after);

        Assert.assertNotEquals(after, before, "❌ The project count did not change after setting min/max broker commission.");
        extentTest.pass("✅ The project count updated after setting min/max broker commission.");
    }

    // User should Reset the Min and Max broker commission percentage and verify project count updated or not
    @Test(priority = 30, description = "Verifies that resetting the broker commission range updates the project list.")
    public void test30_ResetBrokerCommissionValue() {
        // Assume broker commission is set from a previous test or setup step
        String before = projectSearchPage.getProjectCount();
        extentTest.info("Project count before resetting broker commission: " + before);

        projectSearchPage.setBrokerCommissionRange("", ""); // Clear fields
        projectSearchPage.waitFor(2000);

        String after = projectSearchPage.getProjectCount();
        extentTest.info("Project count after resetting broker commission: " + after);

        Assert.assertNotEquals(after, before, "❌ The project count did not change after resetting broker commission.");
        extentTest.pass("✅ The project count updated after resetting broker commission.");
    }

    // User should click on the height class and select Mid Rise option and then verify project count is updated or not.
    @Test(priority = 31, description = "Verifies that selecting 'Mid-Rise' height class filters the project list.")
    public void test31_SelectHeightClassMidRise() {
        String before = projectSearchPage.getProjectCount();
        extentTest.info("Project count before selecting height class: " + before);

        projectSearchPage.toggleHeightClassMidRise(); // Selects Mid-Rise and clicks outside
        projectSearchPage.waitFor(2000);

        String after = projectSearchPage.getProjectCount();
        extentTest.info("Project count after selecting 'Mid-Rise' height class: " + after);

        Assert.assertNotEquals(after, before, "❌ The project count did not change after selecting 'Mid-Rise' option.");
        extentTest.pass("✅ The project count updated after selecting 'Mid-Rise' option.");
    }

    // User should click on the height class and unselect Mid Rise option and then verify project count is updated or not.
    @Test(priority = 32, description = "Verifies that unselecting 'Mid-Rise' height class updates the project list.")
    public void test32_UnselectHeightClassMidRise() {
        // Assume 'Mid-Rise' height class is selected from a previous test or setup step
        String before = projectSearchPage.getProjectCount();
        extentTest.info("Project count before unselecting height class: " + before);

        projectSearchPage.toggleHeightClassMidRise(); // Unselects Mid-Rise and clicks outside
        projectSearchPage.waitFor(2000);

        String after = projectSearchPage.getProjectCount();
        extentTest.info("Project count after unselecting height class: " + after);

        Assert.assertNotEquals(after, before, "❌ The project count did not change after unselecting 'Mid-Rise' option.");
        extentTest.pass("✅ The project count updated after unselecting 'Mid-Rise' option.");
    }

    // User should click on the Brand field and '1 hotels', '25 hours hotels'and then verify project count is updated or not.
    @Test(priority = 33, description = "Verifies that selecting multiple brands ('1 Hotels', '25 Hours Hotels') filters the project list.")
    public void test33_SelectBrandFilter() {
        String before = projectSearchPage.getProjectCount();
        extentTest.info("Project count before selecting brands: " + before);

        projectSearchPage.selectBrands(); // Selects both brands and clicks outside
        projectSearchPage.waitFor(2000);

        String after = projectSearchPage.getProjectCount();
        extentTest.info("Project count after selecting brands: " + after);

        Assert.assertNotEquals(after, before, "❌ The project count did not change after selecting brands.");
        extentTest.pass("✅ The project count updated after selecting brands.");
    }

    // User should click on the Reset text to reset '1 hotels', '25 hours hotels' options and then verify project count is updated or not.
    @Test(priority = 34, description = "Verifies that unselecting multiple brands updates the project list.")
    public void test34_UnselectBrandFilter() {
        // Assume brands are selected from a previous test or setup step
        String before = projectSearchPage.getProjectCount();
        extentTest.info("Project count before unselecting brands: " + before);

        projectSearchPage.unselectBrands(); // Unselects both brands and clicks outside
        projectSearchPage.waitFor(2000);

        String after = projectSearchPage.getProjectCount();
        extentTest.info("Project count after unselecting brands: " + after);

        Assert.assertNotEquals(after, before, "❌ The project count did not change after unselecting brands.");
        extentTest.pass("✅ The project count updated after unselecting brands.");
    }

    // User should click on the furnishing field and select partially furnished and verify projects count updated or not.
    @Test(priority = 35, description = "Verifies that selecting 'Partially Furnished' filters the project list.")
    public void test35_SelectFurnishingPartiallyFurnished() {
        String before = projectSearchPage.getProjectCount();
        extentTest.info("Project count before selecting furnishing: " + before);

        projectSearchPage.toggleFurnishingPartiallyFurnished(); // Selects Partially Furnished and clicks outside
        projectSearchPage.waitFor(2000);

        String after = projectSearchPage.getProjectCount();
        extentTest.info("Project count after selecting furnishing: " + after);

        Assert.assertNotEquals(after, before, "❌ The project count did not change after selecting 'Partially Furnished'.");
        extentTest.pass("✅ The project count updated after selecting 'Partially Furnished'.");
    }

    // User should click on the furnishing field and unselected partially furnished and verify projects count updated or not.
    @Test(priority = 36, description = "Verifies that unselecting 'Partially Furnished' updates the project list.")
    public void test36_UnselectFurnishingPartiallyFurnished() {
        // Assume 'Partially Furnished' is selected from a previous test or setup step
        String before = projectSearchPage.getProjectCount();
        extentTest.info("Project count before unselecting furnishing: " + before);

        projectSearchPage.toggleFurnishingPartiallyFurnished(); // Unselects Partially Furnished and clicks outside
        projectSearchPage.waitFor(2000);

        String after = projectSearchPage.getProjectCount();
        extentTest.info("Project count after unselecting furnishing: " + after);

        Assert.assertNotEquals(after, before, "❌ The project count did not change after unselecting 'Partially Furnished'.");
        extentTest.pass("✅ The project count updated after unselecting 'Partially Furnished'.");
    }

    // user should click on the kitchen field and select the fully equipped option and verify results are updated or not.
    @Test(priority = 37, description = "Verifies that selecting 'Fully Equipped' for kitchen filters the project list.")
    public void test37_SelectKitchenFullyEquipped() {
        String before = projectSearchPage.getProjectCount();
        extentTest.info("Project count before selecting kitchen option: " + before);

        projectSearchPage.toggleKitchenFullyEquipped(); // Selects Fully Equipped and clicks outside
        projectSearchPage.waitFor(2000);

        String after = projectSearchPage.getProjectCount();
        extentTest.info("Project count after selecting kitchen option: " + after);

        Assert.assertNotEquals(after, before, "❌ The project count did not change after selecting 'Fully Equipped' kitchen option.");
        extentTest.pass("✅ The project count updated after selecting 'Fully Equipped' kitchen option.");
    }

    // user should click on the kitchen field and unselect the fully equipped option and verify results are updated or not.
    @Test(priority = 38, description = "Verifies that unselecting 'Fully Equipped' for kitchen updates the project list.")
    public void test38_UnselectKitchenFullyEquipped() {
        // Assume 'Fully Equipped' is selected from a previous test or setup step
        String before = projectSearchPage.getProjectCount();
        extentTest.info("Project count before unselecting kitchen option: " + before);

        projectSearchPage.toggleKitchenFullyEquipped(); // Unselects Fully Equipped and clicks outside
        projectSearchPage.waitFor(2000);

        String after = projectSearchPage.getProjectCount();
        extentTest.info("Project count after unselecting kitchen option: " + after);

        Assert.assertNotEquals(after, before, "❌ The project count did not change after unselecting 'Fully Equipped' kitchen option.");
        extentTest.pass("✅ The project count updated after unselecting 'Fully Equipped' kitchen option.");
    }

    // user should click and select Multiple Amenities and verify results are updated or not.
    @Test(priority = 39, description = "Verifies that selecting 'A La Carte Services' amenity filters the project list.")
    public void test39_SelectMultipleAmenities() {
        String before = projectSearchPage.getProjectCount();
        extentTest.info("Project count before selecting amenities: " + before);

        // This locator targets multiple elements. Assuming the test intends to click the first one found.
        projectSearchPage.selectAmenitiesALaCarteServices();
        projectSearchPage.waitFor(3000); // Amenities filter might take longer

        String after = projectSearchPage.getProjectCount();
        extentTest.info("Project count after selecting amenities: " + after);

        Assert.assertNotEquals(after, before, "❌ The project count did not change after selecting amenities.");
        extentTest.pass("✅ The project count updated after selecting amenities.");
    }

    // user should click on the Reset Filter Text and verify Project count updated.
    @Test(priority = 40, description = "Verifies that clicking the 'Reset Filters' button clears all filters and updates the project count.")
    public void test40_ClickResetFilter() {
        String currentProjectCount = projectSearchPage.getProjectCount(); // Current count (after previous filters)
        extentTest.info("Project count before clicking 'Reset Filters': " + currentProjectCount);

        projectSearchPage.clickResetFiltersButton();
        projectSearchPage.waitFor(2000);

        String projectCountAfterReset = projectSearchPage.getProjectCount(); // Count after reset
        extentTest.info("Project count after clicking 'Reset Filters': " + projectCountAfterReset);

        Assert.assertNotEquals(projectCountAfterReset, currentProjectCount, "❌ The project count did not change after clicking 'Reset Filters'.");
        extentTest.pass("✅ The project count updated after clicking 'Reset Filters'.");
    }

    // This test closes the Advance Filter drawer, typically run after all advance filter tests.
    @Test(priority = 41, description = "Verifies that clicking the 'Apply' button in the Advance Filter section closes it.")
    public void test41_ClickApplyButtonAndVerifyDrawerClosed() {
        // Ensure drawer is open before trying to apply
        if (!projectSearchPage.isAdvanceFilterDrawerVisible()) {
            projectSearchPage.clickAdvanceFilter();
            projectSearchPage.waitFor(1000); // Allow time for drawer to open
            extentTest.info("Re-opened the Advance Filter section for this test.");
        }

        projectSearchPage.clickApplyButton();
        projectSearchPage.waitFor(1000); // Allow time for drawer to close

        Assert.assertFalse(projectSearchPage.isAdvanceFilterDrawerVisible(), "❌ The Advance Filter section is still visible after clicking 'Apply'.");
        extentTest.pass("✅ The 'Apply' button closed the Advance Filter section as expected.");
    }

    // Verify User click and selected the heatmap checkbox or not
    @Test(priority = 42, description = "Verifies that selecting the 'Heatmap' option updates the displayed project count.")
    public void test42_VerifyHeatMapSelected() {
        String before = projectSearchPage.getProjectCount();
        extentTest.info("Project count before selecting Heatmap: " + before);

        projectSearchPage.toggleHeatmapCheckbox();
        projectSearchPage.waitFor(3000);

        String after = projectSearchPage.getProjectCount();
        extentTest.info("Project count after selecting Heatmap: " + after);

        Assert.assertNotEquals(after, before, "❌ The project count did not change after selecting Heatmap.");
        extentTest.pass("✅ The project count updated after selecting Heatmap.");
    }

    @Test(priority = 43, description = "Verifies that sorting the project list by different options (Earliest Completion, Latest Completion, Recently Added) reorders the projects.")
    public void test43_VerifyProjectListUpdatesOnAllSortOptions() {
        // Define all sort options to test
        Map<String, By> sortOptions = new LinkedHashMap<>();
        sortOptions.put("Earliest Completion", projectSearchPage.getSortByEarliestCompletion());
        sortOptions.put("Latest Completion", projectSearchPage.getSortByLatestCompletion());
        sortOptions.put("Recently Added", projectSearchPage.getSortByRecentlyAdded());

        List<String> previousList = projectSearchPage.getProjectNames();
        boolean allOptionsSorted = true;
        extentTest.info("Initial project list retrieved.");

        for (Map.Entry<String, By> entry : sortOptions.entrySet()) {
            String optionName = entry.getKey();
            By locator = entry.getValue();

            extentTest.info("Attempting to sort by: '" + optionName + "'.");
            projectSearchPage.clickSortByDropdown();  // Open dropdown
            projectSearchPage.clickSortOption(locator); // Click the specific option
            projectSearchPage.waitFor(3000); // Allow time for the page to update

            List<String> currentList = projectSearchPage.getProjectNames();
            extentTest.info("Project list after sorting by '" + optionName + "': " + currentList.size() + " projects found.");

            if (!previousList.equals(currentList)) {
                extentTest.pass("✅ Project list successfully reordered/updated after sorting by '" + optionName + "'.");
            } else {
                extentTest.fail("❌ Project list did NOT change after sorting by '" + optionName + "'. (Expected a reorder)");
                allOptionsSorted = false;
            }

            previousList = currentList; // Prepare for next comparison
        }

        Assert.assertTrue(allOptionsSorted, "❌ One or more sort options did not update the project list as expected.");
    }

    @Test(priority = 45, description = "Verifies that submitting the feedback form displays a successful thank-you message.")
    public void test45_SubmitFeedbackForm() {
        String answer1 = "This is a test feedback message for automated testing.";
        String answer2 = "Second part of test feedback.";

        extentTest.info("Clicking the 'Feedback' button and entering test messages.");
        projectSearchPage.submitFeedback(answer1, answer2);

        boolean isThankYouShown = projectSearchPage.isThankYouNodePresent();

        if (isThankYouShown) {
            extentTest.pass("✅ Feedback submitted successfully. A thank-you message appeared.");
        } else {
            extentTest.fail("❌ Feedback submission failed. The thank-you message was not found.");
        }
        Assert.assertTrue(isThankYouShown, "❌ The feedback form did not show the thank-you message.");
    }

    // Special Test method for handeling multiple click actions on the Single Project card.
    @Test(priority = 46, description = "Verifies that clicking elements on a project card (like title, bedrooms, payments) opens the correct linked page/tab.")
    public void test46_ProjectElementNavigationAndVerification() {
        extentTest.info("Checking if clicking different parts of a project card leads to the right sections.");
        Map<String, Boolean> results = projectSearchPage.verifyNavigationForClickableElements();
        boolean allPassed = true;

        for (Map.Entry<String, Boolean> entry : results.entrySet()) {
            String label = entry.getKey();
            boolean passed = entry.getValue();

            if (passed) {
                extentTest.pass("✅ Clicking the '" + label + "' element worked as expected.");
            } else {
                extentTest.fail("❌ Clicking the '" + label + "' element did not lead to the expected page/content.");
                allPassed = false;
            }
        }

        if (!allPassed) {
            Assert.fail("One or more clickable elements on the project card failed to navigate correctly.");
        }
    }
}