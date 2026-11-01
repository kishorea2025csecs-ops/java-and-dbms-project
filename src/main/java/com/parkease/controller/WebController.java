package com.parkease.controller;

import com.parkease.constant.TicketStatus;
import com.parkease.dto.DashboardStatsDto;
import com.parkease.entity.*;
import com.parkease.model.VehicleType;
import com.parkease.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
public class WebController {

    private final ParkingService parkingService;
    private final BillingService billingService;
    private final ReportService reportService;
    private final DatabaseRecordService dbRecordService;
    private final VehicleService vehicleService;
    private final UserService userService;

    @Autowired
    public WebController(
            ParkingService parkingService,
            BillingService billingService,
            ReportService reportService,
            DatabaseRecordService dbRecordService,
            VehicleService vehicleService,
            UserService userService) {
        this.parkingService = parkingService;
        this.billingService = billingService;
        this.reportService = reportService;
        this.dbRecordService = dbRecordService;
        this.vehicleService = vehicleService;
        this.userService = userService;
    }

    // Helper to check authentication
    private boolean isAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @GetMapping("/login")
    public String loginPage(HttpSession session, Model model) {
        if (isAuthenticated(session)) {
            return "redirect:/dashboard";
        }
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpSession session,
            Model model) {
        if (userService.authenticate(username, password)) {
            session.setAttribute("currentUser", username);
            return "redirect:/dashboard";
        }
        model.addAttribute("error", "Invalid username or password. Try admin / admin123");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout=true";
    }

    @GetMapping({"/", "/dashboard"})
    public String dashboard(HttpSession session, Model model) {
        if (!isAuthenticated(session)) {
            // Auto login as admin for demonstration convenience if not logged in
            session.setAttribute("currentUser", "admin");
        }
        DashboardStatsDto stats = reportService.getDashboardStatistics();
        model.addAttribute("stats", stats);
        model.addAttribute("recentTickets", parkingService.getAllTickets().stream().limit(6).toList());
        model.addAttribute("activeNav", "dashboard");
        return "dashboard";
    }

    @GetMapping("/park-vehicle")
    public String parkVehiclePage(HttpSession session, Model model) {
        if (!isAuthenticated(session)) session.setAttribute("currentUser", "admin");
        model.addAttribute("vehicleTypes", VehicleType.values());
        model.addAttribute("activeNav", "park-vehicle");
        return "park-vehicle";
    }

    @GetMapping("/vehicle-exit")
    public String vehicleExitPage(
            @RequestParam(value = "search", required = false) String search,
            HttpSession session,
            Model model) {
        if (!isAuthenticated(session)) session.setAttribute("currentUser", "admin");
        model.addAttribute("searchKey", search);
        model.addAttribute("activeNav", "vehicle-exit");
        return "vehicle-exit";
    }

    @GetMapping("/parking-slots")
    public String parkingSlotsPage(HttpSession session, Model model) {
        if (!isAuthenticated(session)) session.setAttribute("currentUser", "admin");
        model.addAttribute("slots", parkingService.getAllSlots());
        model.addAttribute("activeNav", "parking-slots");
        return "parking-slots";
    }

    @GetMapping("/tickets")
    public String ticketsPage(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "status", required = false) TicketStatus status,
            @RequestParam(value = "type", required = false) VehicleType type,
            HttpSession session,
            Model model) {
        if (!isAuthenticated(session)) session.setAttribute("currentUser", "admin");
        List<ParkingTicket> tickets = parkingService.searchTickets(keyword, status, type);
        model.addAttribute("tickets", tickets);
        model.addAttribute("statuses", TicketStatus.values());
        model.addAttribute("vehicleTypes", VehicleType.values());
        model.addAttribute("selectedStatus", status);
        model.addAttribute("selectedType", type);
        model.addAttribute("keyword", keyword);
        model.addAttribute("activeNav", "tickets");
        return "tickets";
    }

    @GetMapping("/vehicles")
    public String vehiclesPage(HttpSession session, Model model) {
        if (!isAuthenticated(session)) session.setAttribute("currentUser", "admin");
        model.addAttribute("vehicles", vehicleService.getAllVehicles());
        model.addAttribute("activeNav", "vehicles");
        return "vehicles";
    }

    @GetMapping("/billing")
    public String billingPage(HttpSession session, Model model) {
        if (!isAuthenticated(session)) session.setAttribute("currentUser", "admin");
        model.addAttribute("totalRevenue", billingService.getTotalRevenue());
        model.addAttribute("todayRevenue", billingService.getTodayRevenue());
        model.addAttribute("weeklyRevenue", billingService.getWeeklyRevenue());
        model.addAttribute("monthlyRevenue", billingService.getMonthlyRevenue());
        model.addAttribute("revenueBreakdown", billingService.getRevenueBreakdown());
        model.addAttribute("payments", billingService.getAllPayments());
        model.addAttribute("activeNav", "billing");
        return "billing";
    }

    @GetMapping("/reports")
    public String reportsPage(
            @RequestParam(value = "startDate", required = false) String startDateStr,
            @RequestParam(value = "endDate", required = false) String endDateStr,
            HttpSession session,
            Model model) {
        if (!isAuthenticated(session)) session.setAttribute("currentUser", "admin");
        LocalDate start = (startDateStr != null && !startDateStr.isEmpty()) ? LocalDate.parse(startDateStr) : LocalDate.now().minusDays(30);
        LocalDate end = (endDateStr != null && !endDateStr.isEmpty()) ? LocalDate.parse(endDateStr) : LocalDate.now();

        Map<String, Object> report = reportService.getAnalyticsReport(start, end);
        model.addAttribute("report", report);
        model.addAttribute("stats", reportService.getDashboardStatistics());
        model.addAttribute("startDate", start);
        model.addAttribute("endDate", end);
        model.addAttribute("activeNav", "reports");
        return "reports";
    }

    @GetMapping("/database-records")
    public String databaseRecordsPage(HttpSession session, Model model) {
        if (!isAuthenticated(session)) session.setAttribute("currentUser", "admin");
        model.addAttribute("counts", dbRecordService.getTableCounts());
        model.addAttribute("users", dbRecordService.getUsers());
        model.addAttribute("vehicles", dbRecordService.getVehicles());
        model.addAttribute("slots", dbRecordService.getParkingSlots());
        model.addAttribute("tickets", dbRecordService.getParkingTickets());
        model.addAttribute("payments", dbRecordService.getPayments());
        model.addAttribute("rates", dbRecordService.getVehicleRates());
        model.addAttribute("activeNav", "database-records");
        return "database-records";
    }

    @GetMapping("/settings")
    public String settingsPage(HttpSession session, Model model) {
        if (!isAuthenticated(session)) session.setAttribute("currentUser", "admin");
        model.addAttribute("rates", dbRecordService.getVehicleRates());
        model.addAttribute("activeNav", "settings");
        return "settings";
    }
}

