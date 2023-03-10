package com.example.tgbotwithnihad.repository;

import com.example.tgbotwithnihad.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Integer> {

    Question findByKeyAndLanguage(Integer key, String language);

}
