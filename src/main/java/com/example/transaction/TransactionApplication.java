package com.example.transaction;

import com.example.transaction.entity.Customer;
import com.example.transaction.entity.Product;
import com.example.transaction.repository.CustomerRepository;
import com.example.transaction.repository.ProductRepository;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@QuarkusMain
public class TransactionApplication {

    public static void main(String[] args) {
        Quarkus.run(App.class, args);
    }

    public static class App implements QuarkusApplication {
        @Override
        public int run(String... args) {
            Quarkus.waitForExit();
            return 0;
        }
    }
}

@ApplicationScoped
class DataInitializer {

    @Inject
    CustomerRepository customerRepository;
    @Inject
    ProductRepository productRepository;

    @Transactional
    void onStart(@Observes StartupEvent event) {
        if (customerRepository.count() == 0) {
            Customer customer = new Customer();
            customer.setName("Vivek Singh");
            customer.setBalance(100);
            customerRepository.persist(customer);
        }

        if (productRepository.count() == 0) {
            Product p1 = new Product();
            p1.setName("Laptop");
            p1.setPrice(80.00);
            Product p2 = new Product();
            p2.setName("Phone");
            p2.setPrice(50.00);
            productRepository.persist(p1, p2);
        }
    }
}
