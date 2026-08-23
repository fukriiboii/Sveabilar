ALTER TABLE bookings
ADD COLUMN availability_id BIGINT NOT NULL;

ALTER TABLE bookings
ADD CONSTRAINT fk_bookings_availability
    FOREIGN KEY (availability_id)
    REFERENCES availabilities(id);

ALTER TABLE bookings
ADD CONSTRAINT uk_bookings_availability
    UNIQUE (availability_id);