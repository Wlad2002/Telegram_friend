package com.bot.service;

import com.bot.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.util.*;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    //Блок для констант
    private static final String ERROR_TEXT = "Error occurred: ";//константа для лога ошибок
    //блок для констант
    private final BotConfig config;
    private final String[] COURSES_CALLBACK = {"FIRST_COURSE","SECOND_COURSE","THIRD_COURSE","FOURTH_COURSE"};//callback для кнопок курсов
    private final String[] SEMESTERS_CALLBACK = {"FIRST_SEMESTER","SECOND_SEMESTER"};//callback семестров
    private final String[]  THIRD_SEMESTER_FIRST_CALLBACK = {"TLEC","AiPOVS","OSiSP","POKPP","VOSP","LATS","Ek","EkJD","EMS"};//callback для кнопок 3 курс 1 семестр
    private final String[] THIRD_SEMESTER_SECOND_CALLBACK = {"SII","OSiP","OPBD","TPO","EPUvIUS","EMiP","NUATiS","PEiO"};//callback для кнопок 3 курс 3 семестр
    private final String[] THIRD_SEMESTER_FIRST = {"ТЛЭЦ","АиПОВС","ОСиСП","ПО кросс-платф приложений","ВОСП","ЛАТС","Экономика","Экономика","ЭМС"};//текст кнопок, 3 курс 1 семестр
    private final String[] THIRD_SEMESTER_SECOND = {"Системы искусственного интеллекта","ОСиСП","Орг. и проектирование БД","Тест. ПО","ЭПУвИУС","Эл. машины и преобр.","Надежность устр. АТиС","Правила экспл. и охраны труда"};//текст кнопок, 3 курс 2 семестр
    //
    //обще доступные поля
    public TelegramBot(BotConfig config){
        this.config = config;
    }
    @Override
    public String getBotUsername() {
        return config.getBotName();
    }
    @Override
    public String getBotToken(){
        return config.getToken();
    }
    @Override
    public void onUpdateReceived(Update update) {                //основной метод для работы бота
        if(update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            switch (messageText){
                case "/start":
                        start(chatId);
                    break;
                default:
                        sendMessage(chatId, "Некорректный ввод", null);
                    break;
            }
        } else if(update.hasCallbackQuery()){
            String callbackData = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            InlineKeyboardMarkup markupInline;
            if (callbackData.contains("COURSE")){
                markupInline = newButton(2,"семестр",SEMESTERS_CALLBACK,null);
                executeEditMessageText("Выберите семестр",chatId,messageId,markupInline);
            }else if(callbackData.equals("FIRST_SEMESTER")){
                markupInline = newButton(9,null,THIRD_SEMESTER_FIRST_CALLBACK,THIRD_SEMESTER_FIRST);
                executeEditMessageText("Выберите интересующий предмет",chatId,messageId,markupInline);
            }

        }

    }
    private void sendMessage(long chatId, String textToSend, InlineKeyboardMarkup inlineKeyboardMarkup) {// метод отправки любого сообщения
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textToSend);
        if(inlineKeyboardMarkup != null)
            message.setReplyMarkup(inlineKeyboardMarkup);
        try {
            execute(message);
        }catch (TelegramApiException e){
            log.error(ERROR_TEXT + e.getMessage());
        }
    }
    private void executeEditMessageText(String text, long chatId, long messageId, InlineKeyboardMarkup inlineKeyboardMarkup){//метод замены любого сообщения
        EditMessageText message = new EditMessageText();
        message.setChatId(chatId);
        message.setText(text);
        message.setMessageId((int) messageId);
        if(inlineKeyboardMarkup != null)
            message.setReplyMarkup(inlineKeyboardMarkup);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(ERROR_TEXT + e.getMessage());
        }
    }
    private void start(long chatId){//стартовое приветствие
        InlineKeyboardMarkup markupInline = newButton(4,"курс",COURSES_CALLBACK,null);
        sendMessage(chatId,"Добро пожаловать в проект для помощи студентам с литературой и другими материалами. Выберите интересующий вас курс",markupInline);
    }
    private InlineKeyboardMarkup newButton(int length, String textButton,String[] callback, String[] names){//создание заданного количества кнопок
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        InlineKeyboardButton[] coursesButton = new InlineKeyboardButton[length];
        if (names==null) {
            for (var i = 0; i < coursesButton.length; i++) {
                coursesButton[i] = new InlineKeyboardButton();
                coursesButton[i].setText((i + 1) + textButton);
                coursesButton[i].setCallbackData(callback[i]);
                rowInLine.add(coursesButton[i]);
            }
            rowsInLine.add(rowInLine);
        }else{
            try {
                List<InlineKeyboardButton>[] buttons = new ArrayList[length];
                for (var i = 0; i < coursesButton.length; i++) {
                    buttons[i] = new ArrayList<>();
                    coursesButton[i] = new InlineKeyboardButton();
                    coursesButton[i].setText(names[i]);
                    coursesButton[i].setCallbackData(callback[i]);
                    buttons[i].add(coursesButton[i]);
                    rowsInLine.add(buttons[i]);
                }
            }catch (Exception e){
                log.error(ERROR_TEXT + e.getMessage());
            }
        }
        markupInline.setKeyboard(rowsInLine);
        return markupInline;
    }
}