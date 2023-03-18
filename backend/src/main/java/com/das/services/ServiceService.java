package com.das.services;

import com.das.dtos.ServiceDTO;
import com.das.entities.Service;
import com.das.exceptions.ResourceNotFoundException;
import com.das.repositories.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@org.springframework.stereotype.Service
public class ServiceService {
    private final ServiceRepository serviceRepository;
    private final ModelMapper modelMapper;
    public ServiceDTO getService(Integer id) {
        Service service = getServiceOrThrow(id);

        return modelMapper.map(service, ServiceDTO.class);
    }

    public List<ServiceDTO> getServices() {
        List<Service> services = serviceRepository.findAll();

        return services
                .stream()
                .map(service -> modelMapper.map(service, ServiceDTO.class))
                .collect(Collectors.toList());
    }

    public ServiceDTO addService(ServiceDTO serviceDTO) {
        Service service = modelMapper.map(serviceDTO, Service.class);
        service = serviceRepository.save(service);

        return modelMapper.map(service, ServiceDTO.class);
    }

    public ServiceDTO updateService(Integer id, ServiceDTO serviceDTO) {
        Service service = getServiceOrThrow(id);

        service.setDuration(serviceDTO.getDuration());
        service.setName(serviceDTO.getName());
        service.setMinPrice(serviceDTO.getMinPrice());

        service = serviceRepository.save(service);
        return modelMapper.map(service, ServiceDTO.class);
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
