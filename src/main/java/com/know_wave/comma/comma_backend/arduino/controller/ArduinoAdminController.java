package com.know_wave.comma.comma_backend.arduino.controller;

import com.know_wave.comma.comma_backend.arduino.dto.*;
import com.know_wave.comma.comma_backend.arduino.service.ArduinoAdminService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
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
        return new ResponseEntity<>("Created category", HttpStatus.CREATED);
    }

    @PatchMapping("/category/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable("id") Long id, @Valid @RequestBody CategoryNameDto request) {
        adminService.updateCategory(id, request.getCategoryName());
        return new ResponseEntity<>("Updated category", HttpStatus.OK);
    }

    @DeleteMapping("/category/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable("id") Long id) {
        adminService.deleteCategory(id);
        return new ResponseEntity<>("Deleted category", HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> addArduino(@Valid @RequestBody ArduinoCreateForm form) {
        adminService.registerArduino(form);
        return new ResponseEntity<>("Created arduino", HttpStatus.CREATED);
    }

    @PostMapping("/list")
    public ResponseEntity<String> addArduinoList(@Valid @RequestBody ArduinoCreateFormList forms) {
        adminService.registerArduinoList(forms);
        return new ResponseEntity<>("Created arduino list", HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateArduino(@PathVariable("id") Long id, @Valid @RequestBody ArduinoUpdateRequest form) {
        adminService.updateArduino(id, form);
        return new ResponseEntity<>("Updated arduino", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteArduino(@PathVariable("id") Long id) {
        adminService.deleteArduino(id);
        return new ResponseEntity<>("Deleted arduino", HttpStatus.OK);
    }
}
