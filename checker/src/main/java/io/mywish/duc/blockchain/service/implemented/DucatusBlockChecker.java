package io.mywish.duc.blockchain.service.implemented;

import io.mywish.duc.blockchain.condition.implementation.DucatusBlockCondition;
import io.mywish.duc.blockchain.service.Checkable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DucatusBlockChecker implements Checkable {
    private final io.mywish.duc.blockchain.service.helper.DucatusBlockChecker ducatusBlockChecker;

    public DucatusBlockChecker(
            @Autowired DucatusBlockCondition condition,
            @Value("${io.mywish.duc.blockchain.uri}") String uri,
            @Value("${io.mywish.duc.blockchain.uri.sufix}") String suffix) {
        this.ducatusBlockChecker = new io.mywish.duc.blockchain.service.helper.DucatusBlockChecker(
                condition,
                uri,
                suffix);
    }

    @Override
    @Scheduled(cron = "${io.mywish.duc.blockchain.cron.sheduler}")
    public void doCheck() {
        this.ducatusBlockChecker.doRead();
    }
}
