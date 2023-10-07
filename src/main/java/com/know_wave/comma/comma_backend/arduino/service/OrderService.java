package com.know_wave.comma.comma_backend.arduino.service;

import com.know_wave.comma.comma_backend.account.entity.Account;
import com.know_wave.comma.comma_backend.account.repository.AccountRepository;
import com.know_wave.comma.comma_backend.arduino.dto.ArduinoBasketRequest;
import com.know_wave.comma.comma_backend.arduino.dto.OrderRequest;
import com.know_wave.comma.comma_backend.arduino.dto.OrderResponse;
import com.know_wave.comma.comma_backend.arduino.entity.*;
import com.know_wave.comma.comma_backend.arduino.repository.ArduinoRepository;
import com.know_wave.comma.comma_backend.arduino.repository.BasketRepository;
import com.know_wave.comma.comma_backend.arduino.repository.OrderInfoRepository;
import com.know_wave.comma.comma_backend.arduino.repository.OrderRepository;
import com.know_wave.comma.comma_backend.exception.EntityAlreadyExistException;
import com.know_wave.comma.comma_backend.util.GenerateCodeUtils;
import com.know_wave.comma.comma_backend.util.ValidateUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.know_wave.comma.comma_backend.util.ExceptionMessageSource.*;

@Service
@Transactional
public class OrderService {

    private final AccountRepository accountRepository;
    private final ArduinoRepository arduinoRepository;
    private final BasketRepository basketRepository;
    private final OrderRepository orderRepository;
    private final OrderInfoRepository orderInfoRepository;


    public OrderService(AccountRepository accountRepository, ArduinoRepository arduinoRepository, BasketRepository basketRepository, OrderRepository orderRepository, OrderInfoRepository orderInfoRepository) {
        this.accountRepository = accountRepository;
        this.arduinoRepository = arduinoRepository;
        this.basketRepository = basketRepository;
        this.orderRepository = orderRepository;
        this.orderInfoRepository = orderInfoRepository;
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
        List<Basket> baskets = getBasketsFetchArduinoAndAccount(account);

        if (Basket.isOverRequest(baskets)) {
            throw new IllegalArgumentException(NOT_ACCEPTABLE_REQUEST);
        }

        String orderNumber = GenerateCodeUtils.getCodeByIdWithDate(account.getId());
        OrderInfo orderInfo = new OrderInfo(account, orderNumber, request.getSubject(), request.getDescription());

        List<Order> orders = Basket.toOrders(baskets, orderInfo);

        orderRepository.saveAll(orders);
        orderInfoRepository.save(orderInfo);
        basketRepository.deleteAll(baskets);
    }

    public List<OrderResponse> getOrder(String accountId) {

        List<OrderResponse> result = new ArrayList<>();

        Account account = getAccount(accountId);

        List<OrderInfo> orderInfos = getOrderInfos(account);

        orderInfos.forEach(orderInfo -> {
            var arduinoList = new ArrayList<OrderResponse.Arduino>();

            result.add(new OrderResponse(
                    accountId,
                    orderInfo.getDescription(),
                    orderInfo.getCreatedDate(),
                    orderInfo.getStatus().getValue(),
                    orderInfo.getOrderNumber(),
                    orderInfo.getSubject(),
                    arduinoList));

            orderInfo.getOrders().forEach(order ->
                arduinoList.add(
                        new OrderResponse.Arduino(order.getArduino().getId(),
                                order.getArduino().getName(),
                                order.getCount(),
                                order.getArduino().getCategories())
                )
            );
        });

        return result;
    }

    public void cancelOrderRequest(String orderNumber) {

        orderInfoRepository.findById(orderNumber).ifPresentOrElse(orderInfo -> {
                if (!orderInfo.isCancellable()) {
                    String message = NOT_ACCEPTABLE_ORDER_STATUS(orderInfo.getStatus());
                    throw new IllegalArgumentException(message);
                }

                orderInfo.setStatus(OrderStatus.CANCELLATION_REQUEST);
            },
            () -> {
                throw new EntityNotFoundException(NOT_FOUND_VALUE);
            }
        );
    }

    private List<OrderInfo> getOrderInfos(Account account) {
        List<OrderInfo> orderInfos = orderInfoRepository.findAllFetchOrderAndArduino(account);

        ValidateUtils.throwIfEmpty(orderInfos);
        return orderInfos;
    }

    private List<Basket> getBaskets(Account account) {
        List<Basket> arduinoList = basketRepository.findAllByAccount(account);

        ValidateUtils.throwIfEmpty(arduinoList);

        return arduinoList;
    }

    private List<Basket> getBasketsFetchArduinoAndAccount(Account account) {
        List<Basket> arduinoList = basketRepository.findAllFetchArduinoAndAccount(account);

        ValidateUtils.throwIfEmpty(arduinoList);

        return arduinoList;
    }

    private Account getAccount(String accountId) {
        return accountRepository.findById(accountId).orElseThrow(() -> new EntityNotFoundException(NOT_EXIST_ACCOUNT));
    }

    private Arduino getArduino(Long arduinoId) {
        return arduinoRepository.findById(arduinoId).orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_ARDUINO));
    }


}
