package org.nrum.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

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
}
