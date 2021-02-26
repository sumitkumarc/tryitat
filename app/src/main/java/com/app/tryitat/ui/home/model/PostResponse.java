package com.app.tryitat.ui.home.model;

import java.util.HashMap;
import java.util.List;

public class PostResponse {
    String Address;
    String City;
    int Comments;
    HashMap<String, String> Followers;
    String Inyourmind;
    long Latitude;
    HashMap<String, String> LikedUser;
    int Likes;
    long Longitude;
    String Name;
    String Photo;
    String Place;
    int Tries;
    String UserID;
    String UserName;
    String category;
    long createdAt;
    String objectId;
    String rating;

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public int getComments() {
        return Comments;
    }

    public void setComments(int comments) {
        Comments = comments;
    }

    public HashMap<String, String> getFollowers() {
        return Followers;
    }

    public void setFollowers(HashMap<String, String> followers) {
        Followers = followers;
    }

    public HashMap<String, String> getLikedUser() {
        return LikedUser;
    }

    public void setLikedUser(HashMap<String, String> likedUsers) {
        LikedUser = likedUsers;
    }

    public String getInyourmind() {
        return Inyourmind;
    }

    public void setInyourmind(String inyourmind) {
        Inyourmind = inyourmind;
    }

    public long getLatitude() {
        return Latitude;
    }

    public void setLatitude(long latitude) {
        Latitude = latitude;
    }

    public int getLikes() {
        return Likes;
    }

    public void setLikes(int likes) {
        Likes = likes;
    }

    public long getLongitude() {
        return Longitude;
    }

    public void setLongitude(long longitude) {
        Longitude = longitude;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public String getPlace() {
        return Place;
    }

    public void setPlace(String place) {
        Place = place;
    }

    public int getTries() {
        return Tries;
    }

    public void setTries(int tries) {
        Tries = tries;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
