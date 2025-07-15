package miu.edu.mpp.app.service;



import miu.edu.mpp.app.domain.Customer;
import miu.edu.mpp.app.domain.Region;

import java.util.List;

public interface CustomerService {

    public List<Customer> findCustomerAll();
    public List<Customer> findCustomersByRegion(Region region);

    public Customer createCustomer(Customer customer);
    public Customer updateCustomer(Customer customer);
    public Customer deleteCustomer(Customer customer);
    public  Customer getCustomer(Long id);



}
