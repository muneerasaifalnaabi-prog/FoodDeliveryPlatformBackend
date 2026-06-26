package com.fooddelivery.demo.Services;

import com.fooddelivery.demo.Entities.*;
import com.fooddelivery.demo.Exceptions.InvalidOrderStateException;
import com.fooddelivery.demo.Exceptions.ResourceNotFoundException;
import com.fooddelivery.demo.Repositories.*;
import com.fooddelivery.demo.Utils.HelperUtils;
import com.fooddelivery.demo.dto.RequestDTO.CorporateOrderRequestDTO;
import com.fooddelivery.demo.dto.RequestDTO.OrderItemRequestDTO;
import com.fooddelivery.demo.dto.ResponseDTO.CorporateOrderResponseDTO;
import com.fooddelivery.demo.dto.ResponseDTO.OrdersResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private MenuItemRepository menuItemRepository;
    @Autowired
    private CorporateOrderRepository corporateOrderRepository;

    public OrdersResponseDTO createOrder(Integer customerId, Integer restaurantId, List<OrderItemRequestDTO> items) {
        Customer customer = customerRepository.findCustomerById(customerId).orElseThrow(() -> ResourceNotFoundException.notFound("Restaurant", customerId));
        Restaurant restaurant = restaurantRepository.findRestaurantById(restaurantId).orElseThrow(() -> ResourceNotFoundException.notFound("Restaurant", restaurantId));

        if (items == null || items.isEmpty()) {
            throw new InvalidOrderStateException("Order must contain at least one item.");
        }

        Orders order = new Orders();
        order.setCustomer(customer);
        order.setRestaurant(restaurant);
        order.setStatus("PENDING");
        order.setOrderDate(LocalDateTime.now());
        order.setCreatedDate(LocalDateTime.now());
        order.setUpdatedDate(LocalDateTime.now());
        order.setIsActive(true);
        order.setOrderCode(HelperUtils.generateCode("ORD"));

        Orders savedOrder = ordersRepository.save(order);


        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;

        for (OrderItemRequestDTO dto : items) {
            if (dto.getQuantity() <= 0) {
                throw InvalidOrderStateException.invalidState("Quantity must be greater than zero.");
            }

            MenuItem menuItem = menuItemRepository.findMenuItemById(dto.getMenuItemId()).orElseThrow(() -> ResourceNotFoundException.notFound("Menu Item", dto.getMenuItemId()));

            if (!menuItem.getRestaurant().getId().equals(restaurantId)) {
                throw InvalidOrderStateException.invalidState("All menu items must belong to the same restaurant.");
            }

            OrderItem item = new OrderItem();
            item.setOrders(savedOrder);
            item.setMenuItem(menuItem);
            item.setQuantity(dto.getQuantity());
            BigDecimal itemPrice = HelperUtils.calculateItemTotal(menuItem.getPrice(), dto.getQuantity());
            item.setTotalPrice(itemPrice);
            item.setCreatedDate(LocalDateTime.now());
            item.setUpdatedDate(LocalDateTime.now());
            item.setIsActive(true);
            subtotal = subtotal.add(itemPrice);

            orderItemRepository.save(item);
            orderItems.add(item);
        }

        if (subtotal.compareTo(restaurant.getMinOrderAmount()) < 0) {
            throw new InvalidOrderStateException("Order does not meet the minimum order amount.");
        }
        BigDecimal total = subtotal.add(restaurant.getDeliveryFee());
        savedOrder.setSubtotal(subtotal);
        savedOrder.setTotalAmount(total);
        savedOrder.setOrderItems(orderItems);
        savedOrder.setUpdatedDate(LocalDateTime.now());

        Orders updatedOrder = ordersRepository.save(savedOrder);
        return OrdersResponseDTO.fromEntity(updatedOrder);
    }

    public OrdersResponseDTO createOrder(Integer customerId, Integer restaurantId, List<OrderItemRequestDTO> items, String notes) {

        if (items == null || items.isEmpty()) {
            throw new InvalidOrderStateException("Order must contain at least one item.");
        }

        Customer customer = customerRepository.findCustomerById(customerId).orElseThrow(() -> ResourceNotFoundException.notFound("Customer", customerId));
        Restaurant restaurant = restaurantRepository.findRestaurantById(restaurantId).orElseThrow(() -> ResourceNotFoundException.notFound("Restaurant", restaurantId));

        if (!restaurant.getAcceptingOrders()) {
            throw InvalidOrderStateException.invalidState("Restaurant is currently not accepting orders.");
        }

        Orders order = new Orders();
        order.setCustomer(customer);
        order.setRestaurant(restaurant);
        order.setStatus("PENDING");
        order.setDeliveryNotes(notes);
        order.setOrderDate(LocalDateTime.now());
        order.setCreatedDate(LocalDateTime.now());
        order.setUpdatedDate(LocalDateTime.now());
        order.setIsActive(true);
        order.setOrderCode(HelperUtils.generateCode("ORD"));

        Orders savedOrder = ordersRepository.save(order);


        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;

        for (OrderItemRequestDTO dto : items) {
            if (dto.getQuantity() <= 0) {
                throw InvalidOrderStateException.invalidState("Quantity must be greater than zero.");
            }

            MenuItem menuItem = menuItemRepository.findMenuItemById(dto.getMenuItemId()).orElseThrow(() -> ResourceNotFoundException.notFound("Menu Item", dto.getMenuItemId()));

            if (!menuItem.getRestaurant().getId().equals(restaurantId)) {
                throw InvalidOrderStateException.invalidState("All menu items must belong to the same restaurant.");
            }

            OrderItem item = new OrderItem();
            item.setOrders(savedOrder);
            item.setMenuItem(menuItem);
            item.setQuantity(dto.getQuantity());
            BigDecimal itemPrice = HelperUtils.calculateItemTotal(menuItem.getPrice(), dto.getQuantity());
            item.setTotalPrice(itemPrice);
            item.setCreatedDate(LocalDateTime.now());
            item.setUpdatedDate(LocalDateTime.now());
            item.setIsActive(true);
            subtotal = subtotal.add(itemPrice);

            orderItemRepository.save(item);
            orderItems.add(item);
        }

        if (subtotal.compareTo(restaurant.getMinOrderAmount()) < 0) {
            throw new InvalidOrderStateException("Order does not meet the minimum order amount.");
        }
        BigDecimal total = subtotal.add(restaurant.getDeliveryFee());
        savedOrder.setSubtotal(subtotal);
        savedOrder.setTotalAmount(total);
        savedOrder.setOrderItems(orderItems);
        savedOrder.setUpdatedDate(LocalDateTime.now());

        Orders updatedOrder = ordersRepository.save(savedOrder);
        return OrdersResponseDTO.fromEntity(updatedOrder);
    }


    public OrdersResponseDTO addMenuItemToOrder(Integer orderId, Integer menuItemId, int quantity) {
        Orders order = ordersRepository.findOrderById(orderId).orElseThrow(() -> ResourceNotFoundException.notFound("Order", orderId));
        MenuItem menuItem = menuItemRepository.findMenuItemById(menuItemId).orElseThrow(() -> ResourceNotFoundException.notFound("Menu Item", menuItemId));

        if (!(order.getStatus()).equals("PENDING")) {
            throw InvalidOrderStateException.invalidState("Cannot add items to an order that is not PENDING. Current status: " + order.getStatus());
        }
        OrderItem item = new OrderItem();
        item.setOrders(order);
        item.setMenuItem(menuItem);
        item.setQuantity(quantity);
        BigDecimal itemPrice = menuItem.getPrice().multiply(BigDecimal.valueOf(quantity));
        item.setTotalPrice(itemPrice);
        item.setCreatedDate(LocalDateTime.now());
        item.setUpdatedDate(LocalDateTime.now());
        item.setIsActive(true);

        orderItemRepository.save(item);
        order.getOrderItems().add(item);
        BigDecimal updatedTotal = order.getTotalAmount().add(itemPrice);
        order.setTotalAmount(updatedTotal);
        order.setUpdatedDate(LocalDateTime.now());
        return OrdersResponseDTO.fromEntity(ordersRepository.save(order));
    }

    public OrdersResponseDTO removeMenuItemFromOrder(Integer orderId, Integer orderItemId) {

        Orders order = ordersRepository.findOrderById(orderId).orElseThrow(() -> ResourceNotFoundException.notFound("Order", orderId));

        //only PENDING orders can have items removed
        if (!(order.getStatus()).equals("PENDING")) {
            throw InvalidOrderStateException.invalidState("Cannot remove items from an order that is not PENDING. Current status: " + order.getStatus());
        }
        OrderItem item = orderItemRepository.findOrderItemById(orderItemId).orElseThrow(() -> ResourceNotFoundException.notFound("OrderItem", orderItemId));

        // verify this item actually belongs to the order the caller specified —
        if (item.getOrders() == null || !item.getOrders().getId().equals(orderId)) {
            throw ResourceNotFoundException.notFound("OrderItem", orderItemId);
        }

        //  an already-removed item can't be removed again — prevents
        if (!(item.getIsActive()).equals(Boolean.TRUE)) {
            throw ResourceNotFoundException.notFound("OrderItem", orderItemId);
        }

        item.setIsActive(false);
        item.setUpdatedDate(LocalDateTime.now());
        orderItemRepository.save(item);

        BigDecimal updatedTotal = order.getTotalAmount().subtract(item.getTotalPrice());
        order.setTotalAmount(updatedTotal);
        order.setUpdatedDate(LocalDateTime.now());

        return OrdersResponseDTO.fromEntity(ordersRepository.save(order));
    }

    public OrdersResponseDTO applyDiscount(Integer orderId, double discountAmount) {

        Orders order = ordersRepository.findOrderById(orderId).orElseThrow(() -> ResourceNotFoundException.notFound("Order", orderId));

        //  reject a negative discount outright — that would be a surcharge,not a discount, and this method isn't meant to increase the price.
        if (discountAmount < 0) {
            throw new IllegalArgumentException("Discount amount cannot be negative.");
        }

        double subtotal = order.getSubtotal().doubleValue();
        double deliveryFee = order.getRestaurant().getDeliveryFee().doubleValue();

        // a discount can never exceed what's actually owed before it's applied — otherwise the resulting total goes negative.
        double maxDiscount = subtotal + deliveryFee;
        if (discountAmount > maxDiscount) {
            throw new IllegalArgumentException("Discount amount " + discountAmount + " exceeds the order's payable total of " + maxDiscount);
        }

        double total = HelperUtils.calculateTotal(subtotal, deliveryFee, discountAmount);

        //store the discount on the order, not just bake it into the total.
        order.setDiscountAmount(BigDecimal.valueOf(discountAmount));
        order.setTotalAmount(BigDecimal.valueOf(total));
        order.setUpdatedDate(LocalDateTime.now());

        return OrdersResponseDTO.fromEntity(ordersRepository.save(order));
    }

    public OrdersResponseDTO updateOrderStatus(Integer orderId, String newStatus) {
        Orders order = ordersRepository.findOrderById(orderId).orElseThrow(() -> ResourceNotFoundException.notFound("Order", orderId));

        order.setStatus(newStatus);
        order.setUpdatedDate(LocalDateTime.now());
        return OrdersResponseDTO.fromEntity(ordersRepository.save(order));
    }

    public String cancelOrder(Integer orderId) {
        Orders order = ordersRepository.findOrderById(orderId).orElseThrow(() -> ResourceNotFoundException.notFound("Order", orderId));

        if (!order.getStatus().equals("PENDING")) {
            throw InvalidOrderStateException.invalidState("Only pending orders can be cancelled.");
        }
        order.setStatus("CANCELLED");
        order.setUpdatedDate(LocalDateTime.now());

        ordersRepository.save(order);
        return "Order cancelled successfully";
    }

    public OrdersResponseDTO calculateOrderTotals(Integer orderId) {

        Orders order = ordersRepository.findOrderById(orderId).orElseThrow(() -> ResourceNotFoundException.notFound("Order", orderId));
        //calculate subtotal price by getTotalPrice ,if discount amount = null,make it 0
        BigDecimal subtotal = BigDecimal.ZERO;
        for (OrderItem item : order.getOrderItems()) {
            if ((item.getIsActive()).equals(Boolean.TRUE)) {
                subtotal = subtotal.add(item.getTotalPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            }
        }

        BigDecimal deliveryFee = order.getRestaurant().getDeliveryFee();
        BigDecimal discount = order.getDiscountAmount() != null ? order.getDiscountAmount() : BigDecimal.ZERO;

        BigDecimal total = subtotal.add(deliveryFee).subtract(discount);

        order.setSubtotal(subtotal);
        order.setTotalAmount(total);
        order.setUpdatedDate(LocalDateTime.now());

        return OrdersResponseDTO.fromEntity(ordersRepository.save(order));
    }

    //******** check this method later **********
    public CorporateOrderResponseDTO placeCorporateOrder(CorporateOrderRequestDTO dto) {

        if (dto.getItems() == null || dto.getItems().isEmpty()) {
            throw InvalidOrderStateException.invalidState("Corporate order must contain at least one item.");
        }

        Restaurant restaurant = restaurantRepository.findRestaurantById(dto.getRestaurantId()).orElseThrow(() -> ResourceNotFoundException.notFound("Restaurant", dto.getRestaurantId()));

        if (!Boolean.TRUE.equals(restaurant.getAcceptingOrders())) {
            throw InvalidOrderStateException.invalidState("Restaurant is currently not accepting orders.");
        }

        CorporateOrder corporateOrder = new CorporateOrder();
        corporateOrder.setCompanyName(dto.getCompanyName());
        corporateOrder.setCostCenter(dto.getCostCenter());
        corporateOrder.setRestaurant(restaurant);
        corporateOrder.setStatus("PENDING");
        corporateOrder.setCorporateCode(HelperUtils.generateCode("CORP"));
        corporateOrder.setCreatedDate(LocalDateTime.now());
        corporateOrder.setUpdatedDate(LocalDateTime.now());
        corporateOrder.setIsActive(true);

        CorporateOrder savedOrder = corporateOrderRepository.save(corporateOrder);
        List<OrderItem> orderItems = new ArrayList<>();

        BigDecimal subtotal = BigDecimal.ZERO;
        for (OrderItemRequestDTO itemDTO : dto.getItems()) {
            if (itemDTO.getQuantity() <= 0) {
                throw InvalidOrderStateException.invalidState("Quantity must be greater than zero.");
            }

            MenuItem menuItem = menuItemRepository.findMenuItemById(itemDTO.getMenuItemId()).orElseThrow(() -> ResourceNotFoundException.notFound("Menu Item", itemDTO.getMenuItemId()));

            if (!menuItem.getRestaurant().getId().equals(dto.getRestaurantId())) {
                throw InvalidOrderStateException.invalidState("All items must belong to the same restaurant.");
            }

            OrderItem item = new OrderItem();
            item.setCorporateOrder(savedOrder);
            item.setMenuItem(menuItem);
            item.setQuantity(itemDTO.getQuantity());

            BigDecimal itemPrice = HelperUtils.calculateItemTotal(menuItem.getPrice(), itemDTO.getQuantity());
            item.setTotalPrice(itemPrice);
            item.setCreatedDate(LocalDateTime.now());
            item.setUpdatedDate(LocalDateTime.now());
            item.setIsActive(true);

            subtotal = subtotal.add(itemPrice);
            // CorporateOrderRepository.sa
            orderItems.add(item);
        }

        if (subtotal.compareTo(restaurant.getMinOrderAmount()) < 0) {
            throw InvalidOrderStateException.invalidState("Corporate order does not meet minimum order amount.");
        }

        BigDecimal total = subtotal.add(restaurant.getDeliveryFee());

        savedOrder.setTotalAmount(total);
        savedOrder.setCorporateOrderItems(orderItems);
        savedOrder.setUpdatedDate(LocalDateTime.now());

        return CorporateOrderResponseDTO.fromEntity(corporateOrderRepository.save(savedOrder));

    }

    public OrdersResponseDTO getOrderById(Integer orderId) {
        Orders order = ordersRepository.findOrderById(orderId).orElseThrow(() -> ResourceNotFoundException.notFound("Order", orderId));
        return OrdersResponseDTO.fromEntity(order);
    }

    public List<OrdersResponseDTO> getRestaurantOrders(Integer restaurantId, String status) {
        Restaurant restaurant = restaurantRepository.findRestaurantById(restaurantId).orElseThrow(() -> ResourceNotFoundException.notFound("Restaurant", restaurantId));
        List<Orders> orders = ordersRepository.findOrdersByRestaurantAndStatus(restaurant.getId(), status);
        return OrdersResponseDTO.fromEntity(orders);
    }
    public Double getCancellationRate( Date from, Date to ) {
        Integer cancelledOrders = ordersRepository.countCancelledOrders( from, to );
        Integer completedOrders = ordersRepository .countCompletedOrdersBetweenDates( from, to );
        Integer total = cancelledOrders + completedOrders;
        if (total == 0) {
            return 0.0;
        }
        return (cancelledOrders * 100.0) / total;
    }

    public List<Object[]> getBusiestHours() {
        return ordersRepository .getBusiestHours();
    }






}
