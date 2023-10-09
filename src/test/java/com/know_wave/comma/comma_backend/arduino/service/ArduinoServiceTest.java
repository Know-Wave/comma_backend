package com.know_wave.comma.comma_backend.arduino.service;

import com.know_wave.comma.comma_backend.arduino.dto.ArduinoCreateForm;
import com.know_wave.comma.comma_backend.arduino.dto.ArduinoResponse;
import com.know_wave.comma.comma_backend.arduino.dto.CategoryIdDto;
import com.know_wave.comma.comma_backend.arduino.service.admin.ArduinoAdminService;
import com.know_wave.comma.comma_backend.arduino.service.normal.ArduinoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
class ArduinoServiceTest {

    @Autowired
    ArduinoService arduinoService;
    @Autowired
    ArduinoAdminService adminService;

    @BeforeEach
    @Commit
    void setUp() {
        adminService.registerCategory("센서");
        adminService.registerCategory("모듈");
        adminService.registerCategory("기타");

        List<CategoryIdDto> categoryIdDtos1 = List.of(new CategoryIdDto(1L));
        List<CategoryIdDto> categoryIdDtos2 = List.of(new CategoryIdDto(1L), new CategoryIdDto(2L));
        List<CategoryIdDto> categoryIdDtos3 = List.of(new CategoryIdDto(1L), new CategoryIdDto(2L), new CategoryIdDto(3L));

        adminService.registerArduino(new ArduinoCreateForm("testArduino1",  100, "testArduinoDescription1", categoryIdDtos1));
        adminService.registerArduino(new ArduinoCreateForm("testArduino2",  200, "testArduinoDescription2", categoryIdDtos2));
        adminService.registerArduino(new ArduinoCreateForm("testArduino3", 300, "testArduinoDescription3", categoryIdDtos3));
    }

    @Test
    void getOne() {
        ArduinoResponse arduinoResponse = arduinoService.getOne(1L);
        System.out.println(arduinoResponse);
    }
}