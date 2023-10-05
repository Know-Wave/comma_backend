package com.know_wave.comma.comma_backend.arduino.repository;

import com.know_wave.comma.comma_backend.arduino.entity.OrderDescription;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface OrderDescriptionRepository extends CrudRepository<OrderDescription, Long> {
    Optional<OrderDescription> findByOrderNumber(String orderNumber);
}
