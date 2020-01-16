package io.mywish.bot;

import io.mywish.bot.service.*;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.telegram.telegrambots.ApiContext;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.bots.DefaultBotOptions;

@ComponentScan
@Configuration
@PropertySource("classpath:bot.properties")
@EntityScan("io.mywish.bot.model")
@EnableJpaRepositories
public class BotModule {
    static {
        ApiContextInitializer.init();
    }

    @Value("${io.crash.bot.http-proxy:#{null}}")
    private String proxy;

    @ConditionalOnProperty(name = "io.bot.is-ros-com-nadzor", havingValue = "false", matchIfMissing = true)
    @Bean
    @Scope("prototype")
    public TelegramBotsApi telegramBotsApi() {
        return new TelegramBotsApi();
    }

    @ConditionalOnProperty(name = "io.bot.is-ros-com-nadzor", havingValue = "false", matchIfMissing = true)
    @Bean
    public CrashBot crashBot() {
        DefaultBotOptions botOptions = ApiContext.getInstance(DefaultBotOptions.class);
        if (proxy != null) {
            botOptions.setRequestConfig(
                    RequestConfig
                            .custom()
                            .setProxy(HttpHost.create(proxy))
                            .setAuthenticationEnabled(false)
                            .build()
            );
        }
        return new CrashBot(botOptions);
    }

    @ConditionalOnProperty(name = "io.mywish.is-ros-com-nadzor", havingValue = "false", matchIfMissing = true)
    @Bean
    public CrashBotDev crashBotDev() {
        DefaultBotOptions botOptions = ApiContext.getInstance(DefaultBotOptions.class);
        if (proxy != null) {
            botOptions.setRequestConfig(
                    RequestConfig
                            .custom()
                            .setProxy(HttpHost.create(proxy))
                            .setAuthenticationEnabled(false)
                            .build()
            );
        }
        return new CrashBotDev(botOptions);
    }

    @Bean
    public ChatPersister chatPersister() {
        return new ChatDbPersister();
    }

    @ConditionalOnBean({CrashBot.class, CrashBotDev.class})
    @Bean
    public BotIntegration botIntegration() {
        return new BotIntegration();
    }
}
