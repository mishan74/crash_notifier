package io.mywish.duc.blockchain.condition.helper;

import io.mywish.event.service.BaseEventCreator;
import io.mywish.event.service.EventPublisher;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class StatusCondition {
    private final long stopIncrementTime = 7200000;
    private final long startAttentionTime;
    private final String allias;
    private final String network;
    @Getter
    private final String uriprefix;
    @Getter
    private final String urisufix;
    @Getter
    private final String uri;
    private final BaseEventCreator eventCreator;
    private final EventPublisher eventPublisher;

    private long attentionTime;
    private long lastTimeNotify;
    private long lastTimestamp;
    @Getter
    private int status;

    public StatusCondition(long attentionTime, EventPublisher eventPublisher, BaseEventCreator eventCreator, String network, String uriprefix, String urisufix, String allias) {
        this.eventPublisher = eventPublisher;
        this.eventCreator = eventCreator;
        this.attentionTime = startAttentionTime = attentionTime;
        this.lastTimestamp = new Date().getTime();
        this.network = network;
        this.uri = uriprefix.concat(urisufix);
        this.uriprefix = uriprefix;
        this.urisufix = urisufix;
        this.allias = allias;
    }

    public void updateStatus(int status) {
        this.status = status;

        long timestamp = new Date().getTime();
        if (status == 200) {
            lastTimeNotify = 0;
            this.status = status;
            this.lastTimestamp = timestamp;
            log.info("{} by {} network status {} on {} time", allias, network, status, new Date(timestamp));
            attentionTime = startAttentionTime;
        } else if (status != 200) {
            lastTimeNotify += (timestamp - lastTimestamp);
            lastTimestamp = timestamp;
            checkTimeToNotify();
            log.info("On {} status {} almost {} seconds {}", allias, status, Math.round(this.lastTimeNotify / 1000), uri);
        }
    }

    private void checkTimeToNotify() {
        if (lastTimeNotify > attentionTime) {
            upTimer();
            int stuckSeconds = Math.round(this.lastTimeNotify / 1000);
            log.warn("Status 200 does not appear for {} seconds on {}, {} network", stuckSeconds, allias, network);
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
        message.append(String.format("\"Can't connect to %s by url:%s.", allias, uri));
        if (status != -1) {
            message.append(String.format(" Code status %d", status));
        }
        return message.toString();
    }
}
