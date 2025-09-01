Feature: Zara product search and cart operations

  @ui
  Scenario: Search products from Excel and validate cart functionality
    Given the user launches Zara website
    And the user logs in with valid credentials
    When the user navigates to Men > View All
    And the user enters the first keyword from Excel into the search box
    And the user clears the search box
    And the user enters the second keyword from Excel into the search box
    And the user selects a random product from the search results
    Then the product information and price are saved into a text file
    When the user adds the product to the cart
    Then the product price in the cart should match the product page price
    And the user increases the quantity to 2
    When the user removes the product from the cart
    Then the cart should be empty
