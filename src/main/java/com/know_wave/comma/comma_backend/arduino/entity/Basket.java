package com.know_wave.comma.comma_backend.arduino.entity;

import com.know_wave.comma.comma_backend.account.entity.Account;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.util.List;

@Entity
public class Basket {

    protected Basket() {}

    public Basket(Account account, Arduino arduino, int arduinoCount) {
        this.account = account;
        this.arduino = arduino;
        this.arduinoCount = arduinoCount;
    }

    @Id
    @GeneratedValue
    @Column(name = "basket_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arduino_id")
    private Arduino arduino;

    @Min(0)
    @Max(10)
    private int arduinoCount;

    public static boolean isOverRequest(int orderCount, int leftCount) {
        return orderCount > leftCount;
    }

    public static boolean isOverRequest(List<Basket> baskets) {
        return baskets.stream().anyMatch(basket -> basket.getArduinoCount() > basket.getArduino().getCount());
    }

    public static List<Order> toOrders(List<Basket> baskets, Account account, String orderNumber) {
        return baskets.stream()
                .map(basket -> new Order(account, basket.getArduino(), orderNumber, basket.getArduinoCount()))
                .toList();
    }

    public Arduino getArduino() {
        return arduino;
    }

    public int getArduinoCount() {
        return arduinoCount;
    }

    public void setArduinoCount(int count) {
        this.arduinoCount = count;
    }
}
