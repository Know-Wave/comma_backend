package com.know_wave.comma.comma_backend.arduino.repository;

import com.know_wave.comma.comma_backend.account.entity.Account;
import com.know_wave.comma.comma_backend.arduino.entity.OrderInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface OrderInfoRepository extends CrudRepository<OrderInfo, String> {

    @Query("select oi " +
            "from OrderInfo oi " +
            "join fetch oi.orders oo " +
            "join fetch oo.arduino " +
            "join fetch oo.arduino.categories " +
            "where oi.account = :account")
    List<OrderInfo> findAllFetchOrderAndArduino(Account account);

}
