package com.example.tgbotwithnihad.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "reservations")
public class Reservation {

    @Id
    private Long chatId;

    private String restaurant;

    private String name;

    private String dateOfReservation;

    private String timeOfReservation;

    private Integer numberOfPeople;

    private Integer reservatedTable;

    private String phone;

    private Integer status;

    public Reservation(Long chatId, String restaurant, String name) {
        this.chatId = chatId;
        this.restaurant = restaurant;
        this.name = name;
    }
}
