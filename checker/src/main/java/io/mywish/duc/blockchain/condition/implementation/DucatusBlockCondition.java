package io.mywish.duc.blockchain.condition.implementation;

import io.mywish.duc.blockchain.condition.BlockConditional;
import io.mywish.duc.blockchain.condition.helper.BlockCondition;
import io.mywish.event.service.DucatusStuckEventCreator;
import io.mywish.event.service.EventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DucatusBlockCondition implements BlockConditional {
    private final BlockCondition blockCondition;

    public DucatusBlockCondition(
            @Value("${io.mywish.duc.blockchain.attention.time}") long attentionTime,
            @Autowired EventPublisher eventPublisher,
            @Autowired DucatusStuckEventCreator eventCreator
    ) {
        this.blockCondition = new BlockCondition(attentionTime, eventPublisher, eventCreator, "Ducatus");
    }

    public void updateCondition(int lastBlock) {
        this.blockCondition.updateLastBlock(lastBlock);
    }

    @Override
    public String getNetwork() {
        return this.blockCondition.getNetwork();
    }

    @Override
    public int getLastBlock() {
        return this.blockCondition.getLastBlock();
    }
}
