package com.epam.training.arturs_ziemelis.pages;

import com.epam.training.arturs_ziemelis.utils.WebDriverUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalculatorPage {
    private static final By IFRAME_GOOGLE = By.xpath("//iframe[contains(@src, 'cloud.google.com/frame/products/calculator/index')]");
    private static final By IFRAME_MY_FRAME = By.id("myFrame");
    private static final By TOP_MENU_LINK_COMPUTE_ENGINE = By.xpath("//span[@class='ng-binding' and text()='Compute Engine']");
    private static final String NUMBER_OF_INSTANCES_SELECTOR = "#input_98";
    private static final By NUMBER_OF_INSTANCES_INPUT = By.cssSelector(NUMBER_OF_INSTANCES_SELECTOR);
    private static final String SELECTOR_VALUE_BY_TEXT_TEMPLATE = "//md-option[@id='%s']//div[contains(normalize-space(text()),'%s')]";
    private static final By CHECK_BOX_BY_TEXT_TEMPLATE = By.xpath("(//md-checkbox[contains(., 'Add GPUs.')])[1]");
    private static final By DATACENTER_LOCATION_DROPDOWN = By.id("select_167");
    private static final String DATACENTER_ELEMENT_XPATH_TEMPLATE = "//div[contains(text(), '%s')]";
    private static final By ADD_TO_ESTIMATE_BUTTON = By.xpath("//button[@ng-click='listingCtrl.addComputeServer(ComputeEngineForm);']");
    private static final By TOTAL_PRICE_FROM_ESTIMATED = By.xpath("//div[@class='cpc-cart-total']//b[@class='ng-binding']");
    private static final By EMAIL_ESTIMATE_BUTTON = By.xpath("//button[@ng-click='cloudCartCtrl.showEmailForm();']");
    private static final By EMAIL_INPUT_FIELD = By.xpath("//input[@ng-model='emailQuote.user.email']");
    private static final By SEND_EMAIL_BUTTON = By.xpath("//button[@ng-click='emailQuote.emailQuote(true); emailQuote.$mdDialog.hide()']");
    private static final Map<String, String> PLACEHOLDER_TO_ID_MAP = new HashMap<>();

    static {
        String xpathTemplate = "//form[@name='ComputeEngineForm']//md-select-value[@id='select_value_label_%s']";
        PLACEHOLDER_TO_ID_MAP.put("Operating System / Software", String.format(xpathTemplate, "90"));
        PLACEHOLDER_TO_ID_MAP.put("Provisioning model", String.format(xpathTemplate, "91"));
        PLACEHOLDER_TO_ID_MAP.put("Machine Family", String.format(xpathTemplate, "92"));
        PLACEHOLDER_TO_ID_MAP.put("Series", String.format(xpathTemplate, "93"));
        PLACEHOLDER_TO_ID_MAP.put("Machine type", String.format(xpathTemplate, "94"));
        PLACEHOLDER_TO_ID_MAP.put("GPU type", String.format(xpathTemplate, "503"));
        PLACEHOLDER_TO_ID_MAP.put("Number of GPUs", String.format(xpathTemplate, "504"));
        PLACEHOLDER_TO_ID_MAP.put("Local SSD", String.format(xpathTemplate, "463"));
        PLACEHOLDER_TO_ID_MAP.put("Committed Usage", String.format(xpathTemplate, "97"));
    }

    private final WebDriver driver;

    public CalculatorPage(WebDriver driver) {
        this.driver = driver;
    }

    private void waitForAndClick(By locator) {
        WebDriverUtils.waitForElementToBeVisible(driver, locator, 160);
        WebElement element = driver.findElement(locator);
        WebDriverUtils.clickElementWithJS(driver, element);
    }

    private void waitForScrollAndClick(By locator) {
        WebDriverUtils.waitForElementToBeVisible(driver, locator, 160);
        WebElement element = driver.findElement(locator);
        WebDriverUtils.scrollToElement(driver, element);
        WebDriverUtils.clickElementWithJS(driver, element);
    }

    public CalculatorPage switchToCalculatorIframe() {
        WebElement firstFrame = driver.findElement(IFRAME_GOOGLE);
        WebDriverUtils.waitForElementToBeVisible(driver, IFRAME_GOOGLE, 20);
        driver.switchTo().frame(firstFrame);
        WebElement secondFrame = driver.findElement(IFRAME_MY_FRAME);
        WebDriverUtils.waitForElementToBeVisible(driver, IFRAME_MY_FRAME, 20);
        driver.switchTo().frame(secondFrame);
        return this;
    }

    public CalculatorPage clickOnTopMenuLinkFullTitle() {
        waitForAndClick(TOP_MENU_LINK_COMPUTE_ENGINE);
        return this;
    }

    public CalculatorPage setNumberOfInstances(String number) {
        WebDriverUtils.waitForElementToBeVisible(driver, NUMBER_OF_INSTANCES_INPUT, 10);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("document.querySelector('" + NUMBER_OF_INSTANCES_SELECTOR + "').click();");
        js.executeScript("document.querySelector('" + NUMBER_OF_INSTANCES_SELECTOR + "').value = arguments[0];", number);
        js.executeScript("var evt = new Event('input'); document.querySelector('" + NUMBER_OF_INSTANCES_SELECTOR + "').dispatchEvent(evt);");
        return this;
    }

    public CalculatorPage setSelectorValue(String placeholderText, String selectorParentElement, String elementText) {
        String placeholderXPath = PLACEHOLDER_TO_ID_MAP.get(placeholderText);
        if (placeholderXPath == null) {
            throw new AssertionError(placeholderText + " - is not a correct placeholder text");
        }
        waitForScrollAndClick(By.xpath(placeholderXPath));

        String xpathBuilderSelectorValue = String.format(SELECTOR_VALUE_BY_TEXT_TEMPLATE, selectorParentElement, elementText);
        waitForScrollAndClick(By.xpath(xpathBuilderSelectorValue));
        return this;
    }

    public CalculatorPage selectCheckBoxAddGpu() {
        waitForAndClick(CHECK_BOX_BY_TEXT_TEMPLATE);
        return this;
    }

    public CalculatorPage setDatacenterLocation(String selectorValueText) {
        WebDriverUtils.waitForElementToBeVisible(driver, DATACENTER_LOCATION_DROPDOWN, 20);
        WebElement dropdown = driver.findElement(DATACENTER_LOCATION_DROPDOWN);

        String formattedXpath = String.format(DATACENTER_ELEMENT_XPATH_TEMPLATE, selectorValueText);
        By targetElementLocatorBy = By.xpath(formattedXpath);

        WebDriverUtils.scrollUntilElementVisible(driver, dropdown, targetElementLocatorBy);

        WebElement targetElement = driver.findElement(targetElementLocatorBy);

        WebDriverUtils.clickElementWithJS(driver, targetElement);
        return this;
    }

    public void clickAddToEstimateButton() {
        waitForScrollAndClick(ADD_TO_ESTIMATE_BUTTON);
    }

    public double getTotalPriceFromEstimated() {
        String rawText = driver.findElement(TOTAL_PRICE_FROM_ESTIMATED).getText();

        Pattern pattern = Pattern.compile("(\\d{1,3}(,\\d{3})*(\\.\\d{2})?)");
        Matcher matcher = pattern.matcher(rawText);

        if (matcher.find()) {
            return Double.parseDouble(matcher.group(1).replace(",", ""));
        } else {
            throw new IllegalStateException("Can`t extract price from e-mail text");
        }
    }

    public CalculatorPage clickEmailEstimateButton() {
        waitForScrollAndClick(EMAIL_ESTIMATE_BUTTON);
        return this;
    }

    public CalculatorPage enterEmailInputValue(String email) {
        WebDriverUtils.waitForElementToBeVisible(driver, EMAIL_INPUT_FIELD, 20);
        driver.findElement(EMAIL_INPUT_FIELD).sendKeys(email);
        return this;
    }

    public void clickSendEmailButton() {
        waitForScrollAndClick(SEND_EMAIL_BUTTON);
    }
}
