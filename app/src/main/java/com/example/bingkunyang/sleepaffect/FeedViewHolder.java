package com.example.bingkunyang.sleepaffect;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.bingkunyang.sleepaffect.model.Record;

/**
 * Created by bingkunyang on 5/11/18.
 */

public class FeedViewHolder extends RecyclerView.ViewHolder{


    public TextView emailView;
    public TextView contentView;

    public FeedViewHolder(View itemView) {
        super(itemView);

        emailView = itemView.findViewById(R.id.feed_email);
        contentView = itemView.findViewById(R.id.feed_content);
    }

    public void bindToFeed(Record feed) {
        emailView.setText(feed.email);
        contentView.setText(feed.time);
    }

}
