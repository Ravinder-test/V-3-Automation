package com.propertymonitor.pages;

import com.propertymonitor.base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ProjectSearchPage extends BaseTest {

    private WebDriverWait customWait;

    public ProjectSearchPage() {
        customWait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Locators
    private final By locationSearch = By.xpath("//input[@data-test-id='basicFilters.communitySearch']");
    private final By locationSuggestions = By.xpath("//div[@class='chakra-stack css-1igwmid' and .//p[text()='Jumeirah Beach Residence']]");
    private final By locationClear = By.xpath("//button[@data-test-id='basicFilters.communitySearch-clear-input']");
    private final By unitTypeDropdown = By.xpath("//input[@data-test-id='unitType']");
    private final By apartmentCheckbox = By.xpath("//label[contains(., 'Apartment')]//span[contains(@class, 'chakra-checkbox__control')]");
    private final By saleStatusDropdown = By.xpath("//input[@data-test-id='saleStatus']");
    private final By upcomingCheckbox = By.xpath("//label[contains(., 'Upcoming')]//span[contains(@class, 'chakra-checkbox__control')]");
    private final By projectCountText = By.xpath("//p[contains(@class, 'chakra-text') and contains(text(), 'project')]");

    
    
    
    
    // Actions
    public void verifyProjectSearchPageURL(String expectedBaseUrl) {
        // Wait for URL to contain base path like "/projects/project-search"
        customWait.until(ExpectedConditions.urlContains(expectedBaseUrl));
    }

    public String getProjectCount() {
        return customWait.until(ExpectedConditions.visibilityOfElementLocated(projectCountText)).getText().trim();
    }

    public void searchLocation(String locationKeyword) {
        customWait.until(ExpectedConditions.elementToBeClickable(locationSearch)).click();
        customWait.until(ExpectedConditions.visibilityOfElementLocated(locationSearch)).sendKeys(locationKeyword);
        customWait.until(ExpectedConditions.elementToBeClickable(locationSuggestions)).click();
    }

    public void clearLocation() {
        customWait.until(ExpectedConditions.elementToBeClickable(locationClear)).click();
    }

    public void toggleApartmentCheckbox() {
        customWait.until(ExpectedConditions.elementToBeClickable(unitTypeDropdown)).click();
        WebElement checkbox = customWait.until(ExpectedConditions.elementToBeClickable(apartmentCheckbox));
        checkbox.click();
        clickOutside();
    }

    public void toggleUpcomingSaleStatus() {
        customWait.until(ExpectedConditions.elementToBeClickable(saleStatusDropdown)).click();
        customWait.until(ExpectedConditions.elementToBeClickable(upcomingCheckbox)).click();
        clickOutside();
    }

    public void clickOutside() {
        driver.findElement(By.tagName("body")).click();
    }
}
