package io.mywish.duc.blockchain.condition.implementation;

import io.mywish.duc.blockchain.condition.StatusConditional;
import io.mywish.duc.blockchain.condition.helper.StatusCondition;
import io.mywish.event.service.DevConnectionCrushEventCreator;
import io.mywish.event.service.EventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DucatusXStatusCondition implements StatusConditional {
    private final StatusCondition condition;

    public DucatusXStatusCondition(
            @Value("${io.mywish.ducX.blockchain.attention.status.time}") long attentionTime,
            EventPublisher eventPublisher,
            @Autowired DevConnectionCrushEventCreator eventCreator,
            @Value("${io.mywish.ducX.blockchain.uri}") String uri,
            @Value("${io.mywish.ducX.blockchain.uri.sufix}") String suffix) {
        this.condition = new StatusCondition(attentionTime, eventPublisher, eventCreator, "DucatusX", uri, suffix);
    }

    @Override
    public void updateCondition(int status) {
        this.condition.updateStatus(status);
    }

    @Override
    public String getUri() {
        return this.condition.getUriprefix();
    }

    @Override
    public String getSufix() {
        return this.condition.getUrisufix();
    }
}
