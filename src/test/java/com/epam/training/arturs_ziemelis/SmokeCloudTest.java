package com.epam.training.arturs_ziemelis;

import com.epam.training.arturs_ziemelis.infrastructure.ConfigManager;
import com.epam.training.arturs_ziemelis.infrastructure.DriverManager;
import com.epam.training.arturs_ziemelis.pages.CalculatorPage;
import com.epam.training.arturs_ziemelis.pages.CloudMainPage;
import com.epam.training.arturs_ziemelis.pages.SearchResultPage;
import com.epam.training.arturs_ziemelis.pages.YopMailPage;
import org.testng.annotations.*;

@Listeners(com.epam.training.arturs_ziemelis.infrastructure.TestListener.class)
public class SmokeCloudTest {

    @BeforeMethod
    @Parameters({"configFile"})
    public void setUp(@Optional("frankfurt") String configFile) {
        DriverManager.getDriver();
        ConfigManager.loadProperties(configFile);
        System.out.println("Loading properties from: " + configFile);
    }

    @Test
    void testSmokeCloud() {
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
                .setNumberOfInstances(ConfigManager.getProperty("numberOfInstances"));

        YopMailPage yopMailPage = new YopMailPage(DriverManager.getDriver());
        yopMailPage.openNewTab() //Go to YopMail tab
                .openPage(ConfigManager.getProperty("emailServiceUrl"))
                .clickCloseCookiesBanner();
    }

    @AfterMethod
    public void tearDown() {
        DriverManager.closeDriver();
    }
}
