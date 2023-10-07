package com.know_wave.comma.comma_backend.arduino.entity;

import com.know_wave.comma.comma_backend.account.entity.Account;
import jakarta.persistence.*;

@Entity
@Table(name = "arduino_like")
public class Like {

    @Id
    @GeneratedValue
    @Column(name = "arduino_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arduino_id")
    private Arduino arduino;
}
