package com.example.findmyplayer.PoJo;

import java.io.Serializable;

public class HirePoJo implements Serializable {


    String id;
    String recruiterId;
    String recruiterType;
    String playerId;
    String recruiterName;


    public HirePoJo(String id, String recruiterId, String playerId, String recruiterName) {
        this.id = id;
        this.recruiterId = recruiterId;
        this.playerId = playerId;
        this.recruiterName = recruiterName;
    }

    public HirePoJo(String id, String recruiterId, String recruiterType, String playerId, String recruiterName) {
        this.id = id;
        this.recruiterId = recruiterId;
        this.recruiterType = recruiterType;
        this.playerId = playerId;
        this.recruiterName = recruiterName;
    }

    public HirePoJo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecruiterId() {
        return recruiterId;
    }

    public void setRecruiterId(String recruiterId) {
        this.recruiterId = recruiterId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getRecruiterName() {
        return recruiterName;
    }

    public void setRecruiterName(String recruiterName) {
        this.recruiterName = recruiterName;
    }


}
