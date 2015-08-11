package com.codepath.apps.MySimpleTweets.models;

/*
[
    {
        "text": "just another test",
        "contributors": null,
        "id": 240558470661799936,
        "retweet_count": 0,
        "in_reply_to_status_id_str": null,
        "geo": null,
        "retweeted": false,
        "in_reply_to_user_id": null,
        "place": null,
        "source": "<a href="//realitytechnicians.com\"" rel="\"nofollow\"">OAuth Dancer Reborn</a>",
        "user": {
            "name": "OAuth Dancer",
            "profile_sidebar_fill_color": "DDEEF6",
            "profile_background_tile": true,
            "profile_sidebar_border_color": "C0DEED",
            "profile_image_url": "http://a0.twimg.com/profile_images/730275945/oauth-dancer_normal.jpg"
    }
    {
        ........
    }

]

*/

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Tweet implements Serializable {
    private String body;
    private long uid;
    private String createAt;
    private User user;

    public static Tweet fromJSON( JSONObject jsonObject) {
        Tweet tweet = new Tweet();

        try {
            tweet.setBody(jsonObject.getString("text"));
            tweet.setUid(jsonObject.getLong("id"));
            tweet.setCreateAt(jsonObject.getString("created_at"));
            tweet.setUser( User.fromJSON( jsonObject.getJSONObject("user")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return tweet;
    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<>();

        for(int i=0; i<jsonArray.length(); i++) {
            try {
                Tweet tweet = Tweet.fromJSON(jsonArray.getJSONObject(i));
                if (tweet != null) {
                    tweets.add(tweet);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }

        return tweets;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

}
