package com.vapor.daisybot;

import lombok.extern.slf4j.Slf4j;
import com.vdurmont.emoji.*;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class DaisyBot extends TelegramLongPollingBot {

    @Autowired
    public DaisyBot(DefaultBotOptions botOptions) {

        super(botOptions, "6524420386:AAEIZziA-v5zn0pDCvhDjUi5rSORPn7ErcM");
        List<BotCommand> cmdList = new ArrayList<>();
        cmdList.add(new BotCommand("/start", "Welcome Message"));
        cmdList.add(new BotCommand("/help", "Show How to Use this Bot"));

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
            String messageFrom = message.getFrom().getUserName();

            log.info("MessageContent is {}", messageContent);

            if(messageType.equals("bot_command")){
                dealBotCmd(chatId, messageContent);
            }

            if(messageType.equals("mention") && entity.getText().equals("@Daisy2_bot")){
                dealBotContent(chatId, messageFrom);
            }

        }

    }

    public void dealBotCmd(long chatId, String messageContent){

        String startWord = ":smile: Welcome Use Daisy bot";
        String helpWord = ":wink: Need some Help?";
        switch (messageContent){
            case "/start" -> sendMessage(chatId, EmojiParser.parseToUnicode(startWord));
            case "/help" -> sendMessage(chatId, EmojiParser.parseToUnicode(helpWord));
        }
    }

    public void dealBotContent(long chatId, String messageContent){

        String response = ":santa|type_5: 消息来自于： " + messageContent;

        sendMessage(chatId, EmojiParser.parseToUnicode(response));
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
