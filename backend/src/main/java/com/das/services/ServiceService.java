package com.das.services;

import com.das.entities.Service;
import com.das.exceptions.ResourceNotFoundException;
import com.das.repositories.ServiceRepository;
import com.das.requests.ServiceRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RequiredArgsConstructor
@org.springframework.stereotype.Service
public class ServiceService {
    private final ServiceRepository serviceRepository;
    private final ModelMapper modelMapper;

    public Service getService(Integer id) {
        return getServiceOrThrow(id);
    }

    public List<Service> getServices() {
        return serviceRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Service addService(ServiceRequest serviceRequest) {
        Service service = modelMapper.map(serviceRequest, Service.class);

        return serviceRepository.save(service);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public Service updateService(Integer id, ServiceRequest updatedService) {
        Service service = getServiceOrThrow(id);
        modelMapper.map(updatedService, service);

        return serviceRepository.save(service);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteService(Integer id) {
        Service service = getServiceOrThrow(id);

        serviceRepository.delete(service);
    }

    private Service getServiceOrThrow(Integer id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service", "service id", id));
    }
}
