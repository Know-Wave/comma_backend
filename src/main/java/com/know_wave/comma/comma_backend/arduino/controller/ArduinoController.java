package com.know_wave.comma.comma_backend.arduino.controller;

import com.know_wave.comma.comma_backend.arduino.dto.ArduinoResponse;
import com.know_wave.comma.comma_backend.arduino.service.ArduinoService;
import com.know_wave.comma.comma_backend.arduino.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/arduino")
public class ArduinoController {

    private final ArduinoService arduinoService;
    private final OrderService orderService;

    public ArduinoController(ArduinoService arduinoService, OrderService orderService) {
        this.arduinoService = arduinoService;
        this.orderService = orderService;
    }

    @GetMapping("/{id}")
    public ArduinoResponse getArduino(@PathVariable("id") Long id) {
        return arduinoService.getOne(id);
    }
}
