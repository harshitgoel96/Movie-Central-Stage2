package com.example.harshit.twopane_blank.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.harshit.twopane_blank.R;
import com.example.harshit.twopane_blank.model.ReviewItem;
import com.example.harshit.twopane_blank.model.ReviewModel;

import java.util.List;

/**
 * Created by harsh on 12/25/2015.
 */
public class ReviewListAdapter extends BaseAdapter {
    Context mContext;
    ReviewModel reviewsModel;
    List<ReviewItem> reviewItems;

    @Override
    public int getCount() {
        return reviewItems.size();
    }

    @Override
    public Object getItem(int position) {
        return reviewItems.get(position);

    }

    public ReviewListAdapter(Context c, ReviewModel data) {
        reviewsModel = data;
        mContext = c;
        reviewItems = data.getResults();
    }

    @Override
    public long getItemId(int position) {
        if (null != reviewItems.get(position).getId()) {
            return Long.getLong(reviewItems.get(position).getId());
        }
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) mContext).getLayoutInflater().inflate(R.layout.review_item, null);
        }
        final ReviewItem item = reviewItems.get(position);
        TextView tv = (TextView) convertView.findViewById(R.id.trailerText);
        tv.setText("Review by: " + String.valueOf(item.getAuthor()));
        convertView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(item.getUrl()));
                        mContext.startActivity(i);
                    }
                }
        );
        return convertView;
    }
}
