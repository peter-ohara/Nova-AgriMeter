package com.novaagrisystems.agrimeter;

/**
 * Created by peter on 12/30/16.
 */

public class SensorEvent {

    public Long timestamp;
    public Double temperature;
    public Long light;
    public Long soilMoisture;
    public Long humidity;

    public SensorEvent() {
    }

    public SensorEvent(Long timestamp, Double temperature, Long light, Long soilMoisture, Long humidity) {
        this.timestamp = timestamp;
        this.temperature = temperature;
        this.light = light;
        this.soilMoisture = soilMoisture;
        this.humidity = humidity;
    }
}
