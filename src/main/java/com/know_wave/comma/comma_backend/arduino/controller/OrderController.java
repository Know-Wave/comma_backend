package com.know_wave.comma.comma_backend.arduino.controller;

import com.know_wave.comma.comma_backend.arduino.dto.OrderRequest;
import com.know_wave.comma.comma_backend.arduino.dto.OrderDetailResponse;
import com.know_wave.comma.comma_backend.arduino.dto.OrderResponse;
import com.know_wave.comma.comma_backend.arduino.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/order")
    public String order(@Valid @RequestBody OrderRequest request) {
        orderService.order(request);
        return "Ordered";
    }

    @GetMapping("/orders")
    public List<OrderResponse> getOrders() {
        return orderService.getOrders();
    }

    @GetMapping("/orders/{orderNumber}")
    public OrderDetailResponse getOrderDetail(@PathVariable("orderNumber") String id) {
        return orderService.getOrderDetail(id);
    }

    @PatchMapping("/orders/{orderNumber}")
    public String cancelOrderRequest(@PathVariable("orderNumber") String id) {
        orderService.cancelOrderRequest(id);
        return "Requested order cancellation";
    }
}
