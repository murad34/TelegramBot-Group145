package com.example.tgbotwithnihad.service;

import com.example.tgbotwithnihad.model.Restaurant;
import com.example.tgbotwithnihad.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    @Autowired
    private final RestaurantRepository restaurantRepository;

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    public String getMenuOfRestaurant(String restaurant) {
        return restaurantRepository.findByName(restaurant).getMenu();
    }

}
