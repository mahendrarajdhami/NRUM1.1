package org.nrum.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.nrum.app.AppController;
import org.nrum.nrum.Constant;
import org.nrum.ormmodel.Banner;
import org.nrum.ormmodel.Notice;

/**
 * Created by rajdhami on 5/24/2017.
 */
public class MFunction {
    private static final String TAG = MFunction.class.getSimpleName();
    public static boolean isInternetAvailable(Context context)
    {
        NetworkInfo info = (NetworkInfo) ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info == null) {
            Log.d(TAG,"no internet connection");
            return false;
        }
        else {
            if(info.isConnected())
            {
                Log.d(TAG," internet connection available...");
                return true;
            }
            else
            {
                Log.d(TAG," internet connection");
                return true;
            }

        }
    }
    public static JSONObject jsonStrToObj(String jsonString){
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getFormatedString(JSONObject jsonObj, String key, String currentLangID,int trimLimit){
        String mString=null;
        String finalString =null;
        if (jsonObj != null) try {
            mString = jsonObj.getString(currentLangID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        else mString = null;
        finalString = mString !=null?Html.fromHtml(mString).toString():mString;
        if(finalString ==null) return finalString;
        if (finalString.length()>trimLimit){
            return TextUtils.substring(finalString,0,trimLimit).concat("...");
        } else {
            return finalString;
        }
    }

    public static String getFormatedString(JSONObject jsonObj, String key, String currentLangID){
        String mString=null;
        String finalString =null;
        if (jsonObj != null) try {
            mString = jsonObj.getString(currentLangID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        else mString = null;
        finalString = mString !=null?Html.fromHtml(mString).toString():mString;
        return finalString;
}

    /*Data Fetching Methods*/

    public static void fetchAllData(){
        fetchBanners();
        fetchNotice();
        fetchNews();
    }

    public static void fetchBanners(){
        // Creating volley request obj for Banner
        JsonArrayRequest bannerReq = new JsonArrayRequest(Constant.BANNER_API,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ActiveAndroid.beginTransaction();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                // saving to sqlite database
                                Banner ormBanner = new Banner();
                                ormBanner.banner_id = obj.getInt("banner_id");
                                ormBanner.title = (obj.has("title"))?obj.getString("title"):null;
                                ormBanner.description = (obj.has("description"))?obj.getString("description"):null;
                                ormBanner.pc_image = obj.getString("pc_image");
                                ormBanner.display_order = obj.getInt("display_order");
                                ormBanner.save();
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
                VolleyLog.d(TAG,"Error: " + error.getMessage());
            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(bannerReq);
    }

    public static void fetchNotice(){
        JsonArrayRequest noticeReq = new JsonArrayRequest(Constant.NOTICE_API,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ActiveAndroid.beginTransaction();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                // saving to sqlite database
                                Notice ormNotice = new Notice();
                                ormNotice.notice_id = obj.getInt("notice_id");
                                ormNotice.notice_title = obj.getString("notice_title");
                                ormNotice.detail = obj.getString("detail");
                                ormNotice.display_order = obj.getInt("display_order");
                                ormNotice.save();
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
            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(noticeReq);
    }

    public static void fetchNews() {
        // Creating volley request obj
        JsonArrayRequest newsReq = new JsonArrayRequest(Constant.NEWS_API,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
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
                                ormNews.display_order = obj.getInt("display_order");
                                ormNews.publish_date_from = obj.getString("publish_date_from");
                                ormNews.category_name = obj.getString("category_name");
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
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(newsReq);
    }


    /* Fetch News without showing circular progress bar*/
    public static void fetchNews(final SwipeRefreshLayout swipeRefreshLayout) {
        // showing refresh animation before making http call
        swipeRefreshLayout.setRefreshing(true);
        // Creating volley request obj
        JsonArrayRequest newsReq = new JsonArrayRequest(Constant.NEWS_API,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response.length()>0) {
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
                                    ormNews.display_order = obj.getInt("display_order");
                                    ormNews.publish_date_from = obj.getString("publish_date_from");
                                    ormNews.category_name = obj.getString("category_name");
                                    ormNews.save();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            ActiveAndroid.setTransactionSuccessful();
                            ActiveAndroid.endTransaction();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(newsReq);
    }
}
