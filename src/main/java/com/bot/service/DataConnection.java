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
    public static String getURL(String callback) throws SQLException {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:/media/krava/Новый том/Telegram_friend/src/main/resources/list");
            statement = connection.createStatement();
            log.info("connect successful");
            ResultSet rs = statement.executeQuery("SELECT * FROM First_course_first_semester");
            log.info("URL successful");
            return rs.getString("URL");
        }catch (Exception e){
            log.error("Error occurred: " + e.getMessage());
        }
        return null;

    }
}
