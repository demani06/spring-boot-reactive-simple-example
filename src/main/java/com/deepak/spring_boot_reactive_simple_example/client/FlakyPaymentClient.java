package com.deepak.spring_boot_reactive_simple_example.client;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class FlakyPaymentClient {

    public Mono<String> submitPayment(){

        return Mono.defer(() -> {

            int outcome = ThreadLocalRandom.current().nextInt(10);

            if(outcome < 3){
                //Downstream failure
                return Mono.error(new RuntimeException("Payment provider unavailble"));
            }

            if(outcome < 5){
                return Mono.delay(Duration.ofSeconds(4))
                        .map(_ -> "Payment accepted by provider");
            }

            return Mono.just("Payment accepted by provider");


        });

    }
}
