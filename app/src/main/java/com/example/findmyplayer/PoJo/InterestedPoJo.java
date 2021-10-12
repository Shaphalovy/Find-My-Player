package com.example.findmyplayer.PoJo;

import java.io.Serializable;

public class InterestedPoJo implements Serializable {

    String id;
    String eventId;
    String eventCreatorId;
    String interestedUserId;
    String interestedUserName;
    String sports;
    String location;
    String user_event;

    public InterestedPoJo() {
    }

    public InterestedPoJo(String id, String eventId, String eventCreatorId, String interestedUserId, String interestedUserName, String sports, String location, String user_event) {
        this.id = id;
        this.eventId = eventId;
        this.eventCreatorId = eventCreatorId;
        this.interestedUserId = interestedUserId;
        this.interestedUserName = interestedUserName;
        this.sports = sports;
        this.location = location;
        this.user_event = user_event;
    }

    public InterestedPoJo(String id, String eventId, String eventCreatorId, String interestedUserId, String interestedUserName, String sports, String location) {
        this.id = id;
        this.eventId = eventId;
        this.eventCreatorId = eventCreatorId;
        this.interestedUserId = interestedUserId;
        this.interestedUserName = interestedUserName;
        this.sports = sports;
        this.location = location;
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

    public String getInterestedUserId() {
        return interestedUserId;
    }

    public void setInterestedUserId(String interestedUserId) {
        this.interestedUserId = interestedUserId;
    }

    public String getInterestedUserName() {
        return interestedUserName;
    }

    public void setInterestedUserName(String interestedUserName) {
        this.interestedUserName = interestedUserName;
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

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getUser_event() {
        return user_event;
    }

    public void setUser_event(String user_event) {
        this.user_event = user_event;
    }
}
