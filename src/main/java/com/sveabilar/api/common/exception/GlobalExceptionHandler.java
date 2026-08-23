package com.sveabilar.api.common.exception;

import com.sveabilar.api.features.auth.exception.InvalidCredentialsException;
import com.sveabilar.api.features.auth.exception.UserInactiveException;
import com.sveabilar.api.features.availability.exception.AvailabilityNotAvailableException;
import com.sveabilar.api.features.availability.exception.AvailabilityNotFoundException;
import com.sveabilar.api.features.booking.exception.BookingCanNotBeCancelledException;
import com.sveabilar.api.features.booking.exception.BookingNotFoundException;
import com.sveabilar.api.features.user.exception.UserNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(InvalidCredentialsException.class)
        public ResponseEntity<String> handleInvalidCredentials(InvalidCredentialsException exception) {

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getMessage());
        }

        @ExceptionHandler(UserInactiveException.class)
        public ResponseEntity<String> handleUserInactive(UserInactiveException exception) {

                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception.getMessage());
        }

        @ExceptionHandler(UserNotFoundException.class)
        public ResponseEntity<String> handleUserNotFound(UserNotFoundException exception) {

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }

        @ExceptionHandler(AvailabilityNotFoundException.class)
        public ResponseEntity<String> handleAvailabilityNotFound(AvailabilityNotFoundException exception) {

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }

        @ExceptionHandler(AvailabilityNotAvailableException.class)
        public ResponseEntity<String> handleAvailabilityNotAvailable(AvailabilityNotAvailableException exception) {

                return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
        }

        @ExceptionHandler(BookingCanNotBeCancelledException.class)
        public ResponseEntity<String> handleBookingCanNotBeCancelled(BookingCanNotBeCancelledException exception) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage()); 
        }

        @ExceptionHandler(BookingNotFoundException.class)
        public ResponseEntity<String> handleBookingNotFound(BookingNotFoundException exception) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage()); 
        }
}