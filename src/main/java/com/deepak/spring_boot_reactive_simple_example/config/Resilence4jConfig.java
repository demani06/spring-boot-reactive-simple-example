package com.deepak.spring_boot_reactive_simple_example.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.micrometer.tagged.TaggedCircuitBreakerMetrics;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class Resilence4jConfig {

    /*@Bean
    CircuitBreaker paymentCircuitBreaker(){
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .slidingWindowSize(10)
                .minimumNumberOfCalls(5)
                .failureRateThreshold(50)
                .waitDurationInOpenState(Duration.ofSeconds(10))
                .build();

        var cb =  CircuitBreaker.of("paymentCircuitBreaker", circuitBreakerConfig);

        cb.getEventPublisher()
                .onStateTransition(event -> {
                    System.out.println("CB STATE = " + event.getStateTransition());
                })
                .onFailureRateExceeded(event -> {
                    System.out.println("CB FAILURE RATE = " + event.getFailureRate());
                })
                .onCallNotPermitted(event -> {
                    System.out.println("CB REJECTED , Circuit is open !!!!!!!!!!! " );
                });

        return cb;
    }
*/

    @Bean
    CircuitBreakerRegistry circuitBreakerRegistry(){
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .slidingWindowSize(10)
                .minimumNumberOfCalls(5)
                .failureRateThreshold(50)
                .waitDurationInOpenState(Duration.ofSeconds(10))
                .build();

       return CircuitBreakerRegistry.of(circuitBreakerConfig);

    }

    @Bean
    CircuitBreaker paymentCircuitBreaker(CircuitBreakerRegistry circuitBreakerRegistry) {
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .slidingWindowSize(10)
                .minimumNumberOfCalls(5)
                .failureRateThreshold(50)
                .waitDurationInOpenState(Duration.ofSeconds(10))
                .build();

        //var cb =  CircuitBreaker.of("paymentCircuitBreaker", circuitBreakerConfig);

        /*cb.getEventPublisher()
                .onStateTransition(event -> {
                    System.out.println("CB STATE = " + event.getStateTransition());
                })
                .onFailureRateExceeded(event -> {
                    System.out.println("CB FAILURE RATE = " + event.getFailureRate());
                })
                .onCallNotPermitted(event -> {
                    System.out.println("CB REJECTED , Circuit is open !!!!!!!!!!! " );
                });*/

        return circuitBreakerRegistry.circuitBreaker("paymentCircuitBreaker");
    }




    @Bean
    MeterBinder circuitBreakerMetrics(CircuitBreakerRegistry circuitBreakerRegistry){
        return meterRegistry -> TaggedCircuitBreakerMetrics.ofCircuitBreakerRegistry(circuitBreakerRegistry)
                .bindTo(meterRegistry);
    }





}
