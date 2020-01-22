package io.mywish.duc.blockchain.condition.helper;

import io.mywish.event.service.BaseEventCreator;
import io.mywish.event.service.EventPublisher;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class BlockCondition {
    private final long stopIncrementTime = 7200000;
    protected long startAttentionTime;
    protected long attentionTime;
    @Getter
    protected int lastBlock;
    protected long lastTimestamp;
    protected long changeBlockTime;
    protected EventPublisher eventPublisher;
    private final BaseEventCreator eventCreator;
    @Getter
    protected String network;

    public BlockCondition(long attentionTime, EventPublisher eventPublisher, BaseEventCreator eventCreator, String network) {
        this.eventPublisher = eventPublisher;
        this.eventCreator = eventCreator;
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
            checkTimeToNotify();
            log.info("Already old {} block {} almost {} seconds", network, lastBlock, Math.round(this.changeBlockTime / 1000));
        }
    }

    private void checkTimeToNotify() {
        if (changeBlockTime > attentionTime) {
            int stuckSeconds = Math.round(this.changeBlockTime / 1000);
            log.warn("A new block {} does not appear for {} seconds", network, stuckSeconds);
            upTimer();
            String message = String.format("A new block {} does not appear for %d seconds", network, stuckSeconds);
            eventPublisher.publish(eventCreator.createEvent(message));
        }
    }

    private void upTimer() {
        if (attentionTime < stopIncrementTime) {
            attentionTime *= 2;
            if (attentionTime > stopIncrementTime) {
                attentionTime = stopIncrementTime;
            }
        }
    }
}
