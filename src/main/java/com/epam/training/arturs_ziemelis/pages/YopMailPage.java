package com.epam.training.arturs_ziemelis.pages;

import com.epam.training.arturs_ziemelis.helpers.YopMailPageHelper;
import com.epam.training.arturs_ziemelis.utils.WebDriverUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class YopMailPage {
    private static final By RANDOM_EMAIL_GENERATOR_LINK = By.xpath("//a[@href='email-generator']");
    private static final By GENERATED_MAIL_LINK = By.id("geny");
    private static final By CHECK_INBOX_BUTTON = By.xpath("//button[@onclick='egengo();']");
    private static final By COOKIES_AGREE_BUTTON = By.id("accept");
    private static final By ESTIMATED_PRICE_FROM_MAIL = By.xpath("//div[@id='mail']//h2");
    private static final By REFRESH_INBOX = By.id("refresh");
    private final WebDriver driver;
    private final String originalTab;
    private String yopMailTab;

    public YopMailPage(WebDriver driver) {
        this.driver = driver;
        this.originalTab = driver.getWindowHandle();
    }

    public YopMailPage openNewTab() {
        ((JavascriptExecutor) driver).executeScript("window.open();");
        for (String tab : driver.getWindowHandles()) {
            if (!tab.equals(originalTab)) {
                yopMailTab = tab;
                break;
            }
        }
        return switchToNewTab();
    }

    public void switchToOriginalTab() {
        driver.switchTo().window(originalTab);
    }

    public YopMailPage switchToNewTab() {
        driver.switchTo().window(yopMailTab);
        return this;
    }

    public YopMailPage openPage(String url) {
        driver.get(url);
        return this;
    }

    public YopMailPage clickCloseCookiesBanner() {
        WebDriverUtils.waitForElementToBeVisible(driver, COOKIES_AGREE_BUTTON, 10);
        driver.findElement(COOKIES_AGREE_BUTTON).click();
        return this;
    }

    public void clickOnRandomEmailGenerator() {
        WebDriverUtils.waitForElementToBeVisible(driver, RANDOM_EMAIL_GENERATOR_LINK, 10);
        driver.findElement(RANDOM_EMAIL_GENERATOR_LINK).click();
    }

    public String getGeneratedMail() {
        WebDriverUtils.waitForElementToBeVisible(driver, GENERATED_MAIL_LINK, 10);
        return driver.findElement(GENERATED_MAIL_LINK).getText();
    }

    public void clickCheckInboxButton() {
        WebDriverUtils.waitForElementToBeVisible(driver, CHECK_INBOX_BUTTON, 10);
        driver.findElement(CHECK_INBOX_BUTTON).click();
    }

    public double getEstimatedCostFromMail() {
        return Double.parseDouble(YopMailPageHelper.extractEstimatedCostFromMail(driver, ESTIMATED_PRICE_FROM_MAIL, REFRESH_INBOX));
    }
}
