package com.nvtt.repositories;

import java.util.Map;

import com.nvtt.pojo.EventMedia;

public interface EventMediaRepository {

    void deleteEventMedia(EventMedia media);

    EventMedia getEventMedia(Map<String, String> params);
}
