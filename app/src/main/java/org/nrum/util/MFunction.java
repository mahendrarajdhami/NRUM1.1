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
        if(jsonString != null){
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                return jsonObject;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
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
        fetchPages();
        fetchPosts();
        fetchFaq();
    }

    public static void fetchBanners(){
        // Creating volley request obj for Banner

        JsonArrayRequest bannerReq = new JsonArrayRequest(Constant.BANNER_API,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("inside banner response","Banners");
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

    public static void fetchPages() {
        // Creating volley request obj
        JsonArrayRequest pageReq = new JsonArrayRequest(Constant.PAGE_API,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Parsing json
                        ActiveAndroid.beginTransaction();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                org.nrum.ormmodel.Page ormPage = new org.nrum.ormmodel.Page();
                                // saving to sqllite database
                                ormPage.page_id = obj.getInt("page_id");
                                ormPage.page_title = (obj.has("page_title"))?obj.getString("page_title"):null;
                                ormPage.details = (obj.has("details"))?obj.getString("details"):null;
                                ormPage.banner_image = obj.getString("banner_image");
                                ormPage.publish_date= obj.getString("publish_date");
                                ormPage.display_order = obj.getInt("display_order");
                                ormPage.save();
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
        AppController.getInstance().addToRequestQueue(pageReq);
    }

    public static void fetchPosts() {
        // Creating volley request obj
        JsonArrayRequest postReq = new JsonArrayRequest(Constant.POST_API,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Parsing json
                        ActiveAndroid.beginTransaction();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                org.nrum.ormmodel.Post ormPost = new org.nrum.ormmodel.Post();
                                // saving to sqllite database
                                ormPost.post_id = obj.getInt("post_id");
                                ormPost.post_title = (obj.has("post_title"))?obj.getString("post_title"):null;
                                ormPost.details = (obj.has("details"))?obj.getString("details"):null;
                                ormPost.banner_image = obj.getString("banner_image");
                                ormPost.publish_date_from= obj.getString("publish_date");
                                ormPost.display_order = obj.getInt("display_order");
                                ormPost.category_name = obj.getString("category_name");
                                ormPost.save();
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
        AppController.getInstance().addToRequestQueue(postReq);
    }

    public static void fetchFaq() {
        // Creating volley request obj
        JsonArrayRequest faqReq = new JsonArrayRequest(Constant.FAQ_API,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Parsing json
                        ActiveAndroid.beginTransaction();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                org.nrum.ormmodel.Faq ormFaq = new org.nrum.ormmodel.Faq();
                                // saving to sqllite database
                                ormFaq.faq_id = obj.getInt("faq_id");
                                ormFaq.question = (obj.has("question"))?obj.getString("question"):null;
                                ormFaq.answer = (obj.has("answer"))?obj.getString("answer"):null;
                                ormFaq.display_order = obj.getInt("display_order");
                                ormFaq.save();
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
        AppController.getInstance().addToRequestQueue(faqReq);
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

    /* Fetch Posts without showing circular progress bar*/
    public static void fetchPosts(final SwipeRefreshLayout swipeRefreshLayout) {
        // showing refresh animation before making http call
        swipeRefreshLayout.setRefreshing(true);
        // Creating volley request obj
        JsonArrayRequest postReq = new JsonArrayRequest(Constant.POST_API,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response.length()>0) {
                            // Parsing json
                            ActiveAndroid.beginTransaction();
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject obj = response.getJSONObject(i);
                                    org.nrum.ormmodel.Post ormPost = new org.nrum.ormmodel.Post();
                                    // saving to sqllite database
                                    ormPost.post_id = obj.getInt("news_id");
                                    ormPost.post_title = (obj.has("news_title"))?obj.getString("news_title"):null;
                                    ormPost.details = (obj.has("details"))?obj.getString("details"):null;
                                    ormPost.banner_image = obj.getString("feature_image");
                                    ormPost.display_order = obj.getInt("display_order");
                                    ormPost.publish_date_from = obj.getString("publish_date_from");
                                    ormPost.category_name = obj.getString("category_name");
                                    ormPost.save();
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
        AppController.getInstance().addToRequestQueue(postReq);
    }
}
