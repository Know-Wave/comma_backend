package com.know_wave.comma.comma_backend.arduino.dto;

import com.know_wave.comma.comma_backend.arduino.entity.Arduino;
import com.know_wave.comma.comma_backend.arduino.entity.Basket;

import java.util.List;

public class BasketResponse {

    public static BasketResponse of(List<Basket> arduinoInFromBasket) {

        List<BasketInfo> basketInfo = arduinoInFromBasket
                .stream()
                .map(basket -> new BasketInfo(basket.getArduino(), basket.getArduinoCount()))
                .toList();

        return new BasketResponse(basketInfo);
    }

    private final List<BasketInfo> basketInfo;

    public BasketResponse(List<BasketInfo> basketInfo) {
        this.basketInfo = basketInfo;
    }

    public List<BasketInfo> getBasketInfo() {
        return basketInfo;
    }

    public static class BasketInfo {
        private final ArduinoResponse arduino;
        private final int ContainedCount;

        public BasketInfo(Arduino arduino, int ContainedCount) {
            this.arduino = ArduinoResponse.of(arduino);
            this.ContainedCount = ContainedCount;
        }

        public ArduinoResponse getArduino() {
            return arduino;
        }

        public int getContainedCount() {
            return ContainedCount;
        }
    }
}
