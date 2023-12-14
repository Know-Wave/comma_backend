package know_wave.comma.arduino.basket.service;

import know_wave.comma.account.entity.Account;
import know_wave.comma.account.service.system.AccountQueryService;
import know_wave.comma.common.entity.ExceptionMessageSource;
import know_wave.comma.arduino.basket.dto.BasketAddRequest;
import know_wave.comma.arduino.basket.dto.BasketResponse;
import know_wave.comma.arduino.basket.dto.BasketUpdateRequest;
import know_wave.comma.arduino.basket.exception.BasketException;
import know_wave.comma.arduino.basket.repository.BasketRepository;
import know_wave.comma.arduino.component.entity.Arduino;
import know_wave.comma.arduino.basket.entity.Basket;
import know_wave.comma.arduino.component.service.ArduinoComponentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BasketService {

    private final AccountQueryService accountQueryService;
    private final ArduinoComponentService arduinoInfoService;
    private final BasketRepository basketRepository;

    public BasketResponse getBasket() {
        Account account = accountQueryService.findAccount();
        List<Basket> basketList = basketRepository.findAllByAccount(account);

        return BasketResponse.to(basketList);
    }

    public List<Basket> findBasketList() {
        Account account = accountQueryService.findAccount();
        return basketRepository.findAllByAccount(account);
    }

    public void addArduino(BasketAddRequest addRequest) {
        Account account = accountQueryService.findAccount();
        Arduino arduino = arduinoInfoService.getArduino(addRequest.getArduinoId());
        final int containCount = addRequest.getCount();

        if (isAlreadyInBasket(arduino, account)) {
            throw new BasketException(ExceptionMessageSource.ALREADY_IN_BASKET);
        }

        if (!arduino.isValid(containCount)) {
            throw new BasketException(ExceptionMessageSource.INVALID_ARDUINO);
        }

        Basket basket = Basket.create(account, arduino, containCount);

        basketRepository.save(basket);
    }

    public void updateContainCount(BasketUpdateRequest updateRequest) {
        Basket basket = findBasket(updateRequest.getBasketId());
        Arduino arduino = basket.getArduino();
        final int updatedCount = updateRequest.getUpdatedCount();

        if (!(arduino.isValid(updatedCount))) {
            throw new BasketException(ExceptionMessageSource.INVALID_ARDUINO);
        }

        basket.update(updatedCount);
    }

    public void deleteArduino(Long basketId) {
        Basket basket = findBasket(basketId);
        basketRepository.delete(basket);
    }

    public void deleteAllArduino() {
        Account account = accountQueryService.findAccount();
        List<Basket> basketList = basketRepository.findAllByAccount(account);

        if (!basketList.isEmpty()) {
            basketRepository.deleteAll(basketList);
        }
    }

    private boolean isAlreadyInBasket(Arduino arduino, Account account) {
        Optional<Basket> optionalBasket = basketRepository.findByArduinoAndAccount(arduino, account);

        return optionalBasket.isPresent();
    }

    private Basket findBasket(Long basketId) throws BasketException {
        Optional<Basket> optionalBasket = basketRepository.findFetchArduinoById(basketId);
        return optionalBasket.orElseThrow(() -> new BasketException(ExceptionMessageSource.NOT_FOUND_VALUE));
    }

}
