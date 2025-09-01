package com.company.automation.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

import static org.junit.Assert.assertEquals;

public class SearchBar extends BasePage {

    private static final Logger logger = LogManager.getLogger(SearchBar.class);

    private final By searchButton    = By.cssSelector("[data-qa-id='header-search-text-link']");
    private final By searchInput     = By.id("search-home-form-combo-input");
    private final By searchedProduct = By.cssSelector("input[data-qa-qualifier='search-term']");
    private final By clearSearchedProduct = By.cssSelector("[data-qa-action='search-products-form-clear']");

    public void firstSearch(String keyword) {
        waitBySeconds(1);
        click(searchButton);
        type(searchInput, keyword);
        pressEnter(searchInput);
        waitForDomReady();
        waitUrlContains(keyword);
        logger.info("Searched for first keyword: {}", keyword);
    }

    public void search(String keyword) {
        type(searchedProduct, keyword);
        pressEnter(searchedProduct);
        waitForDomReady();
        waitUrlContains(keyword);
        logger.info("Searched for keyword: {}", keyword);
    }

    public void clearAndAssertEmpty() {
        click(clearSearchedProduct);
        logger.info("Search box cleared");
        String value = getAttribute(searchedProduct, "value");
        assertEquals("Search box is not empty after clear.", "", value);
        logger.info("Search box is verified as empty");
    }
}
