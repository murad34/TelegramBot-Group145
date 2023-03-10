package com.example.tgbotwithnihad.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "questionHelper")
public class QuestionHelper {

    @Id
    private Long chatId;

    private Integer key;

    private String language;

    private String restaurant;

    private Integer reservation;

    public QuestionHelper(Long chatId, Integer key) {
        this.chatId = chatId;
        this.key = key;
        this.language = language;
    }

    public QuestionHelper(Long chatId, String restaurant) {
        this.chatId = chatId;
        this.restaurant = restaurant;
    }

}
