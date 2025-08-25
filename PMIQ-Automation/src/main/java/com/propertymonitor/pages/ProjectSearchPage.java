package com.propertymonitor.pages;

// No longer extends BaseTest
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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
    public final By apartmentCheckboxLabel = By.xpath("//label[contains(., 'Apartment')]//span[contains(@class, 'chakra-checkbox__control')]");
    public final By villaCheckboxLabel = By.xpath("//label[contains(., 'Villa')]//span[contains(@class, 'chakra-checkbox__control')]");
    public final By townhouseCheckboxLabel = By.xpath("//label[contains(., 'Townhouse')]//span[contains(@class, 'chakra-checkbox__control')]");
    
    private final By saleStatusDropdown = By.xpath("//input[@data-test-id='saleStatus']");
    public final By upcomingCheckboxLabel = By.xpath("//label[contains(., 'Upcoming')]//span[contains(@class, 'chakra-checkbox__control')]");
    public final By preSaleEoiCheckboxLabel = By.xpath("//label[contains(., 'Pre-Sale (EOI)')]//span[contains(@class, 'chakra-checkbox__control')]");
    public final By onSaleCheckboxLabel = By.xpath("//label[contains(., 'On Sale')]//span[contains(@class, 'chakra-checkbox__control')]");
    public final By soldOutCheckboxLabel = By.xpath("//label[contains(., 'Sold Out')]//span[contains(@class, 'chakra-checkbox__control')]");
    
    private final By projectCountText = By.xpath("//p[contains(@class, 'chakra-text') and contains(text(), 'project')]");

    private final By developersField = By.xpath("//input[@data-test-id='developers']");
    private final By searchingDeveloper = By.xpath("//input[@data-test-id='dropdown-input-developers']");
    private final By developerSuggestions = By.xpath("//label[.//span[text()='Aark Developers']]");

    // Locators on the Advance filter dialog below.
    private final By advanceFilter = By.xpath("//button[@data-test-id='top-filter']");
    private final By advanceFilterDrawer = By.xpath("//input[@data-test-id='advancedFilters.constructionStatus']"); // Added for drawer verification

    // Locators below are of Project status Advance filters.
    private final By constructionStatusDropdown = By.xpath("//input[@data-test-id='advancedFilters.constructionStatus']");
    
    // Locators for each Construction Status checkbox label within the dropdown popup
    // The `data-popper-placement` on the parent `div` helps ensure we target the correct popup.
	 public final By upcomingStatusCheckboxLabel = By.xpath("//label[contains(., 'Upcoming')]//span[contains(@class, 'chakra-checkbox__control')]");
	 public final By underConstructionStatusCheckboxLabel = By.xpath("//label[contains(., 'Under Construction')]//span[contains(@class, 'chakra-checkbox__control')]");
	 public final By onHoldStatusCheckboxLabel = By.xpath("//label[contains(., 'On Hold')]//span[contains(@class, 'chakra-checkbox__control')]");
	 public final By cancelledStatusCheckboxLabel = By.xpath("//label[contains(., 'Cancelled')]//span[contains(@class, 'chakra-checkbox__control')]");
	 public final By completedStatusCheckboxLabel = By.xpath("//label[contains(., 'Completed')]//span[contains(@class, 'chakra-checkbox__control')]");
	 public final By handedOverStatusCheckboxLabel = By.xpath("//label[contains(., 'Handed Over')]//span[contains(@class, 'chakra-checkbox__control')]");
    
    // Locators to select multiple option of Completion date filter.
    private By clickCompletionDate = By.xpath("//div[@class='chakra-stack css-mw8ufx']//input[@placeholder='Select Actual Completion Date']");
    public By clearSelectedDate = By.xpath("//button[@aria-label='reset-date']");

    public final By completedDateOption = By.xpath("//button[contains(@class, 'chakra-button') and normalize-space(.)='Completed']");
    public final By year2025UpcomingDateOption = By.xpath("//button[contains(@class, 'chakra-button') and normalize-space(.)='2025 (upcoming)']");
    public final By year2026DateOption = By.xpath("//button[contains(@class, 'chakra-button') and normalize-space(.)='2026']");
    public final By year2027DateOption = By.xpath("//button[contains(@class, 'chakra-button') and normalize-space(.)='2027']");
    public final By year2028AndLaterDateOption = By.xpath("//button[contains(@class, 'chakra-button') and normalize-space(.)='2028 and later']");
    
    
    // Locators to select multiple sales start date options
    public final By clickSalesStartDate = By.xpath("//input[@placeholder='Select Sales Start Date']/ancestor::div[@class='chakra-input__group css-2kqykg']");
    public final By last7DaysOption = By.xpath("//button[contains(@class, 'chakra-button') and normalize-space(.)='Last 7 days']");
    public final By last30DaysOption = By.xpath("//button[contains(@class, 'chakra-button') and normalize-space(.)='Last 30 days']");
    public final By last90DaysOption = By.xpath("//button[contains(@class, 'chakra-button') and normalize-space(.)='Last 90 days']");
    public final By next7DaysOption = By.xpath("//button[contains(@class, 'chakra-button') and normalize-space(.)='Next 7 days']");
    public final By next30DaysOption = By.xpath("//button[contains(@class, 'chakra-button') and normalize-space(.)='Next 30 days']");

    // below locators are special locators for the percentage value becuase it has the same test-id with Downpayment percentage.
    private By inputMinCompletionPercentage = By.xpath("(//label[text()='Completion Percentage']/following-sibling::div//input[@data-test-id='Min']");
    private By inputMaxCompletionPercentage = By.xpath("(//label[text()='Completion Percentage']/following-sibling::div//input[@data-test-id='Max']");
    
    // Below locators are for Price range.
    
    private By inputMinPriceRange = By.xpath("//input[@data-test-id='advancedFilters.minPrice']");
    private By inputMaxPriceRange = By.xpath("//input[@data-test-id='advancedFilters.maxPrice']");
    // Below locators are for price per ft range
    
    private By inputMinPricePerFtRange = By.xpath("//input[@data-test-id='advancedFilters.minPricePerSqft']");
    private By inputMaxPricePerFtRange = By.xpath("//input[@data-test-id='advancedFilters.maxPricePerSqft']");
    
    // Below locators are for the Internal Area range.
    private By inputMinInternaleAreaRange = By.xpath("//input[@data-test-id='advancedFilters.minInternalArea']");
    private By inputMaxInternaleAreaRange = By.xpath("//input[@data-test-id='advancedFilters.maxInternalArea']");
    
    // Below locators are for the Extenal Area range.
    private By inputMinExternalAreaRange = By.xpath("//input[@data-test-id='advancedFilters.minExternalArea']");
    private By inputMaxExternaleAreaRange = By.xpath("//input[@data-test-id='advancedFilters.maxExternalArea']");
    
    // Locators below are of Units Advance filters
    // Locators to select multiple bedroom filters
    public final By studioBedroomOption = By.xpath("//label[.//span[text()='Studio']]");
    public final By oneBedroomOption = By.xpath("//label[.//span[text()='1']]");
    public final By twoBedroomOption = By.xpath("//label[.//span[text()='2']]");
    public final By threeBedroomOption = By.xpath("//label[.//span[text()='3']]");
    public final By fourBedroomOption = By.xpath("//label[.//span[text()='4']]");
    public final By fiveBedroomOption = By.xpath("//label[.//span[text()='5']]");
    public final By sixBedroomOption = By.xpath("//label[.//span[text()='6']]");
    public final By sevenBedroomOption = By.xpath("//label[.//span[text()='7']]");
    public final By eightPlusBedroomOption = By.xpath("//label[.//span[text()='8+']]");

    public final By availabilityUnitCheckbox = By.xpath("//label[.//span[text()='Show only projects with available units']]");
    
    private By inputMinBuiltupArea = By.xpath("//input[@data-test-id='advancedFilters.minBuiltUpArea']");
    private By inputMaxBuiltupArea = By.xpath("//input[@data-test-id='advancedFilters.maxBuiltUpArea']");

    private By inputMinPlotSize = By.xpath("//input[@data-test-id='advancedFilters.minPlotSize']");
    private By inputMaxPlotSize = By.xpath("//input[@data-test-id='advancedFilters.maxPlotSize']");

    // Below locators are for the advance filters:
    private By inputMinDownPayment = By.xpath("//input[@data-test-id='advancedFilters.minDownPaymentPrice']");
    private By inputMaxDownPayment = By.xpath("//input[@data-test-id='advancedFilters.maxDownPaymentPrice']");
    
    // locators below are special locators of payments and fees
    private By inputMinDownpaymentPercentage = By.xpath("(//label[text()='Down Payment (%)']/following-sibling::div//input[@data-test-id='Min']");
    private By inputMaxDownpaymentPercentage = By.xpath("(//label[text()='Down Payment (%)']/following-sibling::div//input[@data-test-id='Max']");

    public final By clickPaymentPlan = By.xpath("//input[@data-test-id='advancedFilters.paymentPlans']");
    public final By onCompletionOption = By.xpath("//label[contains(., 'On Completion')]//span[contains(@class, 'chakra-checkbox__control')]");
    public final By postHandoverOption = By.xpath("//label[contains(., 'Post Handover')]//span[contains(@class, 'chakra-checkbox__control')]");
    
    public By clickDldTransfer = By.xpath("//input[@data-test-id='advancedFilters.dldTransferOffer']");
    public By selectFiftyPercentageOption = By.xpath("//label[.//span[text()='50%']]");
    public By selectZeroPercentageOption = By.xpath("//label[.//span[text()='0%']]");
    public By selectHundredPercentageOption = By.xpath("//label[.//span[text()='100%']]");

    private By inputMinBrokerCommission = By.xpath("(//label[text()='Broker’s Commission (%)']/following-sibling::div//input[@data-test-id='Min']");
    private By inputMaxBrokerCommission = By.xpath("(//label[text()='Broker’s Commission (%)']/following-sibling::div//input[@data-test-id='Max']");

    // Locators below are of Project Features
    // Locators below are for the multiple options of height classes.
    private By clickHeightClass = By.xpath("//input[@data-test-id='advancedFilters.heightCLass']");
    public final By lowRiseOption = By.xpath("//label[.//span[text()='Low-Rise (1-4 floors)']]");
    public final By midRiseOption = By.xpath("//label[.//span[text()='Mid-Rise (5-12 floors)']]");
    public final By highRiseOption = By.xpath("//label[.//span[text()='High-Rise (13-39 floors)']]");
    public final By skyscraperOption = By.xpath("//label[.//span[text()='Skyscraper (40 floors+)']]");
    public final By singleFamilyOption = By.xpath("//label[.//span[text()='Single Famliy (Villa/Townhouse)']]");

    
    private By clickBrand = By.xpath("//input[@data-test-id='advancedFilters.brand']");
    private By selectBrand1 = By.xpath("//span[text()='Franck Muller']/preceding-sibling::span");
    private By selectBrand2 = By.xpath("//span[text()='Armani']/preceding-sibling::span");

    public By clickFurnishing = By.xpath("//input[@data-test-id='advancedFilters.furnishing']");
    public By partiallyFurnishedOption = By.xpath("//label[contains(., 'Partially Furnished')]//span[contains(@class, 'chakra-checkbox__control')]");
    public By fullyFurnishedOption = By.xpath("//label[contains(., 'Fully Furnished')]//span[contains(@class, 'chakra-checkbox__control')]");
    public By unFurnishedOption = By.xpath("//label[contains(., 'Unfurnished')]//span[contains(@class, 'chakra-checkbox__control')]");

    public By clickKitchen = By.xpath("//input[@data-test-id='advancedFilters.kitchenAppliances']");
    public By fullyEquippedOption = By.xpath("//label[contains(., 'Fully Equipped')]//span[contains(@class, 'chakra-checkbox__control')]");
    public By partiallyEquippedOption = By.xpath("//label[contains(., 'Partially Equipped')]//span[contains(@class, 'chakra-checkbox__control')]");
    public By unEquippedOption = By.xpath("//label[contains(., 'Unequipped')]//span[contains(@class, 'chakra-checkbox__control')]");
    
    // Locators for Amenities
    private final By selectAmenities = By.xpath("//div[@class='css-dvug1b' and contains(text(), 'A La Carte Services')]");

    private By clickResetFilter = By.xpath("//button[@data-test-id='reset' and text()='Reset Filters']");
    private By clickApply = By.xpath("//button[@data-test-id='apply']");

    // Locators below are on the Main P & S page...
    private By selectHeatmap = By.xpath("//label[contains(., 'Unit Completion Heatmap')]//span[contains(@class, 'chakra-checkbox__control')]");

    private By verifyYearRange = By.xpath("//div[@data-group='true']/input[@data-test-id='basicFilters.maxYear']");
    
    private By selectSideBar = By.xpath("//button[@data-test-id='sidebar-float-btn']");
    
    private final By sortByDropdown = By.xpath("//button[@data-test-id='sidebar-panel-sort-by']");

    // Sorting option locators
    private final By sortByRecentlyAdded = By.xpath("//label[.//span[text()='Recently Added']]");
    private final By sortByEarliestCompletion = By.xpath("//label[.//span[text()='Earliest Completion']]");
    private final By sortByLatestCompletion = By.xpath("//label[.//span[text()='Latest Completion']]");
    private final By sortByHighestAvailability = By.xpath("//label[.//span[text()='Highest Availability']]");
    private final By sortByHighestPrice = By.xpath("//label[.//span[text()='Highest Price']]");
    private final By sortByLowestPrice = By.xpath("//label[.//span[text()='Lowest Price']]");
    
    //
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

    // Locator for Onboarding Steps.    
    private final By verifyOnboardingTitles = By.xpath("//div[@class='chakra-stack css-1n3g6t4']/h2");
    private final By onBoardingIcon = By.xpath("//button[@data-test-id='joyride-bulb']");

    private final By exploreButton = By.xpath("//button[translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz') = 'explore']");
    private final By nextButton = By.xpath("//button[translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz') = 'next']");
    private final By gotItButton = By.xpath("//button[translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz') = 'got it']");
    private final By skipButton = By.xpath("//button[translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz') = 'skip']");

    // Locators For News pop-up
    
    private final By newsPopup = By.xpath("//section[@role='dialog' and contains(@class, 'chakra-modal__content')]");
    private final By newsList = By.xpath("//div[@class='chakra-stack css-10tuo4t']");
    private final By externalNewsButton = By.xpath("//div[contains(@class, 'chakra-modal__body')]//div[contains(@class, 'chakra-stack') and (.//img)]//button[contains(@class, 'chakra-button')]");
    private final By newsCloseIcon = By.xpath("//button[contains(@class, 'chakra-modal__close-btn')]");
    private final By newsIcon = By.xpath("//button[@data-test-id='news-button']"); // Adjust if different

    // Actions for Force and Manual Onboarding.
 // Wait until onboarding title is visible
    public boolean isOnboardingDialogVisible() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(verifyOnboardingTitles));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void clickOnboardingIcon() {
        wait.until(ExpectedConditions.elementToBeClickable(onBoardingIcon)).click();
    }

    public void clickExploreButton() {
        wait.until(ExpectedConditions.elementToBeClickable(exploreButton)).click();
    }

    public void clickNextButton() {
        wait.until(ExpectedConditions.elementToBeClickable(nextButton)).click();
    }

    public void clickGotItButton() {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));

            // Wait for "Got it" button to be visible and enabled
            WebElement gotItBtn = shortWait.until(ExpectedConditions.visibilityOfElementLocated(gotItButton));

            // Extra check: wait until it's actually clickable
            wait.until(ExpectedConditions.elementToBeClickable(gotItBtn));

            // Scroll into view and use JS click
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", gotItBtn);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", gotItBtn);

            waitFor(1500); // pause to allow dialog to close

        } catch (Exception e) {
            throw new RuntimeException("❌ Failed to click 'Got it' button: " + e.getMessage());
        }
    }

    public void clickSkipButton() {
        wait.until(ExpectedConditions.elementToBeClickable(skipButton)).click();
    }

    public String getCurrentOnboardingStepTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(verifyOnboardingTitles)).getText();
    }
    
    // Action for the News Pop-up.
    public boolean isNewsDialogVisible() {
        return !driver.findElements(newsPopup).isEmpty();
    }

    public boolean isNewsListPresent() {
        waitForAnySpinnerToDisappear(); // wait for spinner to go before checking content
        return !driver.findElements(newsList).isEmpty();
    }
    
    public void waitForNewsToLoad() {
        try {
            // Wait until the spinner disappears (invisible or not present)
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".chakra-spinner")),
                    ExpectedConditions.not(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".chakra-spinner")))
            ));
            System.out.println("✅ Spinner is gone.");

            // Then wait for either:
            // 1. News list appears, OR
            // 2. A message or condition showing "no news" is available
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.presenceOfElementLocated(newsList),
                    ExpectedConditions.presenceOfElementLocated(newsCloseIcon) // at least ensure pop-up remains
            ));

            System.out.println("✅ News content (or fallback element) detected after spinner.");
        } catch (Exception e) {
            System.err.println("❌ Failed while waiting for News popup content: " + e.getMessage());
        }
    }
    
    public void waitUntilNewsListRenders() {
        try {
            // First wait for the loader to disappear (same logic)
            wait.until(ExpectedConditions.or(
                ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".chakra-spinner")),
                ExpectedConditions.not(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".chakra-spinner")))
            ));
            System.out.println("✅ Spinner is gone.");

            // Then strictly wait for the news list container to be VISIBLE
            wait.until(ExpectedConditions.visibilityOfElementLocated(newsList));
            System.out.println("✅ News list is visible.");
        } catch (Exception e) {
            System.err.println("❌ News list did not render properly: " + e.getMessage());
        }
    }
    

    public boolean isExternalNewsButtonPresent() {
        return !driver.findElements(externalNewsButton).isEmpty();
    }

    public void clickCloseNewsPopup() {
        wait.until(ExpectedConditions.elementToBeClickable(newsCloseIcon)).click();
    }

    public void clickNewsIcon() {
        wait.until(ExpectedConditions.elementToBeClickable(newsIcon)).click();
    }

    public void clickExternalNewsButtonAndVerifyTab() {
        String originalTab = driver.getWindowHandle();
        wait.until(ExpectedConditions.elementToBeClickable(externalNewsButton)).click();

        waitFor(1000); // Optional: small buffer for new tab to open

        for (String tab : driver.getWindowHandles()) {
            if (!tab.equals(originalTab)) {
                driver.switchTo().window(tab);
                waitFor(2000); // Let the external news page load
                driver.close();
                break;
            }
        }
        driver.switchTo().window(originalTab);
    }
    
    
    
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

    // New action method to toggle any unit type checkbox
    public void toggleUnitType(By checkboxLocator) {
        // 1. Click the main Unit Type dropdown to open the options
        wait.until(ExpectedConditions.elementToBeClickable(unitTypeDropdown)).click();
        waitFor(500); // Give time for options to appear

        // 2. Click the specific checkbox label
        WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(checkboxLocator));
        checkbox.click();
        waitFor(500); // Small wait after clicking checkbox

        // 3. Click outside to close the dropdown (if it doesn't auto-close)
        clickOutside();
        waitFor(500); // Small wait after closing dropdown
    }

    // New Action to toggle any Sales Type Checkbox
    public void toggleSaleStatus(By checkboxLocator) {
        // 1. Click the main Sale Status dropdown to open the options
        wait.until(ExpectedConditions.elementToBeClickable(saleStatusDropdown)).click();
        waitFor(500); // Give time for options to appear

        // 2. Click the specific checkbox label
        WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(checkboxLocator));
        checkbox.click();
        waitFor(500); // Small wait after clicking checkbox

        // 3. Click outside to close the dropdown (if it doesn't auto-close)
        clickOutside();
        waitFor(500); // Small wait after closing dropdown
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

    public void toggleConstructionStatus(By checkboxLocator) {
        // 1. Click the main Construction Status dropdown to open the options
        wait.until(ExpectedConditions.elementToBeClickable(constructionStatusDropdown)).click();
        waitFor(500); // Give time for options to appear

        // 2. Click the specific checkbox label
        WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(checkboxLocator));
        checkbox.click();
        waitFor(500); // Small wait after clicking checkbox

        // 3. Click outside to close the dropdown (if it doesn't auto-close)
        clickOutside();
        waitFor(500); // Small wait after closing dropdown
    }

    public void selectCompletionDateOption(By optionLocator) {
        // 1. Click the main Completion Date dropdown to open the options
        wait.until(ExpectedConditions.elementToBeClickable(clickCompletionDate)).click();
        waitFor(500); // Give time for options to appear

        // 2. Click the specific option button
        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(optionLocator));
        option.click();
        waitFor(500); // Small wait after clicking option

        // 3. Click outside to close the dropdown (if it doesn't auto-close)
        // Note: For single-select buttons, clicking an option usually auto-closes.
        // This 'clickOutside' is a safe fallback.
        clickOutside();
        waitFor(500); // Small wait after closing dropdown
    }

    public void clearSelectedCompletionDate() {
        wait.until(ExpectedConditions.elementToBeClickable(clearSelectedDate)).click();
    }

    public void selectSalesStartDateOption(By optionLocator) {
        // 1. Click the main Sales Start Date dropdown to open the options
        wait.until(ExpectedConditions.elementToBeClickable(clickSalesStartDate)).click();
        waitFor(500); // Give time for options to appear

        // 2. Click the specific option button
        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(optionLocator));
        option.click();
        waitFor(500); // Small wait after clicking option

        // 3. Click outside to close the dropdown (if it doesn't auto-close)
        clickOutside();
        waitFor(500); // Small wait after closing dropdown
    }

    public void setCompletionPercentageRange(String min, String max) {
        wait.until(ExpectedConditions.elementToBeClickable(inputMinCompletionPercentage)).clear();
        wait.until(ExpectedConditions.visibilityOfElementLocated(inputMinCompletionPercentage)).sendKeys(min);

        wait.until(ExpectedConditions.elementToBeClickable(inputMaxCompletionPercentage)).clear();
        wait.until(ExpectedConditions.visibilityOfElementLocated(inputMaxCompletionPercentage)).sendKeys(max);
    }

    // Methods below are for the Avaialbilty units Data
    
    // Below method is for Price range.
    
    public void setPriceRange(String min, String max) {
        wait.until(ExpectedConditions.elementToBeClickable(inputMinPriceRange)).clear();
        wait.until(ExpectedConditions.visibilityOfElementLocated(inputMinPriceRange)).sendKeys(min);

        wait.until(ExpectedConditions.elementToBeClickable(inputMaxPriceRange)).clear();
        wait.until(ExpectedConditions.visibilityOfElementLocated(inputMaxPriceRange)).sendKeys(max);
    }
    
    // Below method is for price per sqft range
    public void setPricePerFtRange(String min, String max) {
        wait.until(ExpectedConditions.elementToBeClickable(inputMinPricePerFtRange)).clear();
        wait.until(ExpectedConditions.visibilityOfElementLocated(inputMinPricePerFtRange)).sendKeys(min);

        wait.until(ExpectedConditions.elementToBeClickable(inputMaxPricePerFtRange)).clear();
        wait.until(ExpectedConditions.visibilityOfElementLocated(inputMaxPricePerFtRange)).sendKeys(max);
    }
    
    // Below method is for Internal Area
     
    public void setInternalAreaRange(String min, String max) {
        wait.until(ExpectedConditions.elementToBeClickable(inputMinInternaleAreaRange)).clear();
        wait.until(ExpectedConditions.visibilityOfElementLocated(inputMinInternaleAreaRange)).sendKeys(min);

        wait.until(ExpectedConditions.elementToBeClickable(inputMaxInternaleAreaRange)).clear();
        wait.until(ExpectedConditions.visibilityOfElementLocated(inputMaxInternaleAreaRange)).sendKeys(max);
    }
    // Below method is for the external area 
    public void setExternalAreaRange(String min, String max) {
        wait.until(ExpectedConditions.elementToBeClickable(inputMinExternalAreaRange)).clear();
        wait.until(ExpectedConditions.visibilityOfElementLocated(inputMinExternalAreaRange)).sendKeys(min);

        wait.until(ExpectedConditions.elementToBeClickable(inputMaxExternaleAreaRange)).clear();
        wait.until(ExpectedConditions.visibilityOfElementLocated(inputMaxExternaleAreaRange)).sendKeys(max);
    }
    
    public void toggleBedroomOption(By checkboxLocator) {
        // No need to click a dropdown here, as options are directly visible.

        // 1. Click the specific checkbox label
        WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(checkboxLocator));
        checkbox.click();
        waitFor(500); // Small wait after clicking checkbox

        // 2. Click outside to apply changes/close any transient elements (common for filter UIs)
        clickOutside();
        waitFor(500); // Small wait after clicking outside
    }
    
    public void clickAvailabilityUnitCheckbox() {
        wait.until(ExpectedConditions.elementToBeClickable(availabilityUnitCheckbox)).click();
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

    public void setDownpaymentRange(String min, String max) {
        wait.until(ExpectedConditions.elementToBeClickable(inputMinDownPayment)).clear();
        wait.until(ExpectedConditions.visibilityOfElementLocated(inputMinDownPayment)).sendKeys(min);

        wait.until(ExpectedConditions.elementToBeClickable(inputMaxDownPayment)).clear();
        wait.until(ExpectedConditions.visibilityOfElementLocated(inputMaxDownPayment)).sendKeys(max);
    }
    // 
    
    public void togglePaymentPlanOption(By checkboxLocator) {
        // 1. Click the main Payment Plan dropdown to open the options
        wait.until(ExpectedConditions.elementToBeClickable(clickPaymentPlan)).click();
        waitFor(500); // Give time for options to appear

        // 2. Click the specific checkbox label
        WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(checkboxLocator));
        checkbox.click();
        waitFor(500); // Small wait after clicking checkbox

        // 3. Click outside to close the dropdown (if it doesn't auto-close)
        clickOutside();
        waitFor(500); // Small wait after closing dropdown
    }

 // Below test method is for DLD transfer fee.
    
    public void toggleDldTransferPercent(By checkboxLocator) {
        // 1. Click the main DLD transfer fee dropdown to open the options
        wait.until(ExpectedConditions.elementToBeClickable(clickDldTransfer)).click();
        waitFor(500); // Give time for options to appear

        // 2. Click the specific checkbox label
        WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(checkboxLocator));
        checkbox.click();
        waitFor(500); // Small wait after clicking checkbox

        // 3. Click outside to close the dropdown (if it doesn't auto-close)
        clickOutside();
        waitFor(500); // Small wait after closing dropdown
    }

    public void setBrokerCommissionRange(String min, String max) {
        wait.until(ExpectedConditions.elementToBeClickable(inputMinBrokerCommission)).clear();
        wait.until(ExpectedConditions.visibilityOfElementLocated(inputMinBrokerCommission)).sendKeys(min);

        wait.until(ExpectedConditions.elementToBeClickable(inputMaxBrokerCommission)).clear();
        wait.until(ExpectedConditions.visibilityOfElementLocated(inputMaxBrokerCommission)).sendKeys(max);
    }

    public void toggleHeightClassOption(By checkboxLocator) {
        // 1. Click the main Height Class dropdown to open the options
        wait.until(ExpectedConditions.elementToBeClickable(clickHeightClass)).click();
        waitFor(500); // Give time for options to appear

        // 2. Click the specific checkbox label
        WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(checkboxLocator));
        checkbox.click();
        waitFor(500); // Small wait after clicking checkbox

        // 3. Click outside to close the dropdown (if it doesn't auto-close)
        clickOutside();
        waitFor(500); // Small wait after closing dropdown
    }

    public void selectBrands() {
        wait.until(ExpectedConditions.elementToBeClickable(clickBrand)).click();
        wait.until(ExpectedConditions.elementToBeClickable(selectBrand1)).click();
        wait.until(ExpectedConditions.elementToBeClickable(selectBrand2)).click();
        clickOutside();
    }

    public void unselectBrands() {
        wait.until(ExpectedConditions.elementToBeClickable(clickBrand)).click();
        wait.until(ExpectedConditions.elementToBeClickable(clickBrand)).click();
        wait.until(ExpectedConditions.elementToBeClickable(selectBrand1)).click(); // Clicks again to unselect
        wait.until(ExpectedConditions.elementToBeClickable(selectBrand2)).click(); // Clicks again to unselect
        clickOutside();
    }

    public void toggleFurnishingOptions(By checkboxLocator) {
        // 1. Click the main Height Class dropdown to open the options
        wait.until(ExpectedConditions.elementToBeClickable(clickFurnishing)).click();
        waitFor(500); // Give time for options to appear

        // 2. Click the specific checkbox label
        WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(checkboxLocator));
        checkbox.click();
        waitFor(500); // Small wait after clicking checkbox

        // 3. Click outside to close the dropdown (if it doesn't auto-close)
        clickOutside();
        waitFor(500); // Small wait after closing dropdown
    }
    
    public void toggleKitchenOptions(By checkboxLocator) {
        // 1. Click the main Height Class dropdown to open the options
        wait.until(ExpectedConditions.elementToBeClickable(clickKitchen)).click();
        waitFor(500); // Give time for options to appear

        // 2. Click the specific checkbox label
        WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(checkboxLocator));
        checkbox.click();
        waitFor(500); // Small wait after clicking checkbox

        // 3. Click outside to close the dropdown (if it doesn't auto-close)
        clickOutside();
        waitFor(500); // Small wait after closing dropdown
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

    /**
     * Toggles (selects/unselects) the 'Unit Completion Heatmap' checkbox.
     * After clicking, it waits for the year range filter element to become visible.
     */
    public void toggleHeatmapCheckbox() {
        System.out.println("[INFO] Toggling 'Unit Completion Heatmap' checkbox.");
        wait.until(ExpectedConditions.elementToBeClickable(selectHeatmap)).click();
        // Wait for the year range filter element to appear after toggling heatmap
        wait.until(ExpectedConditions.visibilityOfElementLocated(verifyYearRange));
        System.out.println("[INFO] 'Unit Completion Heatmap' checkbox toggled and year range filter is visible.");
    }
    
    /**
     * Checks if the year range filter element is present and visible.
     * @return true if the year range filter is visible, false otherwise.
     */
    public boolean isYearRangeFilterPresent() { // Renamed from isMinYearFilterPresent
        System.out.println("[INFO] Verifying presence/visibility of Year Range Filter input: " + verifyYearRange.toString());
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(verifyYearRange));
            System.out.println("[INFO] Year Range Filter input is visible.");
            return true;
        } catch (org.openqa.selenium.TimeoutException e) {
            System.out.println("[INFO] Year Range Filter input is NOT visible within timeout.");
            return false;
        }
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
    
    public By getsortByHighestAvailability() {
        return sortByHighestAvailability;
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
    
    public By getsortByHighestPrice() {
        return sortByHighestPrice;
    }
    
    public By getsortByLowestPrice() {
        return sortByLowestPrice;
    }
    //
    
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
    
    // handle Spinner method
    
    public void waitForAnySpinnerToDisappear() {
        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".chakra-spinner")),
                    ExpectedConditions.not(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".chakra-spinner")))
            ));
            System.out.println("Spinner check: Spinner disappeared or was not present.");
        } catch (Exception e) {
            System.err.println("Spinner might not have disappeared within the timeout or an error occurred: " + e.getMessage());
        }
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