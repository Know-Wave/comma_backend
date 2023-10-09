package com.know_wave.comma.comma_backend.arduino.controller;

import com.know_wave.comma.comma_backend.arduino.dto.order.*;
import com.know_wave.comma.comma_backend.arduino.service.normal.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/order/arduino")
    public String order(@Valid @RequestBody OrderRequest request) {
        orderService.order(request);
        return "Ordered";
    }

    @PostMapping("/orders/{orderCode}/arduinos/{arduinoId}")
    public String addOrder(@PathVariable("orderCode") String orderCode,
                           @PathVariable("arduinoId") Long id,
                           @Valid @RequestBody OrderAdditionalRequest request) {
//        orderService.addOrder(orderCode, id, request);
        return "Added order";
    }

    @GetMapping("/orders")
    public List<OrderResponse> getOrders() {
        return orderService.getOrders();
    }

    @GetMapping("/orders/{orderCode}")
    public OrderDetailResponse getOrderDetail(@PathVariable("orderCode") String code) {
        return orderService.getOrderDetail(code);
    }

    @PatchMapping("/orders/{orderCode}")
    public String cancelOrderRequest(@PathVariable("orderCode") String code, @Valid @RequestBody OrderCancelRequest request) {
        orderService.cancelOrderRequest(code, request);
        return "Requested order cancellation";
    }
}
