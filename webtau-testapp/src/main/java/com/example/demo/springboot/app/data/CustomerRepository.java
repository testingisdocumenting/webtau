package com.example.demo.springboot.app.data;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@Api(tags = "customer")
@RepositoryRestResource(collectionResourceRel = "customers", path = "customers")
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @ApiOperation("find all customers ordered by last name")
    Iterable<Customer> findAllByOrderByLastName();

    @Override
    void deleteAll();
}
