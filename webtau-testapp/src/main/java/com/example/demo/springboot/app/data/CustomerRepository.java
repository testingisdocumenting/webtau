package com.example.demo.springboot.app.data;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@Api(tags = "customer")
@RepositoryRestResource(collectionResourceRel = "customers", path = "customers")
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @ApiOperation("find all customers ordered by last name")
    Iterable<Customer> findAllByOrderByLastName();

    @ApiOperation("find all customers ordered by first name")
    Iterable<Customer> findAllByOrderByFirstName();

    @ApiOperation("find customers by first name")
    @RestResource(path = "first-name")
    Iterable<Customer> findByFirstName(@Param("name") String name);

    @ApiOperation("find customers by last name")
    @RestResource(path = "last-name")
    Iterable<Customer> findByLastName(@Param("name") String name);
}
