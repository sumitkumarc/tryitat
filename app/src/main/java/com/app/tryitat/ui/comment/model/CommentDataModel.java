package com.app.tryitat.ui.comment.model;

public class CommentDataModel {
    String Commentxt;
    String UserId;
    String UserName;
    String createdAt;
    String objectId;
    String updatedAt;

    public String getCommentxt() {
        return Commentxt;
    }

    public void setCommentxt(String commentxt) {
        Commentxt = commentxt;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
