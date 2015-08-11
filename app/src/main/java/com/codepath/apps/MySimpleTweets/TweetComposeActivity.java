package com.codepath.apps.MySimpleTweets;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.MySimpleTweets.models.Tweet;
import com.codepath.apps.MySimpleTweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class TweetComposeActivity extends ActionBarActivity {
    private User user;
    private TextView tvScreenName;
    private ImageView ivProfileImage;
    private TextView etMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_compose);

        tvScreenName = (TextView) findViewById(R.id.tvScreenName);
        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        etMessage = (TextView) findViewById(R.id.etMessage);
        user = new User();

        getProfile(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tweet_compose, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getProfile(final Context context) {
        TwitterClient client = TwitterApplication.getRestClient();

        client.getVerifyCredentials(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, org.json.JSONObject jsonObject) {
                // super.onSuccess(statusCode, headers, jsonArray);
                // Log.d("DEBUG", "Twitter VerifyCredentials get Success");
                // Log.d("DEBUG", jsonObject.toString());
                try {
                    // User.fromJSON(jsonObject);
                    // return (JSONObject) jsonObject;
                    user.setUid(jsonObject.getLong("id"));
                    user.setProfileImageUrl(jsonObject.getString("profile_image_url_https"));
                    user.setScreenName(jsonObject.getString("screen_name"));
                    user.setName(jsonObject.getString("name"));

                    tvScreenName.setText(user.getScreenName());
                    ivProfileImage.setImageResource(android.R.color.transparent); // clear out the old image for a recycled view

                    Picasso.with(context).load(user.getProfileImageUrl()).resize(50, 50).into(ivProfileImage);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // aTweets.addAll(Tweet.fromJSONArray(jsonArray));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("DEBUG", responseString);
            }
        });

    }

    public void updateStatuses(String status) throws UnsupportedEncodingException {
        TwitterClient client = TwitterApplication.getRestClient();
        client.updateStatuses(status, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, org.json.JSONObject jsonObject) {
                // super.onSuccess(statusCode, headers, jsonArray);
                Intent data = new Intent();

                Tweet tweet = Tweet.fromJSON(jsonObject);
                // data.putExtra(TimelineActivity.POSTED_STATUS,tweet);
                // Log.d("DEBUG", tweet.toString());

                if ( tweet != null) {
                    data.putExtra(TimelineActivity.POSTED_STATUS, tweet);
                }

                setResult(RESULT_OK, data);
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                // super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d("DEBUG", errorResponse.toString());
            }
        });
    }

    public void onTweetSend(View v) throws UnsupportedEncodingException {

        updateStatuses(etMessage.getText().toString());
        // Intent data = new Intent();

    }

    public void onTweetCancel(View v) {
        Intent data = new Intent();
        setResult(RESULT_CANCELED, data);
        finish();
    }
}
