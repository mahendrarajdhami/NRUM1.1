package org.nrum.nrum;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONObject;
import org.nrum.adapter.CustomMainPostListAdapter;
import org.nrum.model.Post;
import org.nrum.util.MDateConversion;
import org.nrum.util.MFunction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


public class PostFragment extends Fragment {
    private static String currentLangID = "1";
    private List<Post> mainPostItems = new ArrayList<Post>();
    private ListView mainPostListView;
    private CustomMainPostListAdapter mainPostListViewAdapter;
    public PostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(PostFragment.this.getActivity());
        this.currentLangID = sharedPreferences.getString("lang_list", "1");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        ListView mainPostListView = (ListView) view.findViewById(R.id.mainProgList);
        mainPostListViewAdapter = new CustomMainPostListAdapter(PostFragment.this.getActivity(),mainPostItems);
        mainPostListView.setAdapter(mainPostListViewAdapter);
        // click event for list item
        mainPostListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                int postID = mainPostItems.get(position).getPostID();
                Intent intent = new Intent(PostFragment.this.getActivity(),PostDetailActivity.class);
                intent.putExtra("postID", String.valueOf(postID));
                startActivity(intent);
            }
        });
        setPosts(currentLangID);
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sync: {
                setPosts(currentLangID);
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }
    private void setPosts(String currentLangID) {
        List<org.nrum.ormmodel.Post> ormPostList = org.nrum.ormmodel.Post.getPosts(Constant.LIMIT_MAIN_NEWS_FETCH);
        mainPostItems.clear();
        for (org.nrum.ormmodel.Post item:ormPostList) {
            Post post = new Post();
            JSONObject objTitle     = MFunction.jsonStrToObj(item.post_title);
            JSONObject objDetail    = MFunction.jsonStrToObj(item.details);
            JSONObject objCategory  = MFunction.jsonStrToObj(item.category_name);
            String mCategory        = MFunction.getFormatedString(objCategory,"category_name", currentLangID,50);
            String mTitle           = MFunction.getFormatedString(objTitle,"post_title", currentLangID,100);
            String mDetail          = MFunction.getFormatedString(objDetail,"detail", currentLangID,150);
            String mPublishDateFrom = item.publish_date_from;

            SimpleDateFormat fromDFormat=new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat toDFormat=new SimpleDateFormat("yyy-MM-dd");
            String finalDate= null;
            int fromYear=0;
            int fromMonth=0;
            int fromDay=0;
            long dbSeconds =0;
            double showHours = 0;
            double showMinutes =0;
            double showSeconds =0;
            double mRemainder =0;
            try {
                Date date = fromDFormat.parse(mPublishDateFrom);
                /* for getting yyyy,mm,dd*/
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(date);
                fromYear = calendar.get(Calendar.YEAR);
                fromMonth = calendar.get(Calendar.MONTH) + 1;
                fromDay = calendar.get(Calendar.DAY_OF_MONTH);
                dbSeconds = date.getTime()/1000;
                finalDate=   toDFormat.format(date);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
            long currentSeconds = new Date().getTime()/1000;
            long diffSeconds = currentSeconds - dbSeconds;

            if (diffSeconds < (long)86400) {
                showHours = (double)diffSeconds/(double) 3600;
                int intHrPart = (int) showHours;
                showMinutes = (showHours - (double)intHrPart) * (double)60;
                int intMPart = (int) showMinutes;
                showSeconds = (showMinutes - (double) intMPart) * (double)60;
                int intSPart = (int) showSeconds;

                String hr   = (intHrPart != 0)?String.valueOf(intHrPart) + "hr ":"";
                String m    = (intMPart != 0)?String.valueOf(intMPart) + "m ":"";
                String s    = (intSPart != 0)?String.valueOf(intSPart) + "s ":"";
                finalDate   = hr + m + s + getString(R.string.ago);
            } else {
                if(currentLangID.equals(Constant.CURRENT_LANG_ID_2) || currentLangID.equals(Constant.CURRENT_LANG_ID_3)){
                    finalDate = MDateConversion.getNepaliDate(fromYear,fromMonth,fromDay,currentLangID);
                }
            }
            /*Adding news to newsList*/
            post.setPostID(item.post_id);
            post.setPostTitle(mTitle);
            post.setPublishDateFrom(finalDate);
            post.setCategoryName(mCategory);
            mainPostItems.add(post);
        }
        // notifying list adapter about data changes
        // so that it renders the list view with updated data
        mainPostListViewAdapter.notifyDataSetChanged();
    }
}
