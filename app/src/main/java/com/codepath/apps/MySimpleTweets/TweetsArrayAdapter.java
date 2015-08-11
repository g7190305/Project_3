package com.codepath.apps.MySimpleTweets;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.MySimpleTweets.models.Tweet;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by g7190305 on 2015/8/8.
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {

    private long since_id = 1;

    public long getSince_id() {
        return since_id;
    }

    public void setSince_id(long since_id) {
        this.since_id = since_id;
    }

    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, 0, tweets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // return super.getView(position, convertView, parent);
        // 1. Get the tweet
        Tweet tweet = getItem(position);
        // 2. Find or inflate the template
        if ( convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
        }
        // 3. Find the subview to fill with data in the template
        ImageView ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
        TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserNmae);
        TextView tvBody = (TextView) convertView.findViewById(R.id.tvBody);
        TextView tvCreateAt = (TextView) convertView.findViewById(R.id.tvCreateAt);

        // 4. Populate data into the subview
        tvUserName.setText(tweet.getUser().getScreenName());
        tvBody.setText(tweet.getBody());
        tvCreateAt.setText(getRelativeTimeAgo(tweet.getCreateAt()));

        ivProfileImage.setImageResource(android.R.color.transparent); // clear out the old image for a recycled view

        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).resize(50,50).into(ivProfileImage);

        setSince_id(tweet.getUid());

        return convertView;

    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}
