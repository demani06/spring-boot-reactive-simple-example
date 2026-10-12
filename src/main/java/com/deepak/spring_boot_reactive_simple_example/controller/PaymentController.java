package com.deepak.spring_boot_reactive_simple_example.controller;

import com.deepak.spring_boot_reactive_simple_example.service.PaymentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/demo/payment")
    public Mono<String> submitPayment() {
        return paymentService.submitPayment();
    }
}
