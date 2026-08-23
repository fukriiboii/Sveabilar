CREATE TABLE availabilities(

    id BIGINT NOT NULL AUTO_INCREMENT, 

    date DATE NOT NULL, 
    start_time TIME NOT NULL, 
    end_time TIME NOT NULL, 
    status VARCHAR(20) NOT NULL, 
    created_at DATETIME NOT NULL, 
    updated_at DATETIME NOT NULL, 

    PRIMARY KEY (id)
);