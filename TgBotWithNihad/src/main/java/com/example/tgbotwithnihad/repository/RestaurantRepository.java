package com.example.tgbotwithnihad.repository;

import com.example.tgbotwithnihad.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {

    Restaurant findByName(String restaurant);

}
