package com.example.telegrambot.service;


import com.example.telegrambot.config.BotConfig;
import com.example.telegrambot.config.DbHelper;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
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
        if (update.hasMessage() && update.getMessage().hasText()) {

            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText) {
                case "/start" -> startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                case "/enterInTeam" -> sendMessage(chatId, "Введите номер комнаты, в которую хотите войти");
                case "/createTeam" -> {
                    try {
                       sendMessage(chatId,DbHelper.createPost(update.getMessage().getChat().getUserName()));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                case "/exitFromTeam" -> {
                    try {
                        sendMessage(chatId,DbHelper.exitFromTeam(update.getMessage().getChat().getUserName()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (messageText.length() == 4) {

                try {

                    sendMessage(chatId, DbHelper.enterPost(messageText, update.getMessage().getChat().getUserName()));

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

        KeyboardRow row = new KeyboardRow();

        row.add("/enterInTeam");

        row.add("/createTeam");

        row.add("/exitFromTeam");

        keyboardRows.add(row);

        keyboardMarkup.setKeyboard(keyboardRows);

        message.setReplyMarkup(keyboardMarkup);


        try {
            execute(message);
        } catch (TelegramApiException ignored) {

        }

    }




}
