package com.company.automation.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class HomePage extends BasePage {

    private static final Logger logger = LogManager.getLogger(HomePage.class);

    private static final String BASE_URL = "https://www.zara.com/tr/";


    private final By zaraLogo     = By.cssSelector("layout-catalog-logo");
    private final By loginButton  = By.cssSelector("[data-qa-id='layout-header-user-logon']");
    private final By cookiesAccept = By.xpath("//button[contains(.,'Tüm çerezleri kabul et')]");
    private final By cookiesBanner = By.xpath("//*[contains(@aria-label,'Çerez banner')]");

    public void open() {
        driver.get(BASE_URL);
        waitForDomReady();
        logger.info("Opened homepage: {}", BASE_URL);
    }

    public void acceptCookiesIfPresent() {
        try {
            if (exists(cookiesAccept, 2) && isDisplayed(cookiesAccept)) {
                logger.info("Cookies banner detected, attempting to accept.");
                safeClick(cookiesAccept);
                getWait(10).until(ExpectedConditions.invisibilityOfElementLocated(cookiesBanner));
                logger.info("Accepted cookies banner");
            } else {
                logger.info("Cookies banner not displayed, skipping accept step.");
            }
        } catch (TimeoutException e) {
            logger.warn("Timeout while waiting for cookies banner. Skipping accept.");
        } catch (Exception e) {
            logger.warn("Failed to accept cookies: {}", e.getMessage());
        }
    }

    public boolean isLoaded() {
        try {
            if (exists(loginButton, 10) && isDisplayed(loginButton)) {
                logger.info("Homepage loaded successfully via login button.");
                return true;
            }
            if (exists(zaraLogo, 10) && isDisplayed(zaraLogo)) {
                logger.info("Homepage loaded successfully via logo.");
                return true;
            }
            logger.error("Homepage markers not visible (login/logo).");
            return false;
        } catch (TimeoutException e) {
            logger.error("Homepage did not load correctly (timeout).");
            return false;
        }
    }

    public void goToLogin() {
        logger.info("Clicking login button");
        safeClick(loginButton);
        waitForDomReady();
    }
}
