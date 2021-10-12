package com.example.findmyplayer.PoJo;

import java.io.Serializable;

public class UserPoJo implements Serializable {

    String id;
    String name;
    String email;
    String password;
    String phone;
    String address;
    String gender;
    String sports;
    String playerType;
    String profile_img_url;
    String game_role_address;
    String history;
    String userType;
    String price;

    public UserPoJo() {
    }

    public UserPoJo(String id, String name, String email, String password, String phone,
                    String address, String gender, String sports, String playerType,
                    String profile_img_url, String game_role_address,
                    String history, String userType, String price) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
        this.sports = sports;
        this.playerType = playerType;
        this.profile_img_url = profile_img_url;
        this.game_role_address = game_role_address;
        this.history = history;
        this.userType = userType;
        this.price = price;
    }

    public UserPoJo(String name, String email, String password, String phone,
                    String address, String gender, String sports) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
        this.sports = sports;
    }

    public UserPoJo(String name, String email, String password, String phone,
                    String address, String gender, String sports, String playerType,
                    String profile_img_url, String game_role_address) {

        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
        this.sports = sports;
        this.playerType = playerType;
        this.profile_img_url = profile_img_url;
        this.game_role_address = game_role_address;
    }

    public UserPoJo(String id, String name, String email, String password, String phone,
                    String address, String gender, String sports, String playerType,
                    String profile_img_url, String game_role_address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
        this.sports = sports;
        this.playerType = playerType;
        this.profile_img_url = profile_img_url;
        this.game_role_address = game_role_address;
    }

    public UserPoJo(String id, String name, String email, String password, String phone, String address, String gender, String sports, String playerType, String profile_img_url, String game_role_address, String history) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
        this.sports = sports;
        this.playerType = playerType;
        this.profile_img_url = profile_img_url;
        this.game_role_address = game_role_address;
        this.history = history;
    }

    public UserPoJo(String id, String name, String email, String password, String phone, String address, String gender, String sports, String playerType, String profile_img_url, String game_role_address, String history, String userType) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
        this.sports = sports;
        this.playerType = playerType;
        this.profile_img_url = profile_img_url;
        this.game_role_address = game_role_address;
        this.history = history;
        this.userType = userType;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSports() {
        return sports;
    }

    public void setSports(String sports) {
        this.sports = sports;
    }

    public String getPlayerType() {
        return playerType;
    }

    public void setPlayerType(String playerType) {
        this.playerType = playerType;
    }

    public String getProfile_img_url() {
        return profile_img_url;
    }

    public void setProfile_img_url(String profile_img_url) {
        this.profile_img_url = profile_img_url;
    }

    public String getGame_role_address() {
        return game_role_address;
    }

    public void setGame_role_address(String game_role_address) {
        this.game_role_address = game_role_address;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
