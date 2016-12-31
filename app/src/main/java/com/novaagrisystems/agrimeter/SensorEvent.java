package com.novaagrisystems.agrimeter;

/**
 * Created by peter on 12/30/16.
 */

public class SensorEvent {

    public Long datetime;
    public Float value;

    public SensorEvent() {
    }

    public SensorEvent(Long datetime, Float value) {
        this.datetime = datetime;
        this.value = value;
    }
}
