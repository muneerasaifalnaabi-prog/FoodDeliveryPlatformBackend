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
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

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

    @Autowired
    private DeliveryDriverRepository deliveryRepository;

    //****========
    //Create Order
    //==========****
    public OrdersResponseDTO createOrder(Integer customerId, Integer restaurantId, List<OrderItemRequestDTO> items) {

        if (items == null || items.isEmpty()) {
            throw new InvalidOrderStateException("Order must contain at least one item.");
        }

        Customer customer = customerRepository.findCustomerById(customerId).orElseThrow(() -> ResourceNotFoundException.notFound("Customer", customerId));

        Restaurant restaurant = restaurantRepository.findRestaurantById(restaurantId).orElseThrow(() -> ResourceNotFoundException.notFound("Restaurant", restaurantId));

        Orders order = new Orders();
        order.setCustomer(customer);
        order.setRestaurant(restaurant);
        order.setStatus("PENDING");
        order.setOrderDate(LocalDateTime.now());
        order.setCreatedDate(LocalDateTime.now());
        order.setUpdatedDate(LocalDateTime.now());
        order.setIsActive(true);
        order.setOrderCode(HelperUtils.generateCode("ORD"));

        // ⚠️ IMPORTANT: never leave null
        order.setDeliveryFee(restaurant.getDeliveryFee() != null ? restaurant.getDeliveryFee() : BigDecimal.ZERO);

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;

        for (OrderItemRequestDTO dto : items) {

            MenuItem menuItem = menuItemRepository.findMenuItemById(dto.getMenuItemId()).orElseThrow(() -> ResourceNotFoundException.notFound("Menu Item", dto.getMenuItemId()));

            BigDecimal itemTotal = menuItem.getPrice().multiply(BigDecimal.valueOf(dto.getQuantity()));

            OrderItem item = new OrderItem();
            item.setOrders(order);
            item.setMenuItem(menuItem);
            item.setQuantity(dto.getQuantity());
            item.setUnitPrice(menuItem.getPrice());
            item.setTotalPrice(itemTotal);
            item.setCreatedDate(LocalDateTime.now());
            item.setUpdatedDate(LocalDateTime.now());
            item.setIsActive(true);

            orderItems.add(item);
            subtotal = subtotal.add(itemTotal);
        }

        // ✔ SET EVERYTHING BEFORE SAVE
        order.setOrderItems(orderItems);
        order.setSubtotal(subtotal);

        BigDecimal total = subtotal.add(order.getDeliveryFee()).subtract(BigDecimal.ZERO);

        order.setTotalAmount(total);

        // ✔ SINGLE SAVE ONLY
        Orders savedOrder = ordersRepository.save(order);

        return OrdersResponseDTO.fromEntity(savedOrder);
    }

    //****========
    //Create Order With notesw
    //==========****
    public OrdersResponseDTO createOrder(Integer customerId, Integer restaurantId, List<OrderItemRequestDTO> items, String notes) {

        OrdersResponseDTO response = createOrder(customerId, restaurantId, items);
        Orders order = ordersRepository.findOrderById(response.getId()).orElseThrow(() -> ResourceNotFoundException.notFound("Order", response.getId()));

        order.setDeliveryNotes(notes);
        order.setUpdatedDate(LocalDateTime.now());
        return OrdersResponseDTO.fromEntity(ordersRepository.save(order));
    }

    //****========
    //Add Item
    //==========****
    public OrdersResponseDTO addMenuItemToOrder(Integer orderId, Integer menuItemId, int quantity) {

        Orders order = ordersRepository.findOrderById(orderId).orElseThrow(() -> ResourceNotFoundException.notFound("Order", orderId));
        if (!order.getStatus().equals("PENDING")) {
            throw InvalidOrderStateException.invalidState("Only PENDING orders can be modified.");
        }

        if (quantity <= 0) {
            throw InvalidOrderStateException.invalidState("Quantity must be greater than zero.");
        }

        MenuItem menuItem = menuItemRepository.findMenuItemById(menuItemId).orElseThrow(() -> ResourceNotFoundException.notFound("Menu Item", menuItemId));

        if (!menuItem.getRestaurant().getId().equals(order.getRestaurant().getId())) {
            throw InvalidOrderStateException.invalidState("Menu item belongs to another restaurant.");
        }

        BigDecimal itemTotal = menuItem.getPrice().multiply(BigDecimal.valueOf(quantity));

        OrderItem item = new OrderItem();
        item.setOrders(order);
        item.setMenuItem(menuItem);
        item.setQuantity(quantity);
        item.setUnitPrice(menuItem.getPrice());
        item.setTotalPrice(itemTotal);
        item.setCreatedDate(LocalDateTime.now());
        item.setUpdatedDate(LocalDateTime.now());
        item.setIsActive(true);

        OrderItem savedItem = orderItemRepository.save(item);

        if (order.getOrderItems() == null) {
            order.setOrderItems(new ArrayList<>());
        }

        order.getOrderItems().add(savedItem);
        BigDecimal subtotal = order.getSubtotal() != null ? order.getSubtotal().add(itemTotal) : itemTotal;

        BigDecimal deliveryFee = order.getDeliveryFee() != null ? order.getDeliveryFee() : BigDecimal.ZERO;

        BigDecimal discount = order.getDiscountAmount() != null ? order.getDiscountAmount() : BigDecimal.ZERO;

        BigDecimal total = subtotal.add(deliveryFee).subtract(discount);
        order.setSubtotal(subtotal);
        order.setTotalAmount(total);
        order.setUpdatedDate(LocalDateTime.now());

        return OrdersResponseDTO.fromEntity(ordersRepository.save(order));
    }

    //****========
    //Remove item
    //==========****
    public OrdersResponseDTO removeMenuItemFromOrder(Integer orderId, Integer orderItemId) {

        Orders order = ordersRepository.findOrderById(orderId).orElseThrow(() -> ResourceNotFoundException.notFound("Order", orderId));

        if (!order.getStatus().equals("PENDING")) {
            throw InvalidOrderStateException.invalidState("Only PENDING orders can be modified.");
        }
        OrderItem item = orderItemRepository.findOrderItemById(orderItemId).orElseThrow(() -> ResourceNotFoundException.notFound("OrderItem", orderItemId));

        if (!item.getOrders().getId().equals(orderId)) {
            throw ResourceNotFoundException.notFound("OrderItem", orderItemId);
        }

        if (!(item.getIsActive()).equals(Boolean.TRUE)) {
            throw InvalidOrderStateException.invalidState("Item already removed.");
        }

        item.setIsActive(false);
        item.setUpdatedDate(LocalDateTime.now());
        orderItemRepository.save(item);
        BigDecimal subtotal = order.getSubtotal().subtract(item.getTotalPrice());

        BigDecimal deliveryFee = order.getDeliveryFee() != null ? order.getDeliveryFee() : BigDecimal.ZERO;

        BigDecimal discount = order.getDiscountAmount() != null ? order.getDiscountAmount() : BigDecimal.ZERO;

        BigDecimal total = subtotal.add(deliveryFee).subtract(discount);
        order.setSubtotal(subtotal);
        order.setTotalAmount(total);
        order.setUpdatedDate(LocalDateTime.now());
        return OrdersResponseDTO.fromEntity(ordersRepository.save(order));
    }

    //****========
    //Apply Discount
    //==========****
    public OrdersResponseDTO applyDiscount(Integer orderId, double discountAmount) {

        Orders order = ordersRepository.findOrderById(orderId).orElseThrow(() -> ResourceNotFoundException.notFound("Order", orderId));

        if (discountAmount < 0) {
            throw new IllegalArgumentException("Discount cannot be negative.");
        }

        BigDecimal subtotal = order.getSubtotal() != null ? order.getSubtotal() : BigDecimal.ZERO;

        BigDecimal deliveryFee = order.getDeliveryFee() != null ? order.getDeliveryFee() : BigDecimal.ZERO;

        BigDecimal totalBeforeDiscount = subtotal.add(deliveryFee);
        BigDecimal discount = BigDecimal.valueOf(discountAmount);

        if (discount.compareTo(totalBeforeDiscount) > 0) {
            throw new IllegalArgumentException("Discount exceeds total amount.");
        }

        BigDecimal total = totalBeforeDiscount.subtract(discount);
        order.setDiscountAmount(discount);
        order.setTotalAmount(total);
        order.setUpdatedDate(LocalDateTime.now());

        return OrdersResponseDTO.fromEntity(ordersRepository.save(order));
    }

    //****========
    //Update Status
    //==========****
    public OrdersResponseDTO updateOrderStatus(Integer orderId, String newStatus) {

        List<String> validStatuses = Arrays.asList("PENDING", "PREPARING", "PAID", "ON_THE_WAY", "DELIVERED", "CANCELLED");

        if (!validStatuses.contains(newStatus)) {
            throw InvalidOrderStateException.invalidState("Invalid order status.");
        }

        Orders order = ordersRepository.findOrderById(orderId).orElseThrow(() -> ResourceNotFoundException.notFound("Order", orderId));

        order.setStatus(newStatus);
        order.setUpdatedDate(LocalDateTime.now());
        return OrdersResponseDTO.fromEntity(ordersRepository.save(order));
    }

    //****========
    //Cancel order
    //==========****
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

    //****========
    //Calculate totalorder
    //==========****
    public OrdersResponseDTO calculateOrderTotals(Integer orderId) {
        Orders order = ordersRepository.findOrderById(orderId).orElseThrow(() -> ResourceNotFoundException.notFound("Order", orderId));

        BigDecimal subtotal = BigDecimal.ZERO;
        for (OrderItem item : order.getOrderItems()) {
            if ((item.getIsActive().equals(Boolean.TRUE))) {
                subtotal = subtotal.add(item.getTotalPrice());
            }
        }

        BigDecimal deliveryFee = order.getDeliveryFee() != null ? order.getDeliveryFee() : BigDecimal.ZERO;

        BigDecimal discount = order.getDiscountAmount() != null ? order.getDiscountAmount() : BigDecimal.ZERO;

        BigDecimal total = subtotal.add(deliveryFee).subtract(discount);
        order.setSubtotal(subtotal);
        order.setTotalAmount(total);
        order.setUpdatedDate(LocalDateTime.now());
        return OrdersResponseDTO.fromEntity(ordersRepository.save(order));
    }

    public CorporateOrderResponseDTO placeCorporateOrder(CorporateOrderRequestDTO dto) {

        if (dto.getItems() == null || dto.getItems().isEmpty()) {
            throw InvalidOrderStateException.invalidState("Corporate order must contain at least one item.");
        }

        Restaurant restaurant = restaurantRepository.findRestaurantById(dto.getRestaurantId()).orElseThrow(() -> ResourceNotFoundException.notFound("Restaurant", dto.getRestaurantId()));

        if (!Boolean.TRUE.equals(restaurant.getAcceptingOrders())) {
            throw InvalidOrderStateException.invalidState("Restaurant is not accepting orders.");
        }

        CorporateOrder corporateOrder = new CorporateOrder();

        corporateOrder.setCompanyName(dto.getCompanyName());
        corporateOrder.setCostCenter(dto.getCostCenter());
        corporateOrder.setRestaurant(restaurant);

        corporateOrder.setStatus("PENDING");
        corporateOrder.setCorporateCode(HelperUtils.generateCode("CORP"));
        corporateOrder.setOrderDate(LocalDateTime.now());
        corporateOrder.setCreatedDate(LocalDateTime.now());
        corporateOrder.setUpdatedDate(LocalDateTime.now());
        corporateOrder.setIsActive(true);
        List<OrderItem> items = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;
        for (OrderItemRequestDTO itemDTO : dto.getItems()) {
            if (itemDTO.getQuantity() <= 0) {
                throw InvalidOrderStateException.invalidState("Quantity must be greater than zero.");
            }

            MenuItem menuItem = menuItemRepository.findMenuItemById(itemDTO.getMenuItemId()).orElseThrow(() -> ResourceNotFoundException.notFound("Menu Item", itemDTO.getMenuItemId()));

            if (menuItem.getRestaurant() == null || !menuItem.getRestaurant().getId().equals(dto.getRestaurantId())) {
                throw InvalidOrderStateException.invalidState("Menu item does not belong to restaurant.");
            }

            OrderItem item = new OrderItem();
            item.setCorporateOrder(corporateOrder);
            item.setMenuItem(menuItem);
            item.setQuantity(itemDTO.getQuantity());

            BigDecimal itemTotal = menuItem.getPrice().multiply(BigDecimal.valueOf(itemDTO.getQuantity()));
            item.setTotalPrice(itemTotal);
            item.setCreatedDate(LocalDateTime.now());
            item.setUpdatedDate(LocalDateTime.now());
            item.setIsActive(true);
            items.add(item);
            subtotal = subtotal.add(itemTotal);
        }

        if (subtotal.compareTo(restaurant.getMinOrderAmount()) < 0) {
            throw InvalidOrderStateException.invalidState("Below minimum order amount.");
        }

        corporateOrder.setCorporateOrderItems(items);

        BigDecimal total = subtotal.add(restaurant.getDeliveryFee() != null ? restaurant.getDeliveryFee() : BigDecimal.ZERO);

        corporateOrder.setTotalAmount(total);
        CorporateOrder saved = corporateOrderRepository.save(corporateOrder);
        return CorporateOrderResponseDTO.fromEntity(saved);
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

    public Double getCancellationRate(LocalDateTime from, LocalDateTime to) {
        Integer cancelledOrders = ordersRepository.countCancelledOrders(from, to);
        Integer completedOrders = ordersRepository.countCompletedOrdersBetweenDates(from, to);
        Integer total = cancelledOrders + completedOrders;
        if (total == 0) {
            return 0.0;
        }
        return (cancelledOrders * 100.0) / total;
    }

    public List<Object[]> getBusiestHours() {
        return ordersRepository.getBusiestHours();
    }

    public List<String> getOrderTimeline(Integer orderId) {
        Orders order = ordersRepository.findOrderById(orderId).orElseThrow(() -> ResourceNotFoundException.notFound("Order", orderId));

        List<String> timeline = new ArrayList<>();
        timeline.add("Order Created : " + order.getCreatedDate());

        if (order.getStatus().equals("CONFIRMED")) {
            timeline.add("Order Confirmed");
        }

        if (order.getStatus().equals("PAID")) {
            timeline.add("Payment Completed");
        }

        if (order.getStatus().equals("ON_THE_WAY")) {
            timeline.add("Driver Picked Up Order");
        }
        if (order.getStatus().equals("DELIVERED")) {
            timeline.add("Order Delivered");
        }
        if (order.getStatus().equals("CANCELLED")) {
            timeline.add("Order Cancelled");
        }

        return timeline;
    }

    public OrdersResponseDTO reorder(Integer orderId) {
        Orders oldOrder = ordersRepository.findOrderById(orderId).orElseThrow(() -> ResourceNotFoundException.notFound("Order", orderId));

        Orders newOrder = new Orders();
        newOrder.setOrderCode(HelperUtils.generateCode("ORD"));
        newOrder.setCustomer(oldOrder.getCustomer());
        newOrder.setRestaurant(oldOrder.getRestaurant());
        newOrder.setStatus("PENDING");

        newOrder.setDeliveryNotes(oldOrder.getDeliveryNotes());
        newOrder.setOrderDate(LocalDateTime.now());
        newOrder.setCreatedDate(LocalDateTime.now());
        newOrder.setUpdatedDate(LocalDateTime.now());
        newOrder.setIsActive(true);
        newOrder.setDeliveryFee(oldOrder.getDeliveryFee());

        List<OrderItem> newItems = new ArrayList<>();
        double subtotal = 0.0;
        for (OrderItem oldItem : oldOrder.getOrderItems()) {
            OrderItem item = new OrderItem();
            item.setOrders(newOrder);
            item.setMenuItem(oldItem.getMenuItem());
            item.setQuantity(oldItem.getQuantity());
            item.setUnitPrice(oldItem.getUnitPrice());

            item.setSpecialInstructions(oldItem.getSpecialInstructions());
            double itemTotal = oldItem.getUnitPrice().doubleValue() * oldItem.getQuantity();

            item.setTotalPrice(BigDecimal.valueOf(itemTotal));
            item.setCreatedDate(LocalDateTime.now());
            item.setUpdatedDate(LocalDateTime.now());

            item.setIsActive(true);
            subtotal += itemTotal;
            newItems.add(item);
        }

        newOrder.setSubtotal(BigDecimal.valueOf(subtotal));

        double deliveryFee = newOrder.getDeliveryFee().doubleValue();
        double total = subtotal + deliveryFee;
        newOrder.setTotalAmount(BigDecimal.valueOf(total));
        newOrder.setOrderItems(newItems);
        Orders savedOrder = ordersRepository.save(newOrder);

        return OrdersResponseDTO.fromEntity(savedOrder);
    }


    public Page<OrdersResponseDTO> getCustomerOrdersFiltered(Integer customerId, String status, String from, String to, int page, int size) {

        customerRepository.findCustomerById(customerId).orElseThrow(() -> ResourceNotFoundException.notFound("Customer", customerId));

        Pageable pageable = PageRequest.of(page, size);
        LocalDateTime fromDate = null;
        LocalDateTime toDate = null;

        if (from != null && !from.isBlank()) {
            fromDate = LocalDateTime.parse(from + "T00:00:00");
        }

        if (to != null && !to.isBlank()) {
            toDate = LocalDateTime.parse(to + "T23:59:59");
        }

        Page<Orders> orders = ordersRepository.filterCustomerOrders(customerId, status, fromDate, toDate, pageable);

        List<OrdersResponseDTO> response = new ArrayList<>();

        for (Orders order : orders.getContent()) {
            response.add(OrdersResponseDTO.fromEntity(order));
        }

        return new PageImpl<>(response, pageable, orders.getTotalElements());
    }

    public Map<String, Object> getEstimatedDeliveryTime(Integer orderId) {

        Orders order = ordersRepository.findOrderById(orderId).orElseThrow(() -> ResourceNotFoundException.notFound("Order", orderId));
        Delivery delivery = deliveryRepository.findDeliveryByOrderId(orderId).orElseThrow(() -> ResourceNotFoundException.notFound("Delivery", orderId));

        DeliveryDriver driver = delivery.getDeliveryDriver();

        double restaurantLat = 23.5880;
        double restaurantLng = 58.3829;

        double distance = HelperUtils.calculateDistance(restaurantLat, restaurantLng, driver.getCurrentLat(), driver.getCurrentLng());

        double averageSpeedKmPerHour = 40.0;
        double etaHours = distance / averageSpeedKmPerHour;
        int etaMinutes = (int) (etaHours * 60);
        Map<String, Object> result = new HashMap<>();
        result.put("orderId", order.getId());
        result.put("driverId", driver.getId());
        result.put("distanceKm", distance);
        result.put("estimatedMinutes", etaMinutes);
        result.put("status", order.getStatus());

        return result;
    }
}