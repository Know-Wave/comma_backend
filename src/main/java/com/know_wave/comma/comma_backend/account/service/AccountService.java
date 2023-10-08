package com.know_wave.comma.comma_backend.account.service;

import com.know_wave.comma.comma_backend.account.dto.*;
import com.know_wave.comma.comma_backend.account.entity.AcademicMajor;
import com.know_wave.comma.comma_backend.account.entity.AcademicStatus;
import com.know_wave.comma.comma_backend.account.entity.Account;
import com.know_wave.comma.comma_backend.account.entity.AccountEmailVerify;
import com.know_wave.comma.comma_backend.account.entity.auth.Role;
import com.know_wave.comma.comma_backend.account.entity.token.Token;
import com.know_wave.comma.comma_backend.account.repository.AccountRepository;
import com.know_wave.comma.comma_backend.account.repository.AccountVerifyRepository;
import com.know_wave.comma.comma_backend.exception.EmailVerifiedException;
import com.know_wave.comma.comma_backend.exception.NotFoundEmailException;
import com.know_wave.comma.comma_backend.exception.TokenNotFound;
import com.know_wave.comma.comma_backend.util.EmailSender;
import com.know_wave.comma.comma_backend.util.GenerateCodeUtils;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.know_wave.comma.comma_backend.util.ExceptionMessageSource.*;
import static com.know_wave.comma.comma_backend.util.SecurityUtils.getAuthenticatedId;

@Service
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountVerifyRepository accountVerifyRepository;
    private final EmailSender emailSender;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public AccountService(AccountRepository accountRepository, AccountVerifyRepository accountVerifyRepository, EmailSender emailSender, AuthenticationManager authenticationManager, TokenService tokenService, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.accountVerifyRepository = accountVerifyRepository;
        this.emailSender = emailSender;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    public void join(AccountCreateForm form) {
        String email = form.getEmail();

        accountVerifyRepository.findById(email).ifPresentOrElse(
                accountEmailVerify -> {
                    if (!accountEmailVerify.isVerified()) {
                        throw new EmailVerifiedException(NOT_VERIFIED_EMAIL);
                    }
                },
                () -> {
                    throw new NotFoundEmailException(NOT_VERIFIED_EMAIL);
                });

        Account account = new Account(form.getAccountId(), email, form.getName(), passwordEncoder.encode(form.getPassword()), form.getAcademicNumber(), form.getMajor(), form.getStatus());
        accountRepository.save(account);
    }

    public void adminJoin(AdminCreateForm form) {
        Account account = new Account(form.getAccountId(), "none", form.getName(), passwordEncoder.encode(form.getPassword()), "9999999", AcademicMajor.SoftwareEngineering, AcademicStatus.Enrolled, Role.ADMIN);
        accountRepository.save(account);
    }

    public Account getOne(String id) {
        return accountRepository.findById(id).orElseThrow(() -> new RuntimeException(NOT_EXIST_ACCOUNT));
    }

    public void send(String email) {

        final int code = GenerateCodeUtils.getSixRandomCode();

        accountVerifyRepository.findById(email).ifPresentOrElse(accountEmailVerify ->
                {
                    if (accountEmailVerify.isVerified()) {
                        throw new EmailVerifiedException(ALREADY_VERIFIED_EMAIL);
                    }

                    accountEmailVerify.setCode(code);
                    accountEmailVerify.sendCode(emailSender);
                },
                () -> {
                    AccountEmailVerify account = new AccountEmailVerify(email, false, code);
                    account.sendCode(emailSender);
                    accountVerifyRepository.save(account);
                });
    }

    public boolean verifyEmail(String email, int code) {
        // 배열 자체에 대한 참조는 변하지 않음 (final)
        final boolean[] result = {false};

        accountVerifyRepository.findById(email).ifPresentOrElse(accountEmailVerify ->
                {
                    if (accountEmailVerify.isVerified()) {
                        throw new EmailVerifiedException(ALREADY_VERIFIED_EMAIL);
                    }

                    if (accountEmailVerify.verifyCode(code)) {
                        accountEmailVerify.setVerified(true);
                        result[0] = true;
                    }
                },
                ()-> {
                    throw new NotFoundEmailException(NOT_FOUND_EMAIL);
                });

        return result[0];
    }


    public Optional<TokenDto> processAuthentication(AccountSignInForm form) {

        Account account = findAccount(form.getAccountId());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(form.getAccountId(), form.getPassword()));
        } catch (AuthenticationException e) {
            return Optional.empty();
        }

        UserDetails accountDetails = account.toUserDetails();

        final String accessToken = tokenService.generateAccessToken(accountDetails);
        final String refreshToken = tokenService.generateRefreshToken(accountDetails);

        tokenService.revokeAllTokens(accountDetails);
        tokenService.saveToken(refreshToken);

        return Optional.of(new TokenDto(accessToken, refreshToken));
    }

    public TokenDto refreshToken(String token) {

        Optional<Token> findOptionalToken = tokenService.findToken(token);
        Token findToken = findOptionalToken.orElseThrow(() -> new TokenNotFound(NOT_FOUND_TOKEN));

        boolean validToken = tokenService.isValidToken(findToken);

        if (validToken) {
            Account account = findToken.getAccount();
            UserDetails accountDetails = account.toUserDetails();

            final String accessToken = tokenService.generateAccessToken(accountDetails);
            final String refreshToken = tokenService.generateRefreshToken(accountDetails);

            tokenService.revokeAllTokens(accountDetails);
            tokenService.saveToken(refreshToken);

            return new TokenDto(accessToken, refreshToken);
        }

        throw new ExpiredJwtException(null, null, INVALID_TOKEN);
    }

    private Account findAccount(String accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new EntityNotFoundException(NOT_EXIST_ACCOUNT));
    }

    public AccountResponse getAccount() {
        String accountId = getAuthenticatedId();
        Account account = findAccount(accountId);

        return new AccountResponse(
                account.getId(),
                account.getEmail(),
                account.getAcademicNumber(),
                account.getMajor().name(),
                account.getAcademicStatus().getStatus(),
                account.getRole().getGrade()
        );
    }

    public boolean checkMatchPassword(String password) {
        String accountId = getAuthenticatedId();
        Account account = findAccount(accountId);

        return passwordEncoder.matches(password, account.getPassword());
    }

    public void changePassword(String password) {
        String accountId = getAuthenticatedId();
        Account account = findAccount(accountId);

        account.setPassword(passwordEncoder.encode(password));
    }


    public void deleteAccount() {
        String accountId = getAuthenticatedId();
        Account account = findAccount(accountId);

        accountRepository.delete(account);
    }
}
