package com.brunocaramelo.hansardreader.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Configuration
@ComponentScan("com.brunocaramelo.spring.kafka.avro.test")
public class TestConfiguration {
}
