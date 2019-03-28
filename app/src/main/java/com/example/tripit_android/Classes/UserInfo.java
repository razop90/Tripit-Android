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
    public Long lastUpdate;

    public UserInfo() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public UserInfo(String uid, String displayName, String email, String profileImageUrl, Long lastUpdate) {
        this.uid = uid;
        this.displayName = displayName;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.lastUpdate = lastUpdate;

       // if (this.timestamp == 0d)
          //  this.timestamp = Long.parseLong(ServerValue.TIMESTAMP.get("timestamp"));
    }

    public UserInfo(String uid, Map<String, Object> map) {
        this.uid = uid;

        if (map.containsKey("displayName"))
            displayName = (String) map.get("displayName");
        if (map.containsKey("email"))
            email = (String) map.get("email");
        if (map.containsKey("profileImageUrl"))
            profileImageUrl = (String) map.get("profileImageUrl");
       // if (map.containsKey("lastUpdate"))
           // timestamp = (Long) map.get("timestamp");
       // else
        lastUpdate = 0l;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("displayName", displayName);
        result.put("email", email);
        result.put("profileImageUrl", profileImageUrl);
        result.put("timestamp", ServerValue.TIMESTAMP);

        return result;
    }
}
