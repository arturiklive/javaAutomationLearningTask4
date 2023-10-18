package com.epam.training.arturs_ziemelis;

import com.epam.training.arturs_ziemelis.infrastructure.ConfigManager;
import com.epam.training.arturs_ziemelis.infrastructure.DriverManager;
import com.epam.training.arturs_ziemelis.pages.CalculatorPage;
import com.epam.training.arturs_ziemelis.pages.CloudMainPage;
import com.epam.training.arturs_ziemelis.pages.SearchResultPage;
import com.epam.training.arturs_ziemelis.pages.YopMailPage;
import org.testng.annotations.*;

import static org.testng.Assert.assertEquals;

@Listeners(com.epam.training.arturs_ziemelis.infrastructure.TestListener.class)
public class RegressionCloudTest {

    @BeforeMethod
    @Parameters({"configFile"})
    public void setUp(@Optional("frankfurt") String configFile) {
        DriverManager.getDriver();
        ConfigManager.loadProperties(configFile);
        System.out.println("Loading properties from: " + configFile);
    }

    @Test
    void testCloudCalculator() {
        String searchPhrase = "Google Cloud Platform Pricing Calculator";

        CloudMainPage cloudMainPage = new CloudMainPage(DriverManager.getDriver());
        cloudMainPage.openPage(ConfigManager.getProperty("baseUrl"))
                .clickCloseCookiesBanner()
                .searchAndProceed(searchPhrase);

        SearchResultPage searchResultPage = new SearchResultPage(DriverManager.getDriver());
        searchResultPage.clickOnFirstSearchResult();

        CalculatorPage calculatorPage = new CalculatorPage(DriverManager.getDriver());
        calculatorPage.switchToCalculatorIframe()
                .clickOnTopMenuLinkFullTitle()
                .setNumberOfInstances(ConfigManager.getProperty("numberOfInstances"))
                .setSelectorValue("Operating System / Software",
                        ConfigManager.getProperty("osSoftwareOptionValue"),
                        ConfigManager.getProperty("osSoftwareLabel"))
                .setSelectorValue("Provisioning model",
                        ConfigManager.getProperty("provisioningModelOptionValue"),
                        ConfigManager.getProperty("provisioningModelLabel"))
                .setSelectorValue("Machine Family",
                        ConfigManager.getProperty("machineFamilyOptionValue"),
                        ConfigManager.getProperty("machineFamilyLabel"))
                .setSelectorValue("Series",
                        ConfigManager.getProperty("seriesOptionValue"),
                        ConfigManager.getProperty("seriesLabel"))
                .setSelectorValue("Machine type",
                        ConfigManager.getProperty("machineTypeOptionValue"),
                        ConfigManager.getProperty("machineTypeLabel"))
                .selectCheckBoxAddGpu()
                .setSelectorValue("GPU type",
                        ConfigManager.getProperty("gpuTypeOptionValue"),
                        ConfigManager.getProperty("gpuTypeLabel"))
                .setSelectorValue("Number of GPUs",
                        ConfigManager.getProperty("numberOfGpusOptionValue"),
                        ConfigManager.getProperty("numberOfGpusLabel"))
                .setSelectorValue("Local SSD",
                        ConfigManager.getProperty("localSsdOptionValue"),
                        ConfigManager.getProperty("localSsdLabel"))
                .setDatacenterLocation(ConfigManager.getProperty("datacenterLocation"))
                .setSelectorValue("Committed Usage",
                        ConfigManager.getProperty("committedUsageOptionValue"),
                        ConfigManager.getProperty("committedUsageLabel"))
                .clickAddToEstimateButton();

        double estimatedPriceStringFromCalculatorPage = calculatorPage.getTotalPriceFromEstimated();

        YopMailPage yopMailPage = new YopMailPage(DriverManager.getDriver());
        yopMailPage.openNewTab() //Go to YopMail tab
                .openPage(ConfigManager.getProperty("emailServiceUrl"))
                .clickCloseCookiesBanner()
                .clickOnRandomEmailGenerator();
        String generatedMail = yopMailPage.getGeneratedMail();
        yopMailPage.switchToOriginalTab(); //Go to Google Calculator tab

        calculatorPage.switchToCalculatorIframe()
                .clickEmailEstimateButton()
                .enterEmailInputValue(generatedMail)
                .clickSendEmailButton();

        yopMailPage.switchToNewTab() //Go again to YopMail tab
                .clickCheckInboxButton();
        double estimatedPriceFromMail = yopMailPage.getEstimatedCostFromMail();

        assertEquals(estimatedPriceStringFromCalculatorPage, estimatedPriceFromMail);
    }

    @AfterMethod
    public void tearDown() {
        DriverManager.closeDriver();
    }
}