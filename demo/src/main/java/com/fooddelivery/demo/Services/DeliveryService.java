package com.fooddelivery.demo.Services;

import com.fooddelivery.demo.Entities.Delivery;
import com.fooddelivery.demo.Entities.DeliveryDriver;
import com.fooddelivery.demo.Entities.Orders;
import com.fooddelivery.demo.Exceptions.InvalidOrderStateException;
import com.fooddelivery.demo.Exceptions.ResourceNotFoundException;
import com.fooddelivery.demo.Repositories.DeliveryDriverRepository;
import com.fooddelivery.demo.Repositories.DeliveryRepository;
import com.fooddelivery.demo.Repositories.OrdersRepository;
import com.fooddelivery.demo.dto.ResponseDTO.DeliveryDriverResponseDTO;
import com.fooddelivery.demo.dto.ResponseDTO.DeliveryResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DeliveryService {
    @Autowired
    private DeliveryRepository deliveryRepository;
    @Autowired
    private DeliveryDriverRepository deliveryDriverRepository;
    @Autowired
    private OrdersRepository ordersRepository;

    public DeliveryResponseDTO assignDriverToOrders(Integer orderId, Integer driverId) {
        Orders orders = ordersRepository.findById(orderId).orElseThrow(() -> ResourceNotFoundException.notFound("Order", orderId));
        //check if status confirmed or not
        if (!orders.getStatus().equals("CONFIRMED")) {
            throw InvalidOrderStateException.invalidState("cannot assign driver to order");
        }
        DeliveryDriver driver = deliveryDriverRepository.findDriverById(driverId).orElseThrow(() -> ResourceNotFoundException.notFound("Driver", driverId));
        if (!driver.getIsOnline()) {
            throw InvalidOrderStateException.invalidState("cannot assign driver to order");
        }
        Delivery delivery = new Delivery();
        delivery.setOrders(orders);
        delivery.setDeliveryDriver(driver);
        delivery.setStatus("ASSIGNED");
        delivery.setAssignedAt(LocalDateTime.now());
        delivery.setCreatedDate(LocalDateTime.now());
        delivery.setUpdatedDate(LocalDateTime.now());
        delivery.setIsActive(true);
        return DeliveryResponseDTO.fromEntity(deliveryRepository.save(delivery));
    }

    public DeliveryResponseDTO autoAssignDriver(Integer orderId) {
        Orders orders = ordersRepository.findById(orderId).orElseThrow(() -> ResourceNotFoundException.notFound("Order", orderId));
        DeliveryDriver driver = deliveryDriverRepository.findFirstAvailableOnlineDriver().orElseThrow(() -> ResourceNotFoundException.notFound("Driver", orderId));

        if (!orders.getStatus().equals("CONFIRMED")) {
            throw InvalidOrderStateException.invalidState("cannot assign driver to order");
        }

        Delivery delivery = new Delivery();
        delivery.setOrders(orders);
        delivery.setDeliveryDriver(driver);
        delivery.setStatus("ASSIGNED");
        delivery.setAssignedAt(LocalDateTime.now());
        delivery.setCreatedDate(LocalDateTime.now());
        delivery.setUpdatedDate(LocalDateTime.now());
        delivery.setIsActive(true);

        return DeliveryResponseDTO.fromEntity(deliveryRepository.save(delivery));
    }

    public DeliveryDriverResponseDTO updateDriverLocation(Integer driverId, double lat, double lng) {
        DeliveryDriver driver = deliveryDriverRepository.findDriverById(driverId).orElseThrow(() -> ResourceNotFoundException.notFound("Driver", driverId));

        driver.setCurrentLat(lat);
        driver.setCurrentLng(lng);
        driver.setUpdatedDate(LocalDateTime.now());

        return DeliveryDriverResponseDTO.fromEntity(deliveryDriverRepository.save(driver));
    }

}

