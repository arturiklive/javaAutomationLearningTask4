package com.epam.training.arturs_ziemelis.pages;

import com.epam.training.arturs_ziemelis.utils.WebDriverUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SearchResultPage {
    private static final By FIRST_ELEMENT_IN_SEARCH_RESULT = By.xpath("(//a[@class='gs-title'])[1]");
    private final WebDriver driver;

    public SearchResultPage(WebDriver driver) {
        this.driver = driver;
    }
    public void clickOnFirstSearchResult() {
        WebDriverUtils.waitForElementToBeVisible(driver, FIRST_ELEMENT_IN_SEARCH_RESULT, 10);
        driver.findElement(FIRST_ELEMENT_IN_SEARCH_RESULT).click();
    }
}