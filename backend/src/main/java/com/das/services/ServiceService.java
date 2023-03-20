package com.das.services;

import com.das.entities.Service;
import com.das.exceptions.ResourceNotFoundException;
import com.das.repositories.ServiceRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@org.springframework.stereotype.Service
public class ServiceService {
    private final ServiceRepository serviceRepository;
    public Service getService(Integer id) {
        return getServiceOrThrow(id);
    }

    public List<Service> getServices() {
        return serviceRepository.findAll();
    }

    public Service addService(Service service) {
        service.setId(null);

        return serviceRepository.save(service);
    }

    public Service updateService(Integer id, Service updatedService) {
        Service service = getServiceOrThrow(id);

        service.setDuration(updatedService.getDuration());
        service.setName(updatedService.getName());
        service.setMinPrice(updatedService.getMinPrice());

        return serviceRepository.save(service);
    }
    public void deleteService(Integer id) {
        Service service = getServiceOrThrow(id);

        serviceRepository.delete(service);
    }

    private Service getServiceOrThrow(Integer id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service", "service id", id));
    }
}
