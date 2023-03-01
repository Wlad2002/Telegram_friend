package com.bot.Telegram_friend;

import com.bot.config.BotConfig;
import com.bot.config.BotInitializer;
import com.bot.service.TelegramBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@SpringBootApplication
public class TelegramFriendApplication {

	public static void main(String[] args) throws TelegramApiException {
		SpringApplication.run(TelegramFriendApplication.class, args);
		TelegramBot bot = new TelegramBot(new BotConfig());
		BotInitializer botInitializer = new BotInitializer(bot);
		try {
			botInitializer.init();
		} catch (TelegramApiException e) {
			log.error("Error occurred: " + e.getMessage());
		}
	}

}
