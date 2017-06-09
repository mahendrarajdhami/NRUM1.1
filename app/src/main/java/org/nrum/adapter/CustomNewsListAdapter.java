package org.nrum.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.nrum.app.AppController;
import org.nrum.model.News;
import org.nrum.nrum.Constant;
import org.nrum.nrum.R;

import java.util.List;

/**
 * Created by rajdhami on 12/25/2016.
 */
public class CustomNewsListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<News> newsItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomNewsListAdapter(Activity activity, List<News> newsItems) {
        this.activity = activity;
        this.newsItems = newsItems;
    }

    @Override
    public int getCount() {
        return newsItems.size();
    }

    @Override
    public Object getItem(int location) {
        return newsItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row_news, null);
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnail);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView detail = (TextView) convertView.findViewById(R.id.detail);
        TextView categoryName = (TextView) convertView.findViewById(R.id.category);
        TextView timeBefore = (TextView) convertView.findViewById(R.id.timeBefore);
        // getting news data for the row
        News n = newsItems.get(position);
        // thumbnail image
        thumbNail.setImageUrl(Constant.UPLOAD_PATH_NEWS+ "/100_".concat(n.getFeatureImage()), imageLoader);
        // title
        String mTitle = n.getTitle();
        if(mTitle !=null && mTitle.length()> 50) {
            mTitle = TextUtils.substring(n.getTitle(),0,50).concat("...");
        }
        title.setText(mTitle);
        //detail
        String mDetail = n.getDetail();
        if (mDetail !=null && mDetail.length()>100) {
            mDetail = TextUtils.substring(n.getDetail(),0,100).concat("...");
        }
        detail.setText(mDetail);
        categoryName.setText(n.getCategoryName());
        timeBefore.setText(n.getPublishDateFrom());
        return convertView;
    }
}