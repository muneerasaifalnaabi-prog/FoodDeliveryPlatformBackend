package com.fooddelivery.demo.Services;

import com.fooddelivery.demo.Entities.Orders;
import com.fooddelivery.demo.Entities.Payment;
import com.fooddelivery.demo.Exceptions.InvalidOrderStateException;
import com.fooddelivery.demo.Exceptions.ResourceNotFoundException;
import com.fooddelivery.demo.Repositories.OrdersRepository;
import com.fooddelivery.demo.Repositories.PaymentRepository;
import com.fooddelivery.demo.dto.ResponseDTO.PaymentResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PaymentService {
    @Autowired private PaymentRepository paymentRepository;
    @Autowired
    private OrdersRepository ordersRepository;

    public PaymentResponseDTO processPayment(Integer orderId, String method ) {
        Orders order = ordersRepository.findById(orderId).orElseThrow(() -> ResourceNotFoundException.notFound( "Order", orderId ) );
        if (order.getStatus().equals("PENDING")) {
            throw new InvalidOrderStateException( "Only pending orders can be paid." );
        }
        Payment payment=new Payment();
        payment.setOrders(order);
        payment.setPaymentMethod(method);
        payment.setAmount(order.getTotalAmount());
        payment.setStatus("PAID");
        payment.setProcessedAt(LocalDateTime.now());
        payment.setCreatedDate(LocalDateTime.now());
        payment.setUpdatedDate(LocalDateTime.now());
        payment.setIsActive(true);

        order.setStatus("PAID");
        order.setUpdatedDate(LocalDateTime.now());
        ordersRepository.save(order);
        return PaymentResponseDTO.fromEntity(paymentRepository.save(payment));

    }
    public PaymentResponseDTO refundPayment( Integer orderId ) {
        Payment payment =paymentRepository.findPaymentByOrderId( orderId ).orElseThrow(() -> ResourceNotFoundException.notFound( "Payment", orderId ) );
        if (payment.getStatus().equals("PAID")) {
            throw new InvalidOrderStateException( "Only pending orders can be refund." );
        }
        payment.setStatus("REFUNDED");
        payment.setUpdatedDate(LocalDateTime.now());
        payment.setIsActive(false);
        return PaymentResponseDTO.fromEntity(paymentRepository.save(payment));
    }
    public PaymentResponseDTO completePayment( Integer paymentId ) {
       Payment payment = paymentRepository.findPaymentById( paymentId ).orElseThrow(() -> ResourceNotFoundException.notFound( "Payment", paymentId ) );
       payment.setStatus("COMPLETED");
       payment.setUpdatedDate(LocalDateTime.now());
       payment.setProcessedAt(LocalDateTime.now());
       return PaymentResponseDTO.fromEntity(paymentRepository.save(payment));
    }

    public PaymentResponseDTO getPaymentByOrderId( Integer orderId ) {
        Orders orders = ordersRepository.findById(orderId).orElseThrow(() -> ResourceNotFoundException.notFound( "Order", orderId ) );
        Payment payment = paymentRepository.findPaymentByOrderId(orders.getId()).orElseThrow(() -> ResourceNotFoundException.notFound( "Payment", orders.getId() ) );
        return PaymentResponseDTO.fromEntity(payment);
    }
    public Page<PaymentResponseDTO> getPayments(String method, String status, Date from, Date to, int page, int size ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Payment> payments = paymentRepository .filterPayments( method, status, from, to, pageable );
        List<PaymentResponseDTO> response = new ArrayList<>();
        for (Payment payment : payments.getContent()) {
            response.add( PaymentResponseDTO .fromEntity(payment) );
        }
        return new PageImpl<>( response, pageable, payments.getTotalElements() );
    }
    public List<Object[]> getPaymentAnalyticsByMethod() {
        return paymentRepository .getPaymentAnalyticsByMethod();
    }



}
