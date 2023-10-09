package com.know_wave.comma.comma_backend.account.service;

import com.know_wave.comma.comma_backend.account.entity.AccountEmailVerify;
import com.know_wave.comma.comma_backend.account.repository.AccountVerifyRepository;
import com.know_wave.comma.comma_backend.exception.EmailVerifiedException;
import com.know_wave.comma.comma_backend.exception.NotFoundEmailException;
import com.know_wave.comma.comma_backend.util.GenerateCodeUtils;
import com.know_wave.comma.comma_backend.util.message.EmailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.know_wave.comma.comma_backend.util.message.ExceptionMessageSource.ALREADY_VERIFIED_EMAIL;
import static com.know_wave.comma.comma_backend.util.message.ExceptionMessageSource.NOT_FOUND_EMAIL;

@Service
@Transactional
public class EmailService {

    private final AccountVerifyRepository accountVerifyRepository;
    private final EmailSender emailSender;

    public EmailService(AccountVerifyRepository accountVerifyRepository, EmailSender emailSender) {
        this.accountVerifyRepository = accountVerifyRepository;
        this.emailSender = emailSender;
    }

    public void sendAuthCode(String email) {

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
                    AccountEmailVerify emailVerify = new AccountEmailVerify(email, false, code);
                    emailVerify.sendCode(emailSender);
                    accountVerifyRepository.save(emailVerify);
                });
    }

    public boolean verifyAuthCode(String email, int code) {
        // 배열 자체에 대한 참조는 변하지 않으므로 클로저 가능 (final)
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
}
