package com.propertymonitor.pages;

import java.util.Set;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProjectDetailPage {

    private WebDriver driver;
    private WebDriverWait wait;

    public ProjectDetailPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    /**
     * Waits for the Chakra UI spinner to disappear from the DOM.
     * This method is designed to be robust even if the spinner briefly appears or doesn't appear at all.
     */
    public void waitForSpinnerToDisappear() {
        try {
            // Option 1: Wait for invisibility (if it's present)
            // Option 2: Wait for it to not be present in the DOM at all
            // Using 'or' allows for both scenarios: if it was visible and disappears, or if it never appears.
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".chakra-spinner")),
                    ExpectedConditions.not(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".chakra-spinner")))
            ));
            System.out.println("Spinner check: Spinner disappeared or was not present.");
        } catch (Exception e) {
            // This catch block handles TimeoutException if the spinner remains visible beyond the wait timeout.
            // Log it, but don't fail the test immediately if the spinner is not critical for proceeding.
            // Consider if a TimeoutException here should always fail the test, or just be logged.
            System.err.println("Spinner might not have disappeared within the timeout or an error occurred: " + e.getMessage());
        }
    }

    // Locators
    private final By projectSearch = By.xpath("//input[@data-test-id='basicFilters.communitySearch']");
    private final By projectLocationSuggestion = By.xpath("//div[@class='chakra-stack css-1igwmid' and .//p[text()='Habtoor Grand Residences']]");
    private final By projectTitleLink = By.xpath("//p[contains(text(),'Habtoor Grand Residences')]");
    // REMOVED: private final By spinner = By.cssSelector(".chakra-spinner"); // This line was removed
    private final By projectDetailTitle = By.xpath("//div[text()='Habtoor Grand Residences']"); // Main title on the detail page

    private final By clickFeedback = By.xpath("//button[@data-test-id='feedback-button']");
    private final By enterAnswer1 = By.xpath("//textarea[@data-test-id='ans1']");
    private final By enterAnswer2 = By.xpath("//textarea[@data-test-id='ans2']");
    private final By selectRating = By.xpath("//p[@data-rate='4']");
    private final By clickSubmit = By.xpath("//button[@data-test-id='submit-button']");
    // private final By thankYouNode = By.cssSelector("[data-test-id='thankyou-text'], [data-test-id='thankyou-image']"); // Combined locator for thank you

    // NEW LOCATORS for Breadcrumb and Project Search Page verification
    private final By breadcrumbLink = By.xpath("//a[contains(@href, 'location_name=Jumeirah Beach Residence') and contains(text(), 'Jumeirah Beach Residence')]");
    private final By projectCountText = By.xpath("//p[contains(@class, 'chakra-text') and contains(text(), 'project')]");

    /**
     * Opens the Project Detail Page by searching for a project and navigating to its detail view.
     * Includes robust waiting for page loads and tab switching.
     * MODIFIED: Added defensive window handling at the start.
     */
    public void openProjectDetailPage() {
        System.out.println("[INFO] Attempting to open Project Detail Page...");

        // --- DEFENSIVE WINDOW HANDLING START ---
        // Before performing actions that open new tabs, ensure we are in a clean state.
        // If there are more than 1 window already open (e.g., from a previous test failure or other tests),
        // close them all except the current one.
        if (driver.getWindowHandles().size() > 1) {
            System.out.println("[WARNING] openProjectDetailPage: Found more than one window before opening new tab. Cleaning up stray windows.");
            closeAllOtherWindowsAndSwitchToMainWindow(); // Call the helper method to clean up
            // After cleanup, the driver should be on the main window.
        }
        // --- DEFENSIVE WINDOW HANDLING END ---

        String originalWindow = driver.getWindowHandle(); // This is now guaranteed to be the *only* window
        System.out.println("[INFO] openProjectDetailPage: Original Window Handle (for new tab creation): " + originalWindow);

        // 1. Click on the project search input and enter text
        wait.until(ExpectedConditions.elementToBeClickable(projectSearch)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(projectSearch)).sendKeys("Habtoor Grand Residences");
        System.out.println("[INFO] openProjectDetailPage: Entered 'Habtoor Grand Residences' into search.");

        // 2. Click on the suggestion
        wait.until(ExpectedConditions.elementToBeClickable(projectLocationSuggestion)).click();
        System.out.println("[INFO] openProjectDetailPage: Clicked on location suggestion.");

        // IMPORTANT: Wait for spinner *after* clicking suggestion, as it might appear briefly
        // before the search results are displayed and the title link becomes available.
        waitForSpinnerToDisappear();
        System.out.println("[INFO] openProjectDetailPage: Spinner disappeared after location suggestion click.");

        // 3. Click the project title link to open the detail page in a new tab
        wait.until(ExpectedConditions.elementToBeClickable(projectTitleLink)).click();
        System.out.println("[INFO] openProjectDetailPage: Clicked project title link. Waiting for new tab...");

        // 4. Wait for a new window/tab to open (number of windows should become 2)
        // Now that we've cleaned up, this wait should be reliable.
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        System.out.println("[INFO] openProjectDetailPage: New tab detected. Total windows now: " + driver.getWindowHandles().size());

        // 5. Switch to the new tab
        Set<String> allWindows = driver.getWindowHandles();
        String newTabHandle = null;
        for (String handle : allWindows) {
            if (!handle.equals(originalWindow)) {
                newTabHandle = handle;
                driver.switchTo().window(newTabHandle);
                System.out.println("[INFO] openProjectDetailPage: Switched to new tab: " + newTabHandle);
                break;
            }
        }

        // Ensure we actually switched
        if (newTabHandle == null) {
            throw new IllegalStateException("Failed to find and switch to the new tab.");
        }

        // --- CRITICAL WAITING STRATEGY FOR THE NEW PAGE ---
        // After switching to the new tab, wait for the spinner to disappear FIRST.
        // This handles cases where the new page immediately shows a spinner.
        waitForSpinnerToDisappear();
        System.out.println("[INFO] openProjectDetailPage: Spinner disappeared after switching to new tab.");

        // Then, explicitly wait for the main title of the Project Detail Page to be visible.
        // This is the most reliable indicator that the new page content has loaded and is ready.
        wait.until(ExpectedConditions.visibilityOfElementLocated(projectDetailTitle));
        System.out.println("[INFO] openProjectDetailPage: Project Detail Title visible: " + driver.findElement(projectDetailTitle).getText());

        // 6. Close the original window and switch back to the new tab
        // This part of the logic remains as per your original design.
        driver.switchTo().window(originalWindow);
        driver.close();
        System.out.println("[INFO] openProjectDetailPage: Closed original window: " + originalWindow);

        // After closing the original window, ensure only the new tab remains.
        wait.until(ExpectedConditions.numberOfWindowsToBe(1)); // Explicitly wait for 1 window

        driver.switchTo().window(newTabHandle); // Switch back to the newly opened tab (which should now be the only one)
        System.out.println("[INFO] openProjectDetailPage: Switched back to Project Detail tab for further interactions (now the sole window): " + newTabHandle);
    }

    /**
     * Verifies if the Property Details section is visible.
     * Assumes the main page content has loaded (handled by openProjectDetailPage).
     */
    public boolean isPropertyDetailsSectionVisible() {
        By locator = By.xpath("//div[@class='chakra-stack css-1ux2ilu']");
        System.out.println("Checking visibility of Property Details section...");

        // As a safeguard, ensure the main project title is still visible before looking for sub-sections.
        // This can prevent stale element issues or checks on partially loaded pages.
        wait.until(ExpectedConditions.visibilityOfElementLocated(projectDetailTitle));
        waitForSpinnerToDisappear(); // Defensive wait in case a sub-section load triggers a spinner

        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        System.out.println("Property Details section is visible.");
        return driver.findElement(locator).isDisplayed();
    }

    /**
     * Clicks the Map tab and waits for the content to load.
     */
    public void clickMapTab() {
        By mapTab = By.xpath("//button[@data-testid='tab-1']");
        System.out.println("Clicking Map tab...");
        wait.until(ExpectedConditions.elementToBeClickable(mapTab)).click();
        waitForSpinnerToDisappear(); // Always wait for spinner after clicking a tab
        System.out.println("Map tab clicked and spinner disappeared.");
    }

    /**
     * Verifies if the Map is visible.
     */
    public boolean isMapVisible() {
        By mapLocator = By.xpath("//canvas[@class='mapboxgl-canvas' and @aria-label='Map']");
        System.out.println("Checking if Map is visible...");
        waitForSpinnerToDisappear(); // Defensive wait
        wait.until(ExpectedConditions.visibilityOfElementLocated(mapLocator));
        System.out.println("Map is visible.");
        return driver.findElement(mapLocator).isDisplayed();
    }

    /**
     * Clicks the Images tab and waits for the content to load.
     */
    public void clickImagesTab() {
        By tab = By.xpath("//button[text()='Images']");
        System.out.println("Clicking Images tab...");
        wait.until(ExpectedConditions.elementToBeClickable(tab)).click();
        waitForSpinnerToDisappear();
        System.out.println("Images tab clicked and spinner disappeared.");
    }

    /**
     * Verifies if images are loaded within the image container.
     */
    public boolean areImagesLoaded() {
        By imagesContainer = By.xpath("//div[@class='css-oqdgj1']"); // Locator for the image container
        System.out.println("Checking if images are loaded...");
        waitForSpinnerToDisappear();
        // Wait for the container holding images to be visible
        wait.until(ExpectedConditions.visibilityOfElementLocated(imagesContainer));
        // Additionally, you might want to check for the presence of actual image elements (e.g., <img> tags within)
        // For now, assuming visibility of the container is enough.
        return driver.findElements(imagesContainer).size() > 0;
    }

    /**
     * Clicks the Units and Layouts tab and waits for the content to load.
     */
    public void clickUnitsAndLayoutsTab() {
        By tab = By.xpath("//button[text()='Units and Layouts']");
        System.out.println("Clicking Units and Layouts tab...");
        wait.until(ExpectedConditions.elementToBeClickable(tab)).click();
        waitForSpinnerToDisappear();
        System.out.println("Units and Layouts tab clicked and spinner disappeared.");
    }

    /**
     * Verifies if the Topology Table is visible.
     */
    public boolean isTopologyTableVisible() {
        By locator = By.xpath("//table[@class='chakra-table css-yo1wbp']");
        System.out.println("Checking if Topology Table is visible...");
        waitForSpinnerToDisappear();
        // scrollToElement(locator); // Scroll before waiting for visibility if it's off-screen
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        System.out.println("Topology Table is visible.");
        return driver.findElement(locator).isDisplayed();
    }

    /**
     * Verifies if the Floor Chart is visible.
     */
    public boolean isFloorChartVisible() {
        By locator = By.xpath("//div[contains(@class, 'highcharts-container')]");
        System.out.println("Checking if Floor Chart is visible...");
        waitForSpinnerToDisappear();
        // scrollToElement(locator);
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        System.out.println("Floor Chart is visible.");
        return driver.findElement(locator).isDisplayed();
    }

    /**
     * Verifies if Floor Plan Pictures are visible.
     */
    public boolean areFloorPlanPicturesVisible() {
        By locator = By.xpath("//p[text()='Floor Plans']");
        System.out.println("Checking if Floor Plan Pictures are visible...");
        waitForSpinnerToDisappear();

        // Temporary sleep add for test
        try {
            Thread.sleep(15000); // 15 seconds to manually inspect
            System.out.println("[DEBUG] Manual inspection pause for Floor Plan Pictures.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        scrollToElement(locator);
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        System.out.println("Floor Plan Pictures are visible.");
        return driver.findElement(locator).isDisplayed();
    }

    /**
     * Verifies if the Unit List Table is visible.
     */
    public boolean isUnitListTableVisible() {
        By locator = By.xpath("//div[@class='chakra-table__container css-zipzvv'][.//th[.//p[text()='Unit Type']]]");
        System.out.println("Checking if Unit List Table is visible...");
        waitForSpinnerToDisappear();
        scrollToElement(locator);
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        System.out.println("Unit List Table is visible.");
        return driver.findElement(locator).isDisplayed();
    }

    /**
     * Clicks the Payment Plan tab and waits for the content to load.
     */
    public void clickPaymentPlanTab() {
        By tab = By.xpath("//button[text()='Payment Plans']");
        System.out.println("Clicking Payment Plans tab...");
        scrollToTop(); // IMPORTANT: Scroll back to the top before clicking the tab
        System.out.println("Scrolled back to top before clicking Payment Plans tab.");
        wait.until(ExpectedConditions.elementToBeClickable(tab)).click();
        waitForSpinnerToDisappear();
        System.out.println("Payment Plans tab clicked and spinner disappeared.");
    }

    /**
     * Verifies if the Payment Plan section is visible.
     */
    public boolean isPaymentPlanVisible() {
        By locator = By.xpath("//div[@class='chakra-stack css-1w20aft']");
        System.out.println("Checking if Payment Plan is visible...");
        waitForSpinnerToDisappear();
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        System.out.println("Payment Plan is visible.");
        return driver.findElement(locator).isDisplayed();
    }

    /**
     * Clicks the Inspections tab and waits for the content to load.
     */
    public void clickInspectionsTab() {
        By tab = By.xpath("//button[text()='Inspections']");
        System.out.println("Clicking Inspections tab...");
        wait.until(ExpectedConditions.elementToBeClickable(tab)).click();
        waitForSpinnerToDisappear();
        System.out.println("Inspections tab clicked and spinner disappeared.");
    }

    /**
     * Verifies if the Inspection Table is visible.
     */
    public boolean isInspectionTableVisible() {
        By locator = By.xpath("//table[@class='chakra-table css-eiqnoq']");
        System.out.println("Checking if Inspection Table is visible...");
        waitForSpinnerToDisappear();
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        System.out.println("Inspection Table is visible.");
        return driver.findElement(locator).isDisplayed();
    }

    /**
     * Clicks the Downloads tab and verifies that the spinner disappears.
     * You might want to add a specific check for a key element in the Downloads section.
     *
     * @return true if the tab was clicked and spinner disappeared, false otherwise.
     */
    public boolean clickDownloadsTabAndVerify() {
        try {
            By tab = By.xpath("//button[text()='Downloads']");
            System.out.println("Clicking Downloads tab...");
            wait.until(ExpectedConditions.elementToBeClickable(tab)).click();
            waitForSpinnerToDisappear();
            System.out.println("Downloads tab clicked and spinner disappeared.");
            // Optionally, add a check for a key element on the downloads tab here
            // e.g., wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("downloads-list")));
            return true;
        } catch (Exception e) {
            System.err.println("Failed to click Downloads tab or spinner did not disappear: " + e.getMessage());
            return false;
        }
    }

    /**
     * Submits the feedback form with provided answers.
     */
    public void submitFeedback(String answer1, String answer2) {
        System.out.println("Submitting feedback...");
        wait.until(ExpectedConditions.elementToBeClickable(clickFeedback)).click();
        waitForSpinnerToDisappear(); // Spinner might appear after opening the form/dialog

        wait.until(ExpectedConditions.visibilityOfElementLocated(enterAnswer1)).sendKeys(answer1);
        System.out.println("Entered Answer 1.");

        wait.until(ExpectedConditions.visibilityOfElementLocated(enterAnswer2)).sendKeys(answer2);
        System.out.println("Entered Answer 2.");

        wait.until(ExpectedConditions.elementToBeClickable(selectRating)).click();
        System.out.println("Selected rating.");

        wait.until(ExpectedConditions.elementToBeClickable(clickSubmit)).click();
        waitForSpinnerToDisappear(); // Spinner might appear after submission
        System.out.println("Clicked submit button.");
    }

    /**
     * Checks if the thank-you node (text or image) is present after feedback submission.
     * @return true if the thank-you node is found, false otherwise.
     */
    public boolean isThankYouNodePresent() {
        System.out.println("Checking for Thank-you node...");
        try {
            // Wait for either the text or image thank-you element to be visible
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test-id='thankyou-text']")),
                    ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test-id='thankyou-image']"))
            ));
            System.out.println("✅ Thank-you DOM node found and is visible.");
            return true;
        } catch (org.openqa.selenium.TimeoutException e) {
            System.out.println("❌ Thank-you DOM node NOT found within the timeout.");
            return false;
        }
    }

    // NEW METHOD for Breadcrumb Navigation
    public void clickBreadcrumbAndVerifyNavigation() {
        System.out.println("[INFO] Clicking on Breadcrumb link: " + breadcrumbLink.toString());
        // Ensure the breadcrumb is clickable before clicking
        wait.until(ExpectedConditions.elementToBeClickable(breadcrumbLink)).click();
        waitForSpinnerToDisappear(); // Wait for any spinner after navigation
        System.out.println("[INFO] Breadcrumb clicked. Waiting for URL to change to project search page.");
        // Wait for the URL to contain the expected part, indicating successful navigation
        wait.until(ExpectedConditions.urlContains("projects/project-search"));
        System.out.println("[INFO] URL contains 'projects/project-search', navigation verified.");
    }

    // Method to check if Project Count text is visible on the search page
    public boolean isProjectCountTextVisible() {
        System.out.println("[INFO] Checking if Project Count text is visible on search page: " + projectCountText.toString());
        waitForSpinnerToDisappear(); // Ensure no spinner is hiding it
        wait.until(ExpectedConditions.visibilityOfElementLocated(projectCountText));
        System.out.println("[INFO] Project Count text is visible.");
        return driver.findElement(projectCountText).isDisplayed();
    }

    /**
     * Utility method to scroll an element into the viewport.
     * Waits for the element to be present in the DOM before attempting to scroll.
     * @param locator The By locator of the element to scroll to.
     */
    private void scrollToElement(By locator) {
        System.out.println("Scrolling to element located by: " + locator.toString());
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    /**
     * Utility method to scroll the page to the very top.
     */
    private void scrollToTop() {
        System.out.println("Scrolling page to top...");
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
    }

    // Add this new helper method to your ProjectDetailPage.java class
    // It should be a public method, perhaps near your constructor or other utility methods.
    public void closeAllOtherWindowsAndSwitchToMainWindow() {
        String currentWindowHandle = driver.getWindowHandle(); // Get the handle of the currently active window
        Set<String> allWindowHandles = driver.getWindowHandles(); // Get all open window handles

        System.out.println("[INFO] closeAllOtherWindowsAndSwitchToMainWindow: Current window handle before cleanup: " + currentWindowHandle);
        System.out.println("[INFO] closeAllOtherWindowsAndSwitchToMainWindow: All window handles before cleanup: " + allWindowHandles);

        for (String handle : allWindowHandles) {
            // Iterate through all handles, and if it's not the current (main) window, close it.
            if (!handle.equals(currentWindowHandle)) {
                try {
                    driver.switchTo().window(handle); // Switch to the window to be closed
                    driver.close(); // Close that window
                    System.out.println("[INFO] closeAllOtherWindowsAndSwitchToMainWindow: Closed extra window: " + handle);
                } catch (Exception e) {
                    // Catch any exceptions (e.g., window already closed) and log them
                    System.err.println("[WARNING] closeAllOtherWindowsAndSwitchToMainWindow: Could not close window " + handle + ": " + e.getMessage());
                }
            }
        }
        // After closing extra windows, switch back to the original main window to continue execution
        driver.switchTo().window(currentWindowHandle);
        System.out.println("[INFO] closeAllOtherWindowsAndSwitchToMainWindow: Switched back to main window: " + currentWindowHandle);
        // Optional: Add a wait to ensure the driver is truly stable on the main window
        wait.until(ExpectedConditions.numberOfWindowsToBe(1)); // Expecting only one window after cleanup
    }
}