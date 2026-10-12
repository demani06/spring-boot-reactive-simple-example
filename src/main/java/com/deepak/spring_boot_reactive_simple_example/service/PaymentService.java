package com.deepak.spring_boot_reactive_simple_example.service;

import com.deepak.spring_boot_reactive_simple_example.client.FlakyPaymentClient;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PaymentService {


    private final FlakyPaymentClient flakyPaymentClient;
    private final CircuitBreaker paymentCircuitBreaker;

    public PaymentService(FlakyPaymentClient flakyPaymentClient, CircuitBreaker paymentCircuitBreaker) {
        this.flakyPaymentClient = flakyPaymentClient;
        this.paymentCircuitBreaker = paymentCircuitBreaker;
    }

    public Mono<String> submitPayment() {

       return flakyPaymentClient.submitPayment()
               .transformDeferred(CircuitBreakerOperator.of(paymentCircuitBreaker))
               .onErrorResume(e -> Mono.just("Payment provider temporary unavailable"));



    }
}
