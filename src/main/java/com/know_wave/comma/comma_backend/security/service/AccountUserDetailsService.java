package com.know_wave.comma.comma_backend.security.service;

import com.know_wave.comma.comma_backend.account.entity.Account;
import com.know_wave.comma.comma_backend.account.repository.AccountRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    public AccountUserDetailsService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> o = accountRepository.findById(username);

        if (o.isPresent()) {
            Account account = o.get();

            return account.toUserDetails();

        } else {
            throw new UsernameNotFoundException("NotExist Account");
        }
    }
}