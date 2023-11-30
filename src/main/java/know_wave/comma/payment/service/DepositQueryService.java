package know_wave.comma.payment.service;

import know_wave.comma.account.entity.Account;
import know_wave.comma.order.entity.OrderInfo;
import know_wave.comma.payment.entity.Deposit;
import know_wave.comma.payment.repository.DepositRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DepositQueryService {

    private final DepositRepository depositRepository;

    public Deposit getDepositByRequestId(String paymentRequestId) {
        return depositRepository.fetchAccountByPaymentRequestId(paymentRequestId).orElse(null);
    }

    public Deposit getDepositById(Long id) {
        return depositRepository.findById(id).orElse(null);
    }

    public List<Deposit> getDeposits(Account account, OrderInfo orderInfo) {
        return depositRepository.findAllByAccountAndOrderInfo(account, orderInfo);
    }
}
