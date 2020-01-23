package io.mywish.duc.blockchain.service.implemented;

import io.mywish.duc.blockchain.condition.implementation.DucatusBitcoreStatusCondition;
import io.mywish.duc.blockchain.service.Checkable;
import io.mywish.duc.blockchain.service.helper.StatusChecker;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DucatusBitcoreStatusChecker implements Checkable {
    private final StatusChecker statusChecker;

    public DucatusBitcoreStatusChecker(
            @Autowired DucatusBitcoreStatusCondition statusCondition,
            @Autowired CloseableHttpClient closeableHttpClient) {
        this.statusChecker = new StatusChecker(statusCondition, closeableHttpClient);
    }

    @Scheduled(cron = "${io.mywish.duc.blockchain.cron.sheduler}")
    public void doCheck() {
        this.statusChecker.doConnect();
    }

}
