package com.example.demo.springboot.app.data;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CustomerController {
    private final CustomerRepository customerRepository;

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @RequestMapping("/customers/{id}")
    public ResponseEntity<Customer> findCustomer(@PathVariable long id) {
        Customer customer = customerRepository.findOne(id);
        return customer == null ?
                ResponseEntity.notFound().build():
                ResponseEntity.status(HttpStatus.OK).body(customer);
    }

    @PostMapping("/customers")
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerRepository.save(customer));
    }

    @PutMapping("/customers/{id}")
    public ResponseEntity<Customer> updateCustomer(@RequestBody Customer customer, @PathVariable long id) {
        Customer existing = customerRepository.findOne(id);

        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        customer.setId(id);
        customerRepository.save(customer);

        return ResponseEntity.status(HttpStatus.OK).body(customer);
    }

    @PatchMapping("/customers/{id}")
    public ResponseEntity<Customer> patchCustomer(@RequestBody Customer customer, @PathVariable long id) {
        Customer existing = customerRepository.findOne(id);

        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        if (null != customer.getFirstName()) {
            existing.setFirstName(customer.getFirstName());
        }
        if (null != customer.getLastName()) {
            existing.setLastName(customer.getLastName());
        }
        customerRepository.save(customer);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/customers/{id}")
    public ResponseEntity<Object> deleteCustomer(@PathVariable long id) {
        customerRepository.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/customers")
    public Iterable<Customer> getAllCustomers(@RequestParam(value = "sortBy", required = false) String sortBy) {
        if (sortBy != null) {
            return customerRepository.findAll(new Sort(sortBy));
        } else {
            return customerRepository.findAll();
        }
    }

    @GetMapping("/customers/search/first-name")
    public Iterable<Customer> searchByFirstName(@RequestParam("name") String name) {
        return customerRepository.findByFirstName(name);
    }

    @GetMapping("/customers/search/last-name")
    public Iterable<Customer> searchByLastName(@RequestParam("name") String name) {
        return customerRepository.findByLastName(name);
    }
}
