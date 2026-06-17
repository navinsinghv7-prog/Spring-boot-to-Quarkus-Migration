package com.example.transaction.service;

import com.example.transaction.entity.Customer;
import com.example.transaction.entity.CustomerOrder;
import com.example.transaction.entity.Product;
import com.example.transaction.repository.CustomerRepository;
import com.example.transaction.repository.OrderRepository;
import com.example.transaction.repository.ProductRepository;
import com.example.transaction.service.exception.InsufficientFundsException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class PurchaseService {

    @Inject
    CustomerRepository customerRepository;
    @Inject
    ProductRepository productRepository;
    @Inject
    OrderRepository orderRepository;

    @Transactional
    public void purchaseProduct(Long customerId, Long productId) {
        Customer customer = customerRepository.findByIdOptional(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        Product product = productRepository.findByIdOptional(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (customer.getBalance() < product.getPrice()) {
            throw new InsufficientFundsException("Insufficient funds for the purchase");
        }

        customer.setBalance(customer.getBalance() - product.getPrice());

        CustomerOrder order = new CustomerOrder();
        order.setCustomer(customer);
        order.setProduct(product);
        orderRepository.persist(order);
    }
}
