package com.know_wave.comma.comma_backend.arduino.dto.order;

import java.util.List;
import java.util.Map;

public class OrderResponseGroup {

    List<OrderResponse> userOrderList;

    public static List<OrderResponseGroup> ofList(Map<String, List<OrderResponse>> groupedOrderResponses) {
        return  groupedOrderResponses.values().stream()
                .map(OrderResponseGroup::of)
                .toList();
    }

    private static OrderResponseGroup of(List<OrderResponse> userOrderList) {
        return new OrderResponseGroup(userOrderList);
    }

    public OrderResponseGroup(List<OrderResponse> userOrderList) {
        this.userOrderList = userOrderList;
    }

    public List<OrderResponse> getUserOrderList() {
        return userOrderList;
    }
}
