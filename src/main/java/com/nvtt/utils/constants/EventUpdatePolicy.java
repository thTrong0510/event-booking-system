package com.nvtt.utils.constants;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EventUpdatePolicy {

    private static final String STATUS_DRAFT = "DRAFT";
    private static final String STATUS_ONSALE = "ONSALE";
    private static final String STATUS_CANCELLED = "CANCELLED";
    private static final String STATUS_CANCELED = "CANCELED";
    private static final String STATUS_COMPLETED = "COMPLETED";

    private static final Map<String, Set<String>> EDITABLE_FIELDS_BY_STATUS;

    static {
        Map<String, Set<String>> map = new HashMap<>();

        map.put(STATUS_DRAFT, new HashSet<>(Set.of(
                "name",
                "description",
                "startTime",
                "endTime",
                "location",
                "totalTickets",
                "ticketPrice",
                "category",
                "newImages",
                "newVideos",
                "deletedMediaUrls"
        )));

        map.put(STATUS_ONSALE, new HashSet<>(Set.of(
                "description",
                "name",
                "location",
                "category",
                "newImages",
                "newVideos",
                "deletedMediaUrls"
        )));

        map.put(STATUS_CANCELLED, Collections.emptySet());
        map.put(STATUS_CANCELED, Collections.emptySet());
        map.put(STATUS_COMPLETED, Collections.emptySet());

        EDITABLE_FIELDS_BY_STATUS = Collections.unmodifiableMap(map);
    }

    public static Set<String> getEditableFields(String statusName) {
        return EDITABLE_FIELDS_BY_STATUS.getOrDefault(normalizeStatus(statusName), Collections.emptySet());
    }

    public static boolean isFieldEditable(String statusName, String fieldName) {
        return getEditableFields(statusName).contains(fieldName);
    }

    public static void validateEditableFields(String statusName, Set<String> fieldNames) {
        Set<String> allowed = getEditableFields(statusName);
        for (String field : fieldNames) {
            if (!allowed.contains(field)) {
                throw new IllegalArgumentException("Field '" + field + "' cannot be updated when event status is '" + statusName + "'.");
            }
        }
    }

    public static boolean allowsMediaChanges(String statusName) {
        Set<String> allowed = getEditableFields(statusName);
        return allowed.contains("newImages") || allowed.contains("newVideos") || allowed.contains("deletedMediaUrls");
    }

    private static String normalizeStatus(String statusName) {
        if (statusName == null) {
            return "";
        }
        return statusName.trim().toUpperCase();
    }
}
