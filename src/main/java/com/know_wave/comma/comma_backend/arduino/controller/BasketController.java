package com.know_wave.comma.comma_backend.arduino.controller;

import com.know_wave.comma.comma_backend.arduino.dto.ArduinoBasketRequest;
import com.know_wave.comma.comma_backend.arduino.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/basket")
public class BasketController {

    private final OrderService orderService;

    public BasketController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/arduino")
    public String addArduinoToBasket(@Valid @RequestBody ArduinoBasketRequest request) {
        orderService.addArduinoToBasket(request);
        return "Added arduino to basket";
    }

    @DeleteMapping("/arduino")
    public String deleteArduinoFromBasket(@Valid @RequestBody ArduinoBasketRequest request) {
        orderService.deleteArduinoFromBasket(request);
        return "Deleted arduino from basket";
    }

    @PatchMapping("/arduino")
    public String updateArduinoFromBasket(@Valid @RequestBody ArduinoBasketRequest request) {
        orderService.updateArduinoFromBasket(request);
        return "Updated arduino from basket";
    }

    @DeleteMapping("/arduinos")
    public String emptyBasket(@Valid @RequestBody ArduinoBasketRequest request) {
        orderService.emptyBasket();
        return "Deleted all arduino from basket";
    }
}
