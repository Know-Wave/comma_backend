package com.know_wave.comma.comma_backend.arduino.repository;

import com.know_wave.comma.comma_backend.account.entity.Account;
import com.know_wave.comma.comma_backend.arduino.entity.OrderInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface OrderInfoRepository extends CrudRepository<OrderInfo, String> {

    List<OrderInfo> findAllByAccount(Account account);

    @Query("select oi " +
            "from OrderInfo oi " +
            "where oi.account in :list")
    List<OrderInfo> findAllByRelatedAccount(Set<Account> list);

    @Query("select oi " +
            "from OrderInfo oi " +
            "join fetch oi.orders oo " +
            "join fetch oo.arduino " +
            "where oi.orderNumber = :orderNumber")
    Optional<OrderInfo> findFetchById(String orderNumber);
    // Queries in descending order of order creation date

    @Query("select oi " +
            "from OrderInfo oi " +
            "join fetch oi.account " +
            "order by oi.createdDate desc")
    Page<OrderInfo> findAllFetchAccount(Pageable pageable);
}
