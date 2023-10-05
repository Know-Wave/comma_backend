package com.know_wave.comma.comma_backend.arduino.service;

import com.know_wave.comma.comma_backend.arduino.dto.ArduinoCreateForm;
import com.know_wave.comma.comma_backend.arduino.dto.ArduinoResponse;
import com.know_wave.comma.comma_backend.arduino.dto.ArduinoUpdateRequest;
import com.know_wave.comma.comma_backend.arduino.entity.Arduino;
import com.know_wave.comma.comma_backend.arduino.repository.ArduinoRepository;
import com.know_wave.comma.comma_backend.arduino.repository.BasketRepository;
import com.know_wave.comma.comma_backend.arduino.repository.OrderRepository;
import com.know_wave.comma.comma_backend.util.annotation.PermissionProtection;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.know_wave.comma.comma_backend.util.ExceptionMessageSource.ALREADY_EXIST_ARDUINO;
import static com.know_wave.comma.comma_backend.util.ExceptionMessageSource.NOT_FOUND_ARDUINO;

@Service
@Transactional
@PermissionProtection
public class ArduinoAdminService {

    private final ArduinoRepository arduinoRepository;
    private final BasketRepository basketRepository;
    private final OrderRepository orderRepository;

    public ArduinoAdminService(ArduinoRepository arduinoRepository, BasketRepository basketRepository, OrderRepository orderRepository) {
        this.arduinoRepository = arduinoRepository;
        this.basketRepository = basketRepository;
        this.orderRepository = orderRepository;
    }

    public void registerArduino(ArduinoCreateForm form) {

        Optional<Arduino> arduinoOptional = arduinoRepository.findByName(form.getName());

        arduinoOptional.ifPresentOrElse(arduino ->
                {throw new EntityExistsException(ALREADY_EXIST_ARDUINO);},
                () -> arduinoRepository.save(form.toEntity()));
    }

    public void registerArduino(ArduinoCreateForm[] forms) {
        for (ArduinoCreateForm form : forms) {
            registerArduino(form);
        }
    }

    public void updateArduino(ArduinoUpdateRequest request) {

        Optional<Arduino> arduinoOptional = arduinoRepository.findById(request.getId());

        arduinoOptional.ifPresentOrElse(arduino ->
                arduino.update(request.getName(), request.getCount(), request.getOriginalCount(), request.getDescription()),
                () -> {throw new EntityNotFoundException(NOT_FOUND_ARDUINO);});
    }

    public void deleteArduino(Long id) {

        Optional<Arduino> arduinoOptional = arduinoRepository.findById(id);
        arduinoOptional.ifPresentOrElse(
                arduinoRepository::delete,
                () -> {throw new EntityNotFoundException(NOT_FOUND_ARDUINO);});
    }

    public ArduinoResponse getArduino(Long id) {

        Arduino arduino = arduinoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_ARDUINO));

        return new ArduinoResponse(arduino.getId(), arduino.getName(), arduino.getCount(), arduino.getOriginalCount(), arduino.getDescription());
    }


}
