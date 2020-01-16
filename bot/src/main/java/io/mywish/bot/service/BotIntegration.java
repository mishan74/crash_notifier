package io.mywish.bot.service;

import io.mywish.event.model.DucatusStuckEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;

@Slf4j
public class BotIntegration {
    @Autowired
    private CrashBot bot;

    @Autowired
    private CrashBotDev botDev;

    @EventListener
    public void onDucatusStuck(final DucatusStuckEvent event) {
        log.info("find Ducatus stuck event. Send message");
        String message = event.getMessage();
        bot.onDucatusStuck(message);
        botDev.onDucatusStuck(message);
    }
}
