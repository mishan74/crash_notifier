package io.mywish.duc.blockchain.condition.helper;

import io.mywish.event.model.ConnectionCrushEvent;
import io.mywish.event.service.EventPublisher;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@Slf4j
public class StatusCondition {
    protected long startAttentionTime;
    protected long attentionTimer;
    protected long crusTimer;
    protected long lastTimestamp;
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

    public StatusCondition(long attentionTime, EventPublisher eventPublisher, String network, String uriprefix, String urisufix) {
        this.eventPublisher = eventPublisher;
        this.attentionTimer = startAttentionTime = attentionTime;
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
            crusTimer = 0;
            this.status = status;
            this.lastTimestamp = timestamp;
            log.info("{} status {} on {} time", this.network, status, new Date(timestamp));
            attentionTimer = startAttentionTime;
        } else if (status != 200) {
            crusTimer += (timestamp - lastTimestamp);
            lastTimestamp = timestamp;
            checkTime();
            log.info("Incorrect {} status {} almost {} seconds {}", network, status, Math.round(this.crusTimer / 1000), uri);
        }
    }

    private void checkTime() {
        if (crusTimer > attentionTimer) {
            int stuckSeconds = Math.round(this.crusTimer / 1000);
            log.warn("Status 200 does not appear for {} seconds on {}", stuckSeconds, network);
            attentionTimer *= 2;
            eventPublisher.publish(
                    new ConnectionCrushEvent(
                            String.format("Can't connect to %s. Code status %d", uri, status)
                    ));
        }
    }
}
