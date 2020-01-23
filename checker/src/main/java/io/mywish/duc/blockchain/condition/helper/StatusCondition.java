package io.mywish.duc.blockchain.condition.helper;

import io.mywish.event.service.BaseEventCreator;
import io.mywish.event.service.EventPublisher;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@Slf4j
public class StatusCondition {
    private final long stopIncrementTime = 7200000;
    protected long startAttentionTime;
    protected long attentionTime;
    protected long lastTimeNotify;
    protected long lastTimestamp;
    private final BaseEventCreator eventCreator;

    @Getter
    protected int status;
    @Autowired
    protected final EventPublisher eventPublisher;
    @Getter
    protected final String uri;
    protected String network;
    @Getter
    protected final String uriprefix;
    @Getter
    protected final String urisufix;

    public StatusCondition(long attentionTime, EventPublisher eventPublisher, BaseEventCreator eventCreator, String network, String uriprefix, String urisufix) {
        this.eventPublisher = eventPublisher;
        this.eventCreator = eventCreator;
        this.attentionTime = startAttentionTime = attentionTime;
        this.lastTimestamp = new Date().getTime();
        this.network = network;
        this.uri = uriprefix.concat(urisufix);
        this.uriprefix = uriprefix;
        this.urisufix = urisufix;
    }

    public void updateStatus(int status) {
        this.status = status;

        long timestamp = new Date().getTime();
        if (status == 200) {
            lastTimeNotify = 0;
            this.status = status;
            this.lastTimestamp = timestamp;
            log.info("{} status {} on {} time", this.network, status, new Date(timestamp));
            attentionTime = startAttentionTime;
        } else if (status != 200) {
            lastTimeNotify += (timestamp - lastTimestamp);
            lastTimestamp = timestamp;
            checkTimeToNotify();
            log.info("Incorrect {} status {} almost {} seconds {}", network, status, Math.round(this.lastTimeNotify / 1000), uri);
        }
    }

    private void checkTimeToNotify() {
        if (lastTimeNotify > attentionTime) {
            upTimer();
            int stuckSeconds = Math.round(this.lastTimeNotify / 1000);
            log.warn("Status 200 does not appear for {} seconds on {}", stuckSeconds, network);
            eventPublisher.publish(eventCreator.createEvent(getNotifyMessage()));
        }
    }

    private void upTimer() {
        lastTimeNotify = 0;
        if (attentionTime < stopIncrementTime) {
            attentionTime *= 2;
            if (attentionTime > stopIncrementTime) {
                attentionTime = stopIncrementTime;
            }
        }
    }

    private String getNotifyMessage() {
        StringBuilder message = new StringBuilder();
        message.append(String.format("\"Can't connect to %s.", uri));
        if (status != -1) {
            message.append(String.format("Code status %d", status));
        }
        return message.toString();
    }
}
