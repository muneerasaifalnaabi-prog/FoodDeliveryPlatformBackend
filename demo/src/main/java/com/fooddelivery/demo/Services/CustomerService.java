package com.fooddelivery.demo.Services;


import com.fooddelivery.demo.Entities.Customer;
import com.fooddelivery.demo.Entities.CustomerAddress;
import com.fooddelivery.demo.Exceptions.DuplicateResourceException;
import com.fooddelivery.demo.Exceptions.InvalidOrderStateException;
import com.fooddelivery.demo.Exceptions.ResourceNotFoundException;
import com.fooddelivery.demo.Repositories.CustomerAddressRepository;
import com.fooddelivery.demo.Repositories.CustomerRepository;
import com.fooddelivery.demo.Utils.HelperUtils;
import com.fooddelivery.demo.dto.RequestDTO.CustomerAddressRequestDTO;
import com.fooddelivery.demo.dto.RequestDTO.CustomerRequestDTO;
import com.fooddelivery.demo.dto.ResponseDTO.CustomerAddressResponseDTO;
import com.fooddelivery.demo.dto.ResponseDTO.CustomerResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {


    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerAddressRepository customerAddressRepository;

    public CustomerResponseDTO createCustomer(CustomerRequestDTO dto) {
        if (dto == null) {
            throw InvalidOrderStateException.invalidState("Customer data is required");
        }
        Optional<Customer> existCustomer = customerRepository.findByEmail(dto.getEmail());

        if (existCustomer.isPresent()) {
            throw DuplicateResourceException.alreadyExists("Customer email", dto.getEmail());
        }

        Customer customer = dto.toEntity();

        customer.setCustomerCode(HelperUtils.generateCode("CUST"));
        customer.setLoyaltyPoints(0);
        customer.setOrders(new ArrayList<>());
        customer.setReviews(new ArrayList<>());
        customer.setCreatedDate(LocalDateTime.now());
        customer.setUpdatedDate(LocalDateTime.now());
        customer.setIsActive(true);
        Customer savedCustomer = customerRepository.save(customer);

        return CustomerResponseDTO.fromEntity(savedCustomer);
    }

    public CustomerResponseDTO createCustomer(CustomerRequestDTO dto, CustomerAddressRequestDTO custaddress) {
        Optional<Customer> existCustomer = customerRepository.findByEmail(dto.getEmail());

        if (existCustomer.isPresent()) {
            throw DuplicateResourceException.alreadyExists("Customer email", dto.getEmail());
        }
        Customer customer = dto.toEntity();
        customer.setCreatedDate(LocalDateTime.now());
        customer.setUpdatedDate(LocalDateTime.now());
        customer.setIsActive(true);
        customer.setCustomerCode(HelperUtils.generateCode("CUST"));

        Customer savedCustomer = customerRepository.save(customer);

        CustomerAddress address = custaddress.toEntity();
        address.setCustomer(savedCustomer);
        address.setCreatedDate(LocalDateTime.now());
        address.setUpdatedDate(LocalDateTime.now());
        address.setIsActive(true);
        address.setIsDefault(true);
        customerAddressRepository.save(address);

        return CustomerResponseDTO.fromEntity(savedCustomer);
    }

    public CustomerAddressResponseDTO addAddress(Integer customerId, CustomerAddressRequestDTO addressDTO) {

        Customer customer = customerRepository.findCustomerById(customerId).orElseThrow(() -> ResourceNotFoundException.notFound("Customer", customerId));

        CustomerAddress address = addressDTO.toEntity();

        address.setCustomer(customer);
        address.setCreatedDate(LocalDateTime.now());
        address.setUpdatedDate(LocalDateTime.now());
        address.setIsActive(true);

        return CustomerAddressResponseDTO.fromEntity(customerAddressRepository.save(address));

    }

    public CustomerResponseDTO updateLoyaltyPoints(Integer customerId, int points) {
        Customer customer = customerRepository.findCustomerById(customerId).orElseThrow(() -> ResourceNotFoundException.notFound("Customer", customerId));

        customer.setLoyaltyPoints(customer.getLoyaltyPoints() + points);
        customer.setUpdatedDate(LocalDateTime.now());

        return CustomerResponseDTO.fromEntity(customerRepository.save(customer));

    }

    public CustomerResponseDTO applyLoyaltyPenalty(Integer customerId, int pointsDeducted) {
        Customer customer = customerRepository.findCustomerById(customerId).orElseThrow(() -> ResourceNotFoundException.notFound("Customer", customerId));

        int updatPoint = customer.getLoyaltyPoints() - pointsDeducted;
        if (updatPoint < 0) {
            updatPoint = 0;
        }
        customer.setLoyaltyPoints(updatPoint);
        customer.setUpdatedDate(LocalDateTime.now());
        return CustomerResponseDTO.fromEntity(customerRepository.save(customer));
    }

    public String deactivateCustomer(Integer customerId) {
        Customer customer = customerRepository.findCustomerById(customerId).orElseThrow(() -> ResourceNotFoundException.notFound("Customer", customerId));
        if (customer.getIsActive() == null || customer.getIsActive()) {
            customer.setIsActive(false);
            customer.setUpdatedDate(LocalDateTime.now());
            customerRepository.save(customer);

            return "DELETED";
        } else {
            return "NOT FOUND";
        }
    }

    public List<CustomerResponseDTO> getAllCustomers() {
        if (customerRepository.findAllActiveCustomers().isEmpty()) {
            throw ResourceNotFoundException.notFound("Customer", customerRepository.findAll().size());
        }
        List<Customer> customers = customerRepository.findAllActiveCustomers();
        return CustomerResponseDTO.fromEntity(customers);
    }

    public CustomerResponseDTO getCustomerById(Integer customerId) {
        Customer customer = customerRepository.findCustomerById(customerId).orElseThrow(() -> ResourceNotFoundException.notFound("Customer", customerId));
        return CustomerResponseDTO.fromEntity(customer);
    }
    public CustomerResponseDTO getCustomerByEmail(String email) {
        Customer customer = customerRepository.findCustomerByEmail(email).orElseThrow(() -> new ResourceNotFoundException( "Customer with email " + email + " was not found." ) );
        return CustomerResponseDTO.fromEntity(customer);
    }

}
