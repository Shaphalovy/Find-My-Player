package com.example.findmyplayer.PoJo;

import java.io.Serializable;

public class ClientPoJo implements Serializable {

    String id;
    String name;
    String email;
    String password;
    String phone;
    String address;
    String profession;
    String organization ;
    String profile_img_url;
    String description;
    String userType;

    public ClientPoJo() {
    }

    public ClientPoJo(String name, String email, String password, String phone, String address, String profession, String organization, String profile_img_url, String description, String userType) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.profession = profession;
        this.organization = organization;
        this.profile_img_url = profile_img_url;
        this.description = description;
        this.userType = userType;
    }

    public ClientPoJo(String id, String name, String email, String password, String phone, String address, String profession, String organization, String profile_img_url, String description, String userType) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.profession = profession;
        this.organization = organization;
        this.profile_img_url = profile_img_url;
        this.description = description;
        this.userType = userType;
    }

    public ClientPoJo(String name, String email, String password, String phone, String address, String profession, String organization) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.profession = profession;
        this.organization = organization;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getProfile_img_url() {
        return profile_img_url;
    }

    public void setProfile_img_url(String profile_img_url) {
        this.profile_img_url = profile_img_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
