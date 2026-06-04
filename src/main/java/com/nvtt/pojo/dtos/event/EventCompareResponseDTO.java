package com.nvtt.pojo.dtos.event;

import java.math.BigDecimal;

public class EventCompareResponseDTO {

    private Long id;
    private String name;
    private String description;
    private BigDecimal ticket_price;
    private String start_time;
    private String end_time;
    private String location;
    private Integer available_tickets;
    private Integer total_tickets;
    private String representative_image;
    private String video_url;

    private CategoryDTO category;
    private OrganizerDTO organizer;
    private StatisticsDTO statistics;

    public static class CategoryDTO {

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class OrganizerDTO {

        private String full_name;
        private String avatar_url;

        public String getFull_name() {
            return full_name;
        }

        public void setFull_name(String full_name) {
            this.full_name = full_name;
        }

        public String getAvatar_url() {
            return avatar_url;
        }

        public void setAvatar_url(String avatar_url) {
            this.avatar_url = avatar_url;
        }

    }

    public static class StatisticsDTO {

        private Integer total_tickets_sold;
        private Integer total_views;

        public Integer getTotal_tickets_sold() {
            return total_tickets_sold;
        }

        public void setTotal_tickets_sold(Integer total_tickets_sold) {
            this.total_tickets_sold = total_tickets_sold;
        }

        public Integer getTotal_views() {
            return total_views;
        }

        public void setTotal_views(Integer total_views) {
            this.total_views = total_views;
        }

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getTicket_price() {
        return ticket_price;
    }

    public void setTicket_price(BigDecimal ticket_price) {
        this.ticket_price = ticket_price;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getAvailable_tickets() {
        return available_tickets;
    }

    public void setAvailable_tickets(Integer available_tickets) {
        this.available_tickets = available_tickets;
    }

    public Integer getTotal_tickets() {
        return total_tickets;
    }

    public void setTotal_tickets(Integer total_tickets) {
        this.total_tickets = total_tickets;
    }

    public String getRepresentative_image() {
        return representative_image;
    }

    public void setRepresentative_image(String representative_image) {
        this.representative_image = representative_image;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

    public OrganizerDTO getOrganizer() {
        return organizer;
    }

    public void setOrganizer(OrganizerDTO organizer) {
        this.organizer = organizer;
    }

    public StatisticsDTO getStatistics() {
        return statistics;
    }

    public void setStatistics(StatisticsDTO statistics) {
        this.statistics = statistics;
    }

}
