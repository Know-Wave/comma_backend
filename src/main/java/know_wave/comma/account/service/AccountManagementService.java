package know_wave.comma.account.service;

import know_wave.comma.account.dto.AccountResponse;
import know_wave.comma.account.entity.Account;
import know_wave.comma.account.repository.AccountRepository;
import know_wave.comma.account.repository.TokenRepository;
import know_wave.comma.account.service.system.AccountQueryService;
import know_wave.comma.config.security.entity.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountManagementService {

    private final AccountQueryService accountQueryService;
    private final AccountRepository accountRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountResponse getAccount() {
        Account account = accountQueryService.findAccount();

        return AccountResponse.to(account);
    }

    public boolean checkMatchPassword(String password) {
        Account account = accountQueryService.findAccount();

        return passwordEncoder.matches(password, account.getPassword());
    }

    public void changePassword(String password) {
        Account account = accountQueryService.findAccount();

        account.setPassword(passwordEncoder.encode(password));
    }


    public void deleteAccount() {
        Account account = accountQueryService.findAccount();

        List<Token> tokens = tokenRepository.findAllByAccount(account);
        tokenRepository.deleteAll(tokens);

        accountRepository.delete(account);
    }


}
