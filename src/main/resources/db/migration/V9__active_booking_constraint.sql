ALTER TABLE bookings
  ADD COLUMN active_booking TINYINT(1)
  GENERATED ALWAYS AS (
    CASE
      WHEN status = 'CONFIRMED' THEN 1
      ELSE 0
    END
  ) STORED AFTER status;

ALTER TABLE bookings
  ADD UNIQUE KEY ux_active_booking_per_availability (availability_id, active_booking);