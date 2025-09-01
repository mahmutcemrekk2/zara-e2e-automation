package com.company.automation.steps;

import com.company.automation.pages.BasePage;
import com.company.automation.pages.ProductDetailPage;
import com.company.automation.pages.SearchResultsPage;
import core.utils.TextReportWriter;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

public class ProductSteps extends BasePage {

    private static final Logger logger = LogManager.getLogger(ProductSteps.class);

    private final SearchResultsPage resultsPage = new SearchResultsPage();
    private ProductDetailPage productDetailPage;

    private String productTitle;
    private String productPrice;
    private String productDescription;

    @And("the user selects a random product from the search results")
    public void selectRandomProductFromResults() {
        productDetailPage = resultsPage.selectRandomProduct();
        productDetailPage.assertLoaded();

        productTitle = productDetailPage.getProductTitle();
        productPrice = productDetailPage.getProductPrice();


        logger.info("Selected product -> title: [{}], price: [{}]", productTitle, productPrice);
    }

    @Then("the product information and price are saved into a text file")
    public void saveProductInfoToTextFile() {
        String content = String.format("Product Title: %s%nPrice: %s%nDescription: %s%n", productTitle, productPrice, productDescription);

        TextReportWriter writer = new TextReportWriter();
        writer.writeToFile(productTitle, content);

        logger.info("Product info saved to text file successfully.");
    }

    @When("the user adds the product to the cart")
    public void addProductToCart() {
        productDetailPage.clickAddToCart();
        logger.info("Product added to the cart: [{}]", productTitle);
    }

    @Then("the product price in the cart should match the product page price")
    public void verifyProductPriceInCart() {
        String priceInCart = productDetailPage.getCartPrice();
        logger.info("Price in cart: [{}], expected price: [{}]", priceInCart, productPrice);
        assert priceInCart.equals(productPrice) : String.format("Price mismatch! Cart: %s vs Product Page: %s", priceInCart, productPrice);
    }

    @And("the user increases the quantity to {int}")
    public void theUserIncreasesTheQuantityTo(int piece) {
        String newPrice = productDetailPage.addToNewProduct(piece);
        productDetailPage.assertChart(piece, productPrice, newPrice);
    }

    @When("the user removes the product from the cart")
    public void theUserRemovesTheProductFromTheCart() {
        scrollToTop();
        productDetailPage.removeProducts();
    }

    @Then("the cart should be empty")
    public void theCartShouldBeEmpty() {
        By chartIsEmpty = By.cssSelector("div.zds-empty-state__title > span");
        String message = "SEPETİNİZ BOŞ";
        assertTextEquals(chartIsEmpty, message);
    }
}
