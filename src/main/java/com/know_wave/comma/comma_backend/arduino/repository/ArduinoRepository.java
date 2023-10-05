package com.know_wave.comma.comma_backend.arduino.repository;

import com.know_wave.comma.comma_backend.arduino.entity.Arduino;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArduinoRepository extends JpaRepository<Arduino, Long> {
}
