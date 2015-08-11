package com.codepath.apps.MySimpleTweets.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by g7190305 on 2015/8/8.
 */
public class User implements Serializable{
    private String name;
    private long uid;
    private String screenName;
    private String profileImageUrl;

    public static User fromJSON( JSONObject jsonObject ) {
        User user = new User();

        try {
            user.setName(jsonObject.getString("name"));
            user.setUid(jsonObject.getLong("id_str"));
            user.setScreenName(jsonObject.getString("screen_name"));
            user.setProfileImageUrl(jsonObject.getString("profile_image_url"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
