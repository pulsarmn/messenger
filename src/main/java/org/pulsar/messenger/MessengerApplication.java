package org.pulsar.messenger;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class MessengerApplication {

    static void main(String[] args) {
        SpringApplication.run(MessengerApplication.class, args);
    }
}
