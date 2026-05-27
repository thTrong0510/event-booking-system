/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.utils;

import java.util.Map;
import java.util.function.Supplier;

/**
 *
 * @author vthan
 */
public class Utilities {

    public static <T> T getSafe(Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
             throw new RuntimeException(e);
//            return null;
        }
    }

    public static boolean validateRequiredFields(
            Map<String, String> params,
            String... requiredFields
    ) {
        
        for (String field : requiredFields) {

            String value = params.get(field);

            if (value == null || value.trim().isEmpty()) {
                return false;
            }
        }
        
        return true;
    }
}
