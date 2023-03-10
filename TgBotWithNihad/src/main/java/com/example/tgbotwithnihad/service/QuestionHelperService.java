package com.example.tgbotwithnihad.service;

import com.example.tgbotwithnihad.model.QuestionHelper;
import com.example.tgbotwithnihad.repository.QuestionHelperRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionHelperService {

    private final QuestionHelperRepository questionHelperRepository;

    public QuestionHelper getQuestionHelperByChatId(long chatId) {
        return questionHelperRepository.findByChatId(chatId);
    }

    public void saveQuestionHelper(QuestionHelper questionHelper) {
        questionHelperRepository.save(questionHelper);
    }

    public void updateLanguageToChatId(long chatId, String language) {
        questionHelperRepository.updateLanguage(chatId, language);
    }


    public void setRestaurantToChatId(long chatId, String restaurant) {
        questionHelperRepository.updateRestaurant(chatId, restaurant);
    }

    public String getRestaurantByChatId(long chatId) {
        return questionHelperRepository.findByChatId(chatId).getRestaurant();
    }

    public void updateStatusOfReservation(long chatId) {
        questionHelperRepository.updateReservation(chatId, 1);
    }

    public void updateKeyInChatId(long chatId) {
        questionHelperRepository.updateKey(chatId, getQuestionHelperByChatId(chatId).getKey()+1);
    }

    public void deleteChatId(long chatId) {
        questionHelperRepository.delete(getQuestionHelperByChatId(chatId));
    }

}
