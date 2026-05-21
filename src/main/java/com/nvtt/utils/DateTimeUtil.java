/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.utils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vthan
 */

public class DateTimeUtil {

    // Định dạng chuẩn: ngày/tháng/năm giờ:phút:giây (Ví dụ: 19/05/2026 15:30:45)
    private static final String PATTERN = "dd/MM/yyyy HH:mm:ss";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(PATTERN);

    /**
     * Chuyển đổi từ LocalDateTime thành String (Dành cho Java 8+ Entities)
     */
    public static String toString(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return "";
        }
        return localDateTime.format(FORMATTER);
    }

    /**
     * Chuyển đổi từ String thành LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDateTime.parse(dateStr, FORMATTER);
        } catch (Exception e) {
            // Log lỗi nếu chuỗi truyền vào không đúng định dạng
            System.err.println("Lỗi parse chuỗi thời gian: " + e.getMessage());
            return null;
        }
    }

    /**
     * HỖ TRỢ THÊM: Chuyển đổi từ java.util.Date thành String (Nếu DB của bạn dùng Date cũ)
     */
    public static String dateToString(Date date) {
        if (date == null) {
            return "";
        }
        // Chuyển util.Date sang LocalDateTime thông qua System Zone hệ thống rồi định dạng
        LocalDateTime localDateTime = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        return toString(localDateTime);
    }

    /**
     * HỖ TRỢ THÊM: Chuyển đổi từ String thành java.util.Date
     */
    public static Date toDate(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(dateStr);
            return date;
        } catch (ParseException ex) {
            System.err.println("error: parse String to date");
            return null;
        }
    }
}
