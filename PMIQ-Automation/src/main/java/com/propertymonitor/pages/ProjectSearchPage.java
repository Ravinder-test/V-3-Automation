package com.propertymonitor.pages;

// No longer extends BaseTest
import org.openqa.selenium.By;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver; // <-- Add this import
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ProjectSearchPage { // <-- No longer extends BaseTest

    private WebDriver driver; // <-- Instance variable for WebDriver
    private WebDriverWait wait; // <-- Instance variable for WebDriverWait

    public ProjectSearchPage(WebDriver driver, WebDriverWait wait) { // <-- Constructor takes driver and wait
        this.driver = driver;
        this.wait = wait;
        // No need for a separate customWait if BaseTest already provides 'wait' of sufficient duration
        // If you need different timeouts for specific elements, you can create new WebDriverWait instances locally
    }

    // Locators for elements on the Project Search page
    private final By locationSearch = By.xpath("//input[@data-test-id='basicFilters.communitySearch']");
    private final By locationSuggestions = By.xpath("//div[@class='chakra-stack css-1igwmid' and .//p[text()='Jumeirah Beach Residence']]");
    private final By locationClear = By.xpath("//button[@data-test-id='basicFilters.communitySearch-clear-input']");

    private final By unitTypeDropdown = By.xpath("//input[@data-test-id='unitType']");
    private final By apartmentCheckbox = By.xpath("//label[contains(., 'Apartment')]//span[contains(@class, 'chakra-checkbox__control')]");

    private final By saleStatusDropdown = By.xpath("//input[@data-test-id='saleStatus']");
    private final By upcomingCheckbox = By.xpath("//label[contains(., 'Upcoming')]//span[contains(@class, 'chakra-checkbox__control')]");

    private final By projectCountText = By.xpath("//p[contains(@class, 'chakra-text') and contains(text(), 'project')]");

    private final By developersField = By.xpath("//input[@data-test-id='developers']");
    private final By searchingDeveloper = By.xpath("//input[@data-test-id='dropdown-input-developers']");
    private final By developerSuggestions = By.xpath("//label[.//span[text()='Aark Developers']]");

    // Locators on the Advance filter dialog below.
    private final By advanceFilter = By.xpath("//button[@data-test-id='top-filter']");
    private final By advanceFilterDrawer = By.xpath("//div[contains(@class, 'chakra-drawer__content')]"); // Added for drawer verification

    // Locators below are of Project status Advance filters.
    private final By clickConstructionFilter = By.xpath("//input[@data-test-id='advancedFilters.constructionStatus']");
    private final By selectCompletedFilter = By.xpath("//label[contains(., 'Completed')]//span[contains(@class, 'chakra-checkbox__control')]");

    private By clickCompletionDate = By.xpath("//div[@class='chakra-stack css-mw8ufx']//input[@placeholder='Select Actual Completion Date']");
    private By selectDateOnCalendar = By.xpath("//button[@class='chakra-button css-ofgr9y' and text()='2025 (upcoming)']");
    private By clearSelectedDate = By.xpath("//button[@aria-label='reset-date']");

    private By clickSalesStartDate = By.xpath("//input[@placeholder='Select Sales Start Date']/ancestor::div[@class='chakra-input__group css-2kqykg']");
    private By selectSalesStartDate = By.xpath("//button[contains(text(), 'Last 90 days')]");
    private By clearSelectedSalesStartDate = By.xpath("//button[@aria-label='reset-date']");

    // below locators are special locators for the percentage value becuase it has the same test-id with Downpayment percentage.
    private By inputMinCompletionPercentage = By.xpath("(//input[@data-test-id='Min'])[1]");
    private By inputMaxCompletionPercentage = By.xpath("(//input[@data-test-id='Max'])[1]");

    // Locators below are of Units Advance filters
    private By selectBedroom = By.xpath("//label[.//span[text()='5']]");

    private By inputMinBuiltupArea = By.xpath("//input[@data-test-id='advancedFilters.minBuiltUpArea']");
    private By inputMaxBuiltupArea = By.xpath("//input[@data-test-id='advancedFilters.maxBuiltUpArea']");

    private By inputMinPlotSize = By.xpath("//input[@data-test-id='advancedFilters.minPlotSize']");
    private By inputMaxPlotSize = By.xpath("//input[@data-test-id='advancedFilters.maxPlotSize']");

    // locators below are special locators of payments and fees
    private By inputMinDownpaymentPercentage = By.xpath("(//input[@data-test-id='Min'])[2]");
    private By inputMaxDownpaymentPercentage = By.xpath("(//input[@data-test-id='Max'])[2]");

    private By clickPaymentPlan = By.xpath("//input[@data-test-id='advancedFilters.paymentPlans']");
    private By selectPaymentPlan = By.xpath("//label[contains(., 'On Completion')]//span[contains(@class, 'chakra-checkbox__control')]");

    private By clickDldTransfer = By.xpath("//input[@data-test-id='advancedFilters.dldTransferOffer']");
    private By selectDldTransfer = By.xpath("//label[.//span[text()='50%']]");

    private By inputMinBrokerCommission = By.xpath("(//input[@data-test-id='Min'])[3]");
    private By inputMaxBrokerCommission = By.xpath("(//input[@data-test-id='Max'])[3]");

    // Locators below are of Project Features
    private By clickHeightClass = By.xpath("//input[@data-test-id='advancedFilters.heightClass']");
    private By selectHeightClass = By.xpath("//label[.//span[text()='Mid-Rise (5-12 floors)']]");

    private By clickBrand = By.xpath("//input[@data-test-id='advancedFilters.brand']");
    private By selectBrand1 = By.xpath("//span[text()='1 Hotels']/preceding-sibling::span");
    private By selectBrand2 = By.xpath("//span[text()='25 hours hotels']/preceding-sibling::span");

    private By clickFurnishing = By.xpath("//input[@data-test-id='advancedFilters.furnishing']");
    private By selectFurnishing = By.xpath("//label[contains(., 'Partially Furnished')]//span[contains(@class, 'chakra-checkbox__control')]");

    private By clickKitchen = By.xpath("//input[@data-test-id='advancedFilters.kitchenAppliances']");
    private By selectKitchenOption = By.xpath("//label[contains(., 'Fully Equipped')]//span[contains(@class, 'chakra-checkbox__control')]");

    // Locators for Amenities
    private final By selectAmenities = By.xpath("//div[@class='css-dvug1b' and contains(text(), 'A La Carte Services')]");

    private By clickResetFilter = By.xpath("//button[@data-test-id='reset' and text()='Reset Filters']");
    private By clickApply = By.xpath("//button[@data-test-id='apply']");

    // Locators below are on the Main P & S page...
    private By selectHeatmap = By.xpath("//label[contains(., 'Unit Completion Heatmap')]//span[contains(@class, 'chakra-checkbox__control')]");

    private By selectSideBar = By.xpath("//button[@data-test-id='sidebar-float-btn']");
    
    private final By sortByDropdown = By.xpath("//button[@data-test-id='sidebar-panel-sort-by']");

    // Sorting option locators
    private final By sortByRecentlyAdded = By.xpath("//label[.//span[text()='Recently Added']]");
    private final By sortByEarliestCompletion = By.xpath("//label[.//span[text()='Earliest Completion']]");
    private final By sortByLatestCompletion = By.xpath("//label[.//span[text()='Latest Completion']]");

    private By clickFeedback = By.xpath("//button[@data-test-id='feedback-button']");
    private By enterAnswer1 = By.xpath("//textarea[@data-test-id='ans1']");
    private By enterAnswer2 = By.xpath("//textarea[@data-test-id='ans2']");
    private By selectRating = By.xpath("//p[@data-rate='4']");
    private By clickSubmit = By.xpath("//button[@data-test-id='submit-button']");
    // private By thankYouText = By.cssSelector("[data-test-id='thankyou-text']"); // Consider if still needed
    // private By thankYouImage = By.cssSelector("[data-test-id='thankyou-image']"); // Consider if still needed
    
 // Locator for project search input
    private final By projectSearch = By.xpath("//input[@data-test-id='basicFilters.communitySearch']");

    // Locator for location suggestion item
    private final By projectLocationSuggestion = By.xpath("//div[@class='chakra-stack css-1igwmid' and .//p[text()='Habtoor Grand Residences']]");

    // Locator for project title in search results
    private final By projectTitleLink = By.xpath("//p[contains(text(),'Habtoor Grand Residences')]");

    // Locator for project name on detail page
    //private final By projectDetailName = By.xpath("//p[contains(@class, 'css-o7ov3m') and text()='Habtoor Grand Residences']");
    
    // Actions
    // Removed getCustomWait() - use the 'wait' instance directly
    // Also, instead of customWait, use 'this.wait' or simply 'wait' in methods.

    public void verifyProjectSearchPageURL(String expectedBaseUrl) {
        // Wait for URL to contain base path like "/projects/project-search"
        wait.until(ExpectedConditions.urlContains(expectedBaseUrl));
    }

    public String getProjectCount() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(projectCountText)).getText().trim();
    }

    public void searchLocation(String locationKeyword) {
        wait.until(ExpectedConditions.elementToBeClickable(locationSearch)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(locationSearch)).sendKeys(locationKeyword);
        wait.until(ExpectedConditions.elementToBeClickable(locationSuggestions)).click();
    }

    public void clearLocation() {
        wait.until(ExpectedConditions.elementToBeClickable(locationClear)).click();
    }

    public void toggleApartmentCheckbox() {
        wait.until(ExpectedConditions.elementToBeClickable(unitTypeDropdown)).click();
        WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(apartmentCheckbox));
        checkbox.click();
        clickOutside();
    }

    public void toggleUpcomingSaleStatus() {
        wait.until(ExpectedConditions.elementToBeClickable(saleStatusDropdown)).click();
        wait.until(ExpectedConditions.elementToBeClickable(upcomingCheckbox)).click();
        clickOutside();
    }

    public void searchDeveloper() {
        wait.until(ExpectedConditions.elementToBeClickable(developersField)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchingDeveloper)).clear();
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchingDeveloper)).sendKeys("Aark Developer");

        // Assuming a short sleep is needed for suggestions to load before clicking
        waitFor(500); // Use your custom waitFor method
        // driver.sleep(500); // Removed this in favor of waitFor
        // try { Thread.sleep(500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        wait.until(ExpectedConditions.elementToBeClickable(developerSuggestions)).click();
        clickOutside();
    }

    public void clickAdvanceFilter() {
        wait.until(ExpectedConditions.elementToBeClickable(advanceFilter)).click();
    }

    public boolean isAdvanceFilterDrawerVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(advanceFilterDrawer)).isDisplayed();
        } catch (org.openqa.selenium.TimeoutException e) {
            return false;
        }
    }

    public void toggleConstructionStatusCompleted() {
        wait.until(ExpectedConditions.elementToBeClickable(clickConstructionFilter)).click();
        WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(selectCompletedFilter));
        checkbox.click();
        clickOutside();
    }

    public void selectCompletionDate2025Upcoming() {
        wait.until(ExpectedConditions.elementToBeClickable(clickCompletionDate)).click();
        wait.until(ExpectedConditions.elementToBeClickable(selectDateOnCalendar)).click();
        clickOutside();
    }

    public void clearCompletionDate() {
        wait.until(ExpectedConditions.elementToBeClickable(clearSelectedDate)).click();
    }

    public void selectSalesStartDateLast90Days() {
        wait.until(ExpectedConditions.elementToBeClickable(clickSalesStartDate)).click();
        wait.until(ExpectedConditions.elementToBeClickable(selectSalesStartDate)).click();
        clickOutside();
    }

    public void clearSalesStartDate() {
        wait.until(ExpectedConditions.elementToBeClickable(clearSelectedSalesStartDate)).click();
    }

    public void setCompletionPercentageRange(String min, String max) {
        wait.until(ExpectedConditions.elementToBeClickable(inputMinCompletionPercentage)).clear();
        wait.until(ExpectedConditions.visibilityOfElementLocated(inputMinCompletionPercentage)).sendKeys(min);

        wait.until(ExpectedConditions.elementToBeClickable(inputMaxCompletionPercentage)).clear();
        wait.until(ExpectedConditions.visibilityOfElementLocated(inputMaxCompletionPercentage)).sendKeys(max);
    }

    public void toggleBedroom5() {
        wait.until(ExpectedConditions.elementToBeClickable(selectBedroom)).click();
    }

    public void setBuiltupAreaRange(String min, String max) {
        wait.until(ExpectedConditions.elementToBeClickable(inputMinBuiltupArea)).clear();
        wait.until(ExpectedConditions.visibilityOfElementLocated(inputMinBuiltupArea)).sendKeys(min);

        wait.until(ExpectedConditions.elementToBeClickable(inputMaxBuiltupArea)).clear();
        wait.until(ExpectedConditions.visibilityOfElementLocated(inputMaxBuiltupArea)).sendKeys(max);
    }

    public void setPlotSizeRange(String min, String max) {
        wait.until(ExpectedConditions.elementToBeClickable(inputMinPlotSize)).clear();
        wait.until(ExpectedConditions.visibilityOfElementLocated(inputMinPlotSize)).sendKeys(min);

        wait.until(ExpectedConditions.elementToBeClickable(inputMaxPlotSize)).clear();
        wait.until(ExpectedConditions.visibilityOfElementLocated(inputMaxPlotSize)).sendKeys(max);
    }

    public void setDownpaymentPercentageRange(String min, String max) {
        wait.until(ExpectedConditions.elementToBeClickable(inputMinDownpaymentPercentage)).clear();
        wait.until(ExpectedConditions.visibilityOfElementLocated(inputMinDownpaymentPercentage)).sendKeys(min);

        wait.until(ExpectedConditions.elementToBeClickable(inputMaxDownpaymentPercentage)).clear();
        wait.until(ExpectedConditions.visibilityOfElementLocated(inputMaxDownpaymentPercentage)).sendKeys(max);
    }

    public void togglePaymentPlanOnCompletion() {
        wait.until(ExpectedConditions.elementToBeClickable(clickPaymentPlan)).click();
        wait.until(ExpectedConditions.elementToBeClickable(selectPaymentPlan)).click();
        clickOutside();
    }

    public void toggleDldTransfer50Percent() {
        wait.until(ExpectedConditions.elementToBeClickable(clickDldTransfer)).click();
        wait.until(ExpectedConditions.elementToBeClickable(selectDldTransfer)).click();
        clickOutside();
    }

    public void setBrokerCommissionRange(String min, String max) {
        wait.until(ExpectedConditions.elementToBeClickable(inputMinBrokerCommission)).clear();
        wait.until(ExpectedConditions.visibilityOfElementLocated(inputMinBrokerCommission)).sendKeys(min);

        wait.until(ExpectedConditions.elementToBeClickable(inputMaxBrokerCommission)).clear();
        wait.until(ExpectedConditions.visibilityOfElementLocated(inputMaxBrokerCommission)).sendKeys(max);
    }

    public void toggleHeightClassMidRise() {
        wait.until(ExpectedConditions.elementToBeClickable(clickHeightClass)).click();
        wait.until(ExpectedConditions.elementToBeClickable(selectHeightClass)).click();
        clickOutside();
    }

    public void selectBrands() {
        wait.until(ExpectedConditions.elementToBeClickable(clickBrand)).click();
        wait.until(ExpectedConditions.elementToBeClickable(selectBrand1)).click();
        wait.until(ExpectedConditions.elementToBeClickable(selectBrand2)).click();
        clickOutside();
    }

    public void unselectBrands() {
        wait.until(ExpectedConditions.elementToBeClickable(clickBrand)).click();
        wait.until(ExpectedConditions.elementToBeClickable(selectBrand1)).click(); // Clicks again to unselect
        wait.until(ExpectedConditions.elementToBeClickable(selectBrand2)).click(); // Clicks again to unselect
        clickOutside();
    }

    public void toggleFurnishingPartiallyFurnished() {
        wait.until(ExpectedConditions.elementToBeClickable(clickFurnishing)).click();
        wait.until(ExpectedConditions.elementToBeClickable(selectFurnishing)).click();
        clickOutside();
    }

    public void toggleKitchenFullyEquipped() {
        wait.until(ExpectedConditions.elementToBeClickable(clickKitchen)).click();
        wait.until(ExpectedConditions.elementToBeClickable(selectKitchenOption)).click();
        clickOutside();
    }

    public void selectAmenitiesALaCarteServices() {
        wait.until(ExpectedConditions.elementToBeClickable(selectAmenities)).click();
    }

    public void clickResetFiltersButton() {
        wait.until(ExpectedConditions.elementToBeClickable(clickResetFilter)).click();
    }

    public void clickApplyButton() {
        wait.until(ExpectedConditions.elementToBeClickable(clickApply)).click();
    }

    public void toggleHeatmapCheckbox() {
        wait.until(ExpectedConditions.elementToBeClickable(selectHeatmap)).click();
    }

    public void clickSidebar() {
        wait.until(ExpectedConditions.elementToBeClickable(selectSideBar)).click();
    }
    
    // Click the Sort By drop down to open the options
    public void clickSortByDropdown() {
        wait.until(ExpectedConditions.elementToBeClickable(sortByDropdown)).click();
      
    }
    public void clickSortOption(By sortOptionLocator) {
        wait.until(ExpectedConditions.elementToBeClickable(sortOptionLocator)).click();
    }
    
    public By getSortByRecentlyAdded() {
        return sortByRecentlyAdded;
    }

    public By getSortByEarliestCompletion() {
        return sortByEarliestCompletion;
    }

    public By getSortByLatestCompletion() {
        return sortByLatestCompletion;
    }
    
    public List<String> getProjectNames() {
        // Use a more specific wait if projects take time to load after sorting/filtering
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@data-test-id='list-scroll-wrapper']")));
        List<WebElement> projectElements = driver.findElements(By.xpath("//div[@data-test-id='list-scroll-wrapper']/div[contains(@class, 'css-1m1a2hh')]")); 
        List<String> projectNames = new ArrayList<>();
        for (WebElement element : projectElements) {
            projectNames.add(element.getText().trim());
        }
        return projectNames;
    }
    public void submitFeedback(String answer1, String answer2) {
        wait.until(ExpectedConditions.elementToBeClickable(clickFeedback)).click();
        waitFor(1000); // Delay after opening popup

        wait.until(ExpectedConditions.visibilityOfElementLocated(enterAnswer1)).sendKeys(answer1);
        waitFor(1000); // Delay after first input

        wait.until(ExpectedConditions.visibilityOfElementLocated(enterAnswer2)).sendKeys(answer2);
        waitFor(1000); // Delay after second input

        wait.until(ExpectedConditions.elementToBeClickable(selectRating)).click();
        waitFor(1000); // Delay after selecting star

        wait.until(ExpectedConditions.elementToBeClickable(clickSubmit)).click();
        waitFor(2000); // Delay for thank-you to appear
    }
    
   
    public boolean isThankYouNodePresent() {
        try {
            // Use the instance 'wait' or create a new WebDriverWait with a specific duration if needed
            // The existing 'wait' (from constructor) is already set to 10 seconds, which is generally good.
            // If you need a shorter wait just for this check, create a new one.
            // For now, let's use the injected 'wait'
            wait.until(driver -> driver.findElements(By.cssSelector("[data-test-id='thankyou-text']")).size() > 0
                    || driver.findElements(By.cssSelector("[data-test-id='thankyou-image']")).size() > 0);
            System.out.println("✅ Thank-you DOM node found.");
            return true;
        } catch (TimeoutException e) {
            System.out.println("❌ Thank-you DOM node NOT found.");
            return false;
        }
    }   
    // Special method to Handle Multi click on the elements present on the project card on P & S page.
    
    public Map<String, Boolean> verifyNavigationForClickableElements() {
    	
    	wait.until(ExpectedConditions.elementToBeClickable(projectSearch)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(projectSearch)).sendKeys("Habtoor Grand Residences");
        wait.until(ExpectedConditions.elementToBeClickable(projectLocationSuggestion)).click();
        wait.until(ExpectedConditions.elementToBeClickable(projectTitleLink)).click();
    	
        Map<String, Boolean> resultMap = new LinkedHashMap<>();
        String originalWindow = driver.getWindowHandle();

        Map<String, By> clickables = Map.of(
            "Project Title", By.xpath("//p[contains(text(),'Habtoor Grand Residences')]"),
            "Project Units", By.xpath("//p[@data-test-id='project-units']"),
            "Downpayment Link", By.xpath("//a[contains(@href, '/payments')]"),
            "Date Link", By.xpath("//p[@data-test-id='project-timeline']"),
            "Project Image", By.xpath("//div[@data-test-id='draggable-image-component']")
        );

        Map<String, By> verifications = Map.of(
            "Project Title", By.xpath("//*[contains(text(), 'Habtoor Grand')]"),
            "Project Units", By.xpath("//table[@class='chakra-table css-yo1wbp']"),
            "Downpayment Link", By.xpath("//p[text()='Payment Plans']/ancestor::div[contains(@class, 'chakra-stack')][1]"),
            "Date Link", By.xpath("//table[contains(@class, 'chakra-table')]"),
            "Project Image", By.xpath("//p[contains(text(),'Habtoor Grand Residences')]")
        );

        for (String label : clickables.keySet()) {
            try {
                driver.switchTo().window(originalWindow);

                wait.until(ExpectedConditions.elementToBeClickable(clickables.get(label))).click();

                // Wait for new tab
                new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(driver -> driver.getWindowHandles().size() > 1);

                String newTab = driver.getWindowHandles().stream()
                        .filter(handle -> !handle.equals(originalWindow))
                        .findFirst().orElseThrow();

                driver.switchTo().window(newTab);

                // Handle spinner
                try {
                    By spinner = By.cssSelector(".chakra-spinner");
                    new WebDriverWait(driver, Duration.ofSeconds(10))
                            .until(ExpectedConditions.invisibilityOfElementLocated(spinner));
                } catch (Exception ignored) {
                }

                // Verify element in new tab
                WebElement verification = new WebDriverWait(driver, Duration.ofSeconds(15))
                        .until(ExpectedConditions.visibilityOfElementLocated(verifications.get(label)));

                resultMap.put(label, verification != null);
            } catch (Exception e) {
                resultMap.put(label, false);
            } finally {
                // Clean up tab
                if (!driver.getWindowHandle().equals(originalWindow)) {
                    driver.close();
                    driver.switchTo().window(originalWindow);
                }
            }
        }

        return resultMap;
    }
    
    
    
    
    // Generic method to click outside to close dropdowns/calendars
    public void clickOutside() {
        driver.findElement(By.tagName("body")).click();
        waitFor(500); // Use your custom waitFor
        // try { Thread.sleep(500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); } // Small wait after clicking outside
    }

    public void waitFor(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}