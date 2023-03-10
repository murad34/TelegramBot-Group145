package com.example.tgbotwithnihad.service;

import com.example.tgbotwithnihad.model.Question;
import com.example.tgbotwithnihad.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    public Question getQuestionByKeyAndLanguage(Integer key, String language) {
        return questionRepository.findByKeyAndLanguage(key, language);
    }

}
