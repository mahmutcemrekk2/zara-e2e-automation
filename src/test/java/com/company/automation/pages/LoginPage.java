package com.company.automation.pages;

import com.company.automation.utils.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

public class LoginPage extends BasePage {

    private static final Logger logger = LogManager.getLogger(LoginPage.class);

    private final By emailField = By.cssSelector("input[data-qa-input-qualifier='logonId']");
    private final By passwordField = By.cssSelector("input[data-qa-input-qualifier='password']");
    private final By submitBtn = By.cssSelector("button[data-qa-id='logon-form-submit']");
    private final By loginUser = By.cssSelector("a[data-qa-id = 'layout-header-user-account']");
    private final By loginAlertMessage = By.cssSelector(".zds-dialog-header.zds-alert-dialog__header");
    private final By alertAccept = By.cssSelector("[data-qa-id='zds-alert-dialog-accept-button']");
    private final By homePageLink = By.cssSelector(".hlp-header__logo-link");

    public void login() {
        String email = ConfigReader.get("user.email");
        String password = ConfigReader.get("user.password");
        String userName = ConfigReader.get("user.user");

        type(emailField, email);
        type(passwordField, password);
        safeClick(submitBtn);
        if (exists(loginAlertMessage)) {
            click(alertAccept);
            click(homePageLink);
        } else {
            assertTextContains(loginUser, userName);
            logger.info("User [{}] has successfully logged in.", userName);
        }
    }
}
