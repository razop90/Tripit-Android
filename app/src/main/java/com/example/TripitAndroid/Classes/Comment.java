package com.example.TripitAndroid.Classes;

import com.google.firebase.database.Exclude;

public class Comment {
    @Exclude
    public String id;
    public String userID;
    public String comment;
    public Object lastUpdate;
    @Exclude
    public Object timestamp;

    public Comment(String userId, String comment, long timestamp, String id) {
        this.id = id;
        this.userID = userId;
        this.comment = comment;
        this.lastUpdate = timestamp;
        //this.lastUpdate = Consts.General.convertTimestampToStringDate(this.timestamp)
    }
}