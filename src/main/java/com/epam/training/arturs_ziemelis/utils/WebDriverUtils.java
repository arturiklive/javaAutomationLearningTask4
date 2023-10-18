package com.epam.training.arturs_ziemelis.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WebDriverUtils {

    public static void waitForElementToBeVisible(WebDriver driver, By locator, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static void scrollInsideElement(WebDriver driver, WebElement scrollableElement, int pixelsToScroll) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("arguments[0].scrollTop += arguments[1];", scrollableElement, pixelsToScroll);
    }

    public static void scrollUntilElementVisible(WebDriver driver, WebElement dropdown, By elementLocator) {
        boolean isElementVisible = !driver.findElements(elementLocator).isEmpty();
        while (!isElementVisible) {
            scrollInsideElement(driver, dropdown, 50);
            isElementVisible = !driver.findElements(elementLocator).isEmpty();
        }
    }

    public static void scrollToElement(WebDriver driver, WebElement element) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public static void clickElementWithJS(WebDriver driver, WebElement element) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("arguments[0].click();", element);
    }

}
