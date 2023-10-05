package com.know_wave.comma.comma_backend.arduino.repository;

import com.know_wave.comma.comma_backend.arduino.entity.Basket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasketRepository extends JpaRepository<Basket, Long> {
}
