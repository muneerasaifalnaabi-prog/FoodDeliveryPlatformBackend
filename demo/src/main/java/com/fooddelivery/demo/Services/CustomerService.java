package com.fooddelivery.demo.Services;


import com.fooddelivery.demo.Entities.Customer;
import com.fooddelivery.demo.Entities.CustomerAddress;
import com.fooddelivery.demo.Exceptions.DuplicateResourceException;
import com.fooddelivery.demo.Exceptions.InvalidOrderStateException;
import com.fooddelivery.demo.Repositories.CustomerAddressRepository;
import com.fooddelivery.demo.Repositories.CustomerRepository;
import com.fooddelivery.demo.Utils.HelperUtils;
import com.fooddelivery.demo.dto.RequestDTO.CustomerAddressRequestDTO;
import com.fooddelivery.demo.dto.RequestDTO.CustomerRequestDTO;
import com.fooddelivery.demo.dto.ResponseDTO.CustomerResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerAddressRepository customerAddressRepository;

    public CustomerResponseDTO createCustomer(CustomerRequestDTO dto){
        if (dto == null) {
            throw InvalidOrderStateException.invalidState("Customer data is required");
        }
            Optional<Customer> existCustomer =  customerRepository.findByEmail(dto.getEmail());

            if (existCustomer.isPresent()){
                throw DuplicateResourceException.alreadyExists("Customer email", dto.getEmail());
            }

            Customer customer =dto.toEntity();

            customer.setCustomerCode(HelperUtils.generateCode("CUST"));
            customer.setLoyaltyPoints(0);
            customer.setOrders(new ArrayList<>());
            customer.setReviews(new ArrayList<>());
            customer.setCreatedDate(LocalDateTime.now());
            customer.setUpdatedDate(LocalDateTime.now());
            customer.setIsActive(true);
            Customer savedCustomer = customerRepository.save(customer);

        return CustomerResponseDTO.fromEntity( savedCustomer );
    }
    public CustomerResponseDTO createCustomer(CustomerRequestDTO dto, CustomerAddressRequestDTO custaddress) {
        Optional<Customer> existCustomer =  customerRepository.findByEmail(dto.getEmail());

        if (existCustomer.isPresent()){
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

}
