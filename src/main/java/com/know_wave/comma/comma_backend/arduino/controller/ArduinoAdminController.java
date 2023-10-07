package com.know_wave.comma.comma_backend.arduino.controller;

import com.know_wave.comma.comma_backend.arduino.dto.ArduinoCreateForm;
import com.know_wave.comma.comma_backend.arduino.dto.ArduinoCreateFormList;
import com.know_wave.comma.comma_backend.arduino.dto.CategoryNameDto;
import com.know_wave.comma.comma_backend.arduino.service.ArduinoAdminService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/arduino")
public class ArduinoAdminController {

    private final ArduinoAdminService adminService;

    public ArduinoAdminController(ArduinoAdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/category")
    public ResponseEntity<String> addCategory(@Valid @RequestBody CategoryNameDto request) {
        adminService.registerCategory(request.getCategoryName());
        return ResponseEntity.ok("Created category");
    }

    @PostMapping
    public ResponseEntity<String> addArduino(@Valid @RequestBody ArduinoCreateForm form) {
        adminService.registerArduino(form);
        return ResponseEntity.ok("Created arduino");
    }

    @PostMapping("/list")
    public ResponseEntity<String> addArduinoList(@Valid @RequestBody ArduinoCreateFormList forms) {
        adminService.registerArduinoList(forms);
        return ResponseEntity.ok("Created arduino list");
    }
}
