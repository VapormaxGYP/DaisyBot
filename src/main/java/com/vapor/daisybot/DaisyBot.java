package com.vapor.daisybot;

import com.vapor.daisybot.BotService.AIChat;
import com.vapor.daisybot.BotService.PrintHelp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
@PropertySource(value= "classpath:bot.properties")
public class DaisyBot extends TelegramLongPollingBot {


    @Autowired
    PrintHelp printHelp;

    @Autowired
    AIChat aiChat;

    @Value("${ai.token}")
    String aiToken;
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

            Message message = update.getMessage();
            log.info(message.toString());

            long chatId = message.getChatId();
            String messageContent = message.getText();
            String user = message.getFrom().getUserName();

            if(messageContent.contains("@")){
                messageContent = messageContent.split("@")[0];
            }

            log.info("MessageContent is {}", messageContent);

            if(message.getEntities() != null){
                List<MessageEntity> request = message.getEntities();
                MessageEntity entity = request.get(0);
                String messageType = entity.getType();

                if(messageType.equals("bot_command")){
                    dealBotCmd(chatId, messageContent, user);
                }
            }

        }

    }


    public void dealBotCmd(long chatId, String messageContent, String user){

        log.info(aiToken);
        String words = "";
        if(messageContent.contains("/chat")){
            words = messageContent.substring(5);
            messageContent = "/chat";
        }
        log.info("用户输入:" + words.trim());

        switch (messageContent){
            case "/help" -> sendMessage(chatId, printHelp.genHelpMessage(user));
            case "/chat" -> {
                if(words.trim().isEmpty()){
                    sendMessage(chatId, "请输入内容");
                }
                else {
                    sendMessage(chatId, aiChat.genChatMessage(aiToken, words.trim()));
                }
            }
            default -> sendMessage(chatId, "请输入有效内容");
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
