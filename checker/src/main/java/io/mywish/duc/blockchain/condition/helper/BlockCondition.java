package io.mywish.duc.blockchain.condition.helper;

import io.mywish.event.model.DucatusStuckEvent;
import io.mywish.event.service.EventPublisher;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class BlockCondition {
    protected long startAttentionTime;
    protected long attentionTime;
    @Getter
    protected int lastBlock;
    protected long lastTimestamp;
    protected long changeBlockTime;
    protected EventPublisher eventPublisher;
    @Getter
    protected String network;

    public BlockCondition(long attentionTime, EventPublisher eventPublisher, String network) {
        this.eventPublisher = eventPublisher;
        this.attentionTime = startAttentionTime = attentionTime;
        this.lastTimestamp = new Date().getTime();
        this.network = network;
    }

    public void updateLastBlock(int lastBlock) {
        long timestamp = new Date().getTime();
        if (lastBlock > this.lastBlock) {
            changeBlockTime = 0;
            this.lastBlock = lastBlock;
            this.lastTimestamp = timestamp;
            log.info("New {} block {} save on {} time", network, lastBlock, new Date(timestamp));
            attentionTime = startAttentionTime;
        } else if (lastBlock == this.lastBlock) {
            changeBlockTime += (timestamp - lastTimestamp);
            lastTimestamp = timestamp;
            checkTime();
            log.info("Already old {} block {} almost {} seconds", network, lastBlock, Math.round(this.changeBlockTime / 1000));
        }
    }

    private void checkTime() {
        if (changeBlockTime > attentionTime) {
            int stuckSeconds = Math.round(this.changeBlockTime / 1000);
            log.warn("A new block {} does not appear for {} seconds", network, stuckSeconds);
            attentionTime *= 2;
            eventPublisher.publish(
                    new DucatusStuckEvent(
                            String.format("A new block {} does not appear for %d seconds", network, stuckSeconds)
                    ));
        }
    }
}
