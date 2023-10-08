package com.epam.training.arturs_ziemelis.pages;

import com.epam.training.arturs_ziemelis.utils.WebDriverUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YopMailPage {
    private String originalTab;
    private String newTab;
    private static final By RANDOM_EMAIL_GENERATOR_LINK = By.xpath("//a[@href='email-generator']");
    private static final By GENERATED_MAIL_LINK = By.id("geny");
    private static final By CHECK_INBOX_BUTTON = By.xpath("//button[@onclick='egengo();']");
    private static final By COOKIES_AGREE_BUTTON = By.id("accept");
    private static final By ESTIMATED_PRICE_FROM_MAIL = By.xpath("//div[@id='mail']//h2");
    private static final By REFRESH_INBOX = By.id("refresh");
    private final WebDriver driver;

    public YopMailPage(WebDriver driver) {
        this.driver = driver;
    }
    public YopMailPage openNewTab() {
        ((JavascriptExecutor) driver).executeScript("window.open()");
        this.originalTab = driver.getWindowHandle();
        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(originalTab)) {
                newTab = handle;
                driver.switchTo().window(newTab);
                break;
            }
        }
        return this;
    }

    public void switchToOriginalTab() {
        driver.switchTo().window(originalTab);
    }

    public YopMailPage switchToNewTab() {
        driver.switchTo().window(newTab);
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
        int attempts = 0;
        final int MAX_ATTEMPTS = 5;
        String rawText;

        while (true) {
            try {
                WebDriverUtils.waitForElementToBeVisible(driver, By.xpath("//iframe[@name='ifmail']"), 5);
                driver.switchTo().frame(driver.findElement(By.xpath("//iframe[@name='ifmail']")));
                WebDriverUtils.waitForElementToBeVisible(driver, ESTIMATED_PRICE_FROM_MAIL, 5);
                rawText = driver.findElement(ESTIMATED_PRICE_FROM_MAIL).getText();
                break;
            } catch (TimeoutException e) {
                attempts++;
                if (attempts < MAX_ATTEMPTS) {
                    driver.switchTo().defaultContent();
                    driver.findElement(REFRESH_INBOX).click();
                } else {
                    throw new IllegalStateException("Element " + ESTIMATED_PRICE_FROM_MAIL + " not found after " + MAX_ATTEMPTS + " attempts.");
                }
            }
        }

        Pattern pattern = Pattern.compile("(\\d{1,3}(,\\d{3})*(\\.\\d{2})?)");
        Matcher matcher = pattern.matcher(rawText);

        if (matcher.find()) {
            return Double.parseDouble(matcher.group(1).replace(",", ""));
        } else {
            throw new IllegalStateException("Can't extract price from e-mail text");
        }
    }
}
