package com.vapor.daisybot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class DaisyBot extends TelegramLongPollingBot {

    @Autowired
    DefaultBotOptions botOptions;

    public DaisyBot(DefaultBotOptions botOptions) {
        super(botOptions, "6524420386:AAEIZziA-v5zn0pDCvhDjUi5rSORPn7ErcM");
    }

    @Override
    public void onUpdateReceived(Update update) {

        String text = update.getMessage().getText();
        System.out.println(text);

    }

    @Override
    public String getBotUsername() {
        return "Daisy2_bot";
    }
}
