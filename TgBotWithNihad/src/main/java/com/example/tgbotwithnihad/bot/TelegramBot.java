package com.example.tgbotwithnihad.bot;

import com.example.tgbotwithnihad.config.BotConfig;
import com.example.tgbotwithnihad.model.QuestionHelper;
import com.example.tgbotwithnihad.model.Reservation;
import com.example.tgbotwithnihad.model.Restaurant;
import com.example.tgbotwithnihad.service.QuestionHelperService;
import com.example.tgbotwithnihad.service.QuestionService;
import com.example.tgbotwithnihad.service.ReservationService;
import com.example.tgbotwithnihad.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;

    private final QuestionHelperService questionHelperService;

    private final RestaurantService restaurantService;

    private final QuestionService questionService;

    private final ReservationService reservationService;

    private static final Pattern datePattern = Pattern.compile("^(0[1-9]|[1-2][0-9]|3[0-1])\\.(0[1-9]|1[0-2])\\.\\d{4}$");

    private Pattern timePattern = Pattern.compile("^([01]\\d|2[0-3]):[0-5]\\d$");

    private Pattern numberPattern = Pattern.compile("^[0-9]*$");

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {

            String message = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (message.equals("/start")) {

                if (questionHelperService.getQuestionHelperByChatId(chatId) != null) {
                    questionHelperService.deleteChatId(chatId);
                }

                if (reservationService.getReservationByChatId(chatId) != null) {
                    reservationService.deleteReservation(chatId);
                }

                if (questionHelperService.getQuestionHelperByChatId(chatId) == null) {

                    QuestionHelper questionHelper = new QuestionHelper(chatId, 1);

                    questionHelperService.saveQuestionHelper(questionHelper);

                }

                languageSelection(chatId);

            } else if (message.equals("/stop")) {

                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatId);
                sendMessage.setText("Yeniden bashlamag ucun : /start");
                executeMessage(sendMessage);

                questionHelperService.deleteChatId(chatId);

                reservationService.deleteReservation(chatId);

            } else if (!message.equals("/stop")) {

                if (questionHelperService.getQuestionHelperByChatId(chatId).getReservation() == 1) {

                    if (questionHelperService.getQuestionHelperByChatId(chatId).getKey() == 2) {

                        sendQuestionsForReservation(chatId);

                    } else if (questionHelperService.getQuestionHelperByChatId(chatId).getKey() == 3) {

                        if (numberPattern.matcher(update.getMessage().getText()).matches()) {

                            if (questionHelperService.getQuestionHelperByChatId(chatId).getLanguage().equals("aze")) {

                                SendMessage sendMessage = new SendMessage();
                                sendMessage.setChatId(chatId);
                                sendMessage.setText("Adinizi duz qeyd edin zehmet olmasa");
                                executeMessage(sendMessage);

                            } else if (questionHelperService.getQuestionHelperByChatId(chatId).getLanguage().equals("rus")) {

                                SendMessage sendMessage = new SendMessage();
                                sendMessage.setChatId(chatId);
                                sendMessage.setText("Введите имя правильно пожалуйста");
                                executeMessage(sendMessage);

                            } else if (questionHelperService.getQuestionHelperByChatId(chatId).getLanguage().equals("eng")) {

                                SendMessage sendMessage = new SendMessage();
                                sendMessage.setChatId(chatId);
                                sendMessage.setText("Please enter name correctly");
                                executeMessage(sendMessage);
                            }

                            return;
                        }

                        Reservation reservation = new Reservation(chatId, questionHelperService.getRestaurantByChatId(chatId), update.getMessage().getText());

                        reservationService.saveReservation(reservation);

                        sendQuestionsForReservation(chatId);

                    } else if (questionHelperService.getQuestionHelperByChatId(chatId).getKey() == 4) {

                        if (!datePattern.matcher(update.getMessage().getText()).matches()) {

                            if (questionHelperService.getQuestionHelperByChatId(chatId).getLanguage().equals("aze")) {

                                SendMessage sendMessage = new SendMessage();
                                sendMessage.setChatId(chatId);
                                sendMessage.setText("Tarixi duz qeyd edin zehmet olmasa");
                                executeMessage(sendMessage);

                            } else if (questionHelperService.getQuestionHelperByChatId(chatId).getLanguage().equals("rus")) {

                                SendMessage sendMessage = new SendMessage();
                                sendMessage.setChatId(chatId);
                                sendMessage.setText("Введите дату правильно пожалуйста");
                                executeMessage(sendMessage);

                            } else if (questionHelperService.getQuestionHelperByChatId(chatId).getLanguage().equals("eng")) {

                                SendMessage sendMessage = new SendMessage();
                                sendMessage.setChatId(chatId);
                                sendMessage.setText("Please enter date correctly");
                                executeMessage(sendMessage);
                            }

                            return;
                        }

                        reservationService.setDateInReservation(chatId, update.getMessage().getText());

                        sendQuestionsForReservation(chatId);

                    } else if (questionHelperService.getQuestionHelperByChatId(chatId).getKey() == 5) {

                        if (!timePattern.matcher(update.getMessage().getText()).matches()) {

                            if (questionHelperService.getQuestionHelperByChatId(chatId).getLanguage().equals("aze")) {

                                SendMessage sendMessage = new SendMessage();
                                sendMessage.setChatId(chatId);
                                sendMessage.setText("Vaxti duz qeyd edin zehmet olmasa");
                                executeMessage(sendMessage);

                            } else if (questionHelperService.getQuestionHelperByChatId(chatId).getLanguage().equals("rus")) {

                                SendMessage sendMessage = new SendMessage();
                                sendMessage.setChatId(chatId);
                                sendMessage.setText("Введите время правильно пожалуйста");
                                executeMessage(sendMessage);

                            } else if (questionHelperService.getQuestionHelperByChatId(chatId).getLanguage().equals("eng")) {

                                SendMessage sendMessage = new SendMessage();
                                sendMessage.setChatId(chatId);
                                sendMessage.setText("Please enter time correctly");
                                executeMessage(sendMessage);
                            }

                            return;
                        }

                        reservationService.setTimeInReservation(chatId, update.getMessage().getText());

                        sendQuestionsForReservation(chatId);

                    } else if (questionHelperService.getQuestionHelperByChatId(chatId).getKey() == 6) {

                        if (!numberPattern.matcher(update.getMessage().getText()).matches()) {

                            if (questionHelperService.getQuestionHelperByChatId(chatId).getLanguage().equals("aze")) {

                                SendMessage sendMessage = new SendMessage();
                                sendMessage.setChatId(chatId);
                                sendMessage.setText("Adam sayini duz qeyd edin zehmet olmasa");
                                executeMessage(sendMessage);

                            } else if (questionHelperService.getQuestionHelperByChatId(chatId).getLanguage().equals("rus")) {

                                SendMessage sendMessage = new SendMessage();
                                sendMessage.setChatId(chatId);
                                sendMessage.setText("Введите количество людей правильно пожалуйста");
                                executeMessage(sendMessage);

                            } else if (questionHelperService.getQuestionHelperByChatId(chatId).getLanguage().equals("eng")) {

                                SendMessage sendMessage = new SendMessage();
                                sendMessage.setChatId(chatId);
                                sendMessage.setText("Please enter number of people correctly");
                                executeMessage(sendMessage);
                            }

                            return;
                        }

                        reservationService.setQuantityInReservation(chatId, Integer.valueOf(update.getMessage().getText()));

                        sendQuestionsForReservation(chatId);

                    } else if (questionHelperService.getQuestionHelperByChatId(chatId).getKey() == 7) {

                        if (!numberPattern.matcher(update.getMessage().getText()).matches()) {

                            if (questionHelperService.getQuestionHelperByChatId(chatId).getLanguage().equals("aze")) {

                                SendMessage sendMessage = new SendMessage();
                                sendMessage.setChatId(chatId);
                                sendMessage.setText("Secdiyiniz stolu nomresini duz qeyd edin zehmet olmasa");
                                executeMessage(sendMessage);

                            } else if (questionHelperService.getQuestionHelperByChatId(chatId).getLanguage().equals("rus")) {

                                SendMessage sendMessage = new SendMessage();
                                sendMessage.setChatId(chatId);
                                sendMessage.setText("Введите номер стола правильно пожалуйста");
                                executeMessage(sendMessage);

                            } else if (questionHelperService.getQuestionHelperByChatId(chatId).getLanguage().equals("eng")) {

                                SendMessage sendMessage = new SendMessage();
                                sendMessage.setChatId(chatId);
                                sendMessage.setText("Please enter number of table correctly");
                                executeMessage(sendMessage);
                            }

                            return;
                        }

                        reservationService.setReservatedTableInReservation(chatId, Integer.valueOf(update.getMessage().getText()));

                        sendQuestionToGetPhoneNumber(chatId);

                    }

                }

            }

        } else if (update.hasCallbackQuery()) {

            String callBackData = update.getCallbackQuery().getData();

            long chatId = update.getCallbackQuery().getMessage().getChatId();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();

            if (callBackData.equals("aze")) {

                setLanguageToChatId(chatId, messageId, callBackData);

            } else if (callBackData.equals("rus")) {

                setLanguageToChatId(chatId, messageId, callBackData);

            } else if (callBackData.equals("eng")) {

                setLanguageToChatId(chatId, messageId, callBackData);
            }

            List<Restaurant> restaurantList = restaurantService.getAllRestaurants();

            for (Restaurant restaurant : restaurantList) {

                if (callBackData.equals(restaurant.getName())) {

                    questionHelperService.setRestaurantToChatId(chatId, restaurant.getName());

                    String url = restaurantService.getMenuOfRestaurant(questionHelperService.getRestaurantByChatId(chatId));

                    menuAndReservation(chatId, callBackData, messageId, url);
                }

            }

            if (callBackData.equals("reservation")) {

                questionHelperService.updateKeyInChatId(chatId);

                questionHelperService.updateStatusOfReservation(chatId);

                executeEditMessageText(chatId, messageId);
            }

        } else if (update.getMessage().hasContact()) {

            reservationService.setPhoneNumberInReservation(update.getMessage().getChatId(), update.getMessage().getContact().getPhoneNumber());

            sendQuestionsForReservation(update.getMessage().getChatId());

            reservationService.setStatusToReservation(update.getMessage().getChatId());
        }

    }

    //----------------------------------------------------------------------------------------------------------------

    public void languageSelection(long chatId) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText("Hansı dildə dəvam etmək istəyirsiniz ?");

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> inlineRows = new ArrayList<>();

        List<InlineKeyboardButton> inlineRow = new ArrayList<>();

        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("Aze");
        button1.setCallbackData("aze");
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText("Rus");
        button2.setCallbackData("rus");
        InlineKeyboardButton button3 = new InlineKeyboardButton();
        button3.setText("Eng");
        button3.setCallbackData("eng");

        inlineRow.add(button1);
        inlineRow.add(button2);
        inlineRow.add(button3);

        inlineRows.add(inlineRow);
        inlineKeyboardMarkup.setKeyboard(inlineRows);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

        executeMessage(sendMessage);
    }

    //----------------------------------------------------------------------------------------------------------------

    public void setLanguageToChatId(long chatId, long messageId, String callBackData) {

        questionHelperService.updateLanguageToChatId(chatId, callBackData);

        restaurantSelection(chatId, messageId);

    }

    //----------------------------------------------------------------------------------------------------------------

    public void restaurantSelection(long chatId, long messageId) {

        EditMessageText editMessageText = new EditMessageText(reservationProcess(chatId));
        editMessageText.setChatId(chatId);
        editMessageText.setMessageId((int) messageId);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> inlineRows = new ArrayList<>();

        List<Restaurant> restaurantList = restaurantService.getAllRestaurants();

        for (Restaurant restaurant : restaurantList) {

            List<InlineKeyboardButton> inlineRow = new ArrayList<>();

            InlineKeyboardButton button = new InlineKeyboardButton();

            button.setText(restaurant.getName());
            button.setCallbackData(restaurant.getName());

            inlineRow.add(button);
            inlineRows.add(inlineRow);
        }

        inlineKeyboardMarkup.setKeyboard(inlineRows);
        editMessageText.setReplyMarkup(inlineKeyboardMarkup);

        executeMessage(editMessageText);
    }

    //----------------------------------------------------------------------------------------------------------------

    public void menuAndReservation(long chatId, String text, long messageId, String url) {

        EditMessageText editMessageText = new EditMessageText(text);
        editMessageText.setChatId(chatId);
        editMessageText.setMessageId((int) messageId);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> inlineRows = new ArrayList<>();

        List<InlineKeyboardButton> inlineRow = new ArrayList<>();

        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("Menu");
        button1.setCallbackData("menu");
        button1.setUrl(url);
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText("Reservation");
        button2.setCallbackData("reservation");

        inlineRow.add(button1);
        inlineRow.add(button2);
        inlineRows.add(inlineRow);

        inlineKeyboardMarkup.setKeyboard(inlineRows);
        editMessageText.setReplyMarkup(inlineKeyboardMarkup);

        executeMessage(editMessageText);
    }

    //----------------------------------------------------------------------------------------------------------------

    public String reservationProcess(long chatId) {

        return questionService.getQuestionByKeyAndLanguage(questionHelperService.getQuestionHelperByChatId(chatId).getKey(), questionHelperService.getQuestionHelperByChatId(chatId).getLanguage()).getQuestion();
    }

    //----------------------------------------------------------------------------------------------------------------

    public void sendQuestionsForReservation(long chatId) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(reservationProcess(chatId));
        executeMessage(sendMessage);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        questionHelperService.updateKeyInChatId(chatId);
    }

    //----------------------------------------------------------------------------------------------------------------

    public void sendQuestionToGetPhoneNumber(long chatId) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(reservationProcess(chatId));

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();

        row.add(KeyboardButton.builder().text("My phone number").requestContact(true).build());
        keyboardRows.add(row);

        replyKeyboardMarkup.setKeyboard(keyboardRows);

        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        executeMessage(sendMessage);

        questionHelperService.updateKeyInChatId(chatId);
    }

    // ----------------------------------------------------------------------------------------------------------------

    private void executeEditMessageText(long chatId, long messageId) {

        EditMessageText editMessageText = new EditMessageText(reservationProcess(chatId));
        editMessageText.setChatId(chatId);
        editMessageText.setMessageId((int) messageId);
        executeMessage(editMessageText);

        questionHelperService.updateKeyInChatId(chatId);
    }

    //----------------------------------------------------------------------------------------------------------------

    public void executeMessage(BotApiMethod sendMessage) {

        try {
            execute(sendMessage);
        } catch (TelegramApiException telegramApiException) {

        }

    }

    // ----------------------------------------------------------------------------------------------------------------

    //
    //
    //
    //
    //
    //
    //
    //

    // ----------------------------------------------------------------------------------------------------------------

}
