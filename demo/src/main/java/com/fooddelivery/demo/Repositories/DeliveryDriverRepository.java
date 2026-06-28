package com.fooddelivery.demo.Repositories;

import com.fooddelivery.demo.Entities.Delivery;
import com.fooddelivery.demo.Entities.DeliveryDriver;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DeliveryDriverRepository extends JpaRepository<DeliveryDriver, Integer> {
    @Query("SELECT d FROM DeliveryDriver d WHERE d.id = :driverId AND d.isActive = true ")
    Optional<DeliveryDriver> findDriverById(@Param("driverId") Integer driverId);

    @Query(" SELECT d FROM DeliveryDriver d WHERE d.isOnline = true AND d.isActive = true ")
    List<DeliveryDriver> findFirstAvailableOnlineDriver();

    @Query(" SELECT d FROM DeliveryDriver d WHERE d.email = :email AND d.isActive = true ")
    Optional<DeliveryDriver> findDriverByEmail(@Param("email") String email);

    @Query(" SELECT d FROM DeliveryDriver d WHERE d.isActive = true ")
    List<DeliveryDriver> findAllActiveDrivers();

    @Query(" SELECT d FROM DeliveryDriver d WHERE d.isOnline = true AND d.isActive = true ")
    List<DeliveryDriver> findOnlineDrivers();

    /*@Query(" SELECT d FROM delivery_driver d JOIN delivery dl ON d.id = dl.driver_id WHERE d.is_active = true AND dl.is_active = true AND dl.status = 'DELIVERED' GROUP BY d.id ORDER BY COUNT(dl.id) DESC LIMIT 10 ")
    List<DeliveryDriver> getTop10DriverLeaderboard();

     */

    @Query("SELECT d FROM Delivery d WHERE d.orders.id = :orderId AND d.isActive = true")
    Optional<Delivery> findDeliveryByOrderId(@Param("orderId") Integer orderId);


    @Query("SELECT dl.deliveryDriver FROM Delivery dl WHERE dl.status = 'DELIVERED' AND dl.isActive = true GROUP BY dl.deliveryDriver ORDER BY COUNT(dl.id) DESC")
    List<DeliveryDriver> getTop10DriverLeaderboard(Pageable pageable);


}
