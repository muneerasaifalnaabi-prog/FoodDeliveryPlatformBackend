package com.fooddelivery.demo.Controllers;

import com.fooddelivery.demo.Services.OrderService;
import com.fooddelivery.demo.Services.PaymentService;
import com.fooddelivery.demo.dto.RequestDTO.PaymentRequestDTO;
import com.fooddelivery.demo.dto.ResponseDTO.PaymentResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/payments")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private OrderService orderService;

    @PostMapping("/order/{orderId}")
    public ResponseEntity<PaymentResponseDTO> processPayment(@PathVariable Integer orderId, @RequestParam String method) {
        return new ResponseEntity<>(paymentService.processPayment(orderId, method), HttpStatus.CREATED);
    }
    @PutMapping("/{paymentId}/complete")
    public ResponseEntity<PaymentResponseDTO> completePayment( @PathVariable Integer paymentId ) {
        return ResponseEntity.ok( paymentService.completePayment( paymentId ) );
    }
    @PutMapping("/{paymentId}/refund")
    public ResponseEntity<PaymentResponseDTO> refundPayment( @PathVariable Integer paymentId ) {
        return ResponseEntity.ok( paymentService.refundPayment( paymentId ) );
    }
    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentResponseDTO> getPaymentByOrderId( @PathVariable Integer orderId ) {
        return ResponseEntity.ok( paymentService.getPaymentByOrderId( orderId ) ); }





}
