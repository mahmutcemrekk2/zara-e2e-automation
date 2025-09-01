package com.company.automation.steps;

import com.company.automation.core.utils.ApiBase;
import com.company.automation.utils.ConfigReader;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class TrelloApiSteps {

    private static final Logger logger = LogManager.getLogger(TrelloApiSteps.class);

    private static final String BASE_URL = "https://api.trello.com/1";
    private static final String KEY = ConfigReader.get("trello.key");
    private static final String TOKEN = ConfigReader.get("trello.token");
    private static final String USERNAME = ConfigReader.get("user.user");

    private final ApiBase api = new ApiBase();

    private String boardId;
    private String listId;
    private final List<String> cardIds = new ArrayList<>();
    private Response response;

    private String uniqueName(String prefix) {
        return prefix + "-" + USERNAME + "-" + System.currentTimeMillis();
    }

    private void prepareAuth() {
        api.resetRequest();
        api.addTrelloAuth(KEY, TOKEN);
    }

    private String fetchFirstListId(String boardId) {
        prepareAuth();
        api.addPath("id", boardId);
        api.send("GET", BASE_URL + "/boards/{id}/lists");
        api.verifyStatusCode(200);

        List<Map<String, Object>> lists = api.getResponse().jsonPath().getList("$");
        if (lists == null || lists.isEmpty()) {
            throw new IllegalStateException("Board üzerinde list bulunamadı.");
        }
        String id = String.valueOf(lists.get(0).get("id"));
        logger.info("İlk list id: {}", id);
        return id;
    }

    @When("the client creates a new Trello board")
    public void theClientCreatesANewTrelloBoard() {
        String boardName = uniqueName("board");

        prepareAuth();
        api.addQuery("name", boardName);
        api.send("POST", BASE_URL + "/boards");
        api.verifyStatusCode(200);

        response = api.getResponse();
        boardId = response.jsonPath().getString("id");
        if (boardId == null || boardId.isEmpty()) {
            throw new IllegalStateException("Board ID alınamadı.");
        }
        logger.info("Board oluşturuldu: {} ({})", boardName, boardId);

        listId = fetchFirstListId(boardId);
    }

    @And("the client creates two cards on the board")
    public void theClientCreatesTwoCardsOnTheBoard() {
        if (listId == null) {
            listId = fetchFirstListId(boardId);
        }

        for (int i = 1; i <= 2; i++) {
            prepareAuth();
            api.addQuery("idList", listId);
            api.addQuery("name", uniqueName("card-" + i));
            api.send("POST", BASE_URL + "/cards");
            api.verifyStatusCode(200);

            String cardId = api.getResponse().jsonPath().getString("id");
            if (cardId == null || cardId.isEmpty()) {
                throw new IllegalStateException("Card ID alınamadı (i=" + i + ")");
            }
            cardIds.add(cardId);
            logger.info("Kart oluşturuldu: {} (#{})", cardId, i);
        }
    }

    @And("the client updates one random card")
    public void theClientUpdatesOneRandomCard() {
        if (cardIds.isEmpty()) {
            throw new IllegalStateException("Güncellenecek kart bulunamadı.");
        }
        String targetCardId = cardIds.get(new Random().nextInt(cardIds.size()));

        prepareAuth();
        api.addPath("id", targetCardId);
        api.addQuery("name", uniqueName("updated-card"));
        api.addQuery("desc", "Updated via automation");
        api.send("PUT", BASE_URL + "/cards/{id}");
        api.verifyStatusCode(200);

        logger.info("Kart güncellendi: {}", targetCardId);
    }

    @And("the client deletes both cards")
    public void theClientDeletesBothCards() {
        for (String cId : cardIds) {
            prepareAuth();
            api.addPath("id", cId);
            api.send("DELETE", BASE_URL + "/cards/{id}");
            api.verifyStatusCode(200);
            logger.info("Kart silindi: {}", cId);
        }
        cardIds.clear();
    }

    @Then("the client deletes the board")
    public void theClientDeletesTheBoard() {
        if (boardId == null) {
            throw new IllegalStateException("Silinecek boardId yok.");
        }
        prepareAuth();
        api.addPath("id", boardId);
        api.send("DELETE", BASE_URL + "/boards/{id}");
        api.verifyStatusCode(200);
        logger.info("Board silindi: {}", boardId);
        boardId = null;
        listId = null;
    }
}
