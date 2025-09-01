package com.company.automation.pages;

import com.company.automation.core.driver.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Locale;

public abstract class BasePage {
    private static final Logger logger = LogManager.getLogger(BasePage.class);

    protected final WebDriver driver;

    private static final long DEFAULT_TIMEOUT = Long.parseLong(System.getProperty("explicitWait", "15"));
    private static final Duration POLL_INTERVAL = Duration.ofMillis(250);

    protected BasePage() {
        this.driver = DriverManager.getDriver();
    }

    protected String currentUrlDecoded() {
        try {
            String raw = driver.getCurrentUrl();
            String decoded = URLDecoder.decode(raw, StandardCharsets.UTF_8);
            logger.debug("Current URL raw: {} | decoded: {}", raw, decoded);
            return decoded;
        } catch (Exception e) {
            return driver.getCurrentUrl();
        }
    }

    protected WebDriverWait getWait(long seconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
        wait.pollingEvery(POLL_INTERVAL);
        wait.ignoring(NoSuchElementException.class);
        wait.ignoring(StaleElementReferenceException.class);
        wait.ignoring(ElementClickInterceptedException.class);
        return wait;
    }

    public void waitForDomReady() {
        getWait(20).until(d -> "complete".equals(((JavascriptExecutor) d).executeScript("return document.readyState")));
    }

    protected WebElement waitVisible(By locator) {
        return getWait(DEFAULT_TIMEOUT).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitClickable(By locator) {
        return getWait(DEFAULT_TIMEOUT).until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected boolean waitInvisible(By locator) {
        return getWait(DEFAULT_TIMEOUT).until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    protected boolean waitUrlContains(String fragment) {
        final String needle = fragment.toLowerCase(Locale.ROOT);
        return getWait(DEFAULT_TIMEOUT).until(d -> {
            String decoded = currentUrlDecoded().toLowerCase(Locale.ROOT);
            return decoded.contains(needle);
        });
    }

    public boolean exists(By locator, long seconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(seconds))
                    .until(d -> !d.findElements(locator).isEmpty());
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean exists(By locator) {
        return exists(locator, 3);
    }

    public void click(By locator) {
        scrollIntoView(locator);
        waitClickable(locator).click();
    }

    public void click(WebElement element) {
        scrollIntoView(element);
        getWait(DEFAULT_TIMEOUT).until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    public void safeClick(By locator) {
        try {
            click(locator);
        } catch (RuntimeException e) {
            jsClick(locator);
        }
    }

    public void jsClick(By locator) {
        WebElement el = waitVisible(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
    }

    public void jsClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    public void type(By locator, String text) {
        WebElement el = waitVisible(locator);
        el.clear();
        el.sendKeys(text);
    }

    public void pressEnter(By locator) {
        waitVisible(locator).sendKeys(Keys.ENTER);
    }

    public String getText(By locator) {
        return waitVisible(locator).getText().trim();
    }

    public String getAttribute(By locator, String name) {
        return waitVisible(locator).getAttribute(name);
    }

    public boolean isDisplayed(By locator) {
        try {
            return waitVisible(locator).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void hover(By locator) {
        new Actions(driver).moveToElement(waitVisible(locator)).perform();
    }

    public void scrollIntoView(By locator) {
        WebElement el = waitVisible(locator);
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center', inline:'nearest'});", el);
    }

    public void scrollIntoView(WebElement el) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center', inline:'nearest'});", el);
    }

    public void assertTextEquals(By locator, String expected) {
        String actual = getText(locator);
        Assert.assertEquals("Text mismatch for: " + locator, expected, actual);
    }

    public void assertTextContains(By locator, String fragment) {
        String actual = getText(locator);
        Assert.assertTrue("Expected text to contain '" + fragment + "' but was: " + actual, actual.contains(fragment));
    }

    public void waitBySeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
            logger.info("Waited for {} seconds successfully | {} saniye beklendi", seconds, seconds);
        } catch (InterruptedException e) {
            logger.error("Error while waiting for {} seconds. Error: {} ",
                    seconds, e.getMessage(), e);
            Thread.currentThread().interrupt();
        }
    }

    public void scrollToTop() {
        try {
            logger.info("Attempting to scroll to the top of the page");
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollTo(0, 0);");
            logger.info("Successfully scrolled to the top of the page ");
        } catch (Exception e) {
            logger.error("Failed to scroll to the top of the page. Error: {} ", e.getMessage());
            logger.debug("Stack trace:", e);
            throw e;
        }
    }

}
