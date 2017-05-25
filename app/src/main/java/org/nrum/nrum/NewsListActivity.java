package org.nrum.nrum;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
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
import org.nrum.util.MFunction;

import java.util.ArrayList;
import java.util.List;

public class NewsListActivity extends AppCompatActivity {
    // Log tag
    private static final String TAG = NewsListActivity.class.getSimpleName();

    // News json url
//    private static final String url = "http://api.androidhive.info/json/newss.json";
    private static final String url = Constant.NEWS_API + "/lists";
    private ProgressDialog pDialog;
    private List<News> newsList = new ArrayList<News>();
    private ListView listView;
    private CustomListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final String  currentLangID = sharedPreferences.getString("lang_list", "1");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);
        ListView listView = (ListView) findViewById(R.id.list);
        adapter = new CustomListAdapter(this, newsList);
        listView.setAdapter(adapter);
        // click event for list item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                int newsID = newsList.get(position).getNewsID();
                Intent intent = new Intent(NewsListActivity.this,NewsDetailActivity.class);
                intent.putExtra("newsID", String.valueOf(newsID));
                startActivity(intent);
            }
        });

        RequestQueue mRequestQueue = AppController.getInstance().getRequestQueue();
        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(url);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.loading));

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
            fetchNews();
        } else {
            Toast.makeText(NewsListActivity.this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
        }
        setNews(currentLangID);
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

    private void fetchNews() {
        // Creating volley request obj
        pDialog.show();
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
                                org.nrum.ormmodel.News ormNews = new org.nrum.ormmodel.News();
                                // saving to sqllite database
                                ormNews.news_id = obj.getInt("news_id");
                                ormNews.title = (obj.has("news_title"))?obj.getString("news_title"):null;
                                ormNews.details = (obj.has("details"))?obj.getString("details"):null;
                                ormNews.feature_image = obj.getString("feature_image");
                                ormNews.save();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        ActiveAndroid.setTransactionSuccessful();
                        ActiveAndroid.endTransaction();
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
    }

    private void setNews(String currentLangID) {
        List<org.nrum.ormmodel.News> ormNewsList = org.nrum.ormmodel.News.getAllNews();
        for (org.nrum.ormmodel.News item:ormNewsList) {
            News news = new News();
            JSONObject objTitle = MFunction.jsonStrToObj(item.title);
            JSONObject objDetail = MFunction.jsonStrToObj(item.details);
            String mTitle = MFunction.getFormatedString(objTitle,"title", currentLangID,100);
            String mDetail = MFunction.getFormatedString(objDetail,"detail", currentLangID,150);
            news.setNewsID(item.news_id);
            news.setTitle(mTitle);
            news.setDetail(mDetail);
            news.setFeatureImage(item.feature_image);
            newsList.add(news);
        }
        // notifying list adapter about data changes
        // so that it renders the list view with updated data
        adapter.notifyDataSetChanged();
    }
}