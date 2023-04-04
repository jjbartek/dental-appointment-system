package com.das.controllers;

import com.das.entities.Service;
import com.das.requests.ServiceRequest;
import com.das.responses.ServiceResponse;
import com.das.services.ServiceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/services")
@RequiredArgsConstructor
@Tag(name = "Service")
public class ServiceController implements SecuredController {
    private final ServiceService serviceService;

    @GetMapping("{id}")
    public ResponseEntity<Service> getService(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(serviceService.getService(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ServiceResponse> getServices() {
        ServiceResponse response = new ServiceResponse(serviceService.getServices());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Service> createService(@Valid @RequestBody ServiceRequest serviceRequest) {
        return new ResponseEntity<>(serviceService.addService(serviceRequest), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<Service> updateService(@PathVariable Integer id, @Valid @RequestBody ServiceRequest serviceRequest) {
        return new ResponseEntity<>(serviceService.updateService(id, serviceRequest), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Integer id) {
        serviceService.deleteService(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
