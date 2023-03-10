package com.example.tgbotwithnihad.repository;

import com.example.tgbotwithnihad.model.Reservation;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    Reservation findByChatId(long chatId);

    Reservation findAllByStatus(int status);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "update Reservation r set r.dateOfReservation = :dateOfReservation where r.chatId = :chatId")
    void updateDate(@Param("chatId") long chatId, @Param("dateOfReservation") String dateOfReservation);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "update Reservation r set r.numberOfPeople = :numberOfPeople where r.chatId = :chatId")
    void updateQuantity(@Param("chatId") long chatId, @Param("numberOfPeople") Integer numberOfPeople);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "update Reservation r set r.reservatedTable = :reservatedTable where r.chatId = :chatId")
    void updateReservatedTable(@Param("chatId") long chatId, @Param("reservatedTable") Integer reservatedTable);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "update Reservation r set r.phone = :phone where r.chatId = :chatId")
    void updatePhoneNumber(@Param("chatId") long chatId, @Param("phone") String phone);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "update Reservation r set r.status = :status where r.chatId = :chatId")
    void updateStatus(@Param("chatId") long chatId, @Param("status") Integer status);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "update Reservation r set r.timeOfReservation = :timeOfReservation where r.chatId = :chatId")
    void updateTime(@Param("chatId") long chatId, @Param("timeOfReservation") String timeOfReservation);

}
