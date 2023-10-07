package com.know_wave.comma.comma_backend.arduino.service;

import com.know_wave.comma.comma_backend.arduino.dto.ArduinoCreateForm;
import com.know_wave.comma.comma_backend.arduino.dto.ArduinoResponse;
import com.know_wave.comma.comma_backend.arduino.dto.ArduinoUpdateRequest;
import com.know_wave.comma.comma_backend.arduino.dto.CategoryDto;
import com.know_wave.comma.comma_backend.arduino.entity.Arduino;
import com.know_wave.comma.comma_backend.arduino.entity.ArduinoCategory;
import com.know_wave.comma.comma_backend.arduino.entity.Category;
import com.know_wave.comma.comma_backend.arduino.repository.*;
import com.know_wave.comma.comma_backend.exception.EntityAlreadyExistException;
import com.know_wave.comma.comma_backend.util.annotation.PermissionProtection;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.know_wave.comma.comma_backend.util.ExceptionMessageSource.*;

@Service
@Transactional
@PermissionProtection
public class ArduinoAdminService {

    private final ArduinoRepository arduinoRepository;
    private final CategoryCrudRepository categoryCrudRepository;
    private final ArduinoCategoryRepository arduinoCategoryRepository;
    private final BasketRepository basketRepository;
    private final OrderRepository orderRepository;

    public ArduinoAdminService(ArduinoRepository arduinoRepository, CategoryCrudRepository categoryCrudRepository, ArduinoCategoryRepository arduinoCategoryRepository, BasketRepository basketRepository, OrderRepository orderRepository) {
        this.arduinoRepository = arduinoRepository;
        this.categoryCrudRepository = categoryCrudRepository;
        this.arduinoCategoryRepository = arduinoCategoryRepository;
        this.basketRepository = basketRepository;
        this.orderRepository = orderRepository;
    }

    public void registerCategory(String categoryName) {

        if (categoryCrudRepository.findByName(categoryName).isEmpty()) {
            throw new EntityAlreadyExistException(ALREADY_EXIST_VALUE);
        }

        categoryCrudRepository.save(new Category(categoryName));
    }

    public void updateCategory(Long id, String dest) {

        categoryCrudRepository.findById(id).ifPresentOrElse(category ->
                category.setName(dest),
                () -> {throw new EntityNotFoundException(NOT_FOUND_VALUE);}
        );
    }

    public void deleteCategory(Long id) {

        categoryCrudRepository.findById(id).ifPresentOrElse(
                categoryCrudRepository::delete,
                () -> {throw new EntityNotFoundException(NOT_FOUND_VALUE);}
        );
    }

    public List<CategoryDto> getAllCategory() {
        Iterable<Category> findCategory = categoryCrudRepository.findAll();
        List<Category> list = (List<Category>) findCategory;

        return list.stream()
                .map(CategoryDto::new)
                .toList();
    }

    public void registerArduino(ArduinoCreateForm form) {

        arduinoRepository.findByName(form.getArduinoName()).ifPresentOrElse(arduino -> {
                throw new EntityExistsException(ALREADY_EXIST_ARDUINO);
            },

            () -> {
                Arduino savedArduino = arduinoRepository.save(form.toEntity());
                var categories = categoryCrudRepository.findAllById(form.getCategories());
                categories.forEach(arduinoCategoryRepository.save(category -> new ArduinoCategory(savedArduino, category)));
            });
    }

    public void registerArduino(List<ArduinoCreateForm> forms) {
        forms.forEach(this::registerArduino);
    }

    public void updateArduino(ArduinoUpdateRequest request) {

        arduinoRepository.findById(request.getArduinoId()).ifPresentOrElse(arduino -> {

                arduino.update(request.getArduinoName(), request.getArduinoCount(), request.getArduinoOriginalCount(), request.getArduinoDescription());

                // category update logic
                // compare request category, own category
                // then save new request category if nothing matches
                var RequestCategories = categoryCrudRepository.findAllById(request.getArduinoCategories());
                var OwnCategories = arduinoCategoryRepository.findAllFetchByArduino(arduino);

                RequestCategories.forEach(requestCategory -> {
                            if (OwnCategories.stream()
                                    .noneMatch(ownCategory -> ownCategory.getCategory().getId() == requestCategory.getId())) {
                                arduinoCategoryRepository.save(new ArduinoCategory(arduino, requestCategory));
                            }
                        }
                );
            },

            () -> {throw new EntityNotFoundException(NOT_FOUND_ARDUINO);}
        );
    }

    public void deleteArduino(Long id) {

        arduinoRepository.findById(id).ifPresentOrElse(
                arduinoRepository::delete,
                () -> {throw new EntityNotFoundException(NOT_FOUND_ARDUINO);});
    }

    public ArduinoResponse getOne(Long id) {

        Arduino arduino = arduinoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_ARDUINO));

        return new ArduinoResponse(
                arduino.getId(),
                arduino.getName(),
                arduino.getCount(),
                arduino.getOriginalCount(),
                arduino.getDescription(),
                arduino.getCategories());
    }


}
