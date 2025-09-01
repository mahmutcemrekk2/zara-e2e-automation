package com.company.automation.core.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

import java.time.Duration;

public class DriverFactory {

    public static WebDriver createDriver(String browser) {
        String br = browser != null ? browser : System.getProperty("browser", "chrome");
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "false"));
        int implicitWaitSec = Integer.parseInt(System.getProperty("implicitWait", "0"));
        int pageLoadTimeoutSec = Integer.parseInt(System.getProperty("pageLoadTimeout", "60"));

        WebDriver driver;

        switch (br.toLowerCase()) {
            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions eOpts = new EdgeOptions();
                eOpts.setPageLoadStrategy(PageLoadStrategy.NORMAL);
                if (headless) {
                    eOpts.addArguments("--headless=new");
                }
                eOpts.addArguments("--start-maximized");
                eOpts.addArguments("--disable-gpu", "--no-sandbox", "--disable-dev-shm-usage");
                driver = new EdgeDriver(eOpts);
                break;

            case "chrome":
            default:
                WebDriverManager.chromedriver().setup();
                ChromeOptions cOpts = new ChromeOptions();
                cOpts.setPageLoadStrategy(PageLoadStrategy.NORMAL);
                if (headless) {
                    cOpts.addArguments("--headless=new");
                }
                cOpts.addArguments("--start-maximized");
                cOpts.addArguments("--disable-gpu", "--no-sandbox", "--disable-dev-shm-usage");
                driver = new ChromeDriver(cOpts);
                break;
        }

        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadTimeoutSec));
        if (implicitWaitSec > 0) {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWaitSec));
        }
        return driver;
    }
}
