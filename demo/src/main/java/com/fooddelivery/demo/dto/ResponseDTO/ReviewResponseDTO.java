package com.fooddelivery.demo.dto.ResponseDTO;

import com.fooddelivery.demo.Entities.Review;
import com.fooddelivery.demo.dto.SummaryDTO.CustomerSummaryDTO;
import com.fooddelivery.demo.dto.SummaryDTO.DeliveryDriverSummaryDTO;
import com.fooddelivery.demo.dto.SummaryDTO.RestaurantSummaryDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ReviewResponseDTO {
    private Integer id;
    private String targetType;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
    private CustomerSummaryDTO customer;
    private RestaurantSummaryDTO restaurant;
    private DeliveryDriverSummaryDTO driver;

    public static ReviewResponseDTO fromEntity(Review entity) {

        ReviewResponseDTO dto = new ReviewResponseDTO();

        dto.setId(entity.getId());
        dto.setTargetType(entity.getTargetType());
        dto.setRating(entity.getRating());
        dto.setComment(entity.getComment());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setCustomer(CustomerSummaryDTO.fromEntity(entity.getCustomer()));
        if (entity.getRestaurant() != null) {
            dto.setRestaurant(RestaurantSummaryDTO.fromEntity(entity.getRestaurant()));
        }
        if (entity.getDeliveryDriver() != null) {
            dto.setDriver(DeliveryDriverSummaryDTO.fromEntity(entity.getDeliveryDriver()));
        }
        return dto;
    }

    public static List<ReviewResponseDTO> fromEntityList(List<Review> entities) {

        List<ReviewResponseDTO> dtos = new ArrayList<>();

        for (Review entity : entities) {
            dtos.add(fromEntity(entity));
        }
        return dtos;
    }
}
