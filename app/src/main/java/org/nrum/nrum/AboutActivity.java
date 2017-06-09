package org.nrum.nrum;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;
import org.nrum.ormmodel.Page;
import org.nrum.util.MFunction;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final String  currentLangID = sharedPreferences.getString("lang_list", "1");
        Page page = Page.getItem(Constant.PAGE_ID_ABOUT);
        String bannerImage = page.banner_image;
        JSONObject objTitle = MFunction.jsonStrToObj(page.page_title);
        JSONObject objDetail = MFunction.jsonStrToObj(page.details);
        String mTitle = MFunction.getFormatedString(objTitle,"page_title", currentLangID,20);
        String mDetail = MFunction.getFormatedString(objDetail,"details", currentLangID);
        String url = Constant.UPLOAD_PATH_PAGE + "/500_";
        ImageView imageView = (ImageView)findViewById(R.id.bannerImage);
        TextView textDetail = (TextView)findViewById(R.id.detail);
        // Setting Data (image,title,detail)
        if(bannerImage.isEmpty()) {
            imageView.setVisibility(View.GONE);
        } else {

            Picasso.with(getApplicationContext()).load(url.concat(bannerImage)).centerCrop().fit()
            .into(imageView, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    Log.i("Picasso", "onSuccess: TRUE");
                }

                @Override
                public void onError() {
                    Log.i("Picasso", "onError: TRUE");
                }
            });
        }
        setTitle(mTitle);
        textDetail.setText(mDetail);
    }
}