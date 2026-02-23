package kg.zhaparov.telegrambot.bot;

import kg.zhaparov.telegrambot.service.OtpService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.generics.TelegramClient;


@Component
public class OtpBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {
    private final TelegramClient telegramClient;
    private final OtpService otpService;

    public OtpBot(OtpService otpService) {
        this.telegramClient = new OkHttpTelegramClient(getBotToken());
        this.otpService = otpService;
    }

    public String getBotToken() {
        return "";
    }

    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    private ReplyKeyboardMarkup sharePhoneKeyboard() {
        KeyboardButton startButton = KeyboardButton.builder()
                .text("Инструкция")
                .requestContact(true)
                .build();

        KeyboardButton shareButton = KeyboardButton.builder()
                .text("Поделиться номером")
                .requestContact(true)
                .build();

        KeyboardRow row = new KeyboardRow();
        row.add(startButton);
        row.add(shareButton);
        return ReplyKeyboardMarkup.builder()
                .keyboard(java.util.List.of(row))
                .resizeKeyboard(true)
                .oneTimeKeyboard(true)
                .build();
    }

    public void consume(Update update) {
        if (!update.hasMessage()) {
            return;
        }
    }
}
