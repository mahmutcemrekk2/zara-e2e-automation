package com.company.automation.steps;

import com.company.automation.pages.HomePage;
import com.company.automation.pages.LoginPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.And;

import static org.junit.Assert.assertTrue;

public class CommonSteps {

    private final HomePage homePage = new HomePage();
    private final LoginPage loginPage = new LoginPage();

    @Given("the user launches Zara website")
    public void theUserLaunchesZaraWebsite() {
        homePage.open();
        homePage.acceptCookiesIfPresent();
        assertTrue("Homepage did not load correctly", homePage.isLoaded());
    }

    @And("the user logs in with valid credentials")
    public void login() {
        homePage.goToLogin();
        loginPage.login();
    }
}
