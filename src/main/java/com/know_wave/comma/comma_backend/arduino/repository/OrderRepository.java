package com.know_wave.comma.comma_backend.arduino.repository;

import com.know_wave.comma.comma_backend.arduino.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
