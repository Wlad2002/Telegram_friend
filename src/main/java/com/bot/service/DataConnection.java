package com.bot.service;

import lombok.extern.slf4j.Slf4j;
import java.sql.*;
//import java.util.*;

@Slf4j
public class DataConnection {
    private static Connection connection = null;
    private static Statement statement = null;


    public static void bdConnection(){
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:/media/krava/Новый том/Telegram_friend/src/main/resources/list.sqbpro");
            log.info("connect successful");
        }catch (Exception e){
            log.error("Error occurred: " + e.getMessage());
        }
    }
    public static void getValues() throws SQLException {
        try {
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM COMPANY;");
            log.info("connect successful");
        }catch (Exception e){
            log.error("Error occurred: " + e.getMessage());
        }


    }
}
