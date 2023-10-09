package com.know_wave.comma.comma_backend.arduino.controller;

import com.know_wave.comma.comma_backend.arduino.dto.order.OrderCancelRequest;
import com.know_wave.comma.comma_backend.arduino.dto.order.OrderRequest;
import com.know_wave.comma.comma_backend.arduino.dto.order.OrderDetailResponse;
import com.know_wave.comma.comma_backend.arduino.dto.order.OrderResponse;
import com.know_wave.comma.comma_backend.arduino.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/order/arduinos")
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
    public String cancelOrderRequest(@PathVariable("orderNumber") String id, @Valid @RequestBody OrderCancelRequest request) {
        orderService.cancelOrderRequest(id, request);
        return "Requested order cancellation";
    }
}
