package com.epam.training.arturs_ziemelis;

import com.epam.training.arturs_ziemelis.drivers.DriverManager;
import com.epam.training.arturs_ziemelis.pages.CalculatorPage;
import com.epam.training.arturs_ziemelis.pages.CloudMainPage;
import com.epam.training.arturs_ziemelis.pages.SearchResultPage;
import com.epam.training.arturs_ziemelis.pages.YopMailPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CloudTest {

    @BeforeEach
    public void setUp() {
        DriverManager.getDriver();
    }

    @Test
    void testCloudCalculator() {
        String searchPhrase = "Google Cloud Platform Pricing Calculator";

        CloudMainPage cloudMainPage = new CloudMainPage(DriverManager.getDriver());
        cloudMainPage.openPage("https://cloud.google.com/")
                .clickCloseCookiesBanner()
                .searchAndProceed(searchPhrase);

        SearchResultPage searchResultPage = new SearchResultPage(DriverManager.getDriver());
        searchResultPage.clickOnFirstSearchResult();

        CalculatorPage calculatorPage = new CalculatorPage(DriverManager.getDriver());
        calculatorPage.switchToCalculatorIframe()
                .clickOnTopMenuLinkFullTitle()
                .setNumberOfInstances("4")
                .setSelectorValue("Operating System / Software","select_option_100","Free: Debian, CentOS, CoreOS, Ubuntu or BYOL (Bring Your Own License)")
                .setSelectorValue("Provisioning model","select_option_113","Regular")
                .setSelectorValue("Machine Family","select_option_117","General purpose")
                .setSelectorValue("Series","select_option_220","N1")
                .setSelectorValue("Machine type","select_option_469","n1-standard-8 (vCPUs: 8, RAM: 30GB)")
                .selectCheckBoxAddGpu()
                .setSelectorValue("GPU type", "select_option_512","NVIDIA Tesla V100")
                .setSelectorValue("Number of GPUs", "select_option_515", "1")
                .setSelectorValue("Local SSD","select_option_490","2x375 GB")
                .setDatacenterLocation("Frankfurt (europe-west3)")
                .setSelectorValue("Committed Usage","select_option_136", "1 Year")
                .clickAddToEstimateButton();

        double estimatedPriceStringFromCalculatorPage = calculatorPage.getTotalPriceFromEstimated();

        YopMailPage yopMailPage = new YopMailPage(DriverManager.getDriver());
        yopMailPage.openNewTab() //Go to YopMail tab
                .openPage("https://yopmail.com/")
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

        System.out.println(estimatedPriceStringFromCalculatorPage);
        System.out.println(estimatedPriceFromMail);

        assertEquals(estimatedPriceStringFromCalculatorPage, estimatedPriceFromMail);
    }

    @AfterEach
    public void tearDown() {
        DriverManager.closeDriver();
    }
}