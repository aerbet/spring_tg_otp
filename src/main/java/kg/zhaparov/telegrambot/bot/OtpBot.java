package kg.zhaparov.telegrambot.bot;

import kg.zhaparov.telegrambot.service.NormalizeService;
import kg.zhaparov.telegrambot.service.OtpService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;


@Component
public class OtpBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {
    private final TelegramClient telegramClient;
    private final OtpService otpService;
    private final NormalizeService normalizeService;

    public OtpBot(OtpService otpService, NormalizeService normalizeService) {
        this.telegramClient = new OkHttpTelegramClient(getBotToken());
        this.otpService = otpService;
        this.normalizeService = normalizeService;
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

        Message message = update.getMessage();
        long chatId = message.getChatId();

        if (message.hasContact()) {
            String rawPhone = message.getContact().getPhoneNumber();
            String normalizedPhoneNumber = normalizeService.normalizeKg(rawPhone);
            String otp = otpService.generateOtp(normalizedPhoneNumber);

            SendMessage reply = SendMessage.builder()
                    .chatId(chatId)
                    .text("Ваш код подтверждения: " + "<code>" + otp + "</code>\n"
                    + "\nНажав на код, вы скопируете его \uD83D\uDCDD")
                    .parseMode("HTML")
                    .build();
            try {
                telegramClient.execute(reply);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            return;
        }

        if (message.hasText()) {
            String text = message.getText();
            if (!text.isEmpty()) {
                SendMessage ask = SendMessage.builder()
                        .chatId(chatId)
                        .text("Для получения кода, нажмите на кнопку <pre>Поделиться номером</pre>ниже \uD83D\uDC47")
                        .parseMode("HTML")
                        .replyMarkup(sharePhoneKeyboard())
                        .build();
                try {
                    telegramClient.execute(ask);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
