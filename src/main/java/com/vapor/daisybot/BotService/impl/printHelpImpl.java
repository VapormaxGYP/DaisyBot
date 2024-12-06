package com.vapor.daisybot.BotService.impl;

import com.vapor.daisybot.BotService.PrintHelp;
import org.springframework.stereotype.Component;

@Component
public class printHelpImpl implements PrintHelp {
    @Override
    public String genHelpMessage(String userName) {

        String greetingWords = "Welcome! " + userName + "\n";
        String functionList = """
                /help ------> Print Function List
                /chat ------> AI Chat, Enter your message behind /chat. eg: /chat tell a joke!
                """;

        return greetingWords + functionList;
    }
}
