/*
* Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
* Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
*/
package com.nvtt.pojo.dtos.event;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

/**
*
* @author lequa
*/
public class ResEventInfoDTO {

   private Long id;
   private String name;
   private String description;
   private Set<ResEventMediaDTO> eventMedias;
   private Date startTime;
   private Date endTime;
   private String location;
   private int totalTickets;
   private Date createdAt;
   private Date updatedAt;
   private BigDecimal ticketPrice;
   private int availableTickets;
   private String status;
   private String category;
   private int views;
   
   public ResEventInfoDTO(Long id, String name, String description, Set<ResEventMediaDTO> eventMedias, 
       Date startTime, Date endTime, String location, int totalTickets, 
       Date createdAt, Date updatedAt, BigDecimal ticketPrice, int availableTickets, String status, String category, int views) {
       this.id = id;
       this.name = name;
       this.description = description;
       this.eventMedias = eventMedias;
       this.startTime = startTime;
       this.endTime = endTime;
       this.location = location;
       this.totalTickets = totalTickets;
       this.createdAt = createdAt;
       this.updatedAt = updatedAt;
       this.ticketPrice = ticketPrice;
       this.availableTickets = availableTickets;
       this.status = status;
       this.category = category;
       this.views = views;
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
    * @return the startTime
    */
   public Date getStartTime() {
       return startTime;
   }

   /**
    * @param startTime the startTime to set
    */
   public void setStartTime(Date startTime) {
       this.startTime = startTime;
   }

   /**
    * @return the endTime
    */
   public Date getEndTime() {
       return endTime;
   }

   /**
    * @param endTime the endTime to set
    */
   public void setEndTime(Date endTime) {
       this.endTime = endTime;
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
    * @return the totalTickets
    */
   public int getTotalTickets() {
       return totalTickets;
   }

   /**
    * @param totalTickets the totalTickets to set
    */
   public void setTotalTickets(int totalTickets) {
       this.totalTickets = totalTickets;
   }

   /**
    * @return the createdAt
    */
   public Date getCreatedAt() {
       return createdAt;
   }

   /**
    * @param createdAt the createdAt to set
    */
   public void setCreatedAt(Date createdAt) {
       this.createdAt = createdAt;
   }

   /**
    * @return the updatedAt
    */
   public Date getUpdatedAt() {
       return updatedAt;
   }

   /**
    * @param updatedAt the updatedAt to set
    */
   public void setUpdatedAt(Date updatedAt) {
       this.updatedAt = updatedAt;
   }

   /**
    * @return the eventMedias
    */
   public Set<ResEventMediaDTO> getEventMedias() {
       return eventMedias;
   }

   /**
    * @param eventMedias the eventMedias to set
    */
   public void setEventMedias(Set<ResEventMediaDTO> eventMedias) {
       this.eventMedias = eventMedias;
   }

    /**
     * @return the ticketPrice
     */
    public BigDecimal getTicketPrice() {
        return ticketPrice;
    }

    /**
     * @param ticketPrice the ticketPrice to set
     */
    public void setTicketPrice(BigDecimal ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    /**
     * @return the availableTickets
     */
    public int getAvailableTickets() {
        return availableTickets;
    }

    /**
     * @param availableTickets the availableTickets to set
     */
    public void setAvailableTickets(int availableTickets) {
        this.availableTickets = availableTickets;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * @return the views
     */
    public int getViews() {
        return views;
    }

    /**
     * @param views the views to set
     */
    public void setViews(int views) {
        this.views = views;
    }
    

}
