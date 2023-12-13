package know_wave.comma.arduino.order.repository;

import know_wave.comma.account.entity.Account;
import know_wave.comma.arduino.order.entity.Order;
import know_wave.comma.arduino.order.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o join fetch o.deposit d, d.payment p where o.orderNumber = :orderNumber")
    Optional<Order> findFetchByOrderNumber(String orderNumber);

    @Query("select o from Order o join fetch o.deposit d, d.payment p where o.account = :account")
    Page<Order> findAllByAccount(Account account, Pageable pageable);

    @Query("select o from Order o join fetch o.deposit d, d.payment p where o.orderStatus = :orderStatus")
    Page<Order> findAllByOrderStatus(OrderStatus orderStatus, Pageable pageable);
}
