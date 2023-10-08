package com.know_wave.comma.comma_backend.arduino.service;

import com.know_wave.comma.comma_backend.arduino.dto.ArduinoResponse;
import com.know_wave.comma.comma_backend.arduino.dto.CategoryDto;
import com.know_wave.comma.comma_backend.arduino.entity.Arduino;
import com.know_wave.comma.comma_backend.arduino.entity.Category;
import com.know_wave.comma.comma_backend.arduino.repository.ArduinoRepository;
import com.know_wave.comma.comma_backend.arduino.repository.CategoryCrudRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.know_wave.comma.comma_backend.util.ExceptionMessageSource.NOT_FOUND_VALUE;

@Service
@Transactional
public class ArduinoService {

    private final ArduinoRepository arduinoRepository;
    private final CategoryCrudRepository categoryRepository;

    public ArduinoService(ArduinoRepository arduinoRepository, CategoryCrudRepository categoryRepository) {
        this.arduinoRepository = arduinoRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryDto> getAllCategories() {
        var categories = (List<Category>) categoryRepository.findAll();

        return categories.stream()
                .map(CategoryDto::of)
                .sorted(Comparator.comparing(CategoryDto::getCategoryName))
                .toList();
    }

    public ArduinoResponse getOne(Long id) {
        Arduino arduino = arduinoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_VALUE));

        return ArduinoResponse.of(arduino);
    }

    public Optional<Page<ArduinoResponse>> getFirstPage(int size) {
        return getPage(Pageable.ofSize(size));
    }

    public Optional<Page<ArduinoResponse>> getPage(Pageable pageable) {
        Page<ArduinoResponse> result = arduinoRepository.findAll(pageable)
                .map(ArduinoResponse::of);

        if (result.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(result);
    }

}
