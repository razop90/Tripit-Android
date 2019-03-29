package com.example.tripit_android.Classes;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class UserInfo {
    @Exclude
    public String uid;
    public String displayName;
    public String email;
    public String profileImageUrl;
    public Object lastUpdate;

    public UserInfo() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public UserInfo(String uid, String displayName, String email, String profileImageUrl, Object lastUpdate) {
        this.uid = uid;
        this.displayName = displayName;
        this.email = email;
        this.profileImageUrl = profileImageUrl;

        if (lastUpdate == null)
            this.lastUpdate = ServerValue.TIMESTAMP;
        else
            this.lastUpdate = lastUpdate;
    }

    @Exclude
    public long getLastUpdate() {
        return (long) lastUpdate;
    }
}
