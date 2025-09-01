package com.company.automation.steps;

import com.company.automation.pages.NavigationMenu;
import io.cucumber.java.en.When;

public class NavigationSteps {
    private final NavigationMenu menu = new NavigationMenu();

    @When("the user navigates to Men > View All")
    public void goMenViewAll() {
        menu.goToMenViewAll();
    }
}
