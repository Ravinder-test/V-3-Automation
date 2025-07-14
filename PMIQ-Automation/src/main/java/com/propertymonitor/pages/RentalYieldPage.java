package com.propertymonitor.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.openqa.selenium.interactions.Actions;

public class RentalYieldPage {

    WebDriver driver;
    WebDriverWait wait;
    Actions actions;

    // Constructor
    public RentalYieldPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.actions = new Actions(driver);
    }

    // Spinner wait method (already implemented by you)
    public void waitForSpinnerToDisappear() {
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

    // Navigate to the Rental Yield Page
    public void openRentalYieldPage(String baseUrl) {
        driver.get(baseUrl + "indices/rental-yield?limit=25&page=1");
        System.out.println("Navigated to: " + baseUrl + "indices/rental-yield?limit=25&page=1");
    }

    private By loadingSpinner = By.cssSelector(".chakra-spinner");  // Replace with your actual spinner locator if different
    // Locator for Rental Yield Chart
    public final By chartLocator = By.xpath("//div[@data-highcharts-chart='0']");
    
    // Locators for Dubai yield Checkbox locators
    public final By yieldCheckboxes = By.xpath("//div[contains(@class, 'chakra-stack') and contains(@class, 'css-txic59')]");
    // locators for all the 3 yiedls tyoe checkbox.
    
    public final By combinedTransactionTypeOption = By.xpath("//div[contains(@class, 'chakra-stack') and contains(@class, 'css-1av4r4d')]//label[normalize-space(.)='Combined']//span[contains(@class, 'chakra-checkbox__control')]");
    public final By newTransactionTypeOption = By.xpath("//div[contains(@class, 'chakra-stack') and contains(@class, 'css-1av4r4d')]//label[normalize-space(.)='New']//span[contains(@class, 'chakra-checkbox__control')]");
    public final By renewalTransactionTypeOption = By.xpath("//div[contains(@class, 'chakra-stack') and contains(@class, 'css-1av4r4d')]//label[normalize-space(.)='Renewal']//span[contains(@class, 'chakra-checkbox__control')]");
 // Graph path verification
    public final By chartGraphPath = By.xpath("//*[local-name()='g' and contains(@class, 'highcharts-series-0')]/*[local-name()='path' and contains(@class, 'highcharts-graph')]");

    private By locationInput = By.xpath("//input[@data-test-id='community']/parent::div");
    private By suggestionOption = By.xpath("//div[contains(@class, 'chakra-stack') and @data-popper-placement='bottom' and .//p[normalize-space(.)='Arjan']]");

    private By avgYield = By.xpath("//p[normalize-space(.)='Average Gross Investment Yield']/preceding-sibling::p");
    private By momChange = By.xpath("//p[normalize-space(.)='Month-on-Month Change']/preceding-sibling::p");
    private By qoqChange = By.xpath("//p[normalize-space(.)='Quarter-on-Quarter Change']/preceding-sibling::p");
    private By yoyChange = By.xpath("//p[normalize-space(.)='Year-on-Year Change']/preceding-sibling::p");


    // Dropdown Click Triggers
    private By propertyTypeDropdown = By.xpath("//input[@data-test-id='propertyType']");
    private By indexTypeDropdown = By.xpath("//input[@data-test-id='indexType']");
    private By calendarDropdown = By.xpath("//input[@data-test-id='month']");

    // Common dropdown options container (appears for all three dropdowns)
    private By dropdownOptions = By.xpath("//div[contains(@class, 'chakra-stack') and contains(@class, 'css-po4cgo')]//p[contains(@class,'chakra-text')]");

    // Locators for Rental Yield Table.
    public final By rentalYieldTable = By.xpath("//table[contains(@class, 'chakra-table')]");
    
    public final By pageSizeDropdown = By.xpath("//select[@data-test-id='change-page-size']");
    public final By option50 = By.xpath("//select[@data-test-id='change-page-size']/option[text()='50']");
    public final By optionAll = By.xpath("//select[@data-test-id='change-page-size']/option[text()='All']");
    public final By tableRowCountLocator = By.xpath("//div[contains(@class, 'chakra-stack') and .//p[./b] and .//p[contains(., 'from')]]//p[./b]");
    
    public final By paginationButtons = By.xpath("//button[starts-with(@data-test-id, 'paginate-')]");
    
    // Locators to submit feedback
    private final By clickFeedback = By.xpath("//button[@data-test-id='feedback-button']");
    private final By enterAnswer1 = By.xpath("//textarea[@data-test-id='ans1']");
    private final By enterAnswer2 = By.xpath("//textarea[@data-test-id='ans2']");
    private final By selectRating = By.xpath("//p[@data-rate='4']");
    private final By clickSubmit = By.xpath("//button[@data-test-id='submit-button']");
    //private final By thankYouNode = By.cssSelector("[data-test-id='thankyou-text'], [data-test-id='thankyou-image']"); // Combined locator for thank you

    
    
    // Method to check if the chart is visible
    public boolean isRentalYieldChartVisible() {
        waitForSpinnerToDisappear();
        try {
        	WebDriverWait chartWait = new WebDriverWait(driver, Duration.ofSeconds(15));
            chartWait.until(ExpectedConditions.visibilityOfElementLocated(chartLocator));
            return driver.findElement(chartLocator).isDisplayed();
        } catch (Exception e) {
            System.err.println("Chart element not found or not visible: " + e.getMessage());
            return false;
        }
    }
    
    // method to click on each Checkbox of rental yield i.e Combined, new, Renewal
    
 // Wait for checkboxes to be visible
    public boolean waitForCheckboxes() {
        try {
        	Thread.sleep(10000);
            WebDriverWait checkboxWait = new WebDriverWait(driver, Duration.ofSeconds(35));
            checkboxWait.until(ExpectedConditions.visibilityOfElementLocated(yieldCheckboxes));
            return true;
        } catch (Exception e) {
            System.err.println("Checkboxes not visible: " + e.getMessage());
            return false;
        }
    }

    // Toggle checkbox
    public void toggleCheckbox(By locator) {
    	
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }
 // Get all KPI values (before/after)
    public Map<String, String> getKpiValues() {
        // ✅ Scroll to KPI section before reading
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 1000);");

        // ✅ Wait for any potential loading/spinner before fetching values
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingSpinner));
        } catch (TimeoutException e) {
            System.out.println("⚠️ Spinner did not disappear in expected time.");
        }

        Map<String, String> values = new LinkedHashMap<>();
        values.put("Average Gross Investment Yield", getKpiText(avgYield));
        values.put("Month-on-Month Change", getKpiText(momChange));
        values.put("Quarter-on-Quarter Change", getKpiText(qoqChange));
        values.put("Year-on-Year Change", getKpiText(yoyChange));
        return values;
    }

    // Perform search for location
    public void searchAndSelectLocation(String location) {
        wait.until(ExpectedConditions.elementToBeClickable(locationInput)).click();

        WebElement inputField = driver.findElement(By.xpath("//input[@data-test-id='community']"));
        inputField.clear();
        inputField.sendKeys(location);
        
        // ✅ Wait for location-based spinner to disappear
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingSpinner));
        } catch (TimeoutException e) {
            System.out.println("⚠️ Spinner didn't disappear after entering the location.");
        }

        wait.until(ExpectedConditions.elementToBeClickable(suggestionOption)).click();

        // ✅ Wait for location-based spinner to disappear
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingSpinner));
        } catch (TimeoutException e) {
            System.out.println("⚠️ Spinner didn't disappear after selecting location.");
        }

        // ✅ Scroll in case KPI is lazy loaded
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 1000);");
    }

    
    // below Method is to veify the Rental yield Project count on the basis of each option selection.
    public Map<String, String> getKpiValuesForDropdownValidation() {
        Map<String, String> kpiData = new LinkedHashMap<>();
        kpiData.put("Average Yield", wait.until(ExpectedConditions.visibilityOfElementLocated(avgYield)).getText());
        kpiData.put("Month-on-Month", wait.until(ExpectedConditions.visibilityOfElementLocated(momChange)).getText());
        kpiData.put("Quarter-on-Quarter", wait.until(ExpectedConditions.visibilityOfElementLocated(qoqChange)).getText());
        kpiData.put("Year-on-Year", wait.until(ExpectedConditions.visibilityOfElementLocated(yoyChange)).getText());
        return kpiData;
    }
    public List<String> getDropdownOptions(By optionsLocator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(optionsLocator));
        return driver.findElements(optionsLocator)
                     .stream()
                     .map(WebElement::getText)
                     .collect(Collectors.toList());
    }

    private void selectDropdownOption(By dropdownTrigger, String optionToSelect) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(dropdownTrigger)).click();
            List<WebElement> options = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(dropdownOptions));
            for (WebElement option : options) {
                if (option.getText().equalsIgnoreCase(optionToSelect)) {
                    option.click();
                    return;
                }
            }
            throw new NoSuchElementException("Option '" + optionToSelect + "' not found.");
        } catch (Exception e) {
            throw new RuntimeException("Failed to select option '" + optionToSelect + "' from dropdown: " + e.getMessage());
        }
    }

    // Public methods for selecting dropdown values
    public void selectPropertyType(String type) {
        selectDropdownOption(propertyTypeDropdown, type);
    }

    public void selectIndexType(String type) {
        selectDropdownOption(indexTypeDropdown, type);
    }

    public void selectCalendarMonth(String month) {
        selectDropdownOption(calendarDropdown, month);
    }
    
    
    // Above Method is belong to Verify project count on selection of Multiple Options.
    
    public boolean isRentalYieldTableVisible() {
        waitForSpinnerToDisappear();
        try {
            WebElement table = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//table[contains(@class, 'chakra-table')]")
            ));
            return table.isDisplayed();
        } catch (TimeoutException e) {
            System.err.println("❌ Rental Yield table not visible within timeout: " + e.getMessage());
            return false;
        }
    }
    public String getTableRowRangeText() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(tableRowCountLocator));
            WebElement rangeElement = driver.findElement(tableRowCountLocator).findElement(By.tagName("b"));
            return rangeElement.getText().trim();  // e.g., "1 - 25"
        } catch (Exception e) {
            System.err.println("⚠️ Unable to get table row range text: " + e.getMessage());
            return "";
        }
    }
    
    public boolean clickPaginationAndVerifyTableUpdates() {
        waitForSpinnerToDisappear();

        List<WebElement> pageButtons = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(paginationButtons));
        String previousCount = "";

        for (int i = 0; i < pageButtons.size(); i++) {
            pageButtons = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(paginationButtons)); // re-fetch to avoid stale elements
            WebElement pageBtn = pageButtons.get(i);

            String pageNum = pageBtn.getText().trim();
            System.out.println("📄 Clicking on page number: " + pageNum);
            pageBtn.click();

            waitForSpinnerToDisappear();
            waitFor(1000); // slight delay after page load

            String currentCount = wait.until(ExpectedConditions.visibilityOfElementLocated(tableRowCountLocator)).getText().trim();
            System.out.println("📊 Table count text: " + currentCount);

            if (i > 0 && currentCount.equals(previousCount)) {
                System.err.println("❌ Row count did not update after clicking page " + pageNum);
                return false;
            }
            previousCount = currentCount;
        }
        return true;
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
    
     // Helper method to get text from KPI cards
        private String getKpiText(By locator) {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return element.getText().trim();
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