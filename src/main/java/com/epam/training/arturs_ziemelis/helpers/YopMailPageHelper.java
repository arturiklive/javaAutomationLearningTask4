package com.epam.training.arturs_ziemelis.helpers;

import com.epam.training.arturs_ziemelis.utils.WebDriverUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YopMailPageHelper {

    private YopMailPageHelper() {
        throw new AssertionError("Cannot instantiate YopMailPageHelper");
    }

    public static String extractEstimatedCostFromMail(WebDriver driver, By by, By refresh) {
        int maxAttempts = 5;

        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            try {
                WebDriverUtils.waitForElementToBeVisible(driver, By.xpath("//iframe[@name='ifmail']"), 5);
                driver.switchTo().frame(driver.findElement(By.xpath("//iframe[@name='ifmail']")));
                WebDriverUtils.waitForElementToBeVisible(driver, by, 5);
                String rawText = driver.findElement(by).getText();

                Pattern pattern = Pattern.compile("(\\d{1,3}(,\\d{3})*(\\.\\d{2})?)");
                Matcher matcher = pattern.matcher(rawText);

                if (matcher.find()) {
                    return matcher.group(1).replace(",", "");
                }
            } catch (TimeoutException e) {
                driver.switchTo().defaultContent();
                driver.findElement(refresh).click();
            }
        }
        throw new IllegalStateException("Element " + by + " not found after " + maxAttempts + " attempts.");
    }
}
