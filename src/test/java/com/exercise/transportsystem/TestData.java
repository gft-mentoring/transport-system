package com.exercise.transportsystem;

import com.exercise.transportsystem.common.domain.Coordinates;
import com.exercise.transportsystem.common.domain.vehicle.Driver;
import com.exercise.transportsystem.common.domain.vehicle.Vehicle;
import com.exercise.transportsystem.common.domain.vehicle.VehicleStatus;
import com.exercise.transportsystem.common.domain.vehicle.VehicleType;
import com.exercise.transportsystem.vehicles.infra.persistence.mongodb.document.DriverDocument;
import com.exercise.transportsystem.vehicles.infra.persistence.mongodb.document.VehicleDocument;

import java.time.LocalDate;
import java.time.LocalTime;

public class TestData {

    public static Driver driver1;
    public static DriverDocument driver1Document;
    public static Driver driver2;
    public static DriverDocument driver2Document;
    public static Vehicle bus1;
    public static VehicleDocument bus1Document;
    public static Vehicle bus2;
    public static VehicleDocument bus2Document;

    public static void initializeTestData() {
        driver1 = new Driver("test@example.com","Bart", "12345678");
        driver1Document = new DriverDocument("test@example.com","Bart", "12345678","ABC123");
        driver2 = new Driver("test2@example.com","Cletus", "75532211");
        driver2Document = new DriverDocument("test2@example.com","Cletus", "75532211","DCF456");
        bus1 = new Vehicle("ABC123", driver1, 200, VehicleStatus.IN_SERVICE, VehicleType.BUS, new Coordinates(123.00,13.00), LocalTime.now(), LocalDate.now());
        bus1Document = new VehicleDocument("ABC123", driver1.getEmail(), 200, VehicleStatus.IN_SERVICE, VehicleType.BUS, new Coordinates(123.00,13.00), LocalTime.now(), LocalDate.now());
        bus2 = new Vehicle("DCF456", driver2, 170, VehicleStatus.IDLE, VehicleType.BUS, new Coordinates(67.00,91.00), LocalTime.now(), LocalDate.now());
        bus2Document = new VehicleDocument("DCF456", driver2.getEmail() , 170, VehicleStatus.IDLE, VehicleType.BUS, new Coordinates(67.00,91.00), LocalTime.now(), LocalDate.now());
    }

}
