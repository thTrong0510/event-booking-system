/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.controllers.admin;

import com.nvtt.pojo.SystemStatisticsDaily;
import com.nvtt.services.DashboardService;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author vthan
 */
@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/dashboard")
    public String showDashboard(
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            Model model) {

        // 1. Xử lý logic thiết lập mặc định trong 7 ngày gần nhất nếu chưa thực hiện bộ lọc lọc
        if (endDate == null) {
            endDate = new Date(); // Mốc thời gian hiện tại
        }
        if (startDate == null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(endDate);
            cal.add(Calendar.DAY_OF_YEAR, -7); // Lùi về trước 7 ngày
            startDate = cal.getTime();
        }

        // 2. Thu thập dữ liệu nạp vào 4 Cards hiển thị nhanh
        Map<String, Object> metrics = dashboardService.getCardMetrics();
        model.addAttribute("cardActiveEvents", metrics.get("activeEvents"));
        model.addAttribute("cardTotalRevenue", metrics.get("totalRevenue"));
        model.addAttribute("cardTotalTicketsSold", metrics.get("totalTicketsSold"));
        model.addAttribute("cardPendingOrganizers", metrics.get("pendingOrganizers"));

        // 3. Đổ dữ liệu lịch sử ra biểu đồ và bảng dữ liệu chiến lược
        List<SystemStatisticsDaily> reportData = dashboardService.getReportData(startDate, endDate);
        model.addAttribute("chartData", reportData);         // Cung cấp cho đoạn mã JSON Chart.js
        model.addAttribute("tableReportData", reportData);    // Cung cấp cho vòng lặp th:each danh sách bảng

        // 4. Giữ lại dải ngày tháng đã chọn để hiển thị cố định trên thanh Input bộ lọc ngoài View
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return "admin/dashboard"; // Trỏ đến file view dashboard.html trong thư mục admin
    }
}
