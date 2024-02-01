package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.HashMap;
import java.util.Objects;

public class Bot extends TelegramLongPollingBot {

    HashMap<String, String> users = new HashMap<>();
    boolean isXO = false;
    String mess = "Это игра в крестики-нолики внутри моего бота в телеграм\n"
            + "Клетки пронумерованы от 1 до 9 слева сверху до права снизу\n"
            + "Чтобы сходить вы должны проосто написать номер свободной клетки)\n";
    int move = 1;

    @Override
    public void onUpdateReceived(Update update) {

        var msg = update.getMessage();
        users.put(msg.getChatId().toString(), msg.getFrom().getUserName());

        if (isXO) {
            if(move == 1) {
                if (Character.isDigit(msg.getText().toCharArray()[0])) {
                    Game.setSquare(Integer.parseInt(msg.getText()), Game.getX());
                    Game.botAction();
                    try {
                        execute(SendMessage.builder().chatId(msg.getChatId()).text(Game.getGrid()).build());
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    move++;
                }

                if (msg.getText().equals("y")) {
                    try {
                        execute(SendMessage.builder().chatId(msg.getChatId()).text(Game.getGrid()).build());
                        execute(SendMessage.builder().chatId(msg.getChatId()).text("Введите номер клетки").build());
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (msg.getText().equals("n")) {
                    Game.botAction();
                    try {
                        execute(SendMessage.builder().chatId(msg.getChatId()).text(Game.getGrid()).build());
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    move++;
                }
            }

            else {
                try {
                    Game.setSquare(Integer.parseInt(msg.getText()), Game.getX());
                    Game.botAction();
                    execute(SendMessage.builder().chatId(msg.getChatId()).text(Game.getGrid()).build());
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }



                if (Game.end() != null) {
                    move = 1;
                    if(Objects.equals(Game.end(), "user")) {
                        try {
                            execute(SendMessage.builder().chatId(msg.getChatId()).text("Вы выиграли!").build());
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    else if (Objects.equals(Game.end(), "bot")) {
                        try {
                            execute(SendMessage.builder().chatId(msg.getChatId()).text("Вы проиграли(").build());
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    else if (Objects.equals(Game.end(), "none")) {
                        try {
                            execute(SendMessage.builder().chatId(msg.getChatId()).text("Ничья!").build());
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    isXO = false;
                }
            }
        }

        if(msg.isCommand() && msg.getText().equals("/game")) {
            isXO = true;
            try {
                Game.setup();
                execute(SendMessage.builder().chatId(msg.getChatId()).text(mess).build());
                execute(SendMessage.builder().chatId(msg.getChatId()).text("Хотите сходить первым?(y/n)").build());
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


