package com.deepak.spring_boot_reactive_simple_example.controller;

import com.deepak.spring_boot_reactive_simple_example.client.FlakyPaymentClient;
import com.deepak.spring_boot_reactive_simple_example.config.PaymentResilienceTestConfig;
import com.deepak.spring_boot_reactive_simple_example.service.PaymentService;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;

@WebFluxTest(controllers = PaymentController.class)
@Import({
        PaymentResilienceTestConfig.class,
        PaymentService.class
})
class PaymentControllerComponentTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private FlakyPaymentClient flakyPaymentClient;

    @Autowired
    private CircuitBreaker paymentCircuitBreaker;

    @BeforeEach
    void setUp() {
        paymentCircuitBreaker.reset();
        reset(flakyPaymentClient);
    }

    @Test
    void shouldReturnSuccessWhenProviderAcceptsPayment() {
        when(flakyPaymentClient.submitPayment())
                .thenReturn(Mono.just("Payment accepted by provider"));

        webTestClient.get()
                .uri("/demo/payment")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("Payment accepted by provider");

        verify(flakyPaymentClient).submitPayment();
    }

    @Test
    void shouldReturnFailureWhenProviderNotAcceptsPayment() {
        when(flakyPaymentClient.submitPayment()).thenReturn(Mono.error(new RuntimeException("Payment not accepted by provider")));

        webTestClient.get()
                .uri("/demo/payment")
                .exchange()
                .expectStatus().isEqualTo(HttpStatusCode.valueOf(200))
                .expectBody(String.class)
                .isEqualTo("Payment provider temporary unavailable");

        verify(flakyPaymentClient).submitPayment();
    }
}