package com.app.tryitat.ui.pointlist;

public class PointModel {
    String clientID;
    String clientName;
    String clientPic;
    String objectId;
    String showCamera;
    int totalPoints =0;
    String userID;
    int visits= 0;
//
//    public PointModel(String MclientID, String MclientName, String MclientPic, String MobjectId, String MshowCamera, String MtotalPoints, String MuserID, String Mvisits) {
//        this.clientID = MclientID;
//        this.clientName = MclientName;
//        this.clientPic = MclientPic;
//        this.objectId = MobjectId;
//        this.showCamera = MshowCamera;
//        this.totalPoints = MtotalPoints;
//        this.userID = MuserID;
//        this.visits = Mvisits;
//    }
    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientPic() {
        return clientPic;
    }

    public void setClientPic(String clientPic) {
        this.clientPic = clientPic;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getShowCamera() {
        return showCamera;
    }

    public void setShowCamera(String showCamera) {
        this.showCamera = showCamera;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getVisits() {
        return visits;
    }

    public void setVisits(int visits) {
        this.visits = visits;
    }
}
