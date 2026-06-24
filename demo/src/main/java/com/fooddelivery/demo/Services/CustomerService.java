package com.fooddelivery.demo.Services;


import com.fooddelivery.demo.Exceptions.ResourceNotFoundException;
import com.fooddelivery.demo.Repositories.CustomerAddressRepository;
import com.fooddelivery.demo.Repositories.CustomerRepository;
import com.fooddelivery.demo.dto.RequestDTO.CustomerRequestDTO;
import com.fooddelivery.demo.dto.ResponseDTO.CustomerResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private CustomerRepository customerRepository;
       @Autowired
    public CustomerService(CustomerAddressRepository customerAddressRepository) {
        this.customerAddressRepository = customerAddressRepository;
    }

    private CustomerAddressRepository customerAddressRepository;
    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerResponseDTO  createCustomer(CustomerRequestDTO dto){
        if (dto==null){
            throw new ResourceNotFoundException("");
        }
    }





}
