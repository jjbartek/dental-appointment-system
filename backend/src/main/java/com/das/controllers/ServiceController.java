package com.das.controllers;

import com.das.dtos.ServiceDTO;
import com.das.services.ServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/services")
@RequiredArgsConstructor
public class ServiceController {
    private final ServiceService serviceService;

    @GetMapping("{id}")
    public ResponseEntity<ServiceDTO> getService(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(serviceService.getService(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ServiceDTO>> getServices() {
        return new ResponseEntity<>(serviceService.getServices(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ServiceDTO> createService(@Valid @RequestBody ServiceDTO service) {
        return new ResponseEntity<>(serviceService.addService(service), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<ServiceDTO> updateService(@PathVariable Integer id, @Valid @RequestBody ServiceDTO service) {
        return new ResponseEntity<>(serviceService.updateService(id, service), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Integer id) {
        serviceService.deleteService(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
