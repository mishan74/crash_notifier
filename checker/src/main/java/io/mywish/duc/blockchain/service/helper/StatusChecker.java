package io.mywish.duc.blockchain.service.helper;

import io.mywish.duc.blockchain.condition.StatusConditional;
import lombok.SneakyThrows;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;

public class StatusChecker {
    private final StatusConditional satusCondition;
    private final String uri;
    private final String suffix;
    private final CloseableHttpClient client;

    public StatusChecker(StatusConditional satusCondition, CloseableHttpClient client) {
        this.satusCondition = satusCondition;
        this.uri = satusCondition.getUri();
        this.suffix = satusCondition.getSufix();
        this.client = client;
    }

    @SneakyThrows
    public void doConnect() {
        try (CloseableHttpResponse response = client.execute(HttpHost.create(uri), new HttpGet(suffix))) {
            if (response != null) {
                int code = response.getStatusLine().getStatusCode();
                satusCondition.updateCondition(code);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
