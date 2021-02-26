package com.app.tryitat.ui.notification.model;

public class NotificationResponse {

    String Followid;
    String Postimage;
    String ShopeName;
    String Type;
    double createdAt;
    String currentUserId;
    String objectId;
    String postId;
    int starus;
    double updatedAt;


    public NotificationResponse() {
    }

    public NotificationResponse(String followid, String postimage, String shopeName, String type,
                                double createdAt, String currentUserId, String objectId, String postId, int starus,
                                double updatedAt) {
        Followid = followid;
        Postimage = postimage;
        ShopeName = shopeName;
        Type = type;
        this.createdAt = createdAt;
        this.currentUserId = currentUserId;
        this.objectId = objectId;
        this.postId = postId;
        this.starus = starus;
        this.updatedAt = updatedAt;

    }

    public String getFollowid() {
        return Followid;
    }

    public void setFollowid(String followid) {
        Followid = followid;
    }

    public String getPostimage() {
        return Postimage;
    }

    public void setPostimage(String postimage) {
        Postimage = postimage;
    }

    public String getShopeName() {
        return ShopeName;
    }

    public void setShopeName(String shopeName) {
        ShopeName = shopeName;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public double getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(double createdAt) {
        this.createdAt = createdAt;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public int getStarus() {
        return starus;
    }

    public void setStarus(int starus) {
        this.starus = starus;
    }

    public double getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(double updatedAt) {
        this.updatedAt = updatedAt;
    }


}
