package com.know_wave.comma.comma_backend.arduino.entity;

import com.know_wave.comma.comma_backend.account.entity.Account;
import com.know_wave.comma.comma_backend.util.entity.BaseTimeEntity;
import jakarta.persistence.*;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@Entity
public class Order extends BaseTimeEntity {

    protected Order() {}

    public Order(Account account, Arduino arduino, String orderNumber, int count) {
        this.account = account;
        this.arduino = arduino;
        this.orderNumber = orderNumber;
        this.count = count;
        this.status = OrderStatus.APPLIED;
    }

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arduino_id")
    private Arduino arduino;

    private String orderNumber;

    private int count;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public static Map<String, List<Order>> grouping(List<Order> orders) {
        return orders.stream()
                .collect(groupingBy(Order::getOrderNumber));
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public Long getId() {
        return id;
    }

    public Account getAccount() {
        return account;
    }

    public Arduino getArduino() {
        return arduino;
    }

    public int getCount() {
        return count;
    }

    public OrderStatus getStatus() {
        return status;
    }
}
