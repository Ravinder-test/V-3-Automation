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
            extentTest.fail("The test failed: " + result.getThrowable()); // More descriptive failure message
        } else if (result.getStatus() == ITestResult.SKIP) {
            extentTest.skip("This test was skipped."); // Clearer skip message
        } else {
            extentTest.pass("The test passed successfully."); // Clearer pass message
        }
    }
    
    // For Handeling the News and Onboarding Pop-up.
    
    @Test(priority = 1, description = "Master controller to handle onboarding and news popup logic.")
    public void test1_HandleOnboardingAndNewsPopup() {
        extentTest = extent.createTest("Popup Handler", "Determines and executes appropriate pop-up flow");

        boolean isOnboardingVisible = projectSearchPage.isOnboardingDialogVisible();
        boolean isNewsVisible = projectSearchPage.isNewsDialogVisible();

        if (isOnboardingVisible) {
            extentTest.info("Onboarding dialog is visible. Running complete onboarding flow.");
            System.out.println("Onboarding dialog is visible. Running complete onboarding flow.");
            test2_CompleteOnboardingSteps(); // Call directly
            projectSearchPage.waitFor(2000); // wait for news popup to appear
            test3_HandleNewsPopup();        // Then check news popup
        } else if (isNewsVisible) {
            extentTest.info("News popup is visible. Running news popup flow directly.");
            System.out.println("News popup is visible. Running news popup flow directly.");
            test3_HandleNewsPopup();
            test4_SkipOnboardingSteps();
        } else {
            extentTest.info("No popups found. Executing both flows manually.");
            System.out.println("No popups found. Executing both flows manually.");
            test2_CompleteOnboardingSteps(); // Launch manually
            projectSearchPage.waitFor(2000); // wait for news
            test3_HandleNewsPopup();
            
        }
    }
    
    
    
    @Test(priority = 2, description = "Verifies that user can complete all 7 steps of the onboarding dialog.")
    public void test2_CompleteOnboardingSteps() {
        extentTest = extent.createTest("Complete Onboarding Flow", "Verifies that user can complete all onboarding steps.");

        if (!projectSearchPage.isOnboardingDialogVisible()) {
            String info = "INFO: Forced Onboarding dialog is not present. Clicking onboarding icon manually.";
            extentTest.info(info);
            System.out.println(info);
            projectSearchPage.clickOnboardingIcon();
        }

        if (projectSearchPage.isOnboardingDialogVisible()) {
            String info = "INFO: On click of Onboarding Icon the Onboarding dialog is visible.";
            extentTest.info(info);
            System.out.println(info);

            try {
                // Step 1 - Click Explore
                String title1 = projectSearchPage.getCurrentOnboardingStepTitle();
                extentTest.info("Onboarding Step 1 - Title: " + title1);
                System.out.println("Step 1 Title: " + title1);
                projectSearchPage.clickExploreButton();
                System.out.println("Clicked Explore button");
                projectSearchPage.waitFor(1000);

                // Step 2
                String title2 = projectSearchPage.getCurrentOnboardingStepTitle();
                extentTest.info("Onboarding Step 2 - Title: " + title2);
                System.out.println("Step 2 Title: " + title2);
                projectSearchPage.clickNextButton();
                System.out.println("Clicked Next button (Step 2)");
                projectSearchPage.waitFor(1000);

                // Step 3
                String title3 = projectSearchPage.getCurrentOnboardingStepTitle();
                extentTest.info("Onboarding Step 3 - Title: " + title3);
                System.out.println("Step 3 Title: " + title3);
                projectSearchPage.clickNextButton();
                System.out.println("Clicked Next button (Step 3)");
                projectSearchPage.waitFor(1000);

                // Step 4
                String title4 = projectSearchPage.getCurrentOnboardingStepTitle();
                extentTest.info("Onboarding Step 4 - Title: " + title4);
                System.out.println("Step 4 Title: " + title4);
                projectSearchPage.clickNextButton();
                System.out.println("Clicked Next button (Step 4)");
                projectSearchPage.waitFor(1000);

                // Step 5
                String title5 = projectSearchPage.getCurrentOnboardingStepTitle();
                extentTest.info("Onboarding Step 5 - Title: " + title5);
                System.out.println("Step 5 Title: " + title5);
                projectSearchPage.clickNextButton();
                System.out.println("Clicked Next button (Step 5)");
                projectSearchPage.waitFor(1000);

                // Step 6
                String title6 = projectSearchPage.getCurrentOnboardingStepTitle();
                extentTest.info("Onboarding Step 6 - Title: " + title6);
                System.out.println("Step 6 Title: " + title6);
                projectSearchPage.clickNextButton();
                System.out.println("Clicked Next button (Step 6)");
                projectSearchPage.waitFor(1000);

                // Step 7 - Got it
                String title7 = projectSearchPage.getCurrentOnboardingStepTitle();
                extentTest.info("Onboarding Step 7 - Title: " + title7);
                System.out.println("Step 7 Title: " + title7);
                projectSearchPage.clickGotItButton();
                System.out.println("Clicked 'Got it' button");

                extentTest.pass("✅ Clicked on 'Got it' and completed onboarding successfully.");
                System.out.println("✅ Onboarding completed successfully.");
            } catch (Exception e) {
                String error = "❌ Failed during onboarding steps: " + e.getMessage();
                extentTest.fail(error);
                System.out.println(error);
                Assert.fail("Onboarding flow failed: " + e.getMessage());
            }

        } else {
            String error = "❌ Onboarding dialog did not appear even after clicking the icon.";
            extentTest.fail(error);
            System.out.println(error);
            Assert.fail("Onboarding dialog not found.");
        }
    }
    
    @Test(priority = 3, description = "Verifies that user can preview the News pop-up")
    public void test3_HandleNewsPopup() {
        extentTest = extent.createTest("News Popup Test", "Validates news pop-up behavior");

        try {
            if (!projectSearchPage.isNewsDialogVisible()) {
                extentTest.info("News popup not auto-launched. Clicking news icon manually.");
                System.out.println("News popup not auto-launched. Clicking news icon manually.");
                projectSearchPage.clickNewsIcon();
            }
            projectSearchPage.waitUntilNewsListRenders();
            
            if (projectSearchPage.isNewsDialogVisible()) {
                extentTest.info("News popup is visible.");
                System.out.println("News popup is visible.");

                if (!projectSearchPage.isNewsListPresent()) {
                    extentTest.info("No news found. Closing popup.");
                    System.out.println("No news found. Closing popup.");
                    projectSearchPage.clickCloseNewsPopup();
                    extentTest.pass("✅ No news found. Popup closed successfully.");
                    return;
                }

                if (projectSearchPage.isExternalNewsButtonPresent()) {
                    extentTest.info("External news button found. Clicking and verifying tab switch.");
                    System.out.println("External news button found. Clicking and verifying tab switch.");

                    projectSearchPage.clickExternalNewsButtonAndVerifyTab();
                    projectSearchPage.clickCloseNewsPopup();
                    extentTest.pass("✅ External button worked, tab handled, and news popup closed.");
                } else {
                    extentTest.info("No external button found. Closing popup.");
                    System.out.println("No external button found. Closing popup.");
                    projectSearchPage.clickCloseNewsPopup();
                    extentTest.pass("✅ No external button. Popup closed successfully.");
                }
            } else {
                extentTest.fail("❌ News popup did not appear.");
                System.out.println("❌ News popup did not appear.");
            }
        } catch (Exception e) {
            extentTest.fail("❌ Exception in News Popup test: " + e.getMessage());
            System.out.println("❌ Exception in News Popup test: " + e.getMessage());
        }
    }
    
    @Test(priority = 4, description = "Verifies that user can skip the onboarding dialog if shown.")
    public void test4_SkipOnboardingSteps() {
        extentTest = extent.createTest("Skip Onboarding Flow", "Verifies that user can skip onboarding after opening it manually.");

        if (!projectSearchPage.isOnboardingDialogVisible()) {
            extentTest.info("INFO: Forced Onboarding dialog is not present. Clicking onboarding icon manually.");
            projectSearchPage.clickOnboardingIcon();
        }

        if (projectSearchPage.isOnboardingDialogVisible()) {
            extentTest.info("INFO: On click of Onboarding Icon the Onboarding dialog is visible.");
            try {            	
            	// Step 1 - Click Explore
                String title1 = projectSearchPage.getCurrentOnboardingStepTitle();
                extentTest.info("Onboarding Step 1 - Title: " + title1);
                System.out.println("Step 1 Title: " + title1);
                projectSearchPage.clickSkipButton();
                System.out.println("Clicked Skip button");
                projectSearchPage.waitFor(1000);
                // Step 7 - Got it
                String title2 = projectSearchPage.getCurrentOnboardingStepTitle();
                extentTest.info("Onboarding Step 7 - Title: " + title2);
                System.out.println("Step 7 Title: " + title2);
                projectSearchPage.clickGotItButton();
                System.out.println("Clicked 'Got it' button");

                extentTest.pass("✅ Clicked on 'Got it' and onboarding process skipped successfully.");
                System.out.println("✅ Onboarding skipped successfully.");
            } catch (Exception e) {
                String error = "❌ Failed during onboarding steps: " + e.getMessage();
                extentTest.fail(error);
                System.out.println(error);
                Assert.fail("Onboarding flow failed: " + e.getMessage());
            }

        } else {
            String error = "❌ Onboarding dialog did not appear even after clicking the icon.";
            extentTest.fail(error);
            System.out.println(error);
            Assert.fail("Onboarding dialog not found.");
        }
    }
    
    
    // Search for Jumeriah location and click on it, Verify project results updated or not
    @Test(priority = 5, description = "Verifies that searching for a specific location updates the displayed project count.")
    public void test5_SearchLocationUpdatesProjectCount() {
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
    @Test(priority = 6, description = "Verifies that clearing the location filter reverts the project count.")
    public void test6_ClearLocationUpdatesProjectCount() {
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

    @Test(priority = 7, description = "Verifies that selecting and unselecting each Unit Type (Apartment, Villa, Townhouse) updates the project count correctly.")
    public void test7_VerifyAllUnitTypesFiltering() {
        // Define unit types and their locators
        // Using LinkedHashMap to maintain order if desired, though not strictly necessary for this test.
        LinkedHashMap<String, By> unitTypes = new LinkedHashMap<>();
        unitTypes.put("Apartment", projectSearchPage.apartmentCheckboxLabel);
        unitTypes.put("Villa", projectSearchPage.villaCheckboxLabel);
        unitTypes.put("Townhouse", projectSearchPage.townhouseCheckboxLabel);

        boolean allFiltersPassed = true; // Flag to track overall test result

        for (Map.Entry<String, By> entry : unitTypes.entrySet()) {
            String unitTypeName = entry.getKey();
            By locator = entry.getValue();

            // --- Step 1: Select the Unit Type and Verify Update ---
            extentTest.info("Attempting to select Unit Type: '" + unitTypeName + "'.");
            String beforeSelectionCount = projectSearchPage.getProjectCount();
            extentTest.info("Project count before selecting '" + unitTypeName + "': " + beforeSelectionCount);

            try {
                projectSearchPage.toggleUnitType(locator); // Clicks dropdown, selects, clicks outside
                projectSearchPage.waitFor(3000); // Allow time for filtering to apply

                String afterSelectionCount = projectSearchPage.getProjectCount();
                extentTest.info("Project count after selecting '" + unitTypeName + "': " + afterSelectionCount);

                if (!afterSelectionCount.equals(beforeSelectionCount)) {
                    extentTest.pass("✅ Project count updated after selecting '" + unitTypeName + "'.");
                } else {
                    extentTest.fail("❌ Project count did NOT change after selecting '" + unitTypeName + "'. (Expected update)");
                    allFiltersPassed = false;
                }
            } catch (Exception e) {
                String errorMsg = "❌ Failed to select '" + unitTypeName + "' due to: " + e.getMessage();
                extentTest.fail(errorMsg);
                System.out.println(errorMsg);
                allFiltersPassed = false;
                // Optionally re-throw or break if a critical error occurs, or continue to next unit type
            }

            // --- Step 2: Unselect the Unit Type and Verify Update ---
            extentTest.info("Attempting to unselect Unit Type: '" + unitTypeName + "'.");
            String beforeUnselectionCount = projectSearchPage.getProjectCount(); // This should be the 'afterSelectionCount'
            extentTest.info("Project count before unselecting '" + unitTypeName + "': " + beforeUnselectionCount);

            try {
                projectSearchPage.toggleUnitType(locator); // Clicks dropdown, unselects, clicks outside
                projectSearchPage.waitFor(3000); // Allow time for filtering to revert

                String afterUnselectionCount = projectSearchPage.getProjectCount();
                extentTest.info("Project count after unselecting '" + unitTypeName + "': " + afterUnselectionCount);

                if (!afterUnselectionCount.equals(beforeUnselectionCount)) {
                    extentTest.pass("✅ Project count updated after unselecting '" + unitTypeName + "'.");
                } else {
                    extentTest.fail("❌ Project count did NOT change after unselecting '" + unitTypeName + "'. (Expected update)");
                    allFiltersPassed = false;
                }
            } catch (Exception e) {
                String errorMsg = "❌ Failed to unselect '" + unitTypeName + "' due to: " + e.getMessage();
                extentTest.fail(errorMsg);
                System.out.println(errorMsg);
                allFiltersPassed = false;
            }

            // After each pair of select/unselect, ensure the filter is cleared

        }
        Assert.assertTrue(allFiltersPassed, "❌ One or more unit type filter operations failed to update the project count as expected.");
    }

    @Test(priority = 8, description = "Verifies that selecting and unselecting each Sale Status (Upcoming, Pre-Sale, On Sale, Sold Out) updates the project count correctly.")
    public void test8_VerifyAllSaleStatusFiltering() {
        // Define sale statuses and their locators
        LinkedHashMap<String, By> saleStatuses = new LinkedHashMap<>();
        saleStatuses.put("Upcoming", projectSearchPage.upcomingCheckboxLabel);
        saleStatuses.put("Pre-Sale (EOI)", projectSearchPage.preSaleEoiCheckboxLabel);
        saleStatuses.put("On Sale", projectSearchPage.onSaleCheckboxLabel);
        saleStatuses.put("Sold Out", projectSearchPage.soldOutCheckboxLabel);

        boolean allFiltersPassed = true; // Flag to track overall test result

        for (Map.Entry<String, By> entry : saleStatuses.entrySet()) {
            String statusName = entry.getKey();
            By locator = entry.getValue();

            // --- Step 1: Select the Sale Status and Verify Update ---
            extentTest.info("Attempting to select Sale Status: '" + statusName + "'.");
            String beforeSelectionCount = projectSearchPage.getProjectCount();
            extentTest.info("Project count before selecting '" + statusName + "': " + beforeSelectionCount);

            try {
                projectSearchPage.toggleSaleStatus(locator); // Clicks dropdown, selects, clicks outside
                projectSearchPage.waitFor(3000); // Allow time for filtering to apply

                String afterSelectionCount = projectSearchPage.getProjectCount();
                extentTest.info("Project count after selecting '" + statusName + "': " + afterSelectionCount);

                if (!afterSelectionCount.equals(beforeSelectionCount)) {
                    extentTest.pass("✅ Project count updated after selecting '" + statusName + "'.");
                } else {
                    extentTest.fail("❌ Project count did NOT change after selecting '" + statusName + "'. (Expected update)");
                    allFiltersPassed = false;
                }
            } catch (Exception e) {
                String errorMsg = "❌ Failed to select '" + statusName + "' due to: " + e.getMessage();
                extentTest.fail(errorMsg);
                System.out.println(errorMsg);
                allFiltersPassed = false;
                // Optionally re-throw or break if a critical error occurs, or continue to next status
            }

            // --- Step 2: Unselect the Sale Status and Verify Update ---
            extentTest.info("Attempting to unselect Sale Status: '" + statusName + "'.");
            String beforeUnselectionCount = projectSearchPage.getProjectCount(); // This should be the 'afterSelectionCount'
            extentTest.info("Project count before unselecting '" + statusName + "': " + beforeUnselectionCount);

            try {
                projectSearchPage.toggleSaleStatus(locator); // Clicks dropdown, unselects, clicks outside
                projectSearchPage.waitFor(3000); // Allow time for filtering to revert

                String afterUnselectionCount = projectSearchPage.getProjectCount();
                extentTest.info("Project count after unselecting '" + statusName + "': " + afterUnselectionCount);

                if (!afterUnselectionCount.equals(beforeUnselectionCount)) {
                    extentTest.pass("✅ Project count updated after unselecting '" + statusName + "'.");
                } else {
                    extentTest.fail("❌ Project count did NOT change after unselecting '" + statusName + "'. (Expected update)");
                    allFiltersPassed = false;
                }
            } catch (Exception e) {
                String errorMsg = "❌ Failed to unselect '" + statusName + "' due to: " + e.getMessage();
                extentTest.fail(errorMsg);
                System.out.println(errorMsg);
                allFiltersPassed = false;
            }
        }

        // Final assertion for the entire test method
        Assert.assertTrue(allFiltersPassed, "❌ One or more sale status filter operations failed to update the project count as expected.");
    }

    // Click on the developer and search for "Aark Developer" and select it, then verify project is updated or not
    @Test(priority = 9, description = "Verifies that selecting and then unselecting a developer changes the project count correctly.")
    public void test9_SearchDeveloperProjectCount() {
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
    @Test(priority = 10, description = "Verifies that clicking 'Advance Filter' opens the advanced filter section.")
    public void test10_ClickAdvanceFilter() {
        projectSearchPage.clickAdvanceFilter();
        projectSearchPage.waitFor(1000); // Allow time for the drawer to appear

        Assert.assertTrue(projectSearchPage.isAdvanceFilterDrawerVisible(), "❌ The Advance Filter section is not visible.");
        extentTest.pass("✅ The Advance Filter section is visible as expected.");
    }

    // Select and unselect All Construction Status filter and verify project count updated or not
    @Test(priority = 11, description = "Verifies that selecting and unselecting each Construction Status (Upcoming, Under Construction, On Hold, Cancelled, Completed, Handed Over) updates the project count correctly.")
    public void test11_VerifyAllConstructionStatusFiltering() {
        LinkedHashMap<String, By> constructionStatuses = new LinkedHashMap<>();
        constructionStatuses.put("Upcoming", projectSearchPage.upcomingStatusCheckboxLabel);
        constructionStatuses.put("Under Construction", projectSearchPage.underConstructionStatusCheckboxLabel);
        constructionStatuses.put("On Hold", projectSearchPage.onHoldStatusCheckboxLabel);
        constructionStatuses.put("Cancelled", projectSearchPage.cancelledStatusCheckboxLabel);
        constructionStatuses.put("Completed", projectSearchPage.completedStatusCheckboxLabel);
        constructionStatuses.put("Handed Over", projectSearchPage.handedOverStatusCheckboxLabel);

        boolean allFiltersPassed = true;

        for (Map.Entry<String, By> entry : constructionStatuses.entrySet()) {
            String statusName = entry.getKey();
            By locator = entry.getValue();

            // --- Step 1: Select the Construction Status and Verify Update ---
            extentTest.info("Attempting to select Construction Status: '" + statusName + "'.");
            String beforeSelectionCount = projectSearchPage.getProjectCount();
            extentTest.info("Project count before selecting '" + statusName + "': " + beforeSelectionCount);

            try {
                projectSearchPage.toggleConstructionStatus(locator); // Clicks dropdown, selects, clicks outside
                projectSearchPage.waitFor(3000); // Allow time for filtering to apply

                String afterSelectionCount = projectSearchPage.getProjectCount();
                extentTest.info("Project count after selecting '" + statusName + "': " + afterSelectionCount);

                if (!afterSelectionCount.equals(beforeSelectionCount)) {
                    extentTest.pass("✅ Project count updated after selecting '" + statusName + "'.");
                } else {
                    extentTest.fail("❌ Project count did NOT change after selecting '" + statusName + "'. (Expected update)");
                    allFiltersPassed = false;
                }
            } catch (Exception e) {
                String errorMsg = "❌ Failed to select '" + statusName + "' due to: " + e.getMessage();
                extentTest.fail(errorMsg);
                System.out.println(errorMsg);
                allFiltersPassed = false;
            }

            // --- Step 2: Unselect the Construction Status and Verify Update ---
            extentTest.info("Attempting to unselect Construction Status: '" + statusName + "'.");
            String beforeUnselectionCount = projectSearchPage.getProjectCount();
            extentTest.info("Project count before unselecting '" + statusName + "': " + beforeUnselectionCount);

            try {
                projectSearchPage.toggleConstructionStatus(locator); // Clicks dropdown, unselects, clicks outside
                projectSearchPage.waitFor(3000); // Allow time for filtering to revert

                String afterUnselectionCount = projectSearchPage.getProjectCount();
                extentTest.info("Project count after unselecting '" + statusName + "': " + afterUnselectionCount);

                if (!afterUnselectionCount.equals(beforeUnselectionCount)) {
                    extentTest.pass("✅ Project count updated after unselecting '" + statusName + "'.");
                } else {
                    extentTest.fail("❌ Project count did NOT change after unselecting '" + statusName + "'. (Expected update)");
                    allFiltersPassed = false;
                }
            } catch (Exception e) {
                String errorMsg = "❌ Failed to unselect '" + statusName + "' due to: " + e.getMessage();
                extentTest.fail(errorMsg);
                System.out.println(errorMsg);
                allFiltersPassed = false;
            }
        }

        Assert.assertTrue(allFiltersPassed, "❌ One or more construction status filter operations failed to update the project count as expected.");
    }

    // Click and select multiple completion date options and verify projects count updated
    @Test(priority = 12, description = "Verifies that selecting each Actual Completion Date option updates the project count, and that clearing the filter also updates the count.")
    public void test12_VerifyCompletionDateFilteringAndClear() {
        LinkedHashMap<String, By> completionDateOptions = new LinkedHashMap<>();
        completionDateOptions.put("Completed", projectSearchPage.completedDateOption);
        completionDateOptions.put("2025 (upcoming)", projectSearchPage.year2025UpcomingDateOption);
        completionDateOptions.put("2026", projectSearchPage.year2026DateOption);
        completionDateOptions.put("2027", projectSearchPage.year2027DateOption);
        completionDateOptions.put("2028 and later", projectSearchPage.year2028AndLaterDateOption);

        boolean allFiltersPassed = true;
        // String initialGlobalCount = projectSearchPage.getProjectCount(); // Uncomment if you need to verify against true initial unfiltered count after reset.

        for (Map.Entry<String, By> entry : completionDateOptions.entrySet()) {
            String optionName = entry.getKey();
            By locator = entry.getValue();

            extentTest.info("Attempting to select Actual Completion Date option: '" + optionName + "'.");
            String beforeSelectionCount = projectSearchPage.getProjectCount();
            extentTest.info("Project count before selecting '" + optionName + "': " + beforeSelectionCount);

            try {
                projectSearchPage.selectCompletionDateOption(locator); // Clicks dropdown, selects option, clicks outside
                projectSearchPage.waitFor(3000); // Allow time for filtering to apply

                String afterSelectionCount = projectSearchPage.getProjectCount();
                extentTest.info("Project count after selecting '" + optionName + "': " + afterSelectionCount);

                if (!afterSelectionCount.equals(beforeSelectionCount)) {
                    extentTest.pass("✅ Project count updated after selecting '" + optionName + "'.");
                } else {
                    extentTest.fail("❌ Project count did NOT change after selecting '" + optionName + "'. (Expected update)");
                    allFiltersPassed = false;
                }
            } catch (Exception e) {
                String errorMsg = "❌ Failed to select '" + optionName + "' due to: " + e.getMessage();
                extentTest.fail(errorMsg);
                System.out.println(errorMsg);
                allFiltersPassed = false;
            }
        }

        // --- Step: Clear the Selection and Verify Update ---
        extentTest.info("Attempting to clear Actual Completion Date selection.");
        String beforeClearCount = projectSearchPage.getProjectCount(); // This should be the count after the last selection
        extentTest.info("Project count before clearing completion date filter: " + beforeClearCount);

        try {
            projectSearchPage.clearSelectedCompletionDate(); // Clicks the clear icon
            projectSearchPage.waitFor(2000); // Allow time for filter to reset

            String afterClearCount = projectSearchPage.getProjectCount();
            extentTest.info("Project count after clearing completion date filter: " + afterClearCount);

            if (!afterClearCount.equals(beforeClearCount)) { // Verifies count changed after clearing
                extentTest.pass("✅ Project count updated after clearing the completion date filter.");
            } else {
                extentTest.fail("❌ Project count did NOT change after clearing the completion date filter. (Expected update)");
                allFiltersPassed = false;
            }
            // Optional: If you want to assert it returns to the *absolute initial count* of the page load
            // Assert.assertEquals(afterClearCount, initialGlobalCount, "❌ Project count did not revert to initial count after clearing.");
        } catch (Exception e) {
            String errorMsg = "❌ Failed to clear completion date filter due to: " + e.getMessage();
            extentTest.fail(errorMsg);
            System.out.println(errorMsg);
            allFiltersPassed = false;
        }

        Assert.assertTrue(allFiltersPassed, "❌ One or more Actual Completion Date filter operations or clear operation failed to update the project count as expected.");
    }

    //Click on the Sales start date field and select the last 90 days option and verify project count
    @Test(priority = 13, description = "Verifies that selecting each Sales Start Date option updates the project count, and that clearing the filter also updates the count.")
    public void test13_VerifySalesStartDateFilteringAndClear() {
        LinkedHashMap<String, By> salesStartDateOptions = new LinkedHashMap<>();
        salesStartDateOptions.put("Last 7 days", projectSearchPage.last7DaysOption);
        salesStartDateOptions.put("Last 30 days", projectSearchPage.last30DaysOption);
        salesStartDateOptions.put("Last 90 days", projectSearchPage.last90DaysOption);
        salesStartDateOptions.put("Next 7 days", projectSearchPage.next7DaysOption);
        salesStartDateOptions.put("Next 30 days", projectSearchPage.next30DaysOption);

        boolean allFiltersPassed = true;
        // String initialGlobalCount = projectSearchPage.getProjectCount(); // Uncomment if you need to verify against true initial unfiltered count after reset.

        for (Map.Entry<String, By> entry : salesStartDateOptions.entrySet()) {
            String optionName = entry.getKey();
            By locator = entry.getValue();

            extentTest.info("Attempting to select Sales Start Date option: '" + optionName + "'.");
            String beforeSelectionCount = projectSearchPage.getProjectCount();
            extentTest.info("Project count before selecting '" + optionName + "': " + beforeSelectionCount);

            try {
                projectSearchPage.selectSalesStartDateOption(locator); // Clicks dropdown, selects option, clicks outside
                projectSearchPage.waitFor(3000); // Allow time for filtering to apply

                String afterSelectionCount = projectSearchPage.getProjectCount();
                extentTest.info("Project count after selecting '" + optionName + "': " + afterSelectionCount);

                if (!afterSelectionCount.equals(beforeSelectionCount)) {
                    extentTest.pass("✅ Project count updated after selecting '" + optionName + "'.");
                } else {
                    extentTest.fail("❌ Project count did NOT change after selecting '" + optionName + "'. (Expected update)");
                    allFiltersPassed = false;
                }
            } catch (Exception e) {
                String errorMsg = "❌ Failed to select '" + optionName + "' due to: " + e.getMessage();
                extentTest.fail(errorMsg);
                System.out.println(errorMsg);
                allFiltersPassed = false;
            }
        }

        // --- Step: Clear the Selection and Verify Update ---
        extentTest.info("Attempting to clear Sales Start Date selection.");
        String beforeClearCount = projectSearchPage.getProjectCount(); // This should be the count after the last selection
        extentTest.info("Project count before clearing sales start date filter: " + beforeClearCount);

        try {
            projectSearchPage.clearSelectedCompletionDate(); // Clicks the clear icon
            projectSearchPage.waitFor(2000); // Allow time for filter to reset

            String afterClearCount = projectSearchPage.getProjectCount();
            extentTest.info("Project count after clearing sales start date filter: " + afterClearCount);

            if (!afterClearCount.equals(beforeClearCount)) { // Verifies count changed after clearing
                extentTest.pass("✅ Project count updated after clearing the sales start date filter.");
            } else {
                extentTest.fail("❌ Project count did NOT change after clearing the sales start date filter. (Expected update)");
                allFiltersPassed = false;
            }
            // Optional: If you want to assert it returns to the *absolute initial count* of the page load
            // Assert.assertEquals(afterClearCount, initialGlobalCount, "❌ Project count did not revert to initial count after clearing.");
        } catch (Exception e) {
            String errorMsg = "❌ Failed to clear sales start date filter due to: " + e.getMessage();
            extentTest.fail(errorMsg);
            System.out.println(errorMsg);
            allFiltersPassed = false;
        }

        Assert.assertTrue(allFiltersPassed, "❌ One or more Sales Start Date filter operations or clear operation failed to update the project count as expected.");
    }

    // On the Completion Percentage input Min and Max values and Verify the Updated project count
    @Test(priority = 14, description = "Verifies that entering minimum and maximum completion percentage values filters the project list.")
    public void test14_InputMinMaxCompletionPercentage() {
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
    @Test(priority = 15, description = "Verifies that resetting the completion percentage range updates the project list.")
    public void test15_ResetMinMaxCompletionPercentage() {
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
    @Test(priority = 16, description = "Verifies that selecting and unselecting each Bedroom option (Studio, 1-7, 8+) updates the project count correctly.")
    public void test16_VerifyBedroomFiltering() {
        LinkedHashMap<String, By> bedroomOptions = new LinkedHashMap<>();
        bedroomOptions.put("Studio", projectSearchPage.studioBedroomOption);
        bedroomOptions.put("1", projectSearchPage.oneBedroomOption);
        bedroomOptions.put("2", projectSearchPage.twoBedroomOption);
        bedroomOptions.put("3", projectSearchPage.threeBedroomOption);
        bedroomOptions.put("4", projectSearchPage.fourBedroomOption);
        bedroomOptions.put("5", projectSearchPage.fiveBedroomOption);
        bedroomOptions.put("6", projectSearchPage.sixBedroomOption);
        bedroomOptions.put("7", projectSearchPage.sevenBedroomOption);
        bedroomOptions.put("8+", projectSearchPage.eightPlusBedroomOption);

        boolean allFiltersPassed = true;

        for (Map.Entry<String, By> entry : bedroomOptions.entrySet()) {
            String bedroomName = entry.getKey();
            By locator = entry.getValue();

            // --- Step 1: Select the Bedroom option and Verify Update ---
            extentTest.info("Attempting to select Bedroom option: '" + bedroomName + "'.");
            String beforeSelectionCount = projectSearchPage.getProjectCount();
            extentTest.info("Project count before selecting '" + bedroomName + "': " + beforeSelectionCount);

            try {
                projectSearchPage.toggleBedroomOption(locator); // Clicks dropdown, selects, clicks outside
                projectSearchPage.waitFor(3000); // Allow time for filtering to apply

                String afterSelectionCount = projectSearchPage.getProjectCount();
                extentTest.info("Project count after selecting '" + bedroomName + "': " + afterSelectionCount);

                if (!afterSelectionCount.equals(beforeSelectionCount)) {
                    extentTest.pass("✅ Project count updated after selecting '" + bedroomName + "'.");
                } else {
                    extentTest.fail("❌ Project count did NOT change after selecting '" + bedroomName + "'. (Expected update)");
                    allFiltersPassed = false;
                }
            } catch (Exception e) {
                String errorMsg = "❌ Failed to select '" + bedroomName + "' due to: " + e.getMessage();
                extentTest.fail(errorMsg);
                System.out.println(errorMsg);
                allFiltersPassed = false;
            }

            // --- Step 2: Unselect the Bedroom option and Verify Update ---
            extentTest.info("Attempting to unselect Bedroom option: '" + bedroomName + "'.");
            String beforeUnselectionCount = projectSearchPage.getProjectCount(); // This should be the 'afterSelectionCount'
            extentTest.info("Project count before unselecting '" + bedroomName + "': " + beforeUnselectionCount);

            try {
                projectSearchPage.toggleBedroomOption(locator); // Clicks dropdown, unselects, clicks outside
                projectSearchPage.waitFor(3000); // Allow time for filtering to revert

                String afterUnselectionCount = projectSearchPage.getProjectCount();
                extentTest.info("Project count after unselecting '" + bedroomName + "': " + afterUnselectionCount);

                if (!afterUnselectionCount.equals(beforeUnselectionCount)) {
                    extentTest.pass("✅ Project count updated after unselecting '" + bedroomName + "'.");
                } else {
                    extentTest.fail("❌ Project count did NOT change after unselecting '" + bedroomName + "'. (Expected update)");
                    allFiltersPassed = false;
                }
            } catch (Exception e) {
                String errorMsg = "❌ Failed to unselect '" + bedroomName + "' due to: " + e.getMessage();
                extentTest.fail(errorMsg);
                System.out.println(errorMsg);
                allFiltersPassed = false;
            }
        }

        Assert.assertTrue(allFiltersPassed, "❌ One or more Bedroom filter operations failed to update the project count as expected.");
    }

    // User should input Min and Max value on the Built up area and verify Projects Count updated or not.
    @Test(priority = 17, description = "Verifies that entering minimum and maximum built-up area values filters the project list.")
    public void test17_InputBuiltupAreaValue() {
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
    @Test(priority = 18, description = "Verifies that resetting the built-up area range updates the project list.")
    public void test18_ResetBuiltupAreaValue() {
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

    // User should input the Min and Max values on the plot area and verify projects count are updated or not.
    @Test(priority = 19, description = "Verifies that entering minimum and maximum plot area values filters the project list.")
    public void test19_InputPlotAreaValue() {
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
    @Test(priority = 20, description = "Verifies that resetting the plot area range updates the project list.")
    public void test20_ResetPlotAreaValue() {
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
    @Test(priority = 21, description = "Verifies that entering minimum and maximum down payment percentage values filters the project list.")
    public void test21_InputDownPaymentValue() {
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
    @Test(priority = 22, description = "Verifies that resetting the down payment percentage range updates the project list.")
    public void test22_ResetDownPaymentValue() {
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

    // User should select Multiple Payment plan verify Results are updated or not
    @Test(priority = 23, description = "Verifies that selecting and unselecting each Payment Plan option (On Completion, Post Handover) updates the project count correctly.")
    public void test23_VerifyPaymentPlanFiltering() {
        LinkedHashMap<String, By> paymentPlanOptions = new LinkedHashMap<>();
        paymentPlanOptions.put("On Completion", projectSearchPage.onCompletionOption);
        paymentPlanOptions.put("Post Handover", projectSearchPage.postHandoverOption);

        boolean allFiltersPassed = true;

        for (Map.Entry<String, By> entry : paymentPlanOptions.entrySet()) {
            String optionName = entry.getKey();
            By locator = entry.getValue();

            // --- Step 1: Select the Payment Plan option and Verify Update ---
            extentTest.info("Attempting to select Payment Plan option: '" + optionName + "'.");
            String beforeSelectionCount = projectSearchPage.getProjectCount();
            extentTest.info("Project count before selecting '" + optionName + "': " + beforeSelectionCount);

            try {
                projectSearchPage.togglePaymentPlanOption(locator); // Clicks dropdown, selects, clicks outside
                projectSearchPage.waitFor(3000); // Allow time for filtering to apply

                String afterSelectionCount = projectSearchPage.getProjectCount();
                extentTest.info("Project count after selecting '" + optionName + "': " + afterSelectionCount);

                if (!afterSelectionCount.equals(beforeSelectionCount)) {
                    extentTest.pass("✅ Project count updated after selecting '" + optionName + "'.");
                } else {
                    extentTest.fail("❌ Project count did NOT change after selecting '" + optionName + "'. (Expected update)");
                    allFiltersPassed = false;
                }
            } catch (Exception e) {
                String errorMsg = "❌ Failed to select '" + optionName + "' due to: " + e.getMessage();
                extentTest.fail(errorMsg);
                System.out.println(errorMsg);
                allFiltersPassed = false;
            }

            // --- Step 2: Unselect the Payment Plan option and Verify Update ---
            extentTest.info("Attempting to unselect Payment Plan option: '" + optionName + "'.");
            String beforeUnselectionCount = projectSearchPage.getProjectCount(); // This should be the 'afterSelectionCount'
            extentTest.info("Project count before unselecting '" + optionName + "': " + beforeUnselectionCount);

            try {
                projectSearchPage.togglePaymentPlanOption(locator); // Clicks dropdown, unselects, clicks outside
                projectSearchPage.waitFor(3000); // Allow time for filtering to revert

                String afterUnselectionCount = projectSearchPage.getProjectCount();
                extentTest.info("Project count after unselecting '" + optionName + "': " + afterUnselectionCount);

                if (!afterUnselectionCount.equals(beforeUnselectionCount)) {
                    extentTest.pass("✅ Project count updated after unselecting '" + optionName + "'.");
                } else {
                    extentTest.fail("❌ Project count did NOT change after unselecting '" + optionName + "'. (Expected update)");
                    allFiltersPassed = false;
                }
            } catch (Exception e) {
                String errorMsg = "❌ Failed to unselect '" + optionName + "' due to: " + e.getMessage();
                extentTest.fail(errorMsg);
                System.out.println(errorMsg);
                allFiltersPassed = false;
            }
        }

        Assert.assertTrue(allFiltersPassed, "❌ One or more Payment Plan filter operations failed to update the project count as expected.");
    }

    // Below Test case is for selecting multiple DLD tranfer percentage.
    @Test(priority = 24, description = "Verifies that selecting and unselecting each DLD transfer fee option (0%, 50%, 100%) updates the project count correctly.")
    public void test24_VerifyDldTransferPercent() {
        LinkedHashMap<String, By> dldTransferOptions = new LinkedHashMap<>();
        dldTransferOptions.put("0%", projectSearchPage.selectZeroPercentageOption);
        dldTransferOptions.put("50%", projectSearchPage.selectFiftyPercentageOption);
        dldTransferOptions.put("100%", projectSearchPage.selectHundredPercentageOption);

        boolean allFiltersPassed = true;

        for (Map.Entry<String, By> entry : dldTransferOptions.entrySet()) {
            String optionName = entry.getKey();
            By locator = entry.getValue();

            // --- Step 1: Select the DLD transfer option and Verify Update ---
            extentTest.info("Attempting to select DLD transfer percentage option: '" + optionName + "'.");
            String beforeSelectionCount = projectSearchPage.getProjectCount();
            extentTest.info("Project count before selecting '" + optionName + "': " + beforeSelectionCount);

            try {
                projectSearchPage.toggleDldTransferPercent(locator); // Clicks dropdown, selects, clicks outside
                projectSearchPage.waitFor(3000); // Allow time for filtering to apply

                String afterSelectionCount = projectSearchPage.getProjectCount();
                extentTest.info("Project count after selecting '" + optionName + "': " + afterSelectionCount);

                if (!afterSelectionCount.equals(beforeSelectionCount)) {
                    extentTest.pass("✅ Project count updated after selecting '" + optionName + "'.");
                } else {
                    extentTest.fail("❌ Project count did NOT change after selecting '" + optionName + "'. (Expected update)");
                    allFiltersPassed = false;
                }
            } catch (Exception e) {
                String errorMsg = "❌ Failed to select '" + optionName + "' due to: " + e.getMessage();
                extentTest.fail(errorMsg);
                System.out.println(errorMsg);
                allFiltersPassed = false;
            }

            // --- Step 2: Unselect the DLD transfer Percentage Options and Verify Update ---
            extentTest.info("Attempting to unselect Payment Plan option: '" + optionName + "'.");
            String beforeUnselectionCount = projectSearchPage.getProjectCount(); // This should be the 'afterSelectionCount'
            extentTest.info("Project count before unselecting '" + optionName + "': " + beforeUnselectionCount);

            try {
                projectSearchPage.toggleDldTransferPercent(locator); // Clicks dropdown, unselects, clicks outside
                projectSearchPage.waitFor(3000); // Allow time for filtering to revert

                String afterUnselectionCount = projectSearchPage.getProjectCount();
                extentTest.info("Project count after unselecting '" + optionName + "': " + afterUnselectionCount);

                if (!afterUnselectionCount.equals(beforeUnselectionCount)) {
                    extentTest.pass("✅ Project count updated after unselecting '" + optionName + "'.");
                } else {
                    extentTest.fail("❌ Project count did NOT change after unselecting '" + optionName + "'. (Expected update)");
                    allFiltersPassed = false;
                }
            } catch (Exception e) {
                String errorMsg = "❌ Failed to unselect '" + optionName + "' due to: " + e.getMessage();
                extentTest.fail(errorMsg);
                System.out.println(errorMsg);
                allFiltersPassed = false;
            }
        }

        Assert.assertTrue(allFiltersPassed, "❌ One or more DLD transfer percenntage operations failed to update the project count as expected.");
    }


    // User should enter the Min and Max broker commission and verify project count updated or not
    @Test(priority = 25, description = "Verifies that entering minimum and maximum broker commission values filters the project list.")
    public void test25_InputBrokerCommissionValue() {
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
    @Test(priority = 26, description = "Verifies that resetting the broker commission range updates the project list.")
    public void test26_ResetBrokerCommissionValue() {
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
    @Test(priority = 27, description = "Verifies that selecting and unselecting each Height Class option (Low-Rise, Mid-Rise, High-Rise, Skyscraper, Single Family) updates the project count correctly.")
    public void test27_VerifyHeightClassFiltering() {
        LinkedHashMap<String, By> heightClassOptions = new LinkedHashMap<>();
        heightClassOptions.put("Low-Rise (1-4 floors)", projectSearchPage.lowRiseOption);
        heightClassOptions.put("Mid-Rise (5-12 floors)", projectSearchPage.midRiseOption);
        heightClassOptions.put("High-Rise (13-39 floors)", projectSearchPage.highRiseOption);
        heightClassOptions.put("Skyscraper (40 floors+)", projectSearchPage.skyscraperOption);
        heightClassOptions.put("Single Family (Villa/Townhouse)", projectSearchPage.singleFamilyOption);

        boolean allFiltersPassed = true;

        for (Map.Entry<String, By> entry : heightClassOptions.entrySet()) {
            String optionName = entry.getKey();
            By locator = entry.getValue();

            // --- Step 1: Select the Height Class option and Verify Update ---
            extentTest.info("Attempting to select Height Class option: '" + optionName + "'.");
            String beforeSelectionCount = projectSearchPage.getProjectCount();
            extentTest.info("Project count before selecting '" + optionName + "': " + beforeSelectionCount);

            try {
                projectSearchPage.toggleHeightClassOption(locator); // Clicks dropdown, selects, clicks outside
                projectSearchPage.waitFor(3000); // Allow time for filtering to apply

                String afterSelectionCount = projectSearchPage.getProjectCount();
                extentTest.info("Project count after selecting '" + optionName + "': " + afterSelectionCount);

                if (!afterSelectionCount.equals(beforeSelectionCount)) {
                    extentTest.pass("✅ Project count updated after selecting '" + optionName + "'.");
                } else {
                    extentTest.fail("❌ Project count did NOT change after selecting '" + optionName + "'. (Expected update)");
                    allFiltersPassed = false;
                }
            } catch (Exception e) {
                String errorMsg = "❌ Failed to select '" + optionName + "' due to: " + e.getMessage();
                extentTest.fail(errorMsg);
                System.out.println(errorMsg);
                allFiltersPassed = false;
            }

            // --- Step 2: Unselect the Height Class option and Verify Update ---
            extentTest.info("Attempting to unselect Height Class option: '" + optionName + "'.");
            String beforeUnselectionCount = projectSearchPage.getProjectCount(); // This should be the 'afterSelectionCount'
            extentTest.info("Project count before unselecting '" + optionName + "': " + beforeUnselectionCount);

            try {
                projectSearchPage.toggleHeightClassOption(locator); // Clicks dropdown, unselects, clicks outside
                projectSearchPage.waitFor(3000); // Allow time for filtering to revert

                String afterUnselectionCount = projectSearchPage.getProjectCount();
                extentTest.info("Project count after unselecting '" + optionName + "': " + afterUnselectionCount);

                if (!afterUnselectionCount.equals(beforeUnselectionCount)) {
                    extentTest.pass("✅ Project count updated after unselecting '" + optionName + "'.");
                } else {
                    extentTest.fail("❌ Project count did NOT change after unselecting '" + optionName + "'. (Expected update)");
                    allFiltersPassed = false;
                }
            } catch (Exception e) {
                String errorMsg = "❌ Failed to unselect '" + optionName + "' due to: " + e.getMessage();
                extentTest.fail(errorMsg);
                System.out.println(errorMsg);
                allFiltersPassed = false;
            }
        }

        Assert.assertTrue(allFiltersPassed, "❌ One or more Height Class filter operations failed to update the project count as expected.");
    }

    @Test(priority = 28, description = "Select and Unselect multiple brands ('Franck Muller', 'Armani') and verify project Count updates accordingly.")
    public void test28_SelectAndUnselectBrandFilter() {
        extentTest = extent.createTest("Select & Unselect Brand Filter",
                "User selects 'Franck Muller' and 'Armani' brands to filter, then resets the selection and verifies project Count updates both times.");

        // STEP 1: Select Brands and Verify Change
        String countBeforeSelection = projectSearchPage.getProjectCount();
        extentTest.info("Project count before selecting brands: " + countBeforeSelection);

        projectSearchPage.selectBrands();
        projectSearchPage.waitFor(2000);

        String countAfterSelection = projectSearchPage.getProjectCount();
        extentTest.info("Project count after selecting brands: " + countAfterSelection);

        try {
            Assert.assertNotEquals(countAfterSelection, countBeforeSelection, "❌ Project count did not change after selecting brands.");
            extentTest.pass("✅ Project count updated after selecting brands.");
        } catch (AssertionError e) {
            extentTest.fail("❌ " + e.getMessage());
            throw e;
        }

        // STEP 2: Unselect Brands and Verify Change
        String countBeforeUnselect = countAfterSelection;
        extentTest.info("Project count before unselecting brands: " + countBeforeUnselect);

        projectSearchPage.unselectBrands();
        projectSearchPage.waitFor(2000);

        String countAfterUnselect = projectSearchPage.getProjectCount();
        extentTest.info("Project count after unselecting brands: " + countAfterUnselect);

        try {
            Assert.assertNotEquals(countAfterUnselect, countBeforeUnselect, "❌ Project count did not change after unselecting brands.");
            extentTest.pass("✅ Project count updated after unselecting brands.");
        } catch (AssertionError e) {
            extentTest.fail("❌ " + e.getMessage());
            throw e;
        }
    }

    // User should click on the furnishing field and select each options one by one and verify projects count updated or not.
    @Test(priority = 29, description = "Verifies that selecting and unselecting each furnishing option (Fully Furnished, Partially Furnished, Unfurnished) updates the project count correctly.")
    public void test29_VerifyFurnishingFiltering() {
        LinkedHashMap<String, By> furnishingOptions = new LinkedHashMap<>();
        furnishingOptions.put("Fully Furnished", projectSearchPage.fullyFurnishedOption);
        furnishingOptions.put("Partially Furnished", projectSearchPage.partiallyFurnishedOption);
        furnishingOptions.put("Unfurnished", projectSearchPage.unFurnishedOption);

        boolean allFiltersPassed = true;

        for (Map.Entry<String, By> entry : furnishingOptions.entrySet()) {
            String optionName = entry.getKey();
            By locator = entry.getValue();

            // --- Step 1: Select the Furnishing option and Verify Update ---
            extentTest.info("Attempting to select Furnishing option: '" + optionName + "'.");
            String beforeSelectionCount = projectSearchPage.getProjectCount();
            extentTest.info("Project count before selecting '" + optionName + "': " + beforeSelectionCount);

            try {
                projectSearchPage.toggleFurnishingOptions(locator); // Clicks dropdown, selects, clicks outside
                projectSearchPage.waitFor(3000); // Allow time for filtering to apply

                String afterSelectionCount = projectSearchPage.getProjectCount();
                extentTest.info("Project count after selecting '" + optionName + "': " + afterSelectionCount);

                if (!afterSelectionCount.equals(beforeSelectionCount)) {
                    extentTest.pass("✅ Project count updated after selecting '" + optionName + "'.");
                } else {
                    extentTest.fail("❌ Project count did NOT change after selecting '" + optionName + "'. (Expected update)");
                    allFiltersPassed = false;
                }
            } catch (Exception e) {
                String errorMsg = "❌ Failed to select '" + optionName + "' due to: " + e.getMessage();
                extentTest.fail(errorMsg);
                System.out.println(errorMsg);
                allFiltersPassed = false;
            }

            // --- Step 2: Unselect the Furnishing option and Verify Update ---
            extentTest.info("Attempting to unselect Furnishing option: '" + optionName + "'.");
            String beforeUnselectionCount = projectSearchPage.getProjectCount(); // This should be the 'afterSelectionCount'
            extentTest.info("Project count before unselecting '" + optionName + "': " + beforeUnselectionCount);

            try {
                projectSearchPage.toggleFurnishingOptions(locator); // Clicks dropdown, unselects, clicks outside
                projectSearchPage.waitFor(3000); // Allow time for filtering to revert

                String afterUnselectionCount = projectSearchPage.getProjectCount();
                extentTest.info("Project count after unselecting '" + optionName + "': " + afterUnselectionCount);

                if (!afterUnselectionCount.equals(beforeUnselectionCount)) {
                    extentTest.pass("✅ Project count updated after unselecting '" + optionName + "'.");
                } else {
                    extentTest.fail("❌ Project count did NOT change after unselecting '" + optionName + "'. (Expected update)");
                    allFiltersPassed = false;
                }
            } catch (Exception e) {
                String errorMsg = "❌ Failed to unselect '" + optionName + "' due to: " + e.getMessage();
                extentTest.fail(errorMsg);
                System.out.println(errorMsg);
                allFiltersPassed = false;
            }
        }

        Assert.assertTrue(allFiltersPassed, "❌ One or more Furnishing filter operations failed to update the project count as expected.");
    }

    // user should click and select each and every options of Kitchen appliances and verify project count.
    
    @Test(priority = 30, description = "Verifies that selecting and unselecting each Kitchen options (Fully Equipped, Partially Equipped, Unequiooed) updates the project count correctly.")
    public void test30_selectKitchenFiltering() {
        LinkedHashMap<String, By> kitchenOptions = new LinkedHashMap<>();
        kitchenOptions.put("Fully Equipped", projectSearchPage.fullyEquippedOption);
        kitchenOptions.put("Partially Equipped", projectSearchPage.partiallyEquippedOption);
        kitchenOptions.put("Unequipped", projectSearchPage.unEquippedOption);

        boolean allFiltersPassed = true;

        for (Map.Entry<String, By> entry : kitchenOptions.entrySet()) {
            String optionName = entry.getKey();
            By locator = entry.getValue();

            // --- Step 1: Select the Kitchen option and Verify Update ---
            extentTest.info("Attempting to select Kitchen option: '" + optionName + "'.");
            String beforeSelectionCount = projectSearchPage.getProjectCount();
            extentTest.info("Project count before selecting '" + optionName + "': " + beforeSelectionCount);

            try {
                projectSearchPage.toggleKitchenOptions(locator); // Clicks dropdown, selects, clicks outside
                projectSearchPage.waitFor(3000); // Allow time for filtering to apply

                String afterSelectionCount = projectSearchPage.getProjectCount();
                extentTest.info("Project count after selecting '" + optionName + "': " + afterSelectionCount);

                if (!afterSelectionCount.equals(beforeSelectionCount)) {
                    extentTest.pass("✅ Project count updated after selecting '" + optionName + "'.");
                } else {
                    extentTest.fail("❌ Project count did NOT change after selecting '" + optionName + "'. (Expected update)");
                    allFiltersPassed = false;
                }
            } catch (Exception e) {
                String errorMsg = "❌ Failed to select '" + optionName + "' due to: " + e.getMessage();
                extentTest.fail(errorMsg);
                System.out.println(errorMsg);
                allFiltersPassed = false;
            }

            // --- Step 2: Unselect the Kitchen option and Verify Update ---
            extentTest.info("Attempting to unselect Kitchen option: '" + optionName + "'.");
            String beforeUnselectionCount = projectSearchPage.getProjectCount(); // This should be the 'afterSelectionCount'
            extentTest.info("Project count before unselecting '" + optionName + "': " + beforeUnselectionCount);

            try {
                projectSearchPage.toggleKitchenOptions(locator); // Clicks dropdown, unselects, clicks outside
                projectSearchPage.waitFor(3000); // Allow time for filtering to revert

                String afterUnselectionCount = projectSearchPage.getProjectCount();
                extentTest.info("Project count after unselecting '" + optionName + "': " + afterUnselectionCount);

                if (!afterUnselectionCount.equals(beforeUnselectionCount)) {
                    extentTest.pass("✅ Project count updated after unselecting '" + optionName + "'.");
                } else {
                    extentTest.fail("❌ Project count did NOT change after unselecting '" + optionName + "'. (Expected update)");
                    allFiltersPassed = false;
                }
            } catch (Exception e) {
                String errorMsg = "❌ Failed to unselect '" + optionName + "' due to: " + e.getMessage();
                extentTest.fail(errorMsg);
                System.out.println(errorMsg);
                allFiltersPassed = false;
            }
        }

        Assert.assertTrue(allFiltersPassed, "❌ One or more Kitchen filter operations failed to update the project count as expected.");
    }

    // user should click and select Multiple Amenities and verify results are updated or not.
    @Test(priority = 31, description = "Verifies that selecting 'A La Carte Services' amenity filters the project list.")
    public void test31_SelectMultipleAmenities() {
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
    @Test(priority = 32, description = "Verifies that clicking the 'Reset Filters' button clears all filters and updates the project count.")
    public void test32_ClickResetFilter() {
        String currentProjectCount = projectSearchPage.getProjectCount(); // Current count (after previous filters)
        extentTest.info("Project count before clicking 'Reset Filters': " + currentProjectCount);

        projectSearchPage.clickResetFiltersButton();
        projectSearchPage.waitFor(2000);

        projectSearchPage.clickApplyButton();
        String projectCountAfterReset = projectSearchPage.getProjectCount(); // Count after reset
        extentTest.info("Project count after clicking 'Reset Filters': " + projectCountAfterReset);

        Assert.assertNotEquals(projectCountAfterReset, currentProjectCount, "❌ The project count did not change after clicking 'Reset Filters'.");
        extentTest.pass("✅ The project count updated after clicking 'Reset Filters'.");
    }


    // Verify User click and selected the heatmap checkbox or not
    @Test(priority = 33, description = "Verifies that selecting the 'Heatmap' option makes the Year Range Filter visible.")
    public void test33_VerifyHeatMapSelected() {

        extentTest.info("Attempting to toggle Heatmap checkbox and verify Year Range Filter visibility.");    

        projectSearchPage.toggleHeatmapCheckbox(); // This method now includes waiting for verifyYearRange
        
        boolean isYearRangeFilterVisible = projectSearchPage.isYearRangeFilterPresent(); // Changed method call
        
        extentTest.info("Verification: Year Range Filter element presence after toggling Heatmap.");    

        Assert.assertTrue(isYearRangeFilterVisible, "❌ Year Range Filter is not present or visible after toggling Heatmap.");
        extentTest.pass("✅ Year Range Filter is present and visible after toggling Heatmap, confirming heatmap selection functionality.");    
    }

    @Test(priority = 34, description = "Verifies that sorting the project list by different options (Earliest Completion, Latest Completion, Recently Added) reorders the projects.")
    public void test34_VerifyProjectListUpdatesOnAllSortOptions() {
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

//    @Test(priority = 35, description = "Verifies that submitting the feedback form displays a successful thank-you message.")
//    public void test35_SubmitFeedbackForm() {
//        String answer1 = "This is a test feedback message for automated testing.";
//        String answer2 = "Second part of test feedback.";
//
//        extentTest.info("Clicking the 'Feedback' button and entering test messages.");
//        projectSearchPage.submitFeedback(answer1, answer2);
//
//        boolean isThankYouShown = projectSearchPage.isThankYouNodePresent();
//
//        if (isThankYouShown) {
//            extentTest.pass("✅ Feedback submitted successfully. A thank-you message appeared.");
//        } else {
//            extentTest.fail("❌ Feedback submission failed. The thank-you message was not found.");
//        }
//        Assert.assertTrue(isThankYouShown, "❌ The feedback form did not show the thank-you message.");
//    }

    // Special Test method for handeling multiple click actions on the Single Project card.
    @Test(priority = 36, description = "Verifies that clicking elements on a project card (like title, bedrooms, payments) opens the correct linked page/tab.")
    public void test36_ProjectElementNavigationAndVerification() {
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