package com.walmartlabs.ssahu1.nytimesapp;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ssahu6 on 5/26/16.
 */
public class ArticleArrayAdapter extends ArrayAdapter<Article>{

    public ArticleArrayAdapter(Context context, List<Article> articles){
        super(context, android.R.layout.simple_list_item_1, articles);
    }

    @Override
    public void clear() {
        Log.d("DEBUG", "array cleared");
        super.clear();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // get the data item for position
        Article article = this.getItem(position);

        // check if the existing view is reused
        // not using recycle view, then go ahead and inflate the layout
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_article_result, parent, false);
        }
        // find the image view
        ImageView imageView = (ImageView)convertView.findViewById(R.id.ivImage);

        // clear out recycled image from convertView from last time
        imageView.setImageResource(0);

        TextView tvTitle = (TextView)convertView.findViewById(R.id.tvTitle);
        tvTitle.setText(article.getHeadline());

        // populate the thumbnail
        // remote download the image in the background

        String thumbnail = article.getThumbNail();
        if(!TextUtils.isEmpty(thumbnail)){
            Picasso.with(getContext()).load(thumbnail).into(imageView);
        }

        return convertView;
    }
}
