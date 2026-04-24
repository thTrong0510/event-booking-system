/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvtt.pojo;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author vthan
 */
@Entity
@Table(name = "event_media")
@NamedQueries({
    @NamedQuery(name = "EventMedia.findAll", query = "SELECT e FROM EventMedia e"),
    @NamedQuery(name = "EventMedia.findById", query = "SELECT e FROM EventMedia e WHERE e.id = :id"),
    @NamedQuery(name = "EventMedia.findByMediaType", query = "SELECT e FROM EventMedia e WHERE e.mediaType = :mediaType")})
public class EventMedia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "media_type")
    private String mediaType;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "media_url")
    private String mediaUrl;
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Event event;

    public EventMedia() {
    }

    public EventMedia(Long id) {
        this.id = id;
    }

    public EventMedia(Long id, String mediaType, String mediaUrl) {
        this.id = id;
        this.mediaType = mediaType;
        this.mediaUrl = mediaUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : Objects.hash(mediaType, mediaUrl);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        EventMedia other = (EventMedia) object;
        if (id != null && other.id != null) {
            return id.equals(other.id);
        }
        return mediaType.equals(other.mediaType) && mediaUrl.equals(other.mediaUrl);
    }

    @Override
    public String toString() {
        return "com.nvtt.pojo.EventMedia[ id=" + id + " ]";
    }
    
}
