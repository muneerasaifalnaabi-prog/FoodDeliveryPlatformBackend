package com.fooddelivery.demo.Repositories;

import com.fooddelivery.demo.Entities.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Integer> {
    @Query("SELECT d FROM Delivery d WHERE d.deliveryDriver.id = :driverId AND d.status = :status AND d.isActive = true")
    List<Delivery> findByDeliveryDriverIdAndStatus(@Param("driverId") Integer driverId, @Param("status") String status);

    @Query("SELECT d FROM Delivery d WHERE d.status = :status AND d.isActive = true")
    List<Delivery> findByStatus(@Param("status") String status);

    @Query(" SELECT d FROM Delivery d WHERE d.deliveryDriver.id = :driverId AND d.isActive = true ")
    List<Delivery> findDeliveriesByDriverId(@Param("driverId") Integer driverId );

    @Query(" SELECT d FROM Delivery d WHERE d.deliveryDriver.id = :driverId AND d.status IN ('ASSIGNED', 'PICKED_UP') AND d.isActive = true ")
    Optional<Delivery> findActiveDeliveryByDriverId(@Param("driverId") Integer driverId );
    @Query("SELECT d FROM Delivery d WHERE d.id = :deliveryId AND d.isActive = true ")
    Optional<Delivery> findDeliveryById( @Param("deliveryId") Integer deliveryId );

    @Query(" SELECT COUNT(d) FROM Delivery d WHERE d.deliveryDriver.id = :driverId AND d.status = 'DELIVERED' AND d.isActive = true ")
    Double countCompletedDeliveries( @Param("driverId") Integer driverId );

    @Query(value = " SELECT AVG( TIMESTAMPDIFF( MINUTE, d.picked_up_at, d.delivered_at ) ) FROM delivery d WHERE d.driver_id = :driverId AND d.status = 'DELIVERED' AND d.is_active = true ", nativeQuery = true)
    Double getAverageDeliveryTime( @Param("driverId") Integer driverId );


}
