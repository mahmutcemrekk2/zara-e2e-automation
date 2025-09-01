package com.company.automation.hooks;

import com.company.automation.core.driver.DriverFactory;
import com.company.automation.core.driver.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CucumberHooks {

    @Before("@ui")
    public void setUpUi() {
        String browser = System.getProperty("browser", "chrome");
        WebDriver driver = DriverFactory.createDriver(browser);
        DriverManager.setDriver(driver);
    }

    @After("@ui")
    public void tearDownUi(Scenario scenario) {
        try {
            if (scenario.isFailed() && DriverManager.hasDriver()) {
                TakesScreenshot ts = (TakesScreenshot) DriverManager.getDriver();
                byte[] bytes = ts.getScreenshotAs(OutputType.BYTES);
                scenario.attach(bytes, "image/png", "failure-screenshot");

                Path outDir = Path.of("reports", "screenshots");
                Files.createDirectories(outDir);
                String tsName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
                File file = outDir.resolve(
                        "FAILED_" +
                                scenario.getName().replaceAll("[^a-zA-Z0-9-_]", "_") +
                                "_" + tsName + ".png"
                ).toFile();
                FileUtils.writeByteArrayToFile(file, bytes);
            }
        } catch (Exception ignore) {
        } finally {
            DriverManager.quitDriver();
        }
    }
}
