package com.know_wave.comma.comma_backend.arduino.entity;
import jakarta.persistence.*;

import java.util.List;

import static java.util.stream.Collectors.groupingBy;

@Entity
@Table(name = "arduino_order")
public class Order{

    protected Order() {}

    public Order(OrderInfo orderInfo, Arduino arduino, int count) {
        setOrderInfo(orderInfo);
        this.arduino = arduino;
        this.count = count;
    }

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arduino_id")
    private Arduino arduino;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_number")
    private OrderInfo orderInfo;

    private int count;

    public static List<Order> ofList(List<Basket> baskets, OrderInfo orderInfo) {
        return baskets.stream()
                .map(basket -> new Order(orderInfo, basket.getArduino(), basket.getArduinoCount()))
                .toList();
    }

    public void setOrderInfo(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
        orderInfo.getOrders().add(this);
    }

    public OrderInfo getOrderDescription() {
        return orderInfo;
    }

    public Long getId() {
        return id;
    }

    public Arduino getArduino() {
        return arduino;
    }

    public int getCount() {
        return count;
    }

}
