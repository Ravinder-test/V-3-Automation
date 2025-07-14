package com.propertymonitor.pages;

import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException; // <--- ADDED THIS IMPORT
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RentalIndexPage {

    WebDriver driver;
    WebDriverWait wait;
    Actions actions;

    // Constructor
    public RentalIndexPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30)); 
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

    // Navigate to the Rental Index Page
    public void openRentalIndexPage(String baseUrl) {
        driver.get(baseUrl + "indices/rental-index?limit=10&page=1");
        System.out.println("Navigated to: " + baseUrl + "indices/rental-index?limit=10&page=1");
        waitForSpinnerToDisappear();
    }

   // private By loadingSpinner = By.cssSelector(".chakra-spinner"); 
    public final By chartLocator = By.xpath("//div[@data-highcharts-chart='0']");
    
    public final By yieldCheckboxes = By.xpath("//div[contains(@class, 'chakra-stack') and contains(@class, 'css-txic59')]"); 
    
    // Locators for individual Yield checkboxes (assuming these are defined in your test or other parts of your Page Object)
    // You must ensure these are correct By objects in your actual test/page object code.
    // Example: public final By combinedYieldOption = By.xpath("//label[normalize-space(.)='Combined']//span[contains(@class, 'chakra-checkbox__control')]");
    
    public final By clickType = By.xpath("//input[@data-test-id='index']");
    public final By selectRental = By.xpath("//div[contains(@class, 'css-vum78s') and .//p[normalize-space(.)='Rental Price']]");
    
    // Common dropdown options container - this is critical for `selectDropdownOption`
    private By dropdownOptions = By.xpath("//div[contains(@class, 'chakra-stack') and (contains(@class, 'css-po4cgo') or @data-popper-placement)]//p[contains(@class,'chakra-text')]"); 
    
    public final By chartGraphPath = By.xpath("//*[local-name()='g' and contains(@class, 'highcharts-series-0')]/*[local-name()='path' and contains(@class, 'highcharts-graph')]");
    private By locationInput = By.xpath("//input[@data-test-id='community']/parent::div");
    private By suggestionOption = By.xpath("//div[contains(@class, 'chakra-stack') and @data-popper-placement='bottom' and .//p[normalize-space(.)='Damac Hills']]");
    private By clearLocation = By.xpath("//button[@data-test-id='community-clear-input']");
    
    // KPI Locators - UPDATED TO BE MORE ROBUST WITH PRECEDING-SIBLING AND STABLE CLASSES
 //   private By pricePerFt = By.xpath("//p[contains(@class, 'chakra-text') and contains(@class, 'title') and normalize-space(.)='Price Per ft²']/preceding-sibling::p[contains(@class, 'chakra-text') and contains(@class, 'css-1ydh7gf')]");
    public By rentalPerFt = By.xpath("//p[contains(@class, 'chakra-text') and contains(@class, 'title') and normalize-space(.)='Rental Price']/preceding-sibling::p[contains(@class, 'chakra-text') and contains(@class, 'css-1ydh7gf')]"); 
    private By momChange = By.xpath("//p[normalize-space(.)='Month-on-Month Change']/preceding-sibling::p");
    private By qoqChange = By.xpath("//p[normalize-space(.)='Quarter-on-Quarter Change']/preceding-sibling::p");
    private By yoyChange = By.xpath("//p[normalize-space(.)='Year-on-Year Change']/preceding-sibling::p");
    
    // NEW: Locator for the overall KPI section container
    private final By kpiSectionContainer = By.xpath("//p[contains(@class, 'chakra-text') and contains(@class, 'title') and normalize-space(.)='Price Per ft²']/ancestor::div[contains(@class, 'chakra-stack')][last()]"); 

    // Dropdown Click Triggers
    private By propertyTypeDropdown = By.xpath("//input[@data-test-id='propertyType']");
    private By indexTypeDropdown = By.xpath("//input[@data-test-id='indexType']");
    private By calendarDropdown = By.xpath("//input[@data-test-id='month']");
    
    public final By rentalYieldTable = By.xpath("//table[contains(@class, 'chakra-table')]");
    public final By pageSizeDropdown = By.xpath("//select[@data-test-id='change-page-size']");
    public final By option50 = By.xpath("//select[@data-test-id='change-page-size']/option[text()='50']");
    public final By optionAll = By.xpath("//select[@data-test-id='change-page-size']/option[text()='All']");
    public final By tableRowCountLocator = By.xpath("//div[contains(@class, 'chakra-stack') and .//p[./b] and .//p[contains(., 'from')]]//p[./b]"); 
    
    public final By paginationButtons = By.xpath("//button[starts-with(@data-test-id, 'paginate-')]");
    
    private final By clickFeedback = By.xpath("//button[@data-test-id='feedback-button']");
    private final By enterAnswer1 = By.xpath("//textarea[@data-test-id='ans1']");
    private final By enterAnswer2 = By.xpath("//textarea[@data-test-id='ans2']");
    private final By selectRating = By.xpath("//p[@data-rate='4']");
    private final By clickSubmit = By.xpath("//button[@data-test-id='submit-button']");
  //  private final By thankYouNode = By.cssSelector("[data-test-id='thankyou-text'], [data-test-id='thankyou-image']");


    // Helper method to get text from KPI cards - CORRECTED FOR NoSuchElementException & Enhanced with JS Fallback
    private String getKpiText(By locator) {
        // Use a dedicated, potentially longer wait for KPI text to appear.
        WebDriverWait kpiWait = new WebDriverWait(driver, Duration.ofSeconds(60)); // Increased timeout to 60 seconds

        String kpiName = locator.toString(); // Use locator string for logging

        try {
            // First, wait for the element to be *present* in the DOM. This handles NoSuchElementException.
            WebElement element = kpiWait.until(ExpectedConditions.presenceOfElementLocated(locator));

            // Then, scroll it into view to ensure WebDriver can see it optimally
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
            System.out.println("DEBUG: Scrolled " + kpiName + " into precise view.");
            
            // Finally, wait for its text content to be populated/visible.
            kpiWait.until(ExpectedConditions.textToBePresentInElementLocated(locator, ""));

            // Get the text after all waits are successful
            return element.getText().trim(); 

        } catch (TimeoutException e) {
            System.err.println("❌ Timeout: KPI '" + kpiName + "' value not present/visible or text not loaded within timeout. Error: " + e.getMessage());
            // Fallback to JavaScript retrieval if WebDriver wait times out
            try {
                 // Ensure element is at least present before trying JS
                 WebElement elementForJS = wait.until(ExpectedConditions.presenceOfElementLocated(locator)); // Use standard 'wait' here
                 String jsText = (String) ((JavascriptExecutor) driver).executeScript("return arguments[0].textContent;", elementForJS);
                 
                 if (jsText != null && !jsText.trim().isEmpty()) {
                     System.out.println("✅ Fallback JS retrieval success for KPI '" + kpiName + "': " + jsText.trim());
                     return jsText.trim();
                 } else {
                     System.err.println("⚠️ JS Fallback also returned empty/null for KPI: " + kpiName);
                     return "N/A (JS Empty)";
                 }
            } catch (Exception jsException) {
                 System.err.println("❌ Error (JS Fallback): Could not retrieve KPI '" + kpiName + "' via JavaScript. Error: " + jsException.getMessage());
                 return "N/A (JS Error)";
            }
        } catch (NoSuchElementException e) { // Catches if presenceOfElementLocated fails within its timeout
            System.err.println("❌ Element Not Found (after presence wait): KPI '" + kpiName + "' not found in DOM at all within timeout. Error: " + e.getMessage());
            return "N/A (Not Found)";
        } catch (Exception e) { // General catch for other unexpected exceptions
            System.err.println("❌ Unexpected Error retrieving KPI value. Locator: " + kpiName + ". Error: " + e.getMessage());
            return "N/A (Unexpected Error)";
        }
    }

    public boolean isRentalYieldChartVisible() {
        waitForSpinnerToDisappear();
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(chartLocator));
            return driver.findElement(chartLocator).isDisplayed();
        } catch (Exception e) {
            System.err.println("Chart element not found or not visible: " + e.getMessage());
            return false;
        }
    }
    
    public boolean waitForCheckboxes() { 
        try {
            WebDriverWait checkboxWait = new WebDriverWait(driver, Duration.ofSeconds(35)); 
            checkboxWait.until(ExpectedConditions.visibilityOfElementLocated(yieldCheckboxes)); 
            return true;
        } catch (Exception e) {
            System.err.println("Checkboxes container not visible: " + e.getMessage());
            return false;
        }
    }

    public void toggleCheckbox(By locator) { 
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
        clickOutside(); 
        waitForSpinnerToDisappear();
    }
    
    public void searchAndSelectLocation(String location, boolean clearAfterSelection) {
        wait.until(ExpectedConditions.elementToBeClickable(locationInput)).click();

        WebElement inputField = driver.findElement(By.xpath("//input[@data-test-id='community']")); 
        inputField.clear();
        inputField.sendKeys(location);

        wait.until(ExpectedConditions.elementToBeClickable(suggestionOption)).click();

        waitForSpinnerToDisappear(); 
        
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 1000);");

        if (clearAfterSelection) {
            wait.until(ExpectedConditions.elementToBeClickable(clearLocation)).click(); 
            waitForSpinnerToDisappear();
        }
    }
    
    public Map<String, String> getKpiValues() { 
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 1000);");
        waitForSpinnerToDisappear();

        // NEW: Wait for the entire KPI section container to be visible before getting individual KPIs
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(kpiSectionContainer));
            System.out.println("DEBUG: KPI Section Container is visible.");
        } catch (TimeoutException e) {
            System.err.println("❌ Critical: KPI Section Container not visible within timeout! Error: " + e.getMessage());
            return new LinkedHashMap<>(); // Return empty map if container not found
        }

        Map<String, String> values = new LinkedHashMap<>();
        // Removed Price Per Ft capture here as requested for diagnostic
        values.put("Month-on-Month Change", getKpiText(momChange));
        values.put("Quarter-on-Quarter Change", getKpiText(qoqChange));
        values.put("Year-on-Year Change", getKpiText(yoyChange));
        return values;
    }
    
    public Map<String, String> getKpiValuesForDropdownValidation() { 
        // NEW: Wait for the entire KPI section container to be visible
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(kpiSectionContainer));
            System.out.println("DEBUG: KPI Section Container is visible.");
        } catch (TimeoutException e) {
            System.err.println("❌ Critical: KPI Section Container not visible within timeout! Error: " + e.getMessage());
            return new LinkedHashMap<>(); // Return empty map if container not found
        }

        Map<String, String> kpiData = new LinkedHashMap<>();
        // Removed Price Per Ft capture here as requested for diagnostic
        kpiData.put("Month-on-Month", getKpiText(momChange));
        kpiData.put("Quarter-on-Quarter", getKpiText(qoqChange));
        kpiData.put("Year-on-Year", getKpiText(yoyChange));
        return kpiData;
    }

    public Map<String, String> selectRentalPrice(Map<String, String> beforeValues) {
        wait.until(ExpectedConditions.elementToBeClickable(clickType)).click();
        waitFor(500);

        wait.until(ExpectedConditions.visibilityOfElementLocated(dropdownOptions));
        WebElement rentalOptionElement = wait.until(ExpectedConditions.presenceOfElementLocated(selectRental));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", rentalOptionElement);

        waitForSpinnerToDisappear();

        // Retry KPI capture up to 3 times
        Map<String, String> afterValues = new HashMap<>();
        boolean atLeastOneChanged = false;

        int attempts = 0;
        while (attempts < 3 && !atLeastOneChanged) {
            waitFor(3000); // Wait for KPI data to update

            afterValues = getKpiValues();

            for (String key : beforeValues.keySet()) {
                String before = beforeValues.get(key);
                String after = afterValues.getOrDefault(key, "");

                if (!before.equalsIgnoreCase(after)) {
                    atLeastOneChanged = true;
                    break;
                }
            }

            if (!atLeastOneChanged) {
                attempts++;
            }
        }

        return afterValues;
    }
       
    public List<String> getDropdownOptions(By optionsLocator) { 
        List<WebElement> options = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(optionsLocator));
        return options.stream()
                        .map(WebElement::getText)
                        .collect(Collectors.toList());
    }

    private void selectDropdownOption(By dropdownTrigger, String optionToSelect) { 
           try {
               wait.until(ExpectedConditions.elementToBeClickable(dropdownTrigger)).click();
               waitFor(500);

               List<WebElement> options = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(dropdownOptions));
               WebElement targetOption = null;
               for (WebElement option : options) {
                   if (option.getText().equalsIgnoreCase(optionToSelect)) {
                       targetOption = option;
                       break;
                   }
               }
               if (targetOption != null) {
                   wait.until(ExpectedConditions.elementToBeClickable(targetOption)).click();
                   clickOutside(); 
                   waitForSpinnerToDisappear(); 
               } else {
                   throw new NoSuchElementException("Option '" + optionToSelect + "' not found within dropdown triggered by: " + dropdownTrigger.toString());
               }
           } catch (Exception e) {
               throw new RuntimeException("Failed to select option '" + optionToSelect + "' from dropdown " + dropdownTrigger.toString() + ": " + e.getMessage());
           }
       }

    public void selectPropertyType(String type) { 
        selectDropdownOption(propertyTypeDropdown, type);
    }

    public void selectIndexType(String type) { 
        selectDropdownOption(indexTypeDropdown, type);
    }

    public void selectCalendarMonth(String month) { 
        selectDropdownOption(calendarDropdown, month);
    }
       
    public boolean isRentalYieldTableVisible() { 
        waitForSpinnerToDisappear();
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(rentalYieldTable));
            return driver.findElement(rentalYieldTable).isDisplayed();
        } catch (TimeoutException e) {
            System.err.println("❌ Rental Index table not visible within timeout: " + e.getMessage());
            return false;
        }
    }
    public String getTableRowRangeText() { 
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(tableRowCountLocator));
            return driver.findElement(tableRowCountLocator).getText().trim();
        } catch (Exception e) {
            System.err.println("⚠️ Unable to get table row range text: " + e.getMessage());
            return "";
        }
    }
       
    public boolean clickPaginationAndVerifyTableUpdates() { 
        waitForSpinnerToDisappear();

        List<WebElement> pageButtons = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(paginationButtons));
        String previousCountText = ""; 
        
        if (!pageButtons.isEmpty()) {
            previousCountText = getTableRowRangeText();
            System.out.println("Initial page range text: " + previousCountText);
        }

        for (int i = 0; i < pageButtons.size(); i++) {
            pageButtons = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(paginationButtons));
            WebElement pageBtn = pageButtons.get(i);

            String pageNum = pageBtn.getText().trim();
            System.out.println("📄 Clicking on page number: " + pageNum);
            
            wait.until(ExpectedConditions.elementToBeClickable(pageBtn)).click(); 

            waitForSpinnerToDisappear();
            waitFor(1000);

            String currentCountText = getTableRowRangeText();
            System.out.println("📊 Table range text after clicking page " + pageNum + ": " + currentCountText);

            if (currentCountText.equals(previousCountText) && i > 0) {
                System.err.println("❌ Table row range did not update after clicking page " + pageNum);
                return false;
            }
            previousCountText = currentCountText;
        }
        return true;
    }
    
    /**
     * Submits the feedback form with provided answers.
     */
    public void submitFeedback(String answer1, String answer2) { 
        System.out.println("Submitting feedback...");
        wait.until(ExpectedConditions.elementToBeClickable(clickFeedback)).click();
        waitForSpinnerToDisappear();

        wait.until(ExpectedConditions.visibilityOfElementLocated(enterAnswer1)).sendKeys(answer1);
        System.out.println("Entered Answer 1.");

        wait.until(ExpectedConditions.visibilityOfElementLocated(enterAnswer2)).sendKeys(answer2);
        System.out.println("Entered Answer 2.");

        wait.until(ExpectedConditions.elementToBeClickable(selectRating)).click();
        System.out.println("Selected rating.");

        wait.until(ExpectedConditions.elementToBeClickable(clickSubmit)).click();
        waitForSpinnerToDisappear();
        System.out.println("Clicked submit button.");
    }
    
    /**
     * Checks if the thank-you node (text or image) is present after feedback submission.
     * @return true if the thank-you node is found, false otherwise.
     */
    public boolean isThankYouNodePresent() { 
        System.out.println("Checking for Thank-you node...");
        try {
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
    
     // Generic method to click outside to close dropdowns/calendars
    public void clickOutside() { 
        driver.findElement(By.tagName("body")).click();
        waitFor(500); 
    }
    
    // Helper method for generic wait
    
   
    
    public void waitFor(long milliseconds) { 
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
}