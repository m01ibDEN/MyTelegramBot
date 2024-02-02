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

    @Override
    public void onUpdateReceived(Update update) {
        var msg = update.getMessage();
        users.put(msg.getChatId(), msg.getFrom().getUserName());

        if (msg.isCommand() && msg.getText().equals("/start")) {
            sendMessage(msg.getChatId(), "Привет))");
        }

        else if (msg.isCommand() && msg.getText().equals("/xo_with_bot")) {
            Game.setXOWithBot(true);
            Game.setup();
            sendMessage(msg.getChatId(), Game.getMess());
            sendMessage(msg.getChatId(), "Хотите сходить первым?(y/n)");
        }

        else if (Game.isXOWithBot()) {
            if(Game.getMove() == 1) {
                if (Character.isDigit(msg.getText().toCharArray()[0])) {
                    Game.setSquare(Integer.parseInt(msg.getText()), Game.getX());
                    Game.botAction();
                    sendMessage(msg.getChatId(), Game.getGrid());
                    Game.setMove(Game.getMove() + 1);
                }

                if (msg.getText().equals("y")) {
                    sendMessage(msg.getChatId(), Game.getGrid());
                    sendMessage(msg.getChatId(), "Введите номер клетки.");
                }

                if (msg.getText().equals("n")) {
                    Game.botAction();
                    sendMessage(msg.getChatId(), Game.getGrid());
                    Game.setMove(Game.getMove() + 1);
                }
            }

            else {
                Game.setSquare(Integer.parseInt(msg.getText()), Game.getX());
                Game.botAction();
                sendMessage(msg.getChatId(), Game.getGrid());

                if (Game.end() != null) {
                    Game.setMove(1);

                    if(Objects.equals(Game.end(), "user")) {
                        sendMessage(msg.getChatId(), "Вы выиграли!");
                    }

                    else if (Objects.equals(Game.end(), "bot")) {
                        sendMessage(msg.getChatId(), "Вы проиграли(");
                    }

                    else if (Objects.equals(Game.end(), "none")) {
                        sendMessage(msg.getChatId(), "Ничья!");
                    }
                    Game.setXOWithBot(false);
                }
            }
        }

        else if (msg.isCommand() && msg.getText().equals("/xo_with_user")) {
            sendMessage(msg.getChatId(), Game.getMess());
            Game.setXOWithUser(true);
            Game.setup();
            sendMessage(msg.getChatId(), users.values().toString());
            sendMessage(msg.getChatId(),
                    """
                    Введите имя пользователя, с которым хотите сыграть.
                    UPD: Пользователь должен также выбрать этот режим игры в меню.
                    """);
            Game.setInputUser(true);
        }

        else if (Game.isXOWithUser()) {
            if (Game.isAgreement()) {
                if (Character.isDigit(msg.getText().toCharArray()[0])) {
                    if (msg.getChatId() == Game.getIdByGamer1()) {
                        Game.setSquare(Integer.parseInt(msg.getText()), Game.getX());
                        sendMessage(Game.getIdByGamer2(), Game.getGrid());
                    }

                    else if (msg.getChatId() == Game.getIdByGamer2()){
                        Game.setSquare(Integer.parseInt(msg.getText()), Game.getO());
                        sendMessage(Game.getIdByGamer1(), Game.getGrid());
                    }

                    if (Game.end() != null) {
                        Game.setMove(1);

                        if(Objects.equals(Game.end(), "user")) {
                            sendMessage(Game.getIdByGamer1(), "Вы выиграли!");
                            sendMessage(Game.getIdByGamer2(), "Вы проиграли(");
                        }

                        else if (Objects.equals(Game.end(), "bot")) {
                            sendMessage(Game.getIdByGamer2(), "Вы выиграли!");
                            sendMessage(Game.getIdByGamer1(), "Вы проиграли(");
                        }

                        else if (Objects.equals(Game.end(), "none")) {
                            sendMessage(Game.getIdByGamer1(), "Ничья!");
                            sendMessage(Game.getIdByGamer2(), "Ничья!");
                        }

                        Game.setXOWithBot(false);
                    }
                }

                if (msg.getText().equals("y")) {
                    sendMessage(msg.getChatId(), Game.getGrid());
                    sendMessage(msg.getChatId(), "Введите номер клетки.");
                }

                if (msg.getText().equals("n")) {
                    sendMessage(Game.getIdByGamer2(), "Отказано(");
                }
            }

            if (Game.isInputUser()) {
                String username = msg.getText();
                Long chatId = findIdByUserName(username);

                if (chatId != null) {
                    sendMessage(chatId, "С вами хочет поиграть " + msg.getFrom().getUserName());
                    sendMessage(chatId, "Вы согласны?(y/n)");
                    Game.setInputUser(false);
                    Game.setAgreement(true);
                    Game.setIdByGamer1(chatId);
                    Game.setIdByGamer2(msg.getChatId());

                } else if (msg.getChatId() != Game.getIdByGamer2()) {
                    sendMessage(msg.getChatId(), "Пользователь с таким именем не найден.");
                }
            }
        }

        else {
            sendMessage(msg.getChatId(), "Не понятно(");
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
        } catch (TelegramApiException ignored) {}
    }

    public static Long findIdByUserName(String name) {
        Set<Map.Entry<Long, String>> entrySet = users.entrySet();
        for (Map.Entry<Long, String> pair : entrySet) {
            if (name.equals(pair.getValue())) {
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


