package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.*;

public class Bot extends TelegramLongPollingBot {
    static HashMap<Long, String> users = new HashMap<>();
    boolean isXOWithBot = false;
    boolean isXOWithUser = false;
    boolean agreement = false;
    long idByGamer1 = 0, idByGamer2 = 0;
    String mess = """
            Это игра в крестики-нолики внутри моего бота в телеграм
            Клетки пронумерованы от 1 до 9 слева сверху до права снизу
            Чтобы сходить вы должны проосто написать номер свободной клетки).
            """;
    int move = 1;
    boolean inputUser = false;

    @Override
    public void onUpdateReceived(Update update) {
        var msg = update.getMessage();
        users.put(msg.getChatId(), msg.getFrom().getUserName());

        System.out.println(users.toString());
        if (isXOWithBot) {
            if(move == 1) {
                if (Character.isDigit(msg.getText().toCharArray()[0])) {
                    Game.setSquare(Integer.parseInt(msg.getText()), Game.getX());
                    Game.botAction();
                    sendMessage(msg.getChatId(), Game.getGrid());
                    move++;
                }

                if (msg.getText().equals("y")) {
                    sendMessage(msg.getChatId(), Game.getGrid());
                    sendMessage(msg.getChatId(), "Введите номер клетки.");
                }

                if (msg.getText().equals("n")) {
                    Game.botAction();
                    sendMessage(msg.getChatId(), Game.getGrid());
                    move++;
                }
            }

            else {
                Game.setSquare(Integer.parseInt(msg.getText()), Game.getX());
                Game.botAction();
                sendMessage(msg.getChatId(), Game.getGrid());

                if (Game.end() != null) {
                    move = 1;

                    if(Objects.equals(Game.end(), "user")) {
                        sendMessage(msg.getChatId(), "Вы выиграли!");
                    }

                    else if (Objects.equals(Game.end(), "bot")) {
                        sendMessage(msg.getChatId(), "Вы проиграли(");
                    }

                    else if (Objects.equals(Game.end(), "none")) {
                        sendMessage(msg.getChatId(), "Ничья!");
                    }
                    isXOWithBot = false;
                }
            }
        }


        if (isXOWithUser) {
            if (agreement) {
                if (Character.isDigit(msg.getText().toCharArray()[0])) {
                    if (msg.getChatId() == idByGamer1) {
                        Game.setSquare(Integer.parseInt(msg.getText()), Game.getX());
                        sendMessage(idByGamer2, Game.getGrid());
                    }
                    else {
                        Game.setSquare(Integer.parseInt(msg.getText()), Game.getO());
                        sendMessage(idByGamer1, Game.getGrid());
                    }
                }

                if (msg.getText().equals("y")) {
                    sendMessage(msg.getChatId(), Game.getGrid());
                    sendMessage(msg.getChatId(), "Введите номер клетки.");
                }
            }

            if (inputUser) {
                String username = msg.getText();
                Long chatId = findIdByUserName(username);
                if (chatId != null) {
                    sendMessage(chatId, "С вами хочет поиграть " + msg.getFrom().getUserName());
                    sendMessage(chatId, "Вы согласны?(y/n)");
                    inputUser = false;
                    agreement = true;
                    idByGamer1 = chatId;
                    idByGamer2 = msg.getChatId();
                } else {
                    sendMessage(msg.getChatId(), "Пользователь с таким именем не найден.");
                }
            }
        }

        if (msg.isCommand() && msg.getText().equals("/xo_with_bot")) {
            isXOWithBot = true;
            Game.setup();
            sendMessage(msg.getChatId(), mess);
            sendMessage(msg.getChatId(), "Хотите сходить первым?(y/n)");
        }



        if (msg.isCommand() && msg.getText().equals("/xo_with_user")) {
            isXOWithUser = true;
            Game.setup();
            sendMessage(msg.getChatId(), users.values().toString());
            sendMessage(msg.getChatId(),
                                """
                                Введите имя пользователя, с которым хотите сыграть.
                                UPD: Пользователь должен также выбрать этот режим игры в меню.
                                """);
            inputUser = true;
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

    private void sendMessage(Long chatId, String textToSend){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException ignored) {

        }
    }

    public static Long findIdByUserName(String name) {

        Set<Map.Entry<Long, String>> entrySet = users.entrySet();

        for (Map.Entry<Long, String> pair : entrySet) {
            if (name.equals(pair.getValue())) {
                System.out.println("+");
                return pair.getKey();
            }
        }
        return null;
    }

    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(new Bot());
    }
}


