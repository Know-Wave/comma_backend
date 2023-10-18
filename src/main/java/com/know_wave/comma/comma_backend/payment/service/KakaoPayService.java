package com.know_wave.comma.comma_backend.payment.service;

import com.know_wave.comma.comma_backend.payment.dto.PaymentAuthRequest;
import com.know_wave.comma.comma_backend.payment.dto.PaymentAuthResult;
import com.know_wave.comma.comma_backend.payment.dto.kakao.KakaoPayApproveRequest;
import com.know_wave.comma.comma_backend.payment.dto.kakao.KakaoPayApproveResponse;
import com.know_wave.comma.comma_backend.payment.dto.kakao.KakaoPayReadyRequest;
import com.know_wave.comma.comma_backend.payment.dto.kakao.KakaoPayReadyResponse;
import com.know_wave.comma.comma_backend.payment.entity.Deposit;
import com.know_wave.comma.comma_backend.payment.entity.PaymentType;
import com.know_wave.comma.comma_backend.util.GenerateUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class KakaoPayService implements PaymentService {

    private final RestTemplate kakaoPayApiClient;

    private final CommaArduinoDepositPolicy depositPolicy;

    @Value("${kakao.api.customer.id}")
    private String cid;

    private static final String paymentReadyUrl = "https://kapi.kakao.com/v1/payment/ready";
    private static final String paymentApproveUrl = "https://kapi.kakao.com/v1/payment/approve";

    public KakaoPayService(@Qualifier("kakaoPayApiClient") RestTemplate kakaoPayApiClient, CommaArduinoDepositPolicy depositPolicy) {
        this.kakaoPayApiClient = kakaoPayApiClient;
        this.depositPolicy = depositPolicy;
    }

    @Override
    public PaymentAuthResult ready(String idempotencyKey, PaymentAuthRequest request) {
        String paymentRequestId = GenerateUtils.generatedCodeWithDate();

        var readyRequest = KakaoPayReadyRequest.of(idempotencyKey, paymentRequestId, request, cid, depositPolicy);
        var httpEntity = KakaoPayReadyRequest.toHttpEntity(readyRequest);

        var kakaoPayReadyResponse = kakaoPayApiClient.postForObject(
                paymentReadyUrl,
                httpEntity,
                KakaoPayReadyResponse.class);

        return PaymentAuthResult.of(Objects.requireNonNull(kakaoPayReadyResponse), paymentRequestId);
    }

    @Override
    public void pay(Deposit deposit, String paymentToken) {
        var approveRequest = KakaoPayApproveRequest.of(cid, deposit, paymentToken);

        var httpEntity = KakaoPayReadyRequest.toHttpEntity(approveRequest);

        var response = kakaoPayApiClient.postForEntity(
                paymentApproveUrl,
                httpEntity,
                KakaoPayApproveResponse.class);
    }

    @Override
    public void refund(PaymentAuthRequest request) {

    }

    @Override
    public void cancel(PaymentAuthRequest request) {

    }

    @Override
    public boolean supports(PaymentType type) {
        return type == PaymentType.KAKAO;
    }

}
