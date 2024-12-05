package com.exercise.transportsystem.vehicles.api.rest;

import com.exercise.transportsystem.TestData;
import com.exercise.transportsystem.common.domain.vehicle.Vehicle;
import com.exercise.transportsystem.common.domain.vehicle.VehicleStatus;
import com.exercise.transportsystem.vehicles.domain.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RestVehicleControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private VehicleRepository vehicleRepository;

    @BeforeEach
    void setUp() {
        TestData.initializeTestData();
    }

    @Test
    void testFindAllVehicles() {
        when(vehicleRepository.findAll()).thenReturn(Flux.just(TestData.bus1,TestData.bus2));

        webTestClient.get().uri("/api/vehicles")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Vehicle.class)
                .hasSize(2)
                .contains(TestData.bus1, TestData.bus2);
    }

    @Test
    void testFindAllVehiclesByStatus() {
        when(vehicleRepository.findAllByStatus(VehicleStatus.IN_SERVICE)).thenReturn(Flux.just(TestData.bus1));

        webTestClient.get().uri("/api/vehicles?status=IN_SERVICE")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Vehicle.class)
                .hasSize(1)
                .contains(TestData.bus1);
    }

    @Test
    void testFindById() {
        when(vehicleRepository.findById(TestData.bus1.getPlateId())).thenReturn(Mono.just(TestData.bus1));

        webTestClient.get().uri("/api/vehicles/ABC123")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Vehicle.class)
                .isEqualTo(TestData.bus1);
    }

    @Test
    void testFindByIdNotFound() {
        when(vehicleRepository.findById("ABC124")).thenReturn(Mono.empty());

        webTestClient.get().uri("/api/vehicles/ABC124")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testAddVehicle() {
        when(vehicleRepository.insert(TestData.bus1)).thenReturn(Mono.just(TestData.bus1));

        webTestClient.post().uri("/api/vehicles/")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(TestData.bus1)
                .exchange()
                .expectStatus().isCreated()
                .expectBody();
    }

    @Test
    void testUpdateVehicleExists() {
        var updatedVehicleTuple = Tuples.of(true,TestData.bus1);
        when(vehicleRepository.createOrUpdate(TestData.bus1)).thenReturn(Mono.just(updatedVehicleTuple));

        webTestClient.put().uri("/api/vehicles/")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(TestData.bus1)
                .exchange()
                .expectStatus().isOk()
                .expectBody();
    }

    @Test
    void testUpdateVehicleNotExists() {
        var createdVehicleTuple = Tuples.of(false,TestData.bus2);
        when(vehicleRepository.createOrUpdate(TestData.bus2)).thenReturn(Mono.just(createdVehicleTuple));

        webTestClient.put().uri("/api/vehicles/")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(TestData.bus2)
                .exchange()
                .expectStatus().isCreated()
                .expectBody();
    }

    @Test
    void deleteVehicle() {
        when(vehicleRepository.deleteById(TestData.bus1.getPlateId())).thenReturn(Mono.just(mock(Void.class)));

        webTestClient.delete().uri("/api/vehicles/ABC123")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent()
                .expectBody().isEmpty() ;
    }
}