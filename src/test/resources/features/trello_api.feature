Feature: Trello Board and Cards API

  @trello
  Scenario: Create board, add cards, update one card, delete cards and board
    When the client creates a new Trello board
    And the client creates two cards on the board
    And the client updates one random card
    And the client deletes both cards
    Then the client deletes the board