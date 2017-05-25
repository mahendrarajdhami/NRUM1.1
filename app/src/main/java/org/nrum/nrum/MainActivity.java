package org.nrum.nrum;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.nrum.app.AppController;
import org.nrum.ormmodel.Banner;
import org.nrum.ormmodel.Notice;
import org.nrum.util.MFunction;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SliderLayout mDemoSlider;
    String msg = "LogInfo : ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDemoSlider = (SliderLayout)findViewById(R.id.slider);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        setTitle(R.string.app_name);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final String  currentLangID = sharedPreferences.getString("lang_list", "1");

        if(MFunction.isInternetAvailable(getApplicationContext())) {
            this.fetchBanners();
            this.fetchNotice();
        } else {
            Toast.makeText(MainActivity.this,getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
        }
        this.setNoticeText(currentLangID);
        this.setSlider(currentLangID);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.app_name))
                    .setMessage(getString(R.string.want_to_exit))
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            finish();
                        }})
                    .setNegativeButton(getString(R.string.no), null).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        // changing color of share menu
        Drawable drawable = menu.findItem(R.id.action_sync).getIcon();
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, ContextCompat.getColor(this,R.color.colorMenu));
        menu.findItem(R.id.action_sync).setIcon(drawable);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings: {
                Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.action_sync: {
                Toast.makeText(MainActivity.this, "Loading data", Toast.LENGTH_SHORT).show();
                // refresh Activity
                /*finish();
                startActivity(getIntent());*/
                return true;
            }

            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch ( id ){
            case R.id.nav_web : {
                Intent intent = new Intent(MainActivity.this,NrumWebViewActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_news :{
                Intent intent = new Intent(MainActivity.this,NewsListActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_gallery :{
                Toast.makeText(MainActivity.this, R.string.under_construction, Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.nav_calendar :{
                Intent intent = new Intent(MainActivity.this,CalendarActivity.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this,R.string.under_construction, Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.nav_signin :{
                /*Log.d(msg, "nav_signin Menu is clicked");
                Intent intent = new Intent(MainActivity.this,SignInActivity.class);
                startActivity(intent);*/
                Toast.makeText(MainActivity.this, R.string.under_construction, Toast.LENGTH_SHORT).show();
                break;
            }

            case R.id.nav_wordbank :{
                /*Log.d(msg, "nav_signin Menu is clicked");
                Intent intent = new Intent(MainActivity.this,SignInActivity.class);
                startActivity(intent);*/
                Toast.makeText(MainActivity.this,R.string.under_construction, Toast.LENGTH_SHORT).show();
                break;
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // click events for button
    public void openNewsList(View view)
    {
        Intent intent = new Intent(MainActivity.this, NewsListActivity.class);
        startActivity(intent);
    }

    public void openEventList(View view)
    {
        Intent intent = new Intent(MainActivity.this, NewsListActivity.class);
        startActivity(intent);
    }

    public void openDocList(View view)
    {
        Intent intent = new Intent(MainActivity.this, NewsListActivity.class);
        startActivity(intent);
    }

    private void fetchNotice(){
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
                VolleyLog.d(msg, "Error: " + error.getMessage());
            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(noticeReq);
    }

    private void fetchBanners(){
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
                VolleyLog.d(msg, "Error: " + error.getMessage());
            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(bannerReq);
    }

    private void setNoticeText(final String currentLangID){
        final Button noticeButton = (Button)findViewById(R.id.noticeButton);
        Notice latestNotice = Notice.getLatestNotice();
        String mTitle = null;
        if (latestNotice != null) {
            JSONObject objTitle = MFunction.jsonStrToObj(latestNotice.notice_title);
            mTitle = MFunction.getFormatedString(objTitle,"notice_title", currentLangID,200);
            Animation mAnimation = new AlphaAnimation(1, 0);
            mAnimation.setDuration(Constant.NOTICE_BUTTON_BLINK_DURATION);
            mAnimation.setInterpolator(new LinearInterpolator());
            mAnimation.setRepeatCount(Animation.INFINITE);
            mAnimation.setRepeatMode(Animation.REVERSE);
            noticeButton.startAnimation(mAnimation);
            if(mTitle != null) {
                noticeButton.setText(mTitle);
            } else {
                noticeButton.setText(getString(R.string.no_latest_notice));
            }
        }
    }

    private void setSlider(final String currentLangID) {
        List<Banner> ormBannerList = Banner.getAllBanners();
        for (Banner item:ormBannerList) {
            JSONObject objDescription = MFunction.jsonStrToObj(item.description);
            String mDescription = MFunction.getFormatedString(objDescription,"description", currentLangID,150);
            TextSliderView textSliderView = new TextSliderView(getApplicationContext());
            textSliderView
                    .description(mDescription)
                    .image(Constant.UPLOAD_PATH_BANNER + "/600_".concat(item.pc_image))
                    .setScaleType(BaseSliderView.ScaleType.Fit);
            //add your extra information
            textSliderView.bundle(new Bundle());
            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(Constant.BANNER_TRANSITION_DURATION);
    }
}