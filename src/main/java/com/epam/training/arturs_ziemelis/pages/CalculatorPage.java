package com.epam.training.arturs_ziemelis.pages;

import com.epam.training.arturs_ziemelis.helpers.CalculatorPageHelper;
import com.epam.training.arturs_ziemelis.utils.WebDriverUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Arrays;

public class CalculatorPage {
    private static final By IFRAME_GOOGLE = By.xpath("//iframe[contains(@src, 'cloud.google.com/frame/products/calculator/index')]");
    private static final By IFRAME_MY_FRAME = By.id("myFrame");
    private static final By TOP_MENU_LINK_COMPUTE_ENGINE = By.xpath("//span[@class='ng-binding' and text()='Compute Engine']");
    private static final String NUMBER_OF_INSTANCES_SELECTOR = "#input_99";
    private static final By NUMBER_OF_INSTANCES_INPUT = By.cssSelector(NUMBER_OF_INSTANCES_SELECTOR);
    private static final String SELECTOR_VALUE_BY_TEXT_TEMPLATE = "//md-option[@id='%s']//div[contains(normalize-space(text()),'%s')]";
    private static final By CHECK_BOX_BY_TEXT_TEMPLATE = By.xpath("(//md-checkbox[contains(., 'Add GPUs.')])[1]");
    private static final By DATACENTER_LOCATION_DROPDOWN = By.id("select_168");
    private static final String DATACENTER_ELEMENT_XPATH_TEMPLATE = "//div[contains(text(), '%s')]";
    private static final By ADD_TO_ESTIMATE_BUTTON = By.xpath("//button[@ng-click='listingCtrl.addComputeServer(ComputeEngineForm);']");
    private static final By TOTAL_PRICE_FROM_ESTIMATED = By.xpath("//div[@class='cpc-cart-total']//b[@class='ng-binding']");
    private static final By EMAIL_ESTIMATE_BUTTON = By.xpath("//button[@ng-click='cloudCartCtrl.showEmailForm();']");
    private static final By EMAIL_INPUT_FIELD = By.xpath("//input[@ng-model='emailQuote.user.email']");
    private static final By SEND_EMAIL_BUTTON = By.xpath("//button[@ng-click='emailQuote.emailQuote(true); emailQuote.$mdDialog.hide()']");
    private final WebDriver driver;

    public CalculatorPage(WebDriver driver) {
        this.driver = driver;
    }

    private void waitForAndClick(By locator) {
        WebDriverUtils.waitForElementToBeVisible(driver, locator, 120);
        WebElement element = driver.findElement(locator);
        WebDriverUtils.clickElementWithJS(driver, element);
    }

    private void waitForScrollAndClick(By locator) {
        WebDriverUtils.waitForElementToBeVisible(driver, locator, 120);
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
        WebDriverUtils.waitForElementToBeVisible(driver, NUMBER_OF_INSTANCES_INPUT, 20);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("document.querySelector('" + NUMBER_OF_INSTANCES_SELECTOR + "').click();");
        js.executeScript("document.querySelector('" + NUMBER_OF_INSTANCES_SELECTOR + "').value = arguments[0];", number);
        js.executeScript("var evt = new Event('input'); document.querySelector('" + NUMBER_OF_INSTANCES_SELECTOR + "').dispatchEvent(evt);");
        return this;
    }

    public CalculatorPage setSelectorValue(String placeholderText, String selectorParentElement, String elementText) {
        String placeholderXPath = SelectorElements.findByPlaceholder(placeholderText).getXpath();
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
        return CalculatorPageHelper.extractTotalPriceFromEstimated(driver, TOTAL_PRICE_FROM_ESTIMATED);
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

    private enum SelectorElements {
        OS_SOFTWARE("Operating System / Software", "91"),
        PROVISIONING_MODEL("Provisioning model", "92"),
        MACHINE_FAMILY("Machine Family", "93"),
        SERIES("Series", "94"),
        MACHINE_TYPE("Machine type", "95"),
        GPU_TYPE("GPU type", "504"),
        NUM_GPUS("Number of GPUs", "505"),
        LOCAL_SSD("Local SSD", "464"),
        COMMITTED_USAGE("Committed Usage", "98");

        private final String placeholderText;
        private final String id;

        SelectorElements(String placeholderText, String id) {
            this.placeholderText = placeholderText;
            this.id = id;
        }

        public static SelectorElements findByPlaceholder(String placeholder) {
            return Arrays.stream(values())
                    .filter(e -> e.placeholderText.equals(placeholder))
                    .findFirst()
                    .orElseThrow(() -> new AssertionError(placeholder + " - is not a correct placeholder text"));
        }

        public String getXpath() {
            String xpathTemplate = "//form[@name='ComputeEngineForm']//md-select-value[@id='select_value_label_%s']";
            return String.format(xpathTemplate, this.id);
        }
    }
}
