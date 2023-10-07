package com.know_wave.comma.comma_backend.arduino.controller;

import com.know_wave.comma.comma_backend.arduino.dto.ArduinoResponse;
import com.know_wave.comma.comma_backend.arduino.service.ArduinoService;
import com.know_wave.comma.comma_backend.arduino.service.OrderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/arduinos")
public class ArduinoListController {

    private final ArduinoService arduinoService;
    private final OrderService orderService;

    @Value("${spring.data.web.pageable.default-page-size}")
    private int defaultPageSize;

    public ArduinoListController(ArduinoService arduinoService, OrderService orderService) {
        this.arduinoService = arduinoService;
        this.orderService = orderService;
    }

    @GetMapping
    public Page<ArduinoResponse> getArduinoList() {
        return arduinoService.getFirstPage(defaultPageSize);
    }

    @GetMapping("/{page}")
    public Page<ArduinoResponse> getArduinoList(@PathVariable("page") int page) {
        return arduinoService.getPage(PageRequest.of(page, defaultPageSize));
    }

}
