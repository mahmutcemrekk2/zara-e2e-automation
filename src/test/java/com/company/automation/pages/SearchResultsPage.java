package com.company.automation.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertTrue;

public class SearchResultsPage extends BasePage {

    private static final Logger logger = LogManager.getLogger(SearchResultsPage.class);

    private final By productCards = By.cssSelector("div.media__wrapper.media__wrapper--fill");

    public void assertResultsVisible() {
        getWait(10).until(d -> !d.findElements(productCards).isEmpty());
        int count = driver.findElements(productCards).size();
        assertTrue("No products found in search results.", count > 0);
        logger.info("Search results visible. Found {} items.", count);
    }

    public ProductDetailPage selectRandomProduct() {
        assertResultsVisible();

        List<WebElement> products = driver.findElements(productCards);
        int count = products.size();
        int idx = new Random().nextInt(count);
        WebElement chosen = products.get(idx);

        try {
            scrollIntoView(chosen);
            click(chosen);
        } catch (RuntimeException e) {
            logger.warn("Normal click failed, falling back to JS click. Cause: {}", e.getMessage());
            jsClick(chosen);
        }

        waitForDomReady();
        logger.info("Randomly selected product at index {} of {}", idx, count);

        return new ProductDetailPage();
    }
}
