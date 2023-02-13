package com.example.telegrambot.service;


import com.example.telegrambot.Consts;
import com.example.telegrambot.config.BotConfig;
import com.example.telegrambot.config.DbHelper;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.ChatPermissions;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    final BotConfig config;


    public TelegramBot(BotConfig config) {
        this.config = config;
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {

        ChatPermissions chatPermissions = new ChatPermissions();
        chatPermissions.setCanSendMessages(true);

        update.getMessage().getChat().setPermissions(chatPermissions);
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            switch (messageText) {
                case "/start" -> startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                case "Войти в группу" -> {
                    sendMessage(chatId, "Введите номер комнаты, в которую хотите войти");
                }
                case "Создать группу" -> {
                    try {
                       sendMessage(chatId,DbHelper.createPost(update.getMessage().getChat().getUserName()));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                case "Выйти из группы" -> {
                    try {
                        sendMessage(chatId,DbHelper.exitFromTeam(update.getMessage().getChat().getUserName()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                case "Проверить принадлежность к группе" -> {
                    try {
                        sendMessage(chatId, DbHelper.getCodeByTeamId(update.getMessage().getChat().getUserName()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }



            if (messageText.length() == 4) {

                try {
                    sendMessage(chatId, DbHelper.enterPost(messageText.toString(), update.getMessage().getChat().getUserName()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }

    }

    private void startCommandReceived(long chatId, String name) {

        String answer = "Hi, " + name;

        sendMessage(chatId, answer);
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();
        KeyboardRow row4 = new KeyboardRow();
        KeyboardRow row5 = new KeyboardRow();


        row1.add("Войти в группу");

        row2.add("Создать группу");

        row3.add("Выйти из группы");

        row4.add("Проверить принадлежность к группе");


        keyboardRows.add(row1);
        keyboardRows.add(row2);
        keyboardRows.add(row3);
        keyboardRows.add(row4);

        keyboardMarkup.setKeyboard(keyboardRows);

        message.setReplyMarkup(keyboardMarkup);


        try {
            execute(message);
        } catch (TelegramApiException ignored) {

        }

    }




}
