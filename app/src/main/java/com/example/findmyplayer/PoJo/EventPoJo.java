package com.example.findmyplayer.PoJo;

import java.io.Serializable;

public class EventPoJo implements Serializable {

    String id;
    String eventCreatorId;
    String sports;
    String location;
    String date;
    String time;
    String description;
    String downloadUrl;
    String limit;

    public EventPoJo(String id, String eventCreatorId, String sports, String location, String date, String time, String description, String downloadUrl, String limit) {
        this.id = id;
        this.eventCreatorId = eventCreatorId;
        this.sports = sports;
        this.location = location;
        this.date = date;
        this.time = time;
        this.description = description;
        this.downloadUrl = downloadUrl;
        this.limit = limit;
    }

    public EventPoJo(String id, String eventCreatorId, String sports, String location, String date, String time, String description) {
        this.id = id;
        this.eventCreatorId = eventCreatorId;
        this.sports = sports;
        this.location = location;
        this.date = date;
        this.time = time;
        this.description = description;
    }

    public EventPoJo(String id, String sports, String location, String date, String time, String description) {
        this.id = id;
        this.sports = sports;
        this.location = location;
        this.date = date;
        this.time = time;
        this.description = description;
    }

    public EventPoJo(String id, String sports, String location, String date) {
        this.id = id;
        this.sports = sports;
        this.location = location;
        this.date = date;
    }

    public EventPoJo(String sports, String location, String date) {
        this.sports = sports;
        this.location = location;
        this.date = date;
    }

    public EventPoJo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventCreatorId() {
        return eventCreatorId;
    }

    public void setEventCreatorId(String eventCreatorId) {
        this.eventCreatorId = eventCreatorId;
    }

    public String getSports() {
        return sports;
    }

    public void setSports(String sports) {
        this.sports = sports;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }
}
