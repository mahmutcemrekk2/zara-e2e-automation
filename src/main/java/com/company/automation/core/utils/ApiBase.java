package com.company.automation.core.utils;

import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.SSLConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class ApiBase {

    private static final Logger logger = LogManager.getLogger(ApiBase.class);

    private final Map<String, String> headers = new HashMap<>();
    private final Map<String, Object> queryParams = new HashMap<>();
    private final Map<String, Object> pathParams = new HashMap<>();
    private final Map<String, Object> savedValues = new HashMap<>();

    private JSONObject requestBody = new JSONObject();
    private String rawRequestBody = null;
    private boolean useRawBody = false;
    private boolean relaxedSSL = false;

    protected Response response;

    public void resetRequest() {
        headers.clear();
        queryParams.clear();
        pathParams.clear();
        requestBody = new JSONObject();
        rawRequestBody = null;
        useRawBody = false;
        logger.info("Request state reset (headers/query/path/body cleared).");
    }

    public void addQuery(String key, Object value) {
        queryParams.put(key, value);
        logger.info("Query {} = {}", key, value);
    }

    public void addPath(String key, Object value) {
        pathParams.put(key, value);
        logger.info("Path {} = {}", key, value);
    }

    public void addTrelloAuth(String trelloKey, String trelloToken) {
        addQuery("key", trelloKey);
        addQuery("token", trelloToken);
        logger.info("Trello auth query params added.");
    }

    public void send(String method, String url) {
        method = method.toUpperCase(Locale.ROOT);

        RestAssuredConfig config = RestAssuredConfig.newConfig();
        if (relaxedSSL) {
            config = config.sslConfig(SSLConfig.sslConfig().relaxedHTTPSValidation());
        }

        var given = RestAssured.given()
                .config(config)
                .headers(headers)
                .queryParams(queryParams)
                .pathParams(pathParams);

        switch (method) {
            case "GET":
                response = given.get(url);
                break;
            case "DELETE":
                response = given.delete(url);
                break;
            case "POST":
            case "PUT":
            case "PATCH":
                if (useRawBody && rawRequestBody != null) {
                    response = given
                            .contentType(ContentType.JSON)
                            .body(rawRequestBody)
                            .request(method, url);
                } else {
                    response = given
                            .contentType(ContentType.JSON)
                            .body(requestBody.toString())
                            .request(method, url);
                }
                break;
            default:
                logger.error("Unsupported HTTP method: {}", method);
                throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        }

        logRequestAndResponse(method, url);
    }

    public void verifyStatusCode(int expected) {
        int actual = response.getStatusCode();
        if (actual != expected) {
            logger.error("Status code mismatch. Expected: {}, Actual: {}", expected, actual);
            throw new AssertionError("Expected " + expected + " but got " + actual);
        }
        logger.info("Status code verified: {}", expected);
    }

    public Response getResponse() {
        return response;
    }

    private void logRequestAndResponse(String method, String url) {
        try {
            logger.info("{} => {}", method, url);
            logger.info("Headers: {}", headers);
            logger.info("Query:   {}", queryParams);
            logger.info("Path:    {}", pathParams);
            if ("GET".equals(method) || "DELETE".equals(method)) {
                logger.info("Body: (not sent for {} by default)", method);
            } else if (useRawBody && rawRequestBody != null) {
                logger.info("Raw Body:\n{}", rawRequestBody);
            } else {
                logger.info("JSON Body:\n{}", requestBody.toString(2));
            }

            if (response != null) {
                logger.info("Response Status: {}", response.getStatusCode());
                logger.info("Response Body:\n{}", safePretty(response));
            }
        } catch (Exception e) {
            logger.warn("Log compose error: {}", e.getMessage());
        }
    }

    private String safePretty(Response resp) {
        try {
            if (resp.getContentType() != null &&
                    resp.getContentType().contains("application/json")) {
                return resp.getBody().asPrettyString();
            }
            return resp.getBody().asString();
        } catch (Exception ignore) {
            return "(unavailable)";
        }
    }

}
