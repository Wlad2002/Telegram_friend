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
    private final BotConfig config;
    private final String[] COURSES_CALLBACK = {"FIRST_COURSE","SECOND_COURSE","THIRD_COURSE","FOURTH_COURSE"};//callback для кнопок курсов
    private final String[] FIRST_SEMESTERS_CALLBACK = {"1_1_SEMESTER","1_2_SEMESTER"};//callback семестров 1 курса
    private final String[] SECOND_SEMESTERS_CALLBACK = {"2_1_SEMESTER","2_2_SEMESTER"};//callback семестров 2 курса
    private final String[] THIRD_SEMESTERS_CALLBACK = {"3_1_SEMESTER","3_2_SEMESTER"};//callback семестров 3 курса
    private final String[] FOURTH_SEMESTERS_CALLBACK = {"4_1_SEMESTER","4_2_SEMESTER"};//callback семестров 4 курса
    private final String[] FIRST_COURSE_FIRST_CALLBACK = {"BJCH","His","VOV","BJCHR","EMiTER","Inf","VM","Fiz","InGr","FIRST_COURSE"};//callback для кнопок 1 курс 1 семестр
    private final String[] FIRST_COURSE_SECOND_CALLBACK = {"Him","Bel","Polit","InGr","Fil","VM","Fiz","InGr","FIRST_COURSE"};//callback для кнопок 1 курс 2 семестр
    private final String[] SECOND_COURSE_FIRST_CALLBACK = {"TRJSU","Rel","Kult","OSTiZI","EU","TOE","TPS","Ekon","OOTP","TDU","SECOND_COURSE"};//callback для кнопок 2 курс 1 семестр
    private final String[] SECOND_COURSE_SECOND_CALLBACK = {"OsPr","OhTr","OMT","MARES","EU","WEB","TOE","TOAT","SECOND_COURSE"};//callback для кнопок 2 курс 2 семестр
    private final String[]  THIRD_COURSE_FIRST_CALLBACK = {"TLEC","AiPOVS","OSiSP","POKPP","VOSP","LATS","Ek","EkJD","EMS","THIRD_COURSE"};//callback для кнопок 3 курс 1 семестр
    private final String[] THIRD_COURSE_SECOND_CALLBACK = {"SII","OSiP","OPBD","TPO","EPUvIUS","EMiP","NUATiS","PEiO","THIRD_COURSE"};//callback для кнопок 3 курс 2 семестр
    private final String[] FIRST_COURSE_FIRST = {"БЖЧ","История","ВОВ","БЖЧ радиоц.","ЭМиТЭР","Информатика","Высш. мат.","Физика","Инженерная графика","Назад"};//текст кнопок, 1 курс 1 семестр
    private final String[] FIRST_COURSE_SECOND = {"Химия","Бел. яз.","Политология","Инженерная графика","Философия","Высш. мат.","Физика","Информатика","Назад"};//текст кнопок, 1 курс 2 семестр
    private final String[] SECOND_COURSE_FIRST = {"ТРЖСУ","Религиоведение","Культурология","ОСТиЗИ","ЭУ","ТОЭ","ТПС","Экономика","ООТП","ТДУ","Назад"};//текст кнопок, 2 курс 1 семестр
    private final String[] SECOND_COURSE_SECOND = {"Основы права","Охрана труда","ОМТ","МАРЭС","ЭУ","WEB-технологии","ТОЭ","ТОАТ","Назад"};//текст кнопок, 2 курс 2 семестр
    private final String[] THIRD_COURSE_FIRST = {"ТЛЭЦ","АиПОВС","ОСиСП","ПО кросс-платф приложений","ВОСП","ЛАТС","Экономика","Экономика","ЭМС","Назад"};//текст кнопок, 3 курс 1 семестр
    private final String[] THIRD_COURSE_SECOND = {"Системы искусственного интеллекта","ОСиСП","Орг. и проектирование БД","Тест. ПО","ЭПУвИУС","Эл. машины и преобр.","Надежность устр. АТиС","Правила экспл. и охраны труда","Назад"};//текст кнопок, 3 курс 2 семестр
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
    public void onUpdateReceived(Update update) {//основной метод для работы бота
        if(update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            switch (messageText){
                case "/start":
                    InlineKeyboardMarkup markupInline = newButton(4,"курс",COURSES_CALLBACK,null,false);
                    sendMessage(chatId,"Добро пожаловать в проект для помощи студентам с литературой и другими материалами. Выберите интересующий вас курс",markupInline);
                    break;
                default:
                    sendMessage(chatId, "Некорректный ввод", null);
                    break;
            }
        } else if(update.hasCallbackQuery()){
            examination(update);
        }
    }
    //внутренние методы
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
    private void examination(Update update){//метод выбора ответа на нажатие кнопки
        String callbackData = update.getCallbackQuery().getData();
        long messageId = update.getCallbackQuery().getMessage().getMessageId();
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        InlineKeyboardMarkup markupInline = null;
        try {
            if (callbackData.contains("COURSE")) {
                switch (callbackData) {
                    case "FIRST_COURSE" -> markupInline = newButton(2, "семестр", FIRST_SEMESTERS_CALLBACK, null, true);
                    case "SECOND_COURSE" -> markupInline = newButton(2, "семестр", SECOND_SEMESTERS_CALLBACK, null, true);
                    case "THIRD_COURSE" -> markupInline = newButton(2, "семестр", THIRD_SEMESTERS_CALLBACK, null, true);
                    case "FOURTH_COURSE" -> markupInline = newButton(2, "семестр", FOURTH_SEMESTERS_CALLBACK, null, true);
                }
                executeEditMessageText("Выберите семестр", chatId, messageId, markupInline);
            } else if (callbackData.contains("SEMESTER")) {
                switch (callbackData) {
                    case "1_1_SEMESTER" -> markupInline = newButton(10, null, FIRST_COURSE_FIRST_CALLBACK, FIRST_COURSE_FIRST, false);
                    case "1_2_SEMESTER" -> markupInline = newButton(9, null, FIRST_COURSE_SECOND_CALLBACK, FIRST_COURSE_SECOND, false);
                    case "2_1_SEMESTER" -> markupInline = newButton(11, null, SECOND_COURSE_FIRST_CALLBACK, SECOND_COURSE_FIRST, false);
                    case "2_2_SEMESTER" -> markupInline = newButton(9, null, SECOND_COURSE_SECOND_CALLBACK, SECOND_COURSE_SECOND, false);
                    case "3_1_SEMESTER" -> markupInline = newButton(10, null, THIRD_COURSE_FIRST_CALLBACK, THIRD_COURSE_FIRST, false);
                    case "3_2_SEMESTER" -> markupInline = newButton(9, null, THIRD_COURSE_SECOND_CALLBACK, THIRD_COURSE_SECOND, false);
                    //case "4_1_SEMESTER" -> markupInline = newButton();
                    //case "4_2_SEMESTER" -> markupInline = newButton();
                }
                executeEditMessageText("Выберите интересующий предмет", chatId, messageId, markupInline);
            } else if (callbackData.equals("BACK_CURS")) {
                markupInline = newButton(4, "курс", COURSES_CALLBACK, null, false);
                executeEditMessageText("Добро пожаловать в проект для помощи студентам с литературой и другими материалами. Выберите интересующий вас курс", chatId, messageId, markupInline);
            }//else для обработки при выборе предмета
            if (markupInline==null)
                sendMessage(chatId, "Это кнопка находится в разработке", null);
        }catch (Exception e){
            log.error(ERROR_TEXT + e.getMessage());
        }
    }
    private InlineKeyboardMarkup newButton(int length, String textButton,String[] callback, String[] names, boolean flag_back){//создание заданного количества кнопок
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        InlineKeyboardButton[] coursesButton = new InlineKeyboardButton[length];
        if (names==null) {
            try {
                for (var i = 0; i < coursesButton.length; i++) {
                    coursesButton[i] = new InlineKeyboardButton();
                    coursesButton[i].setText((i + 1) + textButton);
                    coursesButton[i].setCallbackData(callback[i]);
                    rowInLine.add(coursesButton[i]);
                }
                rowsInLine.add(rowInLine);
                if(flag_back) {
                    List<InlineKeyboardButton> row = new ArrayList<>();
                    row.add(new InlineKeyboardButton());
                    row.get(0).setText("Назад");
                    row.get(0).setCallbackData("BACK_CURS");
                    rowsInLine.add(row);
                }
            }catch (Exception e) {
                log.error(ERROR_TEXT + e.getMessage());
            }
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