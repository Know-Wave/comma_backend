package com.know_wave.comma.comma_backend.arduino.controller;

import com.know_wave.comma.comma_backend.arduino.dto.basket.BasketDeleteRequest;
import com.know_wave.comma.comma_backend.arduino.dto.basket.BasketRequest;
import com.know_wave.comma.comma_backend.arduino.dto.basket.BasketResponse;
import com.know_wave.comma.comma_backend.arduino.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/basket")
public class BasketController {

    private final OrderService orderService;

    public BasketController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<BasketResponse> getBasket() {
        return orderService.getBasket();
    }

    @PostMapping("/arduino")
    public String addArduinoToBasket(@Valid @RequestBody BasketRequest request) {
        orderService.addArduinoToBasket(request);
        return "Added arduino to basket";
    }

    @DeleteMapping("/arduino")
    public String deleteArduinoFromBasket(@Valid @RequestBody BasketDeleteRequest request) {
        orderService.deleteArduinoFromBasket(request);
        return "Deleted arduino from basket";
    }

    @PatchMapping("/arduino")
    public String updateArduinoFromBasket(@Valid @RequestBody BasketRequest request) {
        orderService.updateArduinoFromBasket(request);
        return "Updated arduino from basket";
    }

    @DeleteMapping
    public String emptyBasket() {
        orderService.emptyBasket();
        return "Deleted all arduino from basket";
    }
}
