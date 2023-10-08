package com.know_wave.comma.comma_backend.arduino.service;

import com.know_wave.comma.comma_backend.account.entity.Account;
import com.know_wave.comma.comma_backend.account.repository.AccountRepository;
import com.know_wave.comma.comma_backend.arduino.dto.*;
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

import static com.know_wave.comma.comma_backend.util.message.ExceptionMessageSource.*;
import static com.know_wave.comma.comma_backend.util.SecurityUtils.getAuthenticatedId;

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

    public BasketResponse getBasket() {

        Account account = getAccount(getAuthenticatedId());
        List<Basket> baskets = getBasketsFetchArduino(account);

        ValidateUtils.throwIfEmpty(baskets);

        return BasketResponse.of(baskets);
    }

    public void addArduinoToBasket(BasketRequest request) {

        Account account = getAccount(getAuthenticatedId());
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

    public void deleteArduinoFromBasket(BasketDeleteRequest request) {

        Account account = getAccount(getAuthenticatedId());
        Arduino arduino = getArduino(request.getArduinoId());

        basketRepository.findByAccountAndArduino(account, arduino)
                .ifPresentOrElse(basketRepository::delete,
                                () -> {throw new EntityNotFoundException(NOT_FOUND_VALUE);});
    }

    public void updateArduinoFromBasket(BasketRequest request) {

        Account account = getAccount(getAuthenticatedId());
        Arduino arduino = getArduino(request.getArduinoId());

        if (Basket.isOverRequest(request.getCount(), arduino.getCount())) {
            throw new IllegalArgumentException(NOT_ACCEPTABLE_REQUEST);
        }

        basketRepository.findByAccountAndArduino(account, arduino)
                .ifPresentOrElse(basket ->
                    basket.setArduinoCount(request.getCount()),
                    () -> {throw new EntityNotFoundException(NOT_FOUND_VALUE);});
    }

    public void emptyBasket() {
        Account account = getAccount(getAuthenticatedId());
        List<Basket> basket = getBaskets(account);

        basketRepository.deleteAll(basket);
    }

    public void order(OrderRequest request) {

        Account account = getAccount(getAuthenticatedId());
        List<Basket> baskets = getBasketsFetchArduino(account);

        if (Basket.isOverRequest(baskets) || Basket.isEmpty(baskets)) {
            throw new IllegalArgumentException(NOT_ACCEPTABLE_REQUEST);
        }

        String orderNumber = GenerateCodeUtils.getCodeByIdWithDate(account.getId());
        OrderInfo orderInfo = new OrderInfo(account, orderNumber, request.getSubject(), request.getDescription());

        List<Order> orders = Order.ofList(baskets, orderInfo);

//        orderRepository.saveAll(orders);
        orderInfoRepository.save(orderInfo);
        basketRepository.deleteAll(baskets);
    }

    public OrderDetailResponse getOrderDetail(String orderNumber) {

        final String accountId = getAuthenticatedId();
        OrderInfo orderInfo = getOrderInfo(orderNumber);

        var orderArduinoList = new ArrayList<OrderDetailResponse.Arduino>();

        OrderDetailResponse result = new OrderDetailResponse(
                accountId,
                orderInfo.getDescription(),
                orderInfo.getCreatedDate(),
                orderInfo.getStatus().getValue(),
                orderInfo.getOrderNumber(),
                orderInfo.getSubject(),
                orderArduinoList
        );

        orderInfo.getOrders().forEach(order ->
                orderArduinoList.add(
                        new OrderDetailResponse.Arduino(order.getArduino().getId(),
                                order.getArduino().getName(),
                                order.getCount(),
                                order.getArduino().getCategories())
                )
        );

        return result;
    }

    public List<OrderResponse> getOrders() {

        List<OrderResponse> result = new ArrayList<>();

        final String accountId = getAuthenticatedId();
        Account account = getAccount(accountId);
        List<OrderInfo> orderInfos = getOrderInfos(account);

        orderInfos.forEach(orderInfo ->
            result.add(new OrderResponse(
                    accountId,
                    orderInfo.getDescription(),
                    orderInfo.getCreatedDate(),
                    orderInfo.getStatus().getValue(),
                    orderInfo.getOrderNumber(),
                    orderInfo.getSubject())
            )
        );

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

    private OrderInfo getOrderInfo(String orderNumber) {
        return orderInfoRepository.findFetchById(orderNumber)
                .orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_VALUE));
    }

    private List<OrderInfo> getOrderInfos(Account account) {
        List<OrderInfo> orderInfos = orderInfoRepository.findAllByAccount(account);

        ValidateUtils.throwIfEmpty(orderInfos);
        return orderInfos;
    }

    private List<Basket> getBaskets(Account account) {
        List<Basket> arduinoList = basketRepository.findAllByAccount(account);

        ValidateUtils.throwIfEmpty(arduinoList);

        return arduinoList;
    }

    private List<Basket> getBasketsFetchArduino(Account account) {
        List<Basket> arduinoList = basketRepository.findAllFetchArduinoByAccount(account);

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
