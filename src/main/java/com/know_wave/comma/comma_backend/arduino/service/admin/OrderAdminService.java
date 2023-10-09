package com.know_wave.comma.comma_backend.arduino.service.admin;

import com.know_wave.comma.comma_backend.account.entity.Account;
import com.know_wave.comma.comma_backend.arduino.dto.order.OrderDetailResponse;
import com.know_wave.comma.comma_backend.arduino.dto.order.OrderResponse;
import com.know_wave.comma.comma_backend.arduino.dto.order.OrderResponseGroup;
import com.know_wave.comma.comma_backend.arduino.entity.OrderInfo;
import com.know_wave.comma.comma_backend.arduino.repository.OrderInfoRepository;
import com.know_wave.comma.comma_backend.arduino.repository.OrderRepository;
import com.know_wave.comma.comma_backend.util.annotation.PermissionProtection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
@PermissionProtection
public class OrderAdminService {

    private final OrderRepository orderRepository;
    private final OrderInfoRepository orderInfoRepository;

    public OrderAdminService(OrderRepository orderRepository, OrderInfoRepository orderInfoRepository) {
        this.orderRepository = orderRepository;
        this.orderInfoRepository = orderInfoRepository;
    }

    public List<OrderResponseGroup> getOrders(Pageable pageable) {
        Page<OrderInfo> orderInfos = orderInfoRepository.findAllFetchAccount(pageable);
        List<OrderInfo> relatedOrderInfo = orderInfoRepository.findAllByRelatedAccount(orderInfos.map(OrderInfo::getAccount).toSet());

        List<OrderResponse> orderResponses = orderInfos.stream().map(OrderResponse::of).toList();
        List<OrderResponse> relatedResponse = relatedOrderInfo.stream().map(OrderResponse::of).toList();

        List<OrderResponse> mergedResponse = Stream.concat(orderResponses.stream(), relatedResponse.stream()).toList();

        Map<String, List<OrderResponse>> groupedOrderResponses = OrderResponse.groupingOrderByAccountId(mergedResponse);

        return OrderResponseGroup.ofList(groupedOrderResponses);
    }

    public OrderDetailResponse getOrderDetail() {

    }

    public List<OrderResponse> getOrdersByAccount() {

    }

    public OrderDetailResponse getOrderDetailByAccount() {

    }

    public List<OrderResponse> getCancelRequestOrders() {

    }

    public void changeOrderStatus() {

    }

    public void changeOrdersStatus() {

    }

}
