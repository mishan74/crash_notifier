package io.mywish.duc.blockchain.model;

import io.mywish.event.model.ConnectionCrushEvent;
import io.mywish.event.service.EventPublisher;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class DucatusStatusCondition {
    private final long startAttentionTime;
    private long attentionTimer;
    private long crusTimer;
    private long lastTimestamp;
    @Getter
    private int status;
    @Autowired
    private final EventPublisher eventPublisher;
    private String uri;

    public  DucatusStatusCondition(@Value("${io.mywish.duc.blockchain.attention.status.time}") long attentionTime, EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
        this.attentionTimer = startAttentionTime = attentionTime;
    }

    public void setCondition(int status, String uri) {
        this.uri = uri;
        this.status = status;
        if (status != 200 && status > 0) {
            log.warn("Status code is {}", status);
            eventPublisher.publish(
                    new ConnectionCrushEvent(
                            String.format("Can't connect to %s. Code status %d", uri, status)
                    ));
        }

        long timestamp = new Date().getTime();
        if (status == 200) {
            crusTimer = 0;
            this.status = status;
            this.lastTimestamp = timestamp;
            log.info("Ducatus status {} on {} time", status, new Date(timestamp));
            attentionTimer = startAttentionTime;
        } else if (status != 200) {
            crusTimer += (timestamp - lastTimestamp);
            lastTimestamp = timestamp;
            checkTime();
            log.info("Already incorrect Ducatus status {} almost {} seconds", status, Math.round(this.crusTimer / 1000));
        }
    }
    private void checkTime() {
        if (crusTimer > attentionTimer) {
            int stuckSeconds = Math.round(this.crusTimer / 1000);
            log.warn("Status 200 does not appear for {} seconds", stuckSeconds);
            attentionTimer *= 2;
            eventPublisher.publish(
                    new ConnectionCrushEvent(
                            String.format("Can't connect to %s. Code status %d", uri, status)
                    ));
        }
    }
}
