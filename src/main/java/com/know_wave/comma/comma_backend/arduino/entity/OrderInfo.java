package com.know_wave.comma.comma_backend.arduino.entity;

import com.know_wave.comma.comma_backend.account.entity.Account;
import com.know_wave.comma.comma_backend.util.entity.BaseTimeEntity;
import jakarta.persistence.*;
import org.springframework.data.domain.Persistable;

import java.util.ArrayList;
import java.util.List;

@Entity
public class OrderInfo extends BaseTimeEntity implements Persistable<String> {

    protected OrderInfo() {}

    public OrderInfo(Account account, String orderNumber, String subject, String description) {
        this.account = account;
        this.orderNumber = orderNumber;
        this.subject = subject;
        this.description = description;
        this.status = OrderStatus.APPLIED;
        this.orders = new ArrayList<>();
    }

    @Id
    @Column(name = "order_number")
    private String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToMany(mappedBy = "orderInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders;

    private String description;

    private String subject;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public boolean isCancellable() {
        return status == OrderStatus.APPLIED || status == OrderStatus.PREPARING;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public String getDescription() {
        return description;
    }

    public String getSubject() {
        return subject;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Account getAccount() {
        return account;
    }

    @Override
    public String getId() {
        return orderNumber;
    }

    @Override
    public boolean isNew() {
        return getCreatedDate() == null;
    }
}
