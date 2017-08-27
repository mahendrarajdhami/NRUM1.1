package org.nrum.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.nrum.model.Faq;
import org.nrum.nrum.R;

import java.util.List;

/**
 * Created by rajdhami on 5/30/2017.
 */
public class CustomFaqListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Faq> faqItems;
    public CustomFaqListAdapter(Activity activity, List<Faq> faqItems) {
        this.activity = activity;
        this.faqItems = faqItems;
    }

    @Override
    public int getCount() {
        return faqItems.size();
    }

    @Override
    public Object getItem(int location) {
        return faqItems.get(location);
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
            convertView = inflater.inflate(R.layout.list_row_faq, null);
        TextView title = (TextView) convertView.findViewById(R.id.question);
        // getting news data for the row
        Faq f = faqItems.get(position);

        // title
        String mTitle = f.getQuestion();
        if(mTitle !=null && mTitle.length()> 100) {
            mTitle = TextUtils.substring(f.getQuestion(),0,100).concat("...");
        }
        title.setText(mTitle);
        return convertView;
    }
}