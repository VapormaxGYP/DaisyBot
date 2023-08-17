package com.vapor.daisybot;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;

@Component
public class BotConfig {

    @Bean
    public DefaultBotOptions botOptions(){
        DefaultBotOptions botOptions = new DefaultBotOptions();
        botOptions.setProxyHost("127.0.0.1");
        botOptions.setProxyPort(7890);
        botOptions.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);

        return botOptions;
    }
}
