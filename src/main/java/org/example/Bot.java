package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Bot extends TelegramLongPollingBot {
    @Override
    public void onUpdateReceived(Update update) {
        var msg = update.getMessage();
        if(msg.isCommand() && msg.getText().equals("/game")) {
            try {
                execute(SendMessage.builder().chatId(msg.getChatId()).text("Тут что-то будет").build());
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            try {
                execute(SendMessage.builder().chatId(msg.getChatId()).text("Я вас не понимаю(\n Попробуйте снова").build());
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return("justBot");
    }

    @Override
    public String getBotToken() {
        return "6207008509:AAFDFEobMskwz3Rt5GNo59INrid5a8BhhIw";
    }

    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(new Bot());
    }
}


