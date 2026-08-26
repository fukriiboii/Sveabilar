SET @has_availability_index = (
	SELECT COUNT(*)
	FROM information_schema.statistics
	WHERE table_schema = DATABASE()
	  AND table_name = 'bookings'
	  AND column_name = 'availability_id'
);

SET @add_availability_index = IF(
	@has_availability_index = 0,
	'ALTER TABLE bookings ADD INDEX idx_bookings_availability (availability_id)',
	'SELECT 1'
);

PREPARE add_availability_index FROM @add_availability_index;
EXECUTE add_availability_index;
DEALLOCATE PREPARE add_availability_index;

SET @has_unique_index = (
	SELECT COUNT(*)
	FROM information_schema.statistics
	WHERE table_schema = DATABASE()
	  AND table_name = 'bookings'
	  AND index_name = 'uk_bookings_availability'
);

SET @drop_unique_index = IF(
	@has_unique_index > 0,
	'ALTER TABLE bookings DROP INDEX uk_bookings_availability',
	'SELECT 1'
);

PREPARE drop_unique_index FROM @drop_unique_index;
EXECUTE drop_unique_index;
DEALLOCATE PREPARE drop_unique_index;