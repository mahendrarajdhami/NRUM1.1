package org.nrum.nrum;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.util.Log;
import com.android.volley.Cache;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.nrum.adapter.CustomListAdapter;
import org.nrum.app.AppController;
import org.nrum.model.News;

import java.util.ArrayList;
import java.util.List;

public class NewsListActivity extends AppCompatActivity {
    // Log tag
    private static final String TAG = NewsListActivity.class.getSimpleName();

    // News json url
//    private static final String url = "http://api.androidhive.info/json/newss.json";
    private static final String url = "http://192.168.100.2/bs.dev/nrum/dataProvider/newsApi/lists";
    private ProgressDialog pDialog;
    private List<News> newsList = new ArrayList<News>();
    private ListView listView;
    private CustomListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);
        ListView listView = (ListView) findViewById(R.id.list);
        adapter = new CustomListAdapter(this, newsList);
        listView.setAdapter(adapter);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final String  currentLangID = sharedPreferences.getString("lang_list", "default_value") ;
        // click event for list item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("onItemClick", "position" + position);
                Intent intent = new Intent(NewsListActivity.this,NewsDetailActivity.class);
                Toast.makeText(getApplicationContext(), String.valueOf(position),Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });


        RequestQueue mRequestQueue = AppController.getInstance().getRequestQueue();

        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(url);

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        // action for refreshWeb
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.refreshWeb);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // refresh Activity
                finish();
                startActivity(getIntent());
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(CheckNetwork.isInternetAvailable(getApplicationContext())) {
            // Creating volley request obj
            JsonArrayRequest newsReq = new JsonArrayRequest(url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            hidePDialog();
                            // Parsing json
                            ActiveAndroid.beginTransaction();
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject obj = response.getJSONObject(i);
                                    News news = new News();
                                    org.nrum.ormmodel.News ormNews = new org.nrum.ormmodel.News();
                                    int  newsID = obj.getInt("news_id");
                                    JSONObject  objTitle = new JSONObject(obj.getString("news_title"));
                                    JSONObject  objDetail = new JSONObject(obj.getString("details"));
                                    String  featureImage = obj.getString("feature_image");

                                    String title = Html.fromHtml(objTitle.getString(currentLangID)).toString();
                                    if (title.length()> 50) {
                                        title = TextUtils.substring(title,0,50).concat("...");
                                    }
                                    String detail = Html.fromHtml(objDetail.getString(currentLangID)).toString();
                                    if(detail.length()> 100) {
                                        detail = TextUtils.substring(detail,0,100).concat("...");
                                    }
                                    news.setFeatureImage(featureImage);
                                    news.setTitle(title);
                                    news.setDetail(detail);
                                    newsList.add(news);

                                    // saving to sqllite database
                                    ormNews.news_id = newsID;
                                    ormNews.title = title;
                                    ormNews.details = detail;
                                    ormNews.feature_image = featureImage;
                                    ormNews.save();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            ActiveAndroid.setTransactionSuccessful();
                            ActiveAndroid.endTransaction();

                            // notifying list adapter about data changes
                            // so that it renders the list view with updated data
                            adapter.notifyDataSetChanged();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    hidePDialog();
                }
            });

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(newsReq);
        } else {
            //no connection
            Toast toast = Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG);
            toast.show();
            List<org.nrum.ormmodel.News> ormNewsList = org.nrum.ormmodel.News.getAllNews();
            for (org.nrum.ormmodel.News item:ormNewsList) {
                News news = new News();
                news.setTitle(item.title);
                news.setDetail(item.details);
                news.setFeatureImage(item.feature_image);
                newsList.add(news);
            }
            // notifying list adapter about data changes
            // so that it renders the list view with updated data
            adapter.notifyDataSetChanged();
            hidePDialog();
        }
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
}