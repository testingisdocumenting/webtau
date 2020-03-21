package com.example.demo.springboot.app.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Iterable<Customer> findByFirstName(@Param("name") String name);

    Iterable<Customer> findByLastName(@Param("name") String name);
}
