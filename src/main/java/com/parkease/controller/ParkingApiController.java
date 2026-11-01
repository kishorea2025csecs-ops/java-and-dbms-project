package com.parkease.controller;

import com.parkease.dto.*;
import com.parkease.entity.ParkingSlot;
import com.parkease.entity.ParkingTicket;
import com.parkease.entity.VehicleRate;
import com.parkease.model.VehicleType;
import com.parkease.repository.VehicleRateRepository;
import com.parkease.service.ParkingService;
import com.parkease.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ParkingApiController {

    private final ParkingService parkingService;
    private final ReportService reportService;
    private final VehicleRateRepository rateRepository;

    @Autowired
    public ParkingApiController(
            ParkingService parkingService,
            ReportService reportService,
            VehicleRateRepository rateRepository) {
        this.parkingService = parkingService;
        this.reportService = reportService;
        this.rateRepository = rateRepository;
    }

    @PostMapping("/park")
    public ResponseEntity<ParkingResponse> parkVehicle(@RequestBody ParkVehicleRequest request) {
        if (request != null) {
            request.validate();
        }
        ParkingResponse response = parkingService.parkVehicle(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/slots")
    public ResponseEntity<List<ParkingSlot>> getAllSlots() {
        return ResponseEntity.ok(parkingService.getAllSlots());
    }

    @GetMapping("/exit/calculate")
    public ResponseEntity<ExitCalculationDto> calculateExit(@RequestParam("search") String searchKey) {
        ExitCalculationDto calculation = parkingService.calculateExitCharge(searchKey);
        return ResponseEntity.ok(calculation);
    }

    @PostMapping("/exit/process")
    public ResponseEntity<ReceiptDto> processExit(@RequestBody ProcessExitRequest request) {
        if (request != null) {
            request.validate();
        }
        ReceiptDto receipt = parkingService.processVehicleExit(request);
        return ResponseEntity.ok(receipt);
    }

    @GetMapping("/dashboard/stats")
    public ResponseEntity<DashboardStatsDto> getDashboardStats() {
        return ResponseEntity.ok(reportService.getDashboardStatistics());
    }

    @GetMapping("/reports/export-csv")
    public ResponseEntity<byte[]> exportTicketsCsv(
            @RequestParam(value = "startDate", required = false) String startStr,
            @RequestParam(value = "endDate", required = false) String endStr) {

        LocalDate start = (startStr != null && !startStr.isEmpty()) ? LocalDate.parse(startStr) : LocalDate.now().minusDays(30);
        LocalDate end = (endStr != null && !endStr.isEmpty()) ? LocalDate.parse(endStr) : LocalDate.now();

        Map<String, Object> report = reportService.getAnalyticsReport(start, end);
        @SuppressWarnings("unchecked")
        List<ParkingTicket> tickets = (List<ParkingTicket>) report.get("tickets");

        String csvData = reportService.generateTicketsCsv(tickets);
        byte[] bytes = csvData.getBytes();

        String filename = "ParkEase_Report_" + LocalDate.now() + ".csv";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
                .body(bytes);
    }

    @PostMapping("/settings/rates")
    public ResponseEntity<Map<String, Object>> updateRate(
            @RequestParam("type") VehicleType type,
            @RequestParam("rate") BigDecimal rate) {
        Map<String, Object> resp = new HashMap<>();
        rateRepository.findByVehicleType(type).ifPresentOrElse(vr -> {
            vr.setHourlyRate(rate);
            rateRepository.save(vr);
            resp.put("success", true);
            resp.put("message", "Rate updated successfully for " + type);
        }, () -> {
            VehicleRate vr = new VehicleRate(type, rate, type.getSlotsRequired());
            rateRepository.save(vr);
            resp.put("success", true);
            resp.put("message", "Rate created successfully for " + type);
        });
        return ResponseEntity.ok(resp);
    }
}
