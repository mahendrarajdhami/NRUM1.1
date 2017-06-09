package org.nrum.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.nrum.model.Post;
import org.nrum.nrum.R;
import java.util.List;

/**
 * Created by rajdhami on 6/1/2017.
 */
public class CustomMainPostListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Post> postItems;
    public CustomMainPostListAdapter(Activity activity, List<Post> postItems) {
        this.activity = activity;
        this.postItems = postItems;
    }

    @Override
    public int getCount() {
        return postItems.size();
    }

    @Override
    public Object getItem(int location) {
        return postItems.get(location);
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
            convertView = inflater.inflate(R.layout.list_row_main_post, null);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView categoryName = (TextView) convertView.findViewById(R.id.category);
        TextView timeBefore = (TextView) convertView.findViewById(R.id.timeBefore);
        // getting post data for the row
        Post p = postItems.get(position);

        // title
        String mTitle = p.getPostTitle();
        if(mTitle !=null && mTitle.length()> 100) {
            mTitle = TextUtils.substring(p.getPostTitle(),0,100).concat("...");
        }
        title.setText(mTitle);
        categoryName.setText(p.getCategoryName());
        timeBefore.setText(p.getPublishDateFrom());
        return convertView;
    }

}
