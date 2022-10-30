package com.example.demo.springboot.app.data;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class CustomerController {
    private final CustomerRepository customerRepository;

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @RequestMapping("/customers/{id}")
    public ResponseEntity<Customer> findCustomer(@PathVariable long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        return customer
                .map(value -> ResponseEntity.status(HttpStatus.OK).body(value))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/customers")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerRepository.save(customer));
    }

    @PutMapping("/customers/{id}")
    public ResponseEntity<Customer> updateCustomer(@RequestBody Customer customer, @PathVariable long id) {
        Optional<Customer> existing = customerRepository.findById(id);

        if (!existing.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        customer.setId(id);
        customerRepository.save(customer);

        return ResponseEntity.status(HttpStatus.OK).body(customer);
    }

    @PatchMapping("/customers/{id}")
    public ResponseEntity<Customer> patchCustomer(@RequestBody Customer customer, @PathVariable long id) {
        Optional<Customer> existing = customerRepository.findById(id);

        if (!existing.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        if (null != customer.getFirstName()) {
            existing.get().setFirstName(customer.getFirstName());
        }
        if (null != customer.getLastName()) {
            existing.get().setLastName(customer.getLastName());
        }
        customerRepository.save(customer);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/customers/{id}")
    public ResponseEntity<Object> deleteCustomer(@PathVariable long id) {
        customerRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/customers")
    public List<Customer> getAllCustomers(@RequestParam(value = "sortBy", required = false) String sortBy) {
        if (sortBy != null) {
            return customerRepository.findAll(Sort.by(sortBy));
        } else {
            return customerRepository.findAll();
        }
    }

    @GetMapping("/customers/search/first-name")
    public List<Customer> searchByFirstName(@RequestParam("name") String name) {
        return customerRepository.findByFirstName(name);
    }

    @GetMapping("/customers/search/last-name")
    public List<Customer> searchByLastName(@RequestParam("name") String name) {
        return customerRepository.findByLastName(name);
    }
}
