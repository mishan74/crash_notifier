package io.mywish.duc.blockchain.model;

import io.mywish.event.model.DucatusStuckEvent;
import io.mywish.event.service.EventPublisher;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Getter
@Slf4j
public class DucatusCondition {
    private long startAttentionTime;
    private long attentionTime;
    private int lastBlock;
    private long lastTimestamp;
    private long changeBlockTime;
    @Autowired
    private final EventPublisher eventPublisher;

    public DucatusCondition(@Value("${io.mywish.duc.blockchain.attention.time}") long attentionTime, EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
        this.attentionTime = startAttentionTime = attentionTime;
    }

    public int getLastBlock() {
        return lastBlock;
    }

    public void setConditions(int lastBlock) {
        long timestamp = new Date().getTime();
        if (lastBlock > this.lastBlock) {
            changeBlockTime = 0;
            this.lastBlock = lastBlock;
            this.lastTimestamp = timestamp;
            log.info("New Ducatus block {} save on {} time", lastBlock, new Date(timestamp));
            attentionTime = startAttentionTime;
        } else if (lastBlock == this.lastBlock) {
            changeBlockTime += (timestamp - lastTimestamp);
            lastTimestamp = timestamp;
            checkTime();
            log.info("Already old Ducatus block {} almost {} seconds", lastBlock, Math.round(this.changeBlockTime / 1000));
        }
    }

    private void checkTime() {
        if (changeBlockTime > attentionTime) {
            int stuckSeconds = Math.round(this.changeBlockTime / 1000);
            log.warn("A new block does not appear for {} seconds", stuckSeconds);
            attentionTime *= 2;
            eventPublisher.publish(
                    new DucatusStuckEvent(
                            String.format("A new block does not appear for %d seconds", stuckSeconds)
                    ));
        }
    }
}
