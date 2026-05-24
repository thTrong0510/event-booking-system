/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.pojo.dtos.event;

import java.math.BigDecimal;

/**
 *
 * @author vthan
 */
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
    private String representative_image; // Lấy ảnh IMAGE đầu tiên từ event_media
    private String video_url; // Nếu cần map thêm từ media hoặc để trống

    private CategoryDTO category;
    private OrganizerDTO organizer;
    private StatisticsDTO statistics;

    public static class CategoryDTO {

        private String name;

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * @param name the name to set
         */
        public void setName(String name) {
            this.name = name;
        }
    }

    public static class OrganizerDTO {

        private String full_name;
        private String avatar_url;

        /**
         * @return the full_name
         */
        public String getFull_name() {
            return full_name;
        }

        /**
         * @param full_name the full_name to set
         */
        public void setFull_name(String full_name) {
            this.full_name = full_name;
        }

        /**
         * @return the avatar_url
         */
        public String getAvatar_url() {
            return avatar_url;
        }

        /**
         * @param avatar_url the avatar_url to set
         */
        public void setAvatar_url(String avatar_url) {
            this.avatar_url = avatar_url;
        }

    }

    public static class StatisticsDTO {

        private Integer total_tickets_sold;
        private Integer total_views;

        /**
         * @return the total_tickets_sold
         */
        public Integer getTotal_tickets_sold() {
            return total_tickets_sold;
        }

        /**
         * @param total_tickets_sold the total_tickets_sold to set
         */
        public void setTotal_tickets_sold(Integer total_tickets_sold) {
            this.total_tickets_sold = total_tickets_sold;
        }

        /**
         * @return the total_views
         */
        public Integer getTotal_views() {
            return total_views;
        }

        /**
         * @param total_views the total_views to set
         */
        public void setTotal_views(Integer total_views) {
            this.total_views = total_views;
        }

    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the ticket_price
     */
    public BigDecimal getTicket_price() {
        return ticket_price;
    }

    /**
     * @param ticket_price the ticket_price to set
     */
    public void setTicket_price(BigDecimal ticket_price) {
        this.ticket_price = ticket_price;
    }

    /**
     * @return the start_time
     */
    public String getStart_time() {
        return start_time;
    }

    /**
     * @param start_time the start_time to set
     */
    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    /**
     * @return the end_time
     */
    public String getEnd_time() {
        return end_time;
    }

    /**
     * @param end_time the end_time to set
     */
    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the available_tickets
     */
    public Integer getAvailable_tickets() {
        return available_tickets;
    }

    /**
     * @param available_tickets the available_tickets to set
     */
    public void setAvailable_tickets(Integer available_tickets) {
        this.available_tickets = available_tickets;
    }

    /**
     * @return the total_tickets
     */
    public Integer getTotal_tickets() {
        return total_tickets;
    }

    /**
     * @param total_tickets the total_tickets to set
     */
    public void setTotal_tickets(Integer total_tickets) {
        this.total_tickets = total_tickets;
    }

    /**
     * @return the representative_image
     */
    public String getRepresentative_image() {
        return representative_image;
    }

    /**
     * @param representative_image the representative_image to set
     */
    public void setRepresentative_image(String representative_image) {
        this.representative_image = representative_image;
    }

    /**
     * @return the video_url
     */
    public String getVideo_url() {
        return video_url;
    }

    /**
     * @param video_url the video_url to set
     */
    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    /**
     * @return the category
     */
    public CategoryDTO getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

    /**
     * @return the organizer
     */
    public OrganizerDTO getOrganizer() {
        return organizer;
    }

    /**
     * @param organizer the organizer to set
     */
    public void setOrganizer(OrganizerDTO organizer) {
        this.organizer = organizer;
    }

    /**
     * @return the statistics
     */
    public StatisticsDTO getStatistics() {
        return statistics;
    }

    /**
     * @param statistics the statistics to set
     */
    public void setStatistics(StatisticsDTO statistics) {
        this.statistics = statistics;
    }

}
