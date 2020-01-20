package io.mywish.duc.blockchain;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.*;

@ComponentScan
@Configuration
@PropertySource("classpath:bitcore.properties")
public class BitcoreModule {
    @Bean
    CloseableHttpClient closeableHttpClient() {
        return HttpClientBuilder.create()
                .setMaxConnTotal(100)
                .build();
    }
}
