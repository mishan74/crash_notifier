package io.mywish.duc.blockchain.service.implemented;

import io.mywish.duc.blockchain.condition.implementation.DucatusWServiceStatusCondition;
import io.mywish.duc.blockchain.service.Checkable;
import io.mywish.duc.blockchain.service.helper.StatusChecker;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DucatusWServiceStatusChecker implements Checkable {
    private final StatusChecker statusChecker;

    public DucatusWServiceStatusChecker(
            @Autowired DucatusWServiceStatusCondition statusCondition,
            @Autowired CloseableHttpClient closeableHttpClient) {
        this.statusChecker = new StatusChecker(statusCondition, closeableHttpClient);
    }

    @Scheduled(cron = "${io.mywish.duc.blockchain.cron.sheduler}")
    public void doCheck() {
        this.statusChecker.doConnect();
    }
}
