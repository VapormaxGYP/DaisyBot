package com.vapor.daisybot;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import com.vdurmont.emoji.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@PropertySource(value="classpath:command.properties")
public class DaisyBot extends TelegramLongPollingBot {


    @Autowired
    public DaisyBot(DefaultBotOptions botOptions) {

        super(botOptions, "6524420386:AAEIZziA-v5zn0pDCvhDjUi5rSORPn7ErcM");
        List<BotCommand> cmdList = new ArrayList<>();
        cmdList.add(new BotCommand("/help", "Show How to Use this Bot"));
        cmdList.add(new BotCommand("/chat", "Use DeepSeek to do sth"));

        try {
            execute(new SetMyCommands(cmdList, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Set Commands List Error {}" ,e.getMessage());
        }

    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update!= null && update.hasMessage()) {

            long chatId = update.getMessage().getChatId();

            Message message = update.getMessage();

            //log.info(message.toString());

            List<MessageEntity> request = update.getMessage().getEntities();
            log.info("Get from bot: {}", request);

            MessageEntity entity = request.get(0);
            String messageType = entity.getType();
            String messageContent = message.getText().split("@")[0];

            log.info("MessageContent is {}", messageContent);

            if(messageType.equals("bot_command")){
                dealBotCmd(chatId, messageContent);
            }

        }

    }


    public void dealBotCmd(long chatId, String messageContent){

        switch (messageContent){
            //case "/help" -> sendMessage(chatId, EmojiParser.parseToUnicode(helpMessage));
            //case "/chat" -> sendMessage(chatId, helpMessage);
        }
    }

    public void sendMessage(long chatId, String content){

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(content);

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
