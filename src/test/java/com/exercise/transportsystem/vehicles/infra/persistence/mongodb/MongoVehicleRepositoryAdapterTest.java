package com.exercise.transportsystem.vehicles.infra.persistence.mongodb;

import com.exercise.transportsystem.TestData;
import com.exercise.transportsystem.common.domain.vehicle.Vehicle;
import com.exercise.transportsystem.common.domain.vehicle.VehicleStatus;
import com.exercise.transportsystem.vehicles.domain.exception.VehiclesDomainException;
import com.exercise.transportsystem.vehicles.infra.persistence.mongodb.mapper.VehicleMapper;
import com.exercise.transportsystem.vehicles.infra.persistence.mongodb.repository.MongoDriverRepository;
import com.exercise.transportsystem.vehicles.infra.persistence.mongodb.repository.MongoVehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.verification.VerificationMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest()
@ExtendWith(MockitoExtension.class)
class MongoVehicleRepositoryAdapterTest {

    @MockBean
    private MongoDriverRepository driverRepo;
    @MockBean
    private MongoVehicleRepository vehicleRepo;
    //@Mock
    @Autowired
    private VehicleMapper mapper;
    @Autowired
    MongoVehicleRepositoryAdapter vehicleRepositoryAdapter;

    @BeforeEach
    void setUp() {
        TestData.initializeTestData();
    }


    @Test
    void testFindById() {
        when(vehicleRepo.findById(TestData.bus1.getPlateId())).thenReturn(Mono.just(TestData.bus1Document));
        when(driverRepo.findById(TestData.bus1.getDriver().getEmail())).thenReturn(Mono.just(TestData.driver1Document));

        Mono<Vehicle> result = vehicleRepositoryAdapter.findById("ABC123");

        StepVerifier.create(result)
                .expectNext(TestData.bus1)
                .expectComplete()
                .verify();
    }

    @Test
    void testFindByIdThrowsException() {
        when(vehicleRepo.findById(TestData.bus1.getPlateId())).thenReturn(Mono.error(new SQLException("Error")));

        Mono<Vehicle> result = vehicleRepositoryAdapter.findById("ABC123");

        StepVerifier.create(result)
                .expectError(VehiclesDomainException.class)
                .verify();
    }

    @Test
    void testFindAll() {
        when(vehicleRepo.findAll()).thenReturn(Flux.just(TestData.bus1Document,TestData.bus2Document));
        when(driverRepo.findById(TestData.bus1.getDriver().getEmail())).thenReturn(Mono.just(TestData.driver1Document));
        when(driverRepo.findById(TestData.bus2.getDriver().getEmail())).thenReturn(Mono.just(TestData.driver2Document));

        Flux<Vehicle> result = vehicleRepositoryAdapter.findAll();

        StepVerifier.create(result)
                .expectNext(TestData.bus1)
                .expectNext(TestData.bus2)
                .expectComplete()
                .verify();
    }

    @Test
    void testFindAllThrowsException() {
        when(vehicleRepo.findAll()).thenReturn(Flux.error(new SQLException("Error")));

        Flux<Vehicle> result = vehicleRepositoryAdapter.findAll();

        StepVerifier.create(result)
                .expectError(VehiclesDomainException.class)
                .verify();
    }

    @Test
    void testFindAllByStatus() {
        when(vehicleRepo.findAll()).thenReturn(Flux.just(TestData.bus1Document,TestData.bus2Document));
        when(driverRepo.findById(TestData.bus1.getDriver().getEmail())).thenReturn(Mono.just(TestData.driver1Document));
        when(driverRepo.findById(TestData.bus2.getDriver().getEmail())).thenReturn(Mono.just(TestData.driver2Document));

        Flux<Vehicle> result = vehicleRepositoryAdapter.findAllByStatus(VehicleStatus.IN_SERVICE);

        StepVerifier.create(result)
                .expectNext(TestData.bus1)
                .expectComplete()
                .verify();
    }

    @Test
    void testInsert() {
        when(vehicleRepo.existsById(TestData.bus1.getPlateId())).thenReturn(Mono.just(false));
        when(driverRepo.save(TestData.driver1Document)).thenReturn(Mono.just(TestData.driver1Document));
        when(vehicleRepo.save(TestData.bus1Document)).thenReturn(Mono.just(TestData.bus1Document));

        Mono<Vehicle> result = vehicleRepositoryAdapter.insert(TestData.bus1);

        StepVerifier.create(result)
                .expectNext(TestData.bus1)
                .expectComplete()
                .verify();

    }

    @Test
    void testCreateOrUpdate() {
        when(vehicleRepo.existsById(TestData.bus1.getPlateId())).thenReturn(Mono.just(true));
        when(driverRepo.save(TestData.driver1Document)).thenReturn(Mono.just(TestData.driver1Document));
        when(vehicleRepo.save(TestData.bus1Document)).thenReturn(Mono.just(TestData.bus1Document));

        Mono<Tuple2<Boolean, Vehicle>> result = vehicleRepositoryAdapter.createOrUpdate(TestData.bus1);

        StepVerifier.create(result)
                .assertNext(tuple -> {
                    assertTrue(tuple.getT1());
                    assertEquals(TestData.bus1,tuple.getT2());
                })
                .expectComplete()
                .verify();
    }

    @Test
    void testDeleteById() {
        when(vehicleRepo.findById(TestData.bus1.getPlateId())).thenReturn(Mono.just(TestData.bus1Document));
        when(driverRepo.findById(TestData.driver1.getEmail())).thenReturn(Mono.just(TestData.driver1Document));
        when(driverRepo.save(TestData.driver1Document)).thenReturn(Mono.just(TestData.driver1Document));
        when(vehicleRepo.deleteById(TestData.bus1.getPlateId())).thenReturn(Mono.just(mock(Void.class)));

        Mono<Void> result = vehicleRepositoryAdapter.deleteById(TestData.bus1.getPlateId());

        StepVerifier.create(result)
                .expectNext()
                .expectComplete()
                .verify();

        verify(vehicleRepo, atMostOnce()).findById(TestData.bus1.getPlateId());
        verify(driverRepo, atMostOnce()).findById(TestData.driver1.getEmail());
        verify(driverRepo, atMostOnce()).save(TestData.driver1Document);
        verify(vehicleRepo, atMostOnce()).deleteById(TestData.bus1.getPlateId());
    }

    @Test
    void testDeleteAll() {
        when(driverRepo.deleteAll()).thenReturn(Mono.just(mock(Void.class)));
        when(vehicleRepo.deleteAll()).thenReturn(Mono.just(mock(Void.class)));

        Mono<Void> result = vehicleRepositoryAdapter.deleteAll();

        StepVerifier.create(result)
                .expectNext()
                .expectComplete()
                .verify();

        verify(driverRepo, atMostOnce()).deleteAll();
        verify(vehicleRepo, atMostOnce()).deleteAll();
    }
}