package com.example.tgbotwithnihad.repository;

import com.example.tgbotwithnihad.model.QuestionHelper;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionHelperRepository extends JpaRepository<QuestionHelper, Integer> {

      QuestionHelper findByChatId(long chatId);

      @Modifying(clearAutomatically = true)
      @Transactional
      @Query(value = "update QuestionHelper qh set qh.restaurant = :restaurant where qh.chatId = :chatId")
      void updateRestaurant(@Param("chatId") long chatId, @Param("restaurant") String restaurant);

      @Modifying(clearAutomatically = true)
      @Transactional
      @Query(value = "update QuestionHelper qh set qh.language = :language where qh.chatId = :chatId")
      void updateLanguage(@Param("chatId") long chatId, @Param("language") String language);

      @Modifying(clearAutomatically = true)
      @Transactional
      @Query(value = "update QuestionHelper qh set qh.reservation = :reservation where qh.chatId = :chatId")
      void updateReservation(@Param("chatId") long chatId, @Param("reservation") int reservation);

      @Modifying(clearAutomatically = true)
      @Transactional
      @Query(value = "update QuestionHelper qh set qh.key = :key where qh.chatId = :chatId")
      void updateKey(@Param("chatId") long chatId, @Param("key") int key);

}
