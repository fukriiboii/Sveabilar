package com.sveabilar.api.features.booking.entity;

public enum ServiceType {
    TIRE_CHANGE,
    HEADLIGHT_REPAIR,
    CAR_SERVICE,
    CAR_TRANSPORT, 
    MINOR_REPAIRS;

    public String getDisplayName() {
        return switch (this) {
            case TIRE_CHANGE -> "Däckskifte";
            case HEADLIGHT_REPAIR -> "Strålkastarrenovering";
            case CAR_SERVICE -> "Service";
            case CAR_TRANSPORT -> "Biltransport";
            case MINOR_REPAIRS -> "Mindre reparationer";
        };
    }
}