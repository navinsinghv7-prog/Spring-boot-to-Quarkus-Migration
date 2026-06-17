package com.example.transaction.service;

import com.example.transaction.entity.Customer;
import com.example.transaction.entity.CustomerOrder;
import com.example.transaction.entity.Product;
import com.example.transaction.repository.CustomerRepository;
import com.example.transaction.repository.OrderRepository;
import com.example.transaction.repository.ProductRepository;
import com.example.transaction.service.exception.InsufficientFundsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PurchaseServiceTest {

    @InjectMocks
    private PurchaseService purchaseService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderRepository orderRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testPurchaseProduct_Success() {
        // Arrange
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Vivek Singh");
        customer.setBalance(100.0);

        Product product = new Product();
        product.setId(1L);
        product.setName("Laptop");
        product.setPrice(80.0);

        when(customerRepository.findByIdOptional(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findByIdOptional(1L)).thenReturn(Optional.of(product));
        doNothing().when(orderRepository).persist(any(CustomerOrder.class));

        // Act
        purchaseService.purchaseProduct(1L, 1L);

        // Assert
        verify(customerRepository, times(1)).findByIdOptional(1L);
        verify(productRepository, times(1)).findByIdOptional(1L);
        verify(orderRepository, times(1)).persist(any(CustomerOrder.class));

        assertEquals(20.0, customer.getBalance());
    }

    @Test
    public void testPurchaseProduct_InsufficientFunds() {
        // Arrange
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Vivek Singh");
        customer.setBalance(50.0);

        Product product = new Product();
        product.setId(1L);
        product.setName("Laptop");
        product.setPrice(80.0);

        when(customerRepository.findByIdOptional(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findByIdOptional(1L)).thenReturn(Optional.of(product));

        // Act & Assert
        InsufficientFundsException exception = assertThrows(InsufficientFundsException.class, () -> {
            purchaseService.purchaseProduct(1L, 1L);
        });

        assertEquals("Insufficient funds for the purchase", exception.getMessage());

        verify(customerRepository, times(1)).findByIdOptional(1L);
        verify(productRepository, times(1)).findByIdOptional(1L);
        verify(orderRepository, never()).persist(any(CustomerOrder.class));
    }

    @Test
    public void testPurchaseProduct_CustomerNotFound() {
        // Arrange
        when(customerRepository.findByIdOptional(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            purchaseService.purchaseProduct(1L, 1L);
        });

        assertEquals("Customer not found", exception.getMessage());

        verify(customerRepository, times(1)).findByIdOptional(1L);
        verify(productRepository, never()).findByIdOptional(1L);
        verify(orderRepository, never()).persist(any(CustomerOrder.class));
    }

    @Test
    public void testPurchaseProduct_ProductNotFound() {
        // Arrange
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Vivek Singh");
        customer.setBalance(100.0);

        when(customerRepository.findByIdOptional(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findByIdOptional(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            purchaseService.purchaseProduct(1L, 1L);
        });

        assertEquals("Product not found", exception.getMessage());

        verify(customerRepository, times(1)).findByIdOptional(1L);
        verify(productRepository, times(1)).findByIdOptional(1L);
        verify(orderRepository, never()).persist(any(CustomerOrder.class));
    }
}
