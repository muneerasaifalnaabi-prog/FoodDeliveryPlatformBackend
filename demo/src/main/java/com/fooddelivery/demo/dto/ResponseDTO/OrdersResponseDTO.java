package com.fooddelivery.demo.dto.ResponseDTO;

import com.fooddelivery.demo.Entities.Orders;
import com.fooddelivery.demo.dto.SummaryDTO.CustomerSummaryDTO;
import com.fooddelivery.demo.dto.SummaryDTO.DeliverySummaryDTO;
import com.fooddelivery.demo.dto.SummaryDTO.PaymentSummaryDTO;
import com.fooddelivery.demo.dto.SummaryDTO.RestaurantSummaryDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class OrdersResponseDTO {

    private Integer id;
    private String orderCode;
    private LocalDateTime orderDate;
    private String status;
    private BigDecimal subtotal;
    private BigDecimal deliveryFee;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;
    private String deliveryNotes;
    private CustomerSummaryDTO customer;
    private RestaurantSummaryDTO restaurant;
    private DeliverySummaryDTO delivery;
    private PaymentSummaryDTO payment;
    private List<OrderItemResponseDTO> orderItems;

    public static OrdersResponseDTO fromEntity(Orders entity) {
        OrdersResponseDTO dto = new OrdersResponseDTO();

        dto.setId(entity.getId());
        dto.setOrderCode(entity.getOrderCode());
        dto.setOrderDate(entity.getOrderDate());
        dto.setStatus(entity.getStatus());
        dto.setSubtotal(entity.getSubtotal());
        dto.setDeliveryFee(entity.getDeliveryFee());
        dto.setDiscountAmount(entity.getDiscountAmount());
        dto.setTotalAmount(entity.getTotalAmount());
        dto.setDeliveryNotes(entity.getDeliveryNotes());
        dto.setCustomer(CustomerSummaryDTO.fromEntity(entity.getCustomer()));
        dto.setRestaurant(RestaurantSummaryDTO.fromEntity(entity.getRestaurant()));

        if (entity.getDelivery() != null) {
            dto.setDelivery(DeliverySummaryDTO.fromEntity(entity.getDelivery()));
        }

        if (entity.getPayment() != null) {
            dto.setPayment(PaymentSummaryDTO.fromEntity(entity.getPayment()));
        }

        dto.setOrderItems(OrderItemResponseDTO.fromEntityList(entity.getOrderItems()));

        return dto;
    }

    public static List<OrdersResponseDTO> fromEntityList(List<Orders> entities){

        List<OrdersResponseDTO> dtos = new ArrayList<>();
        for (Orders entity : entities) {
            dtos.add(fromEntity(entity));
        }
        return dtos;
    }
}