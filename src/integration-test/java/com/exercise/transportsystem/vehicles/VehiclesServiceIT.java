package com.exercise.transportsystem.vehicles;

import com.exercise.transportsystem.develop.DataPopulator;
import com.exercise.transportsystem.domain.vehicle.Vehicle;
import com.exercise.transportsystem.domain.vehicle.VehicleStatus;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("it")
public class VehiclesServiceIT implements WithAssertions {

    @LocalServerPort
    private int appPort;
    @Autowired
    private WebTestClient webClient;

    @SpyBean
    private DataPopulator populator;

    @Container
    public static MongoDBContainer mongoContainer = new MongoDBContainer("mongo:4.0.10");

    @BeforeAll
    static void startContainers() {
            mongoContainer.start();
    }

    @AfterAll
    static void stopContainers() {
        mongoContainer.stop();
    }

    @BeforeEach
    void cleanUpDb() {
        populator.cleanUpDatabase().block();
    }

    @DynamicPropertySource
    static void setupMongoProps(DynamicPropertyRegistry registry) {
        String host = mongoContainer.getHost();
        int port = mongoContainer.getFirstMappedPort();
        registry.add("spring.data.mongodb.host", () -> host);
        registry.add("spring.data.mongodb.port", () -> port);
    }

    @DisplayName("No filtering in /api/vehicles")
    @Test
    void noFiltering() {
        int vehiclesCount = 30;
        populator.insertRandomVehicles(vehiclesCount)
                .blockLast();
        var actual = webClient.get()
                .uri("/api/vehicles")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(Vehicle.class)
                .returnResult().getResponseBody();

        assertThat(actual.size()).isEqualTo(vehiclesCount);
    }

    @DisplayName("Filtering by status works as expected")
    @EnumSource(VehicleStatus.class)
    @ParameterizedTest
    void filterByStatus(VehicleStatus inputStatus) {

        var mockData = List.of(
                Vehicle.builder().plateId("ABC").status(VehicleStatus.IN_SERVICE).build(),
                Vehicle.builder().plateId("DEF").status(VehicleStatus.IDLE).build(),
                Vehicle.builder().plateId("GHI").status(VehicleStatus.IN_SERVICE).build(),
                Vehicle.builder().plateId("JKL").status(VehicleStatus.IDLE).build(),
                Vehicle.builder().plateId("MNO").status(VehicleStatus.MAINTENANCE).build()
        );

        populator.insertLisOfVehicles(mockData)
                .blockLast();
        var actual = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/vehicles").queryParam("status", inputStatus).build())
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(Vehicle.class)
                .returnResult().getResponseBody();

        assertThat(actual.stream()
                .allMatch(v -> v.getStatus().equals(inputStatus)))
                .isTrue();

    }
}