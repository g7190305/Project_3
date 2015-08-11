package com.codepath.apps.MySimpleTweets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.codepath.apps.MySimpleTweets.models.EndlessScrollListener;
import com.codepath.apps.MySimpleTweets.models.Tweet;

import org.json.JSONArray;

import java.util.ArrayList;

public class TimelineActivity extends ActionBarActivity {

    private TwitterClient client;
    private ListView lvTweets;
    private ArrayList<Tweet> tweets;
    private TweetsArrayAdapter aTweets;
    private SwipeRefreshLayout swipeContainer;
    private final int TWEET_ACTIVATE_ID=1;
    public static final String POSTED_STATUS="g7190305_project_3_POSTED_STATUS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        lvTweets = (ListView) findViewById(R.id.lvTweets);
        tweets = new ArrayList<>();
        aTweets = new TweetsArrayAdapter(this, tweets);
        lvTweets.setAdapter(aTweets);

        // display logo
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                populateTimeline(1);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                populateTimeline(aTweets.getSince_id());
            }
        });

        client = TwitterApplication.getRestClient();
        populateTimeline(aTweets.getSince_id());
    }

    private void populateTimeline(long since_id) {
        // client.getHomeTimeline();

        client.getHomeTimeline(since_id, new com.loopj.android.http.JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, org.json.JSONArray jsonArray) {
                // super.onSuccess(statusCode, headers, jsonArray);
                Log.d("DEBUG","Twitter Client get Success");
                aTweets.addAll(Tweet.fromJSONArray(jsonArray));
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, Throwable throwable, JSONArray Response) {
                //super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d("DEBUG", Response.toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
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
            Intent i = new Intent(TimelineActivity.this, TweetComposeActivity.class);
            i.putExtra(POSTED_STATUS, "");
            startActivityForResult(i, TWEET_ACTIVATE_ID);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        if ( resultCode == RESULT_OK && requestCode == TWEET_ACTIVATE_ID ) {
            Tweet tweet = (Tweet) data.getSerializableExtra(POSTED_STATUS);
            // aTweets.add(tweet);
            aTweets.insert(tweet, 0);

            // Log.d("DEBUG", data.toString());
            // setupInfo = (SetupInfo) data.getSerializableExtra("setupInfo");
            // Log.i("DEBUG", setupInfo.toString());
        }
    }
}
