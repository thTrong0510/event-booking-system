/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.repositories;

import java.util.List;
import java.util.Map;

import com.nvtt.pojo.EventMedia;

/**
 *
 * @author lequa
 */
public interface EventMediaRepository {
    void deleteEventMedia(EventMedia media);
    EventMedia getEventMedia(Map<String, String> params);
}
