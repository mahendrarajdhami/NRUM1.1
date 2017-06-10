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
import org.nrum.model.Post;
import org.nrum.nrum.Constant;
import org.nrum.nrum.R;

import java.util.List;

/**
 * Created by rajdhami on 6/10/2017.
 */
public class CustomPostListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Post> postItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomPostListAdapter(Activity activity, List<Post> postItems) {
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
        Post n = postItems.get(position);
        // thumbnail image
        thumbNail.setImageUrl(Constant.UPLOAD_PATH_POST+ "/100_".concat(n.getBannerImage()), imageLoader);
        // title
        String mTitle = n.getPostTitle();
        if(mTitle !=null && mTitle.length()> 50) {
            mTitle = TextUtils.substring(n.getPostTitle(),0,50).concat("...");
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
