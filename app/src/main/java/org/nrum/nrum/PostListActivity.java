package org.nrum.nrum;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.RequestQueue;

import org.json.JSONObject;
import org.nrum.adapter.CustomPostListAdapter;
import org.nrum.app.AppController;
import org.nrum.model.Post;
import org.nrum.util.MDateConversion;
import org.nrum.util.MFunction;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class PostListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static String currentLangID = "en";
    private static DecimalFormat df2 = new DecimalFormat("0.00");
    private ProgressDialog pDialog;
    private List<Post> postList = new ArrayList<Post>();
    private ListView listView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CustomPostListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        currentLangID = sharedPreferences.getString("lang_list", "1");
        ListView listView = (ListView) findViewById(R.id.list);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        adapter = new CustomPostListAdapter(this, postList);
        listView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(this);

        // click event for list item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                int postID = postList.get(position).getPostID();
                Intent intent = new Intent(PostListActivity.this,PostDetailActivity.class);
                intent.putExtra("postID", String.valueOf(postID));
                startActivity(intent);
            }
        });

        RequestQueue mRequestQueue = AppController.getInstance().getRequestQueue();
        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(Constant.POST_API);

        pDialog = new ProgressDialog(this);
        pDialog.setTitle(getString(R.string.app_name));
        pDialog.setMessage(getString(R.string.loading));
        if(MFunction.isInternetAvailable(getApplicationContext())) {
            MFunction.fetchPosts(swipeRefreshLayout);
        } else {
            Toast.makeText(PostListActivity.this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
        }
        setPosts(currentLangID);
    }

    /**
     * This method is called when swipe refresh is pulled down
     */
    @Override
    public void onRefresh() {
        MFunction.fetchPosts(swipeRefreshLayout);
        setPosts(currentLangID);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    private void setPosts(String currentLangID) {
        List<org.nrum.ormmodel.Post> ormPostList = org.nrum.ormmodel.Post.getAllPosts();
        postList.clear();
        for (org.nrum.ormmodel.Post item:ormPostList) {
            Post post = new Post();
            JSONObject objTitle     = MFunction.jsonStrToObj(item.post_title);
            JSONObject objDetail    = MFunction.jsonStrToObj(item.details);
            JSONObject objCategory  = MFunction.jsonStrToObj(item.category_name);
            String mCategory        = MFunction.getFormatedString(objCategory,"category_name", currentLangID,50);
            String mTitle           = MFunction.getFormatedString(objTitle,"title", currentLangID,100);
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
            /*Adding post to postList*/
            post.setPostID(item.post_id);
            post.setPostTitle(mTitle);
            post.setDetail(mDetail);
            post.setFeatureImage(item.banner_image);
            post.setPublishDateFrom(finalDate);
            post.setCategoryName(mCategory);
            postList.add(post);
        }
        // notifying list adapter about data changes
        // so that it renders the list view with updated data
        adapter.notifyDataSetChanged();
    }
}
