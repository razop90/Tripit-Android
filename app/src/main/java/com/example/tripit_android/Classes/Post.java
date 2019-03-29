package com.example.tripit_android.Classes;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;

import javax.sql.CommonDataSource;

public class Post {
    @Exclude
    public String id;
    public String userID;
    public String location;
    public String description;
    @Exclude
    public String creationDateStringFormat;
    public Object creationDate;
    public Object lastUpdate;
    public String imageUrl;
    public  HashMap<String, String> likes; //contains user id's
    public int isDeleted; //0 for false, 1 for true
    public ArrayList<Comment> comments;

    public Post() {

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
