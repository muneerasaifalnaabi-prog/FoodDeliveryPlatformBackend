package com.fooddelivery.demo.Services;


import com.fooddelivery.demo.Entities.Customer;
import com.fooddelivery.demo.Entities.CustomerAddress;
import com.fooddelivery.demo.Entities.Orders;
import com.fooddelivery.demo.Exceptions.DuplicateResourceException;
import com.fooddelivery.demo.Exceptions.InvalidOrderStateException;
import com.fooddelivery.demo.Exceptions.ResourceNotFoundException;
import com.fooddelivery.demo.Repositories.CustomerAddressRepository;
import com.fooddelivery.demo.Repositories.CustomerRepository;
import com.fooddelivery.demo.Repositories.OrdersRepository;
import com.fooddelivery.demo.Utils.HelperUtils;
import com.fooddelivery.demo.dto.CustomerPatchDTO;
import com.fooddelivery.demo.dto.RequestDTO.CustomerAddressRequestDTO;
import com.fooddelivery.demo.dto.RequestDTO.CustomerRequestDTO;
import com.fooddelivery.demo.dto.ResponseDTO.CustomerAddressResponseDTO;
import com.fooddelivery.demo.dto.ResponseDTO.CustomerResponseDTO;
import com.fooddelivery.demo.dto.ResponseDTO.OrdersResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {


    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerAddressRepository customerAddressRepository;
    @Autowired
    OrdersRepository ordersRepository;

    //****========
    //add new customer by first check if return empty,if customer already exist or not
    //==========****
    public CustomerResponseDTO createCustomer(CustomerRequestDTO dto) {
        if (dto == null) {
            throw InvalidOrderStateException.invalidState("Customer data is required");
        }
        Optional<Customer> existCustomer = customerRepository.findCustomerByEmail(dto.getEmail());

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
        Optional<Customer> existCustomer = customerRepository.findCustomerByEmail(dto.getEmail());

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
        Customer customer = customerRepository.findCustomerByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Customer with email " + email + " was not found."));
        return CustomerResponseDTO.fromEntity(customer);
    }

    public List<CustomerAddressResponseDTO> getCustomerAddresses(Integer customerId) {
        Customer customer = customerRepository.findCustomerById(customerId).orElseThrow(() -> ResourceNotFoundException.notFound("Customer", customerId));
        List<CustomerAddress> addresses = customerAddressRepository.findByCustomerId(customer.getId());
        return CustomerAddressResponseDTO.fromEntity(addresses);

    }

    public String deleteAddress(Integer addressId) {
        CustomerAddress address = customerAddressRepository.findAddressById(addressId).orElseThrow(() -> ResourceNotFoundException.notFound("Customer Address", addressId));
        if (address.getIsActive() == null || address.getIsActive()) {
            address.setIsActive(false);
            address.setUpdatedDate(LocalDateTime.now());
            customerAddressRepository.save(address);
            return "DELETED";
        } else {
            return "NOT FOUND";
        }
    }

    public CustomerAddressResponseDTO setDefaultAddress(Integer addressId) {
        CustomerAddress address = customerAddressRepository.findAddressById(addressId).orElseThrow(() -> ResourceNotFoundException.notFound("Customer Address", addressId));
        List<CustomerAddress> customerAddresses = customerAddressRepository.findByCustomerId(address.getCustomer().getId());

        for (CustomerAddress item : customerAddresses) {
            item.setIsDefault(false);
            item.setUpdatedDate(LocalDateTime.now());
            customerAddressRepository.save(item);
        }
        address.setIsDefault(true);
        address.setUpdatedDate(LocalDateTime.now());
        CustomerAddress updatedAddress = customerAddressRepository.save(address);
        return CustomerAddressResponseDTO.fromEntity(updatedAddress);
    }


    //=========**
    //Search and Pagination
    //========**


    public Page<CustomerResponseDTO> searchCustomersByName(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Customer> customers = customerRepository.searchCustomersByName(name, pageable);
        return customers.map(CustomerResponseDTO::fromEntity);
    }

    public Page<OrdersResponseDTO> getCustomerOrders(Integer customerId, String status, Date from, Date to, int page, int size) {
        customerRepository.findCustomerById(customerId).orElseThrow(() -> ResourceNotFoundException.notFound("Customer", customerId));
        Pageable pageable = PageRequest.of(page, size);
        Page<Orders> orders = ordersRepository.findCustomerOrdersWithFilters(customerId, status, from, to, pageable);
        return orders.map(OrdersResponseDTO::fromEntity);
    }


    public CustomerResponseDTO patchCustomer(Integer customerId, CustomerPatchDTO dto) {
        Customer customer = customerRepository.findCustomerById(customerId).orElseThrow(() -> ResourceNotFoundException.notFound("Customer", customerId));
        if (dto.getFirstName() != null) {
            customer.setFirstName(dto.getFirstName());
        }
        if (dto.getLastName() != null) {
            customer.setLastName(dto.getLastName());
        }

        if (dto.getPhone() != null) {
            customer.setPhone(dto.getPhone());
        }

        if (dto.getEmail() != null) {
            customer.setEmail(dto.getEmail());
        }

        customer.setUpdatedDate(LocalDateTime.now());

        customerRepository.save(customer);

        CustomerResponseDTO response = new CustomerResponseDTO();
        response.setId(customer.getId());
        response.setFirstName(customer.getFirstName());
        response.setLastName(customer.getLastName());
        response.setPhone(customer.getPhone());
        response.setEmail(customer.getEmail());

        return response;
    }



}




