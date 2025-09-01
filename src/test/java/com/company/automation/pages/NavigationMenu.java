package com.company.automation.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

public class NavigationMenu extends BasePage {

    private static final Logger logger = LogManager.getLogger(NavigationMenu.class);

    private final By hamburgerMenu = By.cssSelector("[data-qa-id='layout-header-toggle-menu']");
    private final By selectMan     = By.cssSelector("a[data-categoryid='1885841']");
    private final By viewAll       = By.cssSelector("li[data-seocategoryid= '7465']");

    public void goToMenViewAll() {
        safeClick(hamburgerMenu);
        safeClick(selectMan);
        safeClick(viewAll);
        waitUrlContains("erkek");
        logger.info("Navigated to Men > View All page");
    }
}
