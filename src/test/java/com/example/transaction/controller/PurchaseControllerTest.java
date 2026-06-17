package com.example.transaction.controller;

import com.example.transaction.service.PurchaseService;
import com.example.transaction.service.exception.InsufficientFundsException;
import org.junit.jupiter.api.Test;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@QuarkusTest
public class PurchaseControllerTest {

    @InjectMock
    PurchaseService purchaseService;

    @Test
    public void testPurchaseProduct_Success() {
        // Arrange
        Long customerId = 1L;
        Long productId = 1L;

        // Act & Assert
        given()
                .when().post("/api/purchase/{customerId}/{productId}", customerId, productId)
                .then()
                .statusCode(200)
                .body(is("Purchase successful"));

        verify(purchaseService).purchaseProduct(customerId, productId);
    }

    @Test
    public void testPurchaseProduct_InsufficientFunds() {
        // Arrange
        Long customerId = 1L;
        Long productId = 1L;

        doThrow(new InsufficientFundsException("Insufficient funds for the purchase")).when(purchaseService).purchaseProduct(customerId, productId);

        // Act & Assert
        given()
                .when().post("/api/purchase/{customerId}/{productId}", customerId, productId)
                .then()
                .statusCode(400)
                .body(is("Insufficient funds for the purchase"));

        verify(purchaseService).purchaseProduct(customerId, productId);
    }

    @Test
    public void testPurchaseProduct_InternalServerError() {
        // Arrange
        Long customerId = 1L;
        Long productId = 1L;

        doThrow(new RuntimeException("An error occurred")).when(purchaseService).purchaseProduct(customerId, productId);

        // Act & Assert
        given()
                .when().post("/api/purchase/{customerId}/{productId}", customerId, productId)
                .then()
                .statusCode(500)
                .body(is("An error occurred"));

        verify(purchaseService).purchaseProduct(customerId, productId);
    }
}
