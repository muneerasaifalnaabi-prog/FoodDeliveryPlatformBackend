package com.fooddelivery.demo.Services;

import com.fooddelivery.demo.Entities.Customer;
import com.fooddelivery.demo.Entities.DeliveryDriver;
import com.fooddelivery.demo.Exceptions.ResourceNotFoundException;
import com.fooddelivery.demo.Repositories.CustomerRepository;
import com.fooddelivery.demo.Repositories.DeliveryDriverRepository;
import com.fooddelivery.demo.Repositories.OrdersRepository;
import com.fooddelivery.demo.Repositories.RestaurantRepository;
import com.fooddelivery.demo.dto.ResponseDTO.CustomerResponseDTO;
import com.fooddelivery.demo.dto.ResponseDTO.DeliveryDriverResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportingService {

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private DeliveryDriverRepository deliveryDriverRepository;

    public Double getRestaurantRevenueByDate(Integer restaurantId, LocalDate date) {

        restaurantRepository.findRestaurantById(restaurantId)
                .orElseThrow(() -> ResourceNotFoundException.notFound("Restaurant", restaurantId));

        LocalDateTime startDate = date.atStartOfDay();
        LocalDateTime endDate = date.atTime(LocalTime.MAX);
        Double revenue = ordersRepository.getRestaurantRevenueByDate(restaurantId, startDate, endDate);
        return revenue != null ? revenue : 0.0;
    }

    public Integer countRestaurantOrders(Integer restaurantId) {

        restaurantRepository.findRestaurantById(restaurantId)
                .orElseThrow(() -> ResourceNotFoundException.notFound("Restaurant", restaurantId));
        Integer totalOrders = ordersRepository.countRestaurantOrders(restaurantId);

        return totalOrders != null ? totalOrders : 0;
    }

    public List<CustomerResponseDTO> findTopLoyaltyCustomers() {

        Pageable pageable = PageRequest.of(0, 10);

        List<Customer> customers = customerRepository.findTopLoyaltyCustomers(pageable);
        List<CustomerResponseDTO> response = new ArrayList<>();
        for (Customer customer : customers) {
            response.add(CustomerResponseDTO.fromEntity(customer));
        }
        return response;
    }

    public List<DeliveryDriverResponseDTO> getTop10DriverLeaderboard() {

        Pageable pageable = PageRequest.of(0, 10);

        List<DeliveryDriver> drivers = deliveryDriverRepository.getTop10DriverLeaderboard(pageable);

        List<DeliveryDriverResponseDTO> response = new ArrayList<>();

        for (DeliveryDriver driver : drivers) {
            response.add(DeliveryDriverResponseDTO.fromEntity(driver));
        }
        return response;
    }

    public Map<String, Object> getPlatformDailySummary(LocalDate date) {

        Integer totalOrders = ordersRepository.countDailyOrders(date);
        BigDecimal deliveryFees = ordersRepository.getPlatformDailySummary(date);

        Map<String, Object> result = new HashMap<>();
        result.put("date", date);
        result.put("totalOrders", totalOrders != null ? totalOrders : 0);
        result.put("deliveryFees", deliveryFees != null ? deliveryFees : BigDecimal.ZERO);
        return result;
    }

    public Map<String, Object> getRestaurantRevenue(Integer restaurantId, LocalDate from, LocalDate to) {

        restaurantRepository.findRestaurantById(restaurantId)
                .orElseThrow(() -> ResourceNotFoundException.notFound("Restaurant", restaurantId));

        LocalDateTime fromDate = from.atStartOfDay();
        LocalDateTime toDate = to.atTime(LocalTime.MAX);
        Double revenue = ordersRepository.getRestaurantRevenueBetweenDates(restaurantId, fromDate, toDate);
        Integer totalOrders = ordersRepository.countRestaurantOrdersBetweenDates(restaurantId, fromDate, toDate);

        Map<String, Object> result = new HashMap<>();
        result.put("restaurantId", restaurantId);
        result.put("from", from);
        result.put("to", to);
        result.put("revenue", revenue != null ? revenue : 0.0);
        result.put("totalOrders", totalOrders != null ? totalOrders : 0);

        return result;
    }

    public Map<String, Object> getDriverEarnings(Integer driverId, LocalDate from, LocalDate to) {

        deliveryDriverRepository.findDriverById(driverId)
                .orElseThrow(() -> ResourceNotFoundException.notFound("Driver", driverId));

        LocalDateTime fromDate = from.atStartOfDay();
        LocalDateTime toDate = to.atTime(LocalTime.MAX);
        Double earnings = ordersRepository.getDriverEarnings(driverId, fromDate, toDate);

        Map<String, Object> result = new HashMap<>();
        result.put("driverId", driverId);
        result.put("from", from);
        result.put("to", to);
        result.put("earnings", earnings != null ? earnings : 0.0);

        return result;
    }

    public Map<String, Object> getCancellationRate(LocalDate from, LocalDate to) {

        LocalDateTime fromDate = from.atStartOfDay();
        LocalDateTime toDate = to.atTime(LocalTime.MAX);
        Integer cancelledOrders = ordersRepository.countCancelledOrders(fromDate, toDate);
        Integer completedOrders = ordersRepository.countCompletedOrders(fromDate, toDate);

        int cancelled = cancelledOrders != null ? cancelledOrders : 0;
        int completed = completedOrders != null ? completedOrders : 0;
        int total = cancelled + completed;
        double cancellationRate = total == 0
                ? 0
                : ((double) cancelled / total) * 100;

        Map<String, Object> result = new HashMap<>();
        result.put("from", from);
        result.put("to", to);
        result.put("cancelledOrders", cancelled);
        result.put("completedOrders", completed);
        result.put("cancellationRate", cancellationRate);

        return result;
    }

    public List<Object[]> getBusiestHours() {
        return ordersRepository.getBusiestHours();
    }
}