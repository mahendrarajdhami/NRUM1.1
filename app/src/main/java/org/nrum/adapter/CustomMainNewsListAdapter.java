package org.nrum.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.nrum.model.News;
import org.nrum.nrum.R;

import java.util.List;

/**
 * Created by rajdhami on 5/30/2017.
 */
public class CustomMainNewsListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<News> newsItems;
    public CustomMainNewsListAdapter(Activity activity, List<News> newsItems) {
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
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row_main_news, null);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView categoryName = (TextView) convertView.findViewById(R.id.category);
        TextView timeBefore = (TextView) convertView.findViewById(R.id.timeBefore);
        // getting news data for the row
        News n = newsItems.get(position);

        // title
        String mTitle = n.getTitle();
        if(mTitle !=null && mTitle.length()> 100) {
            mTitle = TextUtils.substring(n.getTitle(),0,100).concat("...");
        }
        title.setText(mTitle);
        categoryName.setText(n.getCategoryName());
        timeBefore.setText(n.getPublishDateFrom());
        return convertView;
    }
}