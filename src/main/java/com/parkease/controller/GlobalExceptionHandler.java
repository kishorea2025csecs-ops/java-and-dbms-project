package com.parkease.controller;

import com.parkease.dto.ParkingResponse;
import com.parkease.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ParkingFullException.class)
    @ResponseBody
    public ResponseEntity<ParkingResponse> handleParkingFull(ParkingFullException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ParkingResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(DuplicateVehicleException.class)
    @ResponseBody
    public ResponseEntity<ParkingResponse> handleDuplicateVehicle(DuplicateVehicleException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ParkingResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(TicketNotFoundException.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleTicketNotFound(TicketNotFoundException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("success", false);
        error.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(InvalidTicketStateException.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleInvalidState(InvalidTicketStateException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("success", false);
        error.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(DuplicatePaymentException.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleDuplicatePayment(DuplicatePaymentException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("success", false);
        error.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(InvalidVehicleException.class)
    @ResponseBody
    public ResponseEntity<ParkingResponse> handleInvalidVehicle(InvalidVehicleException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ParkingResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("success", false);
        error.put("message", "An unexpected error occurred: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
