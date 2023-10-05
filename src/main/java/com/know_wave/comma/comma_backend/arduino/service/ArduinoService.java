package com.know_wave.comma.comma_backend.arduino.service;

import com.know_wave.comma.comma_backend.account.entity.Account;
import com.know_wave.comma.comma_backend.account.repository.AccountRepository;
import com.know_wave.comma.comma_backend.arduino.dto.ArduinoBasketRequest;
import com.know_wave.comma.comma_backend.arduino.dto.OrderRequest;
import com.know_wave.comma.comma_backend.arduino.dto.OrderResponse;
import com.know_wave.comma.comma_backend.arduino.entity.Arduino;
import com.know_wave.comma.comma_backend.arduino.entity.Basket;
import com.know_wave.comma.comma_backend.arduino.entity.Order;
import com.know_wave.comma.comma_backend.arduino.entity.OrderDescription;
import com.know_wave.comma.comma_backend.arduino.repository.ArduinoRepository;
import com.know_wave.comma.comma_backend.arduino.repository.BasketRepository;
import com.know_wave.comma.comma_backend.arduino.repository.OrderDescriptionRepository;
import com.know_wave.comma.comma_backend.arduino.repository.OrderRepository;
import com.know_wave.comma.comma_backend.exception.EntityAlreadyExistException;
import com.know_wave.comma.comma_backend.util.GenerateCodeUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.know_wave.comma.comma_backend.util.ExceptionMessageSource.*;

@Service
@Transactional
public class ArduinoService {

    private final AccountRepository accountRepository;
    private final ArduinoRepository arduinoRepository;
    private final BasketRepository basketRepository;
    private final OrderRepository orderRepository;
    private final OrderDescriptionRepository orderDescriptionRepository;


    public ArduinoService(AccountRepository accountRepository, ArduinoRepository arduinoRepository, BasketRepository basketRepository, OrderRepository orderRepository, OrderDescriptionRepository orderDescriptionRepository) {
        this.accountRepository = accountRepository;
        this.arduinoRepository = arduinoRepository;
        this.basketRepository = basketRepository;
        this.orderRepository = orderRepository;
        this.orderDescriptionRepository = orderDescriptionRepository;
    }

    public void addArduinoToBasket(ArduinoBasketRequest request) {

        Account account = getAccount(request.getAccountId());
        Arduino arduino = getArduino(request.getArduinoId());
        int orderCount = request.getCount();

        if (Basket.isOverRequest(orderCount, arduino.getCount())) {
            throw new IllegalArgumentException(NOT_ACCEPTABLE_REQUEST);
        }

        basketRepository.findByAccountAndArduino(account, arduino)
                .ifPresentOrElse(basket ->
                        {throw new EntityAlreadyExistException(ALREADY_IN_BASKET);},
                        () -> basketRepository.save(new Basket(account, arduino, orderCount)));
    }

    public void deleteArduinoFromBasket(ArduinoBasketRequest request) {

        Account account = getAccount(request.getAccountId());
        Arduino arduino = getArduino(request.getArduinoId());

        basketRepository.findByAccountAndArduino(account, arduino)
                .ifPresentOrElse(basketRepository::delete,
                                () -> basketRepository.save(new Basket(account, arduino, request.getCount())));
    }

    public void updateArduinoFromBasket(ArduinoBasketRequest request) {

        Account account = getAccount(request.getAccountId());
        Arduino arduino = getArduino(request.getArduinoId());

        basketRepository.findByAccountAndArduino(account, arduino)
                .ifPresentOrElse(basket ->
                    basket.setArduinoCount(request.getCount()),
                    () -> {throw new EntityNotFoundException(NOT_FOUND_VALUE);});
    }

    public void emptyBasket(String accountId) {
        Account account = getAccount(accountId);
        List<Basket> basket = getBaskets(account);

        basketRepository.deleteAll(basket);
    }

    public void order(OrderRequest request) {

        Account account = getAccount(request.getAccountId());
        List<Basket> baskets = getBasketsFetchArduino(account);

        if (Basket.isOverRequest(baskets)) {
            throw new IllegalArgumentException(NOT_ACCEPTABLE_REQUEST);
        }

        String orderNumber = GenerateCodeUtils.getCodeByIdWithDate(account.getId());

        OrderDescription orderDescription = new OrderDescription(request.getDescription(), orderNumber, request.getPurpose());

        List<Order> orders = Basket.toOrders(baskets, account, orderNumber);

        orderRepository.saveAll(orders);
        orderDescriptionRepository.save(orderDescription);
        basketRepository.deleteAll(baskets);
    }

    public List<OrderResponse> getOrder(String accountId) {

        List<OrderResponse> result = new ArrayList<>();

        Account account = getAccount(accountId);
        List<Order> AllOrders = getApplyStatusOrders(account);

        // grouping by orderNumber
        Map<String, List<Order>> orderedMap = Order.grouping(AllOrders);

        // make OrderResponse List
        // OrderResponse has own meta info, arduino list
        // arduino of OrderResponse is implemented inner class (1 OrderResponse : N arduino types)
        // if user has been ordered twice, OrderResponse count is 2 (each OrderResponse has N arduino types)
        orderedMap.keySet()
                .forEach(key -> {
                    List<Order> orders = orderedMap.get(key);
                    Order groupedOrder = orders.get(0);

                    OrderDescription orderDescription = orderDescriptionRepository.findByOrderNumber(groupedOrder.getOrderNumber())
                            .orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_VALUE));

                    List<OrderResponse.Arduino> arduinoList = new ArrayList<>();

                    OrderResponse orderResponse = new OrderResponse(accountId,
                            orderDescription.getDescription(),
                            groupedOrder.getCreatedDate(),
                            groupedOrder.getStatus().getStatus(),
                            groupedOrder.getOrderNumber(),
                            orderDescription.getPurpose(),
                            arduinoList);

                    orders.forEach(order -> arduinoList.add(
                            new OrderResponse.Arduino(order.getArduino().getId(),
                            order.getArduino().getName(),
                            order.getCount())));

                    result.add(orderResponse);
        });

        return result;
    }

    private List<Order> getApplyStatusOrders(Account account) {
        List<Order> allOrders = orderRepository.findAllApplyStatusByAccount(account);

        if (allOrders.isEmpty()) {
            throw new EntityNotFoundException(NOT_EXIST_VALUE);
        }

        return allOrders;
    }

    private List<Basket> getBaskets(Account account) {
        List<Basket> arduinoList = basketRepository.findAllByAccount(account);

        if (arduinoList.isEmpty()) {
            throw new EntityNotFoundException(NOT_EXIST_VALUE);
        }
        return arduinoList;
    }

    private List<Basket> getBasketsFetchArduino(Account account) {
        List<Basket> arduinoList = basketRepository.findAllFetchArduinoByAccount(account);

        if (arduinoList.isEmpty()) {
            throw new EntityNotFoundException(NOT_EXIST_VALUE);
        }
        return arduinoList;
    }

    private Account getAccount(String accountId) {
        return accountRepository.findById(accountId).orElseThrow(() -> new EntityNotFoundException(NOT_EXIST_ACCOUNT));
    }

    private Arduino getArduino(Long arduinoId) {
        return arduinoRepository.findById(arduinoId).orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_ARDUINO));
    }


}
