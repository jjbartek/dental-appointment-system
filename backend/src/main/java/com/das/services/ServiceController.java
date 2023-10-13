package com.das.services;

import com.das.common.controllers.SecuredController;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Service> createService(@Valid @RequestBody ServiceRequest serviceRequest) {
        return new ResponseEntity<>(serviceService.addService(serviceRequest), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{id}")
    public ResponseEntity<Service> updateService(@PathVariable Integer id, @Valid @RequestBody ServiceRequest serviceRequest) {
        return new ResponseEntity<>(serviceService.updateService(id, serviceRequest), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Integer id) {
        serviceService.deleteService(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
