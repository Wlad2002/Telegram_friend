package com.bot.config;

import lombok.Data;
//import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Data
@PropertySource("application.properties")
public class BotConfig {
    //@Value("${bot.name}")
    String botName = "the_lost_child_bot";
    //@Value("${bot.token}")
     String token = "6224083322:AAF_byoyY6guk_Zdf2PqP2QuGrrK_3j__ng";
}
