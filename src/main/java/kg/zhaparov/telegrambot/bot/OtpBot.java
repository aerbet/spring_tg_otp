package kg.zhaparov.telegrambot.bot;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;


@Component
public class OtpBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {
    private final TelegramClient telegramClient;

    public OtpBot() {
        this.telegramClient = new OkHttpTelegramClient(getBotToken());
    }

    public String getBotToken() {
        return "";
    }

    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    public void consume(Update update) {
        if (!update.hasMessage()) {
            return;
        }
    }
}
