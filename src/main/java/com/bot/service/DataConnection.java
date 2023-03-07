package com.bot.service;

import lombok.extern.slf4j.Slf4j;
import java.sql.*;
//import java.util.*;

@Slf4j
public abstract class DataConnection {
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
            ResultSet resultSet = statement.executeQuery("SELECT * FROM First_course_first_semester");
            while (resultSet.next()){
                String callbackBD = resultSet.getString("Callback");
                if(callback.equals(callbackBD)){
                    log.info("URL successful");
                    return resultSet.getString("URL");
                }
            }
            resultSet.close();
            statement.close();
            connection.close();
        }catch (Exception e){
            log.error("Error occurred: " + e.getMessage());
        }
        return null;
    }
}
