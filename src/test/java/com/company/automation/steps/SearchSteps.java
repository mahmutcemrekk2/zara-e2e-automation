package com.company.automation.steps;

import com.company.automation.core.utils.ExcelReader;
import com.company.automation.pages.SearchBar;
import io.cucumber.java.en.And;

public class SearchSteps {

    private final SearchBar searchBar = new SearchBar();

    private final String excel = "data/zara-products.xlsx";
    private final String sheet = "Sheet1";
    private final ExcelReader reader = new ExcelReader(excel);

    @And("the user enters the first keyword from Excel into the search box")
    public void enterFirstKeywordFromExcel() {
        if (!reader.resourceExists()) {
            throw new AssertionError("Excel not found on classpath or filesystem: " + excel);
        }
        String keyword = reader.getCellValue(sheet, 0, 0);
        searchBar.firstSearch(keyword);
    }

    @And("the user clears the search box")
    public void clearSearchBox() {
        searchBar.clearAndAssertEmpty();
    }

    @And("the user enters the second keyword from Excel into the search box")
    public void enterSecondKeywordFromExcel() {
        String keyword = reader.getCellValue(sheet, 0, 1);
        searchBar.search(keyword);
    }
}
