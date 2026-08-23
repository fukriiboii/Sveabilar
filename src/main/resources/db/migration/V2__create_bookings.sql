CREATE TABLE bookings(
    
    id BIGINT NOT NULL AUTO_INCREMENT,

    customer_name VARCHAR(255) NOT NULL,
    customer_email VARCHAR(255) NOT NULL,
    customer_phone VARCHAR(255) NOT NULL, 
    address VARCHAR(255) NOT NULL, 

    booking_date DATE NOT NULL,
    start_time TIME NOT NULL, 
    end_time TIME NOT NULL,

    service_type VARCHAR(50) NOT NULL, 
    status VARCHAR(50) NOT NULL, 

    created_at DATETIME NOT NULL, 
    updated_at DATETIME NOT NULL, 

    PRIMARY KEY (id) 


)