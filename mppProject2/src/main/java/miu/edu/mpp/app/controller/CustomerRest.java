package miu.edu.mpp.app.controller;

import miu.edu.mpp.app.domain.Customer;
import miu.edu.mpp.app.domain.Region;
import miu.edu.mpp.app.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/customers")
public class CustomerRest {


   public final CustomerService customerService;

    // -------------------Retrieve All Customers--------------------------------------------

    @GetMapping
    public ResponseEntity<List<Customer>> listAllCustomers(@RequestParam(name = "regionId" , required = false) Long regionId ) {
        log.info("get listAllCustomer INFO" );
        List<Customer> customers ;
        if (null ==  regionId) {
            customers = customerService.findCustomerAll();
            if (customers.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
        }else{
            Region region= Region.builder()
            .id(regionId).build();
            customers = customerService.findCustomersByRegion(region);
            if ( null == customers ) {
                log.error("Customers with Region id {} not found.", regionId);
                return  ResponseEntity.notFound().build();
            }
        }

        return  ResponseEntity.ok(customers);
    }

    // -------------------Retrieve Single Customer------------------------------------------

    @GetMapping(value = "/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable("id") long id) {
        log.info("Fetching Customer with id {}", id);
        Customer customer = customerService.getCustomer(id);
        return  ResponseEntity.ok(customer);
    }

    // -------------------Create a Customer-------------------------------------------

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody Customer customer) {
        log.info("Creating Customer : {}", customer);

       Customer customerDB = customerService.createCustomer (customer);

        return  ResponseEntity.status( HttpStatus.CREATED).body(customerDB);
    }

    // ------------------- Update a Customer ------------------------------------------------

    @PutMapping(value = "/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable("id") long id, @RequestBody Customer customer) {
        log.info("Updating Customer with id {}", id);

        customer.setId(id);
        Customer currentCustomer=customerService.updateCustomer(customer);
        return  ResponseEntity.ok(currentCustomer);
    }

    // ------------------- Delete a Customer-----------------------------------------

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Customer> deleteCustomer(@PathVariable("id") long id) {
        log.info("Fetching & Deleting Customer with id {}", id);

        Customer customer = customerService.getCustomer(id);

        customer = customerService.deleteCustomer(customer);
        return  ResponseEntity.ok(customer);
    }



}
