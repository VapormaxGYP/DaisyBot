package com.vapor.daisybot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
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
        log.info("Get from bot: {}", text);

        sendMessage(update.getMessage().getChatId(), text);
    }

    public void sendMessage(long chatId, String response){

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(response);

        try{
            execute(sendMessage);
        } catch (TelegramApiException e){
            log.info("Send Message Error {}" , e.getMessage());
        }

    }

    @Override
    public String getBotUsername() {
        return "Daisy2_bot";
    }
}
