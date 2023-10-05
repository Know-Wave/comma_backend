package com.know_wave.comma.comma_backend.arduino.entity;

import com.know_wave.comma.comma_backend.account.entity.Account;
import jakarta.persistence.*;

@Entity
public class Like {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arduino_id")
    private Arduino arduino;
}
