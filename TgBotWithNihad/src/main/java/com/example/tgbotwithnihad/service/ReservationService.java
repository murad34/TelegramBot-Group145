package com.example.tgbotwithnihad.service;

import com.example.tgbotwithnihad.model.Reservation;
import com.example.tgbotwithnihad.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public Reservation getReservationByChatId(long chatId) {
        return reservationRepository.findByChatId(chatId);
    }

    public void saveReservation(Reservation reservation) {
        reservationRepository.save(reservation);
    }

    public void setDateInReservation(long chatId, String date) {
        reservationRepository.updateDate(chatId, date);
    }

    public void setTimeInReservation(long chatId, String time) {
        reservationRepository.updateTime(chatId, time);
    }

    public void setQuantityInReservation(long chatId, Integer number) {
        reservationRepository.updateQuantity(chatId, number);
    }

    public void setReservatedTableInReservation(long chatId, Integer number) {
        reservationRepository.updateReservatedTable(chatId, number);
    }

    public void deleteReservation(long chatId) {
        reservationRepository.delete(getReservationByChatId(chatId));
    }

    public void setPhoneNumberInReservation(long chatId, String phoneNumber) {
        reservationRepository.updatePhoneNumber(chatId, phoneNumber);
    }

    public void setStatusToReservation(long chatId) {
        reservationRepository.updateStatus(chatId, 0);
    }

}
