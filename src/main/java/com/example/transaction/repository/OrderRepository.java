package com.example.transaction.repository;

import com.example.transaction.entity.CustomerOrder;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OrderRepository implements PanacheRepository<CustomerOrder> {
}
