package com.das.services;

import com.das.entities.Service;
import com.das.exceptions.ResourceNotFoundException;
import com.das.repositories.ServiceRepository;
import com.das.requests.ServiceRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceServiceTest {
    @Mock
    private ServiceRepository serviceRepository;
    @Mock
    private ModelMapper modelMapper;
    private ServiceService underTest;

    @BeforeEach
    void setUp() {
        underTest = new ServiceService(serviceRepository, modelMapper);
    }

    @Test
    void testGetService_shouldThrowResourceNotFound() {
        //given
        Integer id = 2;

        //when
        when(serviceRepository.findById(eq(id))).thenReturn(Optional.empty());

        //then
        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> underTest.getService(id))
                .withMessage("Service not found with service id: " + id);
    }

    @Test
    void testGetService_shouldReturnService() {
        //given
        Integer id = 2;
        Service service = new Service(2, "Tooth filling", BigDecimal.valueOf(200), 60);

        //when
        when(serviceRepository.findById(eq(id))).thenReturn(Optional.of(service));
        Service resultService = underTest.getService(id);

        //then
        assertThat(resultService).isEqualTo(service);
    }

    @Test
    void testGetServices_shouldReturnList() {
        //given
        Integer id = 2;
        List<Service> services = List.of(
                new Service(1, "Tooth filling", BigDecimal.valueOf(200), 60),
                new Service(2, "Teeth whitening", BigDecimal.valueOf(400), 60)
        );

        //when
        when(serviceRepository.findAll()).thenReturn(services);
        List<Service> resultList = underTest.getServices();

        //then
        assertThat(resultList).isEqualTo(services);
    }

    @Test
    void testAddService_shouldAddAndReturnService() {
        //given
        Integer id = 2;
        ServiceRequest newServiceRequest = new ServiceRequest("Tartar removal", BigDecimal.valueOf(300), 40);
        Service mappedService = new Service(null, "Tartar removal", BigDecimal.valueOf(300), 40);
        Service savedService = new Service(3, "Tartar removal", BigDecimal.valueOf(300), 40);

        //when
        when(modelMapper.map(eq(newServiceRequest), eq(Service.class))).thenReturn(mappedService);
        when(serviceRepository.save(eq(mappedService))).thenReturn(savedService);
        Service resultService = underTest.addService(newServiceRequest);

        //then
        assertThat(resultService).isEqualTo(savedService);
    }


    @Test
    void testUpdateService_shouldThrowResourceNotFound() {
        //given
        Integer id = 2;
        ServiceRequest serviceUpdateRequest = new ServiceRequest("Tartar removal", BigDecimal.valueOf(300), 40);

        //when
        when(serviceRepository.findById(eq(id))).thenReturn(Optional.empty());

        //then
        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> underTest.updateService(id, serviceUpdateRequest))
                .withMessage("Service not found with service id: " + id);
    }

    @Test
    void testUpdateService_shouldUpdateAndReturnService() {
        //given
        Integer id = 2;
        String serviceName = "Tartar removal";
        BigDecimal minPrice = BigDecimal.valueOf(300);
        Service existingService = new Service(2, "Teeth whitening", BigDecimal.valueOf(400), 60);
        Service updatedService = new Service(2, serviceName, minPrice, 60);
        ServiceRequest serviceUpdateRequest = new ServiceRequest(serviceName, minPrice, 40);

        //when
        when(serviceRepository.findById(eq(id))).thenReturn(Optional.of(existingService));
        when(serviceRepository.save(eq(existingService))).thenReturn(updatedService);
        Service resultService = underTest.updateService(id, serviceUpdateRequest);

        //then
        verify(modelMapper, times(1)).map(eq(serviceUpdateRequest), eq(existingService));
        assertThat(resultService).isEqualTo(updatedService);
    }

    @Test
    void testDeleteService_shouldThrowResourceNotFound() {
        //given
        Integer id = 2;

        //when
        when(serviceRepository.findById(eq(id))).thenReturn(Optional.empty());

        //then
        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> underTest.deleteService(id))
                .withMessage("Service not found with service id: " + id);
    }

    @Test
    void testDeleteService_shouldDeleteService() {
        //given
        Integer id = 2;
        Service existingService = new Service(2, "Teeth whitening", BigDecimal.valueOf(400), 60);

        //when
        when(serviceRepository.findById(eq(id))).thenReturn(Optional.of(existingService));
        underTest.deleteService(id);

        //then
        verify(serviceRepository, times(1)).delete(eq(existingService));
    }
}