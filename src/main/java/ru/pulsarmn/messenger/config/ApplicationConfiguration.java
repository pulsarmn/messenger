package ru.pulsarmn.messenger.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import java.time.Clock;


@Configuration
@EnableSpringDataWebSupport
public class ApplicationConfiguration {

    @Bean
    Clock defaultClock() {
        return Clock.systemUTC();
    }
}
