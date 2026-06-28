package com.fooddelivery.demo.Services;

import com.fooddelivery.demo.Entities.Delivery;
import com.fooddelivery.demo.Entities.DeliveryDriver;
import com.fooddelivery.demo.Entities.Orders;
import com.fooddelivery.demo.Exceptions.DuplicateResourceException;
import com.fooddelivery.demo.Exceptions.InvalidOrderStateException;
import com.fooddelivery.demo.Exceptions.ResourceNotFoundException;
import com.fooddelivery.demo.Repositories.DeliveryDriverRepository;
import com.fooddelivery.demo.Repositories.DeliveryRepository;
import com.fooddelivery.demo.Repositories.OrdersRepository;
import com.fooddelivery.demo.Repositories.ReviewRepository;
import com.fooddelivery.demo.Utils.HelperUtils;
import com.fooddelivery.demo.dto.RequestDTO.DeliveryDriverRequestDTO;
import com.fooddelivery.demo.dto.ResponseDTO.DeliveryDriverResponseDTO;
import com.fooddelivery.demo.dto.ResponseDTO.DeliveryResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class DeliveryService {
    @Autowired
    private DeliveryRepository deliveryRepository;
    @Autowired
    private DeliveryDriverRepository deliveryDriverRepository;
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    ReviewRepository reviewRepository;

    //****========
    //assign Driver to order manually
    //==========****
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

    //****========
    //auto assign Driver to order
    //==========****
    public DeliveryResponseDTO autoAssignDriver(Integer orderId) {
        Orders orders = ordersRepository.findById(orderId).orElseThrow(() -> ResourceNotFoundException.notFound("Order", orderId));
        List<DeliveryDriver> drivers = deliveryDriverRepository.findFirstAvailableOnlineDriver();
        if (drivers.isEmpty()) {
            throw ResourceNotFoundException.notFound( "Online Driver", orderId );
        } DeliveryDriver driver = drivers.get(0);

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

    //****========
    //update driver location
    //==========****
    public DeliveryDriverResponseDTO updateDriverLocation(Integer driverId, double lat, double lng) {
        DeliveryDriver driver = deliveryDriverRepository.findDriverById(driverId).orElseThrow(() -> ResourceNotFoundException.notFound("Driver", driverId));

        driver.setCurrentLat(lat);
        driver.setCurrentLng(lng);
        driver.setUpdatedDate(LocalDateTime.now());

        return DeliveryDriverResponseDTO.fromEntity(deliveryDriverRepository.save(driver));
    }

    //****========
    //mark delivery picked up

    public DeliveryResponseDTO markDeliveryPickedUp(Integer deliveryId) {

        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(() -> ResourceNotFoundException.notFound("Delivery", deliveryId));

        if (!delivery.getStatus().equals("ASSIGNED")) {
            throw InvalidOrderStateException.invalidState("Delivery must be ASSIGNED before pickup.");
        }

        delivery.setStatus("PICKED_UP");
        delivery.setPickedUpAt(LocalDateTime.now());
        delivery.setUpdatedDate(LocalDateTime.now());
        Orders orders = delivery.getOrders();
        orders.setStatus("ON_THE_WAY");

        ordersRepository.save(orders);
        return DeliveryResponseDTO.fromEntity(deliveryRepository.save(delivery));
    }


    public DeliveryResponseDTO markDeliveryDelivered(Integer deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(() -> ResourceNotFoundException.notFound("Delivery", deliveryId));
        if (!delivery.getStatus().equals("PICKED_UP")) {
            throw InvalidOrderStateException.invalidState( "Delivery must be PICKED_UP before delivery." );
        }
        delivery.setStatus("DELIVERED");
        delivery.setDeliveredAt(LocalDateTime.now());
        deliveryRepository.save(delivery);
        Orders orders = delivery.getOrders();
        orders.setStatus("DELIVERED");
        ordersRepository.save(orders);
        ordersRepository.save(orders);
        return DeliveryResponseDTO.fromEntity(deliveryRepository.save(delivery));
    }

    public List<DeliveryResponseDTO> getDeliveriesForDriver(Integer driverId, String status) {
        DeliveryDriver driver = deliveryDriverRepository.findDriverById(driverId).orElseThrow(() -> ResourceNotFoundException.notFound("Driver", driverId));

        List<Delivery> deliveries = deliveryRepository.findByDeliveryDriverIdAndStatus(driverId, status);
        return DeliveryResponseDTO.fromEntity(deliveries);

    }

    //****========
    //make driver online
    //==========****
    public String toggleDriverOnlineStatus(Integer driverId, boolean isOnline) {
        DeliveryDriver driver = deliveryDriverRepository.findDriverById(driverId).orElseThrow(() -> ResourceNotFoundException.notFound("Driver", driverId));
        driver.setIsOnline(isOnline);
        driver.setUpdatedDate(LocalDateTime.now());
        deliveryDriverRepository.save(driver);
        if (isOnline) {
            return "Driver is now ONLINE";
        }
        return "Driver is now OFFLINE";
    }

    //****========
    //Create Driver
    //==========****
    public DeliveryDriverResponseDTO createDriver(DeliveryDriverRequestDTO dto) {
        Optional<DeliveryDriver> existingDriver = deliveryDriverRepository.findDriverByEmail(dto.getEmail());

        if (existingDriver.isPresent()) {
            throw DuplicateResourceException.alreadyExists("Driver email", dto.getEmail());
        }

        DeliveryDriver driver = dto.toEntity();
        driver.setDriverCode(HelperUtils.generateCode("DRV"));
        driver.setCreatedDate(LocalDateTime.now());
        driver.setUpdatedDate(LocalDateTime.now());
        driver.setIsActive(true);

        DeliveryDriver savedDriver = deliveryDriverRepository.save(driver);
        return DeliveryDriverResponseDTO.fromEntity(savedDriver);
    }

    //****========
    //get all driver
    //==========****
    public List<DeliveryDriverResponseDTO> getAllDrivers() {
        List<DeliveryDriver> drivers = deliveryDriverRepository.findAllActiveDrivers();
        return DeliveryDriverResponseDTO.fromEntity(drivers);
    }

    //****========
    //Get online Driver
    //==========****
    public List<DeliveryDriverResponseDTO> getOnlineDrivers() {
        List<DeliveryDriver> drivers = deliveryDriverRepository.findOnlineDrivers();
        return DeliveryDriverResponseDTO.fromEntity(drivers);

    }

    public List<DeliveryResponseDTO> getDriverDeliveries(Integer driverId) {
        DeliveryDriver driver = deliveryDriverRepository.findDriverById(driverId).orElseThrow(() -> ResourceNotFoundException.notFound("Driver", driverId));
        List<Delivery> deliveries = deliveryRepository.findDeliveriesByDriverId(driver.getId());
        return DeliveryResponseDTO.fromEntity(deliveries);
    }

    public DeliveryResponseDTO getActiveDriverDelivery(Integer driverId) {
        DeliveryDriver driver = deliveryDriverRepository.findDriverById(driverId).orElseThrow(() -> ResourceNotFoundException.notFound("Driver", driverId));
        Delivery delivery = deliveryRepository.findActiveDeliveryByDriverId(driver.getId()).orElseThrow(() -> new ResourceNotFoundException("No active delivery found for this driver."));
        return DeliveryResponseDTO.fromEntity(delivery);
    }

    //****========
    //Get Delivery By Id
    //==========****
    public DeliveryResponseDTO getDeliveryById(Integer deliveryId) {
        Delivery delivery = deliveryRepository.findDeliveryById(deliveryId).orElseThrow(() -> ResourceNotFoundException.notFound("Delivery", deliveryId));
        return DeliveryResponseDTO.fromEntity(delivery);
    }

    //****========
    //Get delivery by status
    //==========****
    public List<DeliveryResponseDTO> getDeliveriesByStatus(String status) {
        List<Delivery> deliveries = deliveryRepository.findByStatus(status);
        return DeliveryResponseDTO.fromEntity(deliveries);
    }

    //****========
    //Get Near driver
    //==========****
    public List<DeliveryDriverResponseDTO> getNearbyDrivers(double lat, double lng, double radiusKm) {
        List<DeliveryDriver> drivers = deliveryDriverRepository.findOnlineDrivers();
        List<DeliveryDriverResponseDTO> response = new ArrayList<>();
        for (DeliveryDriver driver : drivers) {
            double distance = HelperUtils.calculateDistance(lat, lng, driver.getCurrentLat(), driver.getCurrentLng());
            if (distance <= radiusKm) {
                response.add(DeliveryDriverResponseDTO.fromEntity(driver));
            }
        }
        return response;
    }

    //****========
    //Get driver performance
    //==========****
    public Map<String, Object> getDriverPerformance( Integer driverId ) {
        deliveryDriverRepository.findDriverById(driverId) .orElseThrow(() -> ResourceNotFoundException.notFound( "Driver", driverId ) );
     Double completedDeliveries = deliveryRepository.countCompletedDeliveries(driverId);
    Double averageDeliveryTime = deliveryRepository.getAverageDeliveryTime(driverId);
    Double averageRating = reviewRepository.getDriverAverageRating(driverId);
    // prevent null values
    if (completedDeliveries == null) {
        completedDeliveries = 0.0;
    } if (averageDeliveryTime == null) {
        averageDeliveryTime = 0.0;
    } if (averageRating == null) {
        averageRating = 0.0;
    } Map<String, Object> performance = new HashMap<>();
    performance.put( "completedDeliveries",
    completedDeliveries );
    performance.put( "averageDeliveryTimeMinutes", averageDeliveryTime );
    performance.put( "averageRating", averageRating );
    return performance;
}

    //****========
    //get Driver Earnings
    //==========****
    public Double getDriverEarnings(Integer driverId, Date from, Date to) {
        Double earnings = deliveryRepository.getDriverEarnings(driverId, from, to);
        return earnings != null ? earnings : 0.0;
    }
}

