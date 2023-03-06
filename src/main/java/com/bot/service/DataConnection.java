package com.bot.service;

import lombok.extern.slf4j.Slf4j;
import java.sql.*;
//import java.util.*;

@Slf4j
public class DataConnection {
    public static void bdConnection(){
        Connection connection = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:/media/krava/Новый том/Telegram_friend/src/main/resources/list.sqbpro");
            log.info("connect successful");
        }catch (Exception e){
            log.error("Error occurred: " + e.getMessage());
        }
    }
}
