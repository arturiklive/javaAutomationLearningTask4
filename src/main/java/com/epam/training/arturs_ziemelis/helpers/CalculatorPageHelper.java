package com.epam.training.arturs_ziemelis.helpers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalculatorPageHelper {

    private CalculatorPageHelper() {
        throw new AssertionError("Cannot instantiate CalculatorPageHelper");
    }

    public static double extractTotalPriceFromEstimated(WebDriver driver, By totalPriceLocator) {
        String rawText = driver.findElement(totalPriceLocator).getText();

        Pattern pattern = Pattern.compile("(\\d{1,3}(,\\d{3})*(\\.\\d{2})?)");
        Matcher matcher = pattern.matcher(rawText);

        if (matcher.find()) {
            return Double.parseDouble(matcher.group(1).replace(",", ""));
        } else {
            throw new IllegalStateException("Can't extract price from e-mail text");
        }
    }
}
