package com.example.TripitAndroid.Classes;

import com.example.TripitAndroid.Consts;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Post {
    @Exclude
    public String id;
    public String userID;
    public String location;
    public String description;
    @Exclude
    public String creationDateStringFormat;
    public Object creationDate;
    @Exclude
    public long creationDateLongFormat;
    public Object lastUpdate;
    @Exclude
    public long lastUpdateLongFormat;
    public String imageUrl;
    public  HashMap<String, String> likes; //contains user id's
    public int isDeleted; //0 for false, 1 for true
    public ArrayList<Comment> comments;

    public Post() {

    }

    public Post(DataSnapshot snapshot) {
        String key = snapshot.getKey();
        Map values = (Map) snapshot.getValue();

        id = key;
        userID = (String) values.get("userID");
        location = (String) values.get("location");
        description = (String) values.get("description");
        imageUrl = (String) values.get("imageUrl");

        if(values.containsKey("likes"))
            likes = new HashMap<String, String>((Map)values.get("likes"));
        else
            likes = new HashMap<>();

        comments = new ArrayList<>();
        creationDate = values.get("creationDate");

        creationDateLongFormat = 0;
        try {
            creationDateLongFormat = (long)creationDate;
        } catch (Exception e) { }

        creationDateStringFormat = Consts.General.convertTimestampToStringDate(creationDateLongFormat, null);
        lastUpdate =  values.get("lastUpdate");

        lastUpdateLongFormat = 0;
        try {
            lastUpdateLongFormat = (long)lastUpdate;
        } catch (Exception e) { }
        //isDeleted = (int) values.get("isDeleted");
    }

    public Post(String _userID,String _id,String _location,String _description,long _creationDate,String _imageUrl,long _lastUpdate){
        id = _id;
        userID = _userID;
        location = _location;
        description = _description;
        imageUrl = _imageUrl;
        likes = new HashMap<String, String>();
        comments = new ArrayList<Comment>();
        creationDate = _creationDate;
        //creationDateStringFormat = Consts.General.convertTimestampToStringDate(self.creationDate)
        lastUpdate = _lastUpdate;
        isDeleted = 0;
    }

    public void addLike(String userId) {
        if(!likes.containsKey(userId))
            likes.put(userId, "");
    }

    public void removeLike(String userId) {
        if(likes.containsKey(userId))
            likes.remove(userId);
    }
}
