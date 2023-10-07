package com.know_wave.comma.comma_backend.arduino.service;

import com.know_wave.comma.comma_backend.account.entity.Account;
import com.know_wave.comma.comma_backend.account.repository.AccountRepository;
import com.know_wave.comma.comma_backend.arduino.dto.*;
import com.know_wave.comma.comma_backend.arduino.entity.*;
import com.know_wave.comma.comma_backend.arduino.repository.*;
import com.know_wave.comma.comma_backend.exception.EntityAlreadyExistException;
import com.know_wave.comma.comma_backend.util.GenerateCodeUtils;
import com.know_wave.comma.comma_backend.util.ValidateUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.know_wave.comma.comma_backend.util.ExceptionMessageSource.*;
import static java.util.stream.Collectors.toList;

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
        var result = (List<Category>) categoryRepository.findAll();
        return result.stream().map(CategoryDto::of).toList();
    }

    public ArduinoResponse getOne(Long id) {
        Arduino arduino = arduinoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_VALUE));

        return ArduinoResponse.of(arduino);
    }

    public Page<ArduinoResponse> getFirstPage(int size) {
        return getPage(Pageable.ofSize(size));
    }

    public Page<ArduinoResponse> getPage(Pageable pageable) {
        return arduinoRepository.findAll(pageable)
                .map(ArduinoResponse::of);
    }

}
