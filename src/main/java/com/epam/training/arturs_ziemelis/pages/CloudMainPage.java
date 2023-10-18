package com.epam.training.arturs_ziemelis.pages;

import com.epam.training.arturs_ziemelis.utils.WebDriverUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CloudMainPage {
    private static final By SEARCH_GOOGLE_ICON = By.xpath("//div[@class='p1o4Hf']");
    private static final By SEARCH_TEXT_FIELD = By.xpath("//input[@class='mb2a7b']");
    private static final By COOKIES_AGREE_BUTTON = By.xpath("//button[@class='pvUife']");
    private final WebDriver driver;

    public CloudMainPage(WebDriver driver) {
        this.driver = driver;
    }

    public CloudMainPage openPage(String url) {
        driver.get(url);
        return this;
    }

    public CloudMainPage clickCloseCookiesBanner() {
        WebDriverUtils.waitForElementToBeVisible(driver, COOKIES_AGREE_BUTTON, 10);
        driver.findElement(COOKIES_AGREE_BUTTON).click();
        return this;
    }

    public void searchAndProceed(String searchString) {
        WebDriverUtils.waitForElementToBeVisible(driver, SEARCH_GOOGLE_ICON, 10);
        driver.findElement(SEARCH_GOOGLE_ICON).click();
        WebElement searchInput = driver.findElement(SEARCH_TEXT_FIELD);
        searchInput.sendKeys(searchString);
        searchInput.sendKeys(Keys.ENTER);
    }
}