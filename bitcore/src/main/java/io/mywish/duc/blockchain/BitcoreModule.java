package io.mywish.duc.blockchain;

import io.mywish.duc.blockchain.service.BitcoreNodeReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@ComponentScan
@Configuration
@PropertySource("classpath:bitcore.properties")
public class BitcoreModule {
    @Bean
    BitcoreNodeReader bitcoreReader() {
        return new BitcoreNodeReader();
    }
}
