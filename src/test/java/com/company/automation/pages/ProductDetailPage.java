package com.company.automation.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ProductDetailPage extends BasePage {

    private static final Logger logger = LogManager.getLogger(ProductDetailPage.class);

    private final By productTitle = By.cssSelector("[data-qa-id='product-name'], h3");
    private final By productPrice = By.cssSelector("[data-qa-id='price-container-current']");
    private final By productTitleProductPage = By.cssSelector("[data-qa-qualifier='product-detail-info-name']");
    private final By productPriceProductPage = By.cssSelector("[data-qa-qualifier='price-amount-current']");
    private final By addToChartButton = By.cssSelector("[data-qa-action='add-to-cart']");
    private final By selectFirstSize = By.xpath("(//button[@data-qa-action='size-in-stock'])[1]");
    private final By closeAdvisePopUp = By.cssSelector("[data-qa-id='zds-alert-dialog-cancel-button']");
    private final By goToChartPopUp = By.xpath("//button[@data-qa-action='nav-to-cart']");
    private final By myChart = By.cssSelector("[data-qa-id='layout-header-go-to-cart']");
    private final By priceOnChart = By.cssSelector("[data-qa-qualifier='totals-total-amount']");
    private final By addProduct = By.cssSelector("[data-qa-id='add-order-item-unit']");
    private final By removeProducts = By.cssSelector("[data-qa-action='remove-order-item']");
    private final By emptyChartMessage = By.cssSelector("zds-empty-state__title");


    public void assertLoaded() {
        boolean titleVisible = isDisplayed(productTitle);
        boolean priceVisible = isDisplayed(productPrice);
        assertTrue("Product title not visible", titleVisible);
        assertTrue("Product price not visible", priceVisible);
        logger.info("Product Detail Page loaded with title and price visible.");
    }

    public String getProductTitle() {
        String title = getText(productTitleProductPage);
        logger.info("Captured product title: {}", title);
        return title;
    }

    public String getProductPrice() {
        String price = getText(productPriceProductPage);
        logger.info("Captured product price: {}", price);
        return price;
    }

    public void clickAddToCart(){
        click(addToChartButton);
        click(selectFirstSize);
        if (exists(closeAdvisePopUp)){
            click(closeAdvisePopUp);
        }
    }

    public String getCartPrice(){
        if (exists(goToChartPopUp)){
            click(goToChartPopUp);
        }else {
            click(myChart);
        }
        return getText(priceOnChart);
    }

    public String addToNewProduct(int pieces){
        for (int i = 1; i < pieces; i++) {
            click(addProduct);
        }
        waitBySeconds(1);
        return getText(priceOnChart);
    }

    public void assertChart(int piece, String oldPrice, String newPrice){
        String oldPriceInt = oldPrice.replace("TL", "").trim().replace(".", "").replace(",", ".");
        double oldPriceDouble = Double.parseDouble(oldPriceInt);
        logger.info("Parsed double price: {}", oldPriceDouble);
        String newPriceInt = newPrice.replace("TL", "").trim().replace(".", "").replace(",", ".");
        double newPriceDouble = Double.parseDouble(newPriceInt);
        logger.info("Parsed double price: {}", newPriceDouble);
        double expectedTotal = oldPriceDouble * piece;
        logger.info("Old price: {}, Piece: {}, Expected total: {}, New price: {}", oldPriceDouble, piece, expectedTotal, newPriceDouble);

        assertEquals("Prices are not equal", expectedTotal, newPriceDouble, 0.01);
    }

    public void removeProducts(){
        hover(removeProducts);
        jsClick(removeProducts);
    }

}
