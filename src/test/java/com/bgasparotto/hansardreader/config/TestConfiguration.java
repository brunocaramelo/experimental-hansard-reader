package com.bgasparotto.hansardreader.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Configuration
@ComponentScan("com.bgasparotto.spring.kafka.avro.test")
public class TestConfiguration {
}
