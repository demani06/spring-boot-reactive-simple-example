package com.deepak.spring_boot_reactive_simple_example.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class PaymentResilienceTestConfig {

    @Bean
    CircuitBreaker paymentCircuitBreaker() {
        return CircuitBreaker.ofDefaults("paymentProvider");
    }
}
