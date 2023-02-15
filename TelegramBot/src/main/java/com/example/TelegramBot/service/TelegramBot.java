package com.example.telegrambot.service;

import com.example.telegrambot.config.BotConfig;
import com.example.telegrambot.config.DbHelper;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.ChatPermissions;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

        if (update.hasMessage() && update.getMessage().hasText() || (update.hasMessage() && update.getMessage().hasLocation())) {
            update.getMessage().getChat().setPermissions(chatPermissions);
            long chatId = update.getMessage().getChatId();
            String messageText = update.getMessage().getText();
            if (update.getMessage().hasLocation()) {
                try {
                    sendMessage(chatId,DbHelper.sendLoc(update.getMessage().getLocation().getLongitude(), update.getMessage().getLocation().getLatitude()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            switch (messageText) {
                case "/start" -> startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                case "Войти в группу" -> {
                    sendMessage(chatId, "Введите номер комнаты, в которую хотите войти");
                }
                case "Создать группу" -> {
                    try {
                       addInlineKeyboard(chatId,DbHelper.createPost(update.getMessage().getChat().getUserName()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (TelegramApiException e) {
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
            else if (update.hasCallbackQuery()) {
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            if (Objects.equals(update.getCallbackQuery().getData(), "Query location"))
            {

            }
        }


    }

    private void startCommandReceived(long chatId, String name) {

        String answer = "Hi, " + name;

        sendMessage(chatId, answer);
    }

    private void addInlineKeyboard(long chatId,String textToSend) throws TelegramApiException {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText("Запросить геопозицию пользователей");
        inlineKeyboardButton.setCallbackData("Query location");
        List<InlineKeyboardButton> inlineKeyboardButtonList = new ArrayList<>();
        inlineKeyboardButtonList.add(inlineKeyboardButton);
        List<List<InlineKeyboardButton>> keyBoardButtons = new ArrayList<>();
        keyBoardButtons.add(inlineKeyboardButtonList);
        inlineKeyboardMarkup.setKeyboard(keyBoardButtons);
        message.setText(textToSend);
        message.setReplyMarkup(inlineKeyboardMarkup);
        execute(message);
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
        KeyboardRow row6 = new KeyboardRow();

        keyboardRows.add(row6);
        keyboardMarkup.setKeyboard(keyboardRows);

        message.setReplyMarkup(keyboardMarkup);
        try {
            execute(message);
        } catch (TelegramApiException ignored) {

        }

    }




}
