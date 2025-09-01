package com.company.automation.runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com.company.automation.steps", "com.company.automation.hooks"},
        plugin = {
                "pretty",
                "html:reports/cucumber/cucumber.html",
                "json:reports/cucumber/cucumber.json",
                "junit:reports/cucumber/cucumber.xml"
        },
        monochrome = true,
        publish = false
)
public class E2ERunnerTest {
}
