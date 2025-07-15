package miu.edu.mpp.app.service.impl;

import miu.edu.mpp.app.domain.Customer;
import miu.edu.mpp.app.domain.Region;
import miu.edu.mpp.app.error.exception.BusinessException;
import miu.edu.mpp.app.error.exception.ResourceNotFoundException;
import miu.edu.mpp.app.repository.CustomerRepository;
import miu.edu.mpp.app.service.CustomerService;
import miu.edu.mpp.app.util.Constant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl  implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public List<Customer> findCustomerAll() {
        return customerRepository.findAll();
    }

    @Override
    public List<Customer> findCustomersByRegion(Region region) {
        return customerRepository.findByRegion(region);
    }

    @Override
    public Customer createCustomer(Customer customer) {
        log.debug("createCustomer");
        Customer customerDB = customerRepository.findByNumberID ( customer.getNumberID () );
        if (customerDB != null){
            return  customerDB;
        }

        customer.setState ( Constant.STATE_CREATED );
        customer.setCreatedBy ( "admin" );
        if (customer.getRegion().getName().equals("Europa")){
            throw new BusinessException("No  atendemos en Europa");
        }
        customerDB = customerRepository.save ( customer );
        return customerDB;
    }

    @Override
    public Customer updateCustomer(Customer customer) {
        Customer customerDB = getCustomer(customer.getId());
        customerDB.setNumberID(customer.getNumberID());
        customerDB.setFirstName(customer.getFirstName());
        customerDB.setLastName(customer.getLastName());
        customerDB.setEmail(customer.getEmail());
        customerDB.setPhotoUrl(customer.getPhotoUrl());
        customerDB.setUpdatedBy ( "admin" );
        return  customerRepository.save(customerDB);
    }

    @Override
    public Customer deleteCustomer(Customer customer) {
        Customer customerDB = this.getCustomer(customer.getId());

        customerDB.setState ( Constant.STATE_DELETED );
        return customerRepository.save(customerDB);
    }

    @Override
    public Customer getCustomer(Long id) {

        return  customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Customer with id = " + id));

    }
}
