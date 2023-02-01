package com.example.demo.springboot.app.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    List<Account> findByFirstName(@Param("name") String name);

    List<Account> findByLastName(@Param("name") String name);
}
