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
import org.nrum.model.Member;
import org.nrum.nrum.Constant;
import org.nrum.nrum.R;

import java.util.List;

/**
 * Created by rajdhami on 12/25/2016.
 */
public class CustomMemberListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Member> memberItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomMemberListAdapter(Activity activity, List<Member> memberItems) {
        this.activity = activity;
        this.memberItems = memberItems;
    }

    @Override
    public int getCount() {
        return memberItems.size();
    }

    @Override
    public Object getItem(int location) {
        return memberItems.get(location);
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
            convertView = inflater.inflate(R.layout.list_row_member, null);
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnail);
        TextView name           = (TextView) convertView.findViewById(R.id.name);
        TextView designation    = (TextView) convertView.findViewById(R.id.designation);
        TextView address        = (TextView) convertView.findViewById(R.id.address);
        TextView phone          = (TextView) convertView.findViewById(R.id.phone);
        // getting news data for the row
        Member m = memberItems.get(position);
        // name
        String mName = m.getName();
        if(mName !=null && mName.length()> 50) {
            mName = TextUtils.substring(m.getName(),0,50).concat("...");
        }
        //designation
        String mDesignation = m.getDesignation();
        if (mDesignation !=null && mDesignation.length()>100) {
            mDesignation = TextUtils.substring(m.getDesignation(),0,100).concat("...");
        }

        //address
        String mAddress = m.getAddress();
        if (mAddress !=null && mAddress.length()>100) {
            mAddress = TextUtils.substring(m.getAddress(),0,100).concat("...");
        }

        // thumbnail image
        thumbNail.setImageUrl(Constant.UPLOAD_PATH_MEMBER+ "/100_".concat(m.getProfileImage()), imageLoader);
        name.setText(m.getName());
        designation.setText(mDesignation);
        address.setText(mAddress);
        phone.setText(m.getPhone());
        return convertView;
    }
}