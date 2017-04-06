package org.nrum.nrum;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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

import java.util.HashMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SliderLayout mDemoSlider;
    String msg = "LogInfo : ";

    // Banners json url
    private static final String bannerUrl = "http://192.168.0.100/bs.dev/nrum/dataProvider/bannerApi/lists";

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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // HashMap for Banner image and description
        final HashMap<String,String> url_maps = new HashMap<String, String>();

        // Creating volley request obj for Banner
        JsonArrayRequest bannerReq = new JsonArrayRequest(bannerUrl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(msg, response.toString());
                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                String url = "http://192.168.0.100/bs.dev/nrum/uploads/company_1/banner/";
                                String imageName = obj.getString("pc_image");
                                String imageUrl = url.concat(imageName);
                                String description = obj.getString("description");
                                TextSliderView textSliderView = new TextSliderView(getApplicationContext());
                                // initialize a SliderLayout
                                textSliderView
                                        .description(description)
                                        .image(imageUrl)
                                        .setScaleType(BaseSliderView.ScaleType.Fit);
                                //add your extra information
                                textSliderView.bundle(new Bundle());
                                mDemoSlider.addSlider(textSliderView);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(msg, "Error: " + error.getMessage());
            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(bannerReq);

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("NRUM")
                    .setMessage("Do you really want to Exit?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
//                          Toast.makeText(MainActivity.this, "Yes", Toast.LENGTH_SHORT).show();
                            finish();
                        }})
                    .setNegativeButton(android.R.string.no, null).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch ( id ){
            case R.id.nav_web : {

                Log.d(msg, "WebView Menu is clicked");
                Intent intent = new Intent(MainActivity.this,NrumWebViewActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_news :{

                Log.d(msg, "nav_news Menu is clicked");
                Intent intent = new Intent(MainActivity.this,NewsListActivity.class);
                startActivity(intent);

                break;
            }
            case R.id.nav_gallery :{

                Log.d(msg, "nav_gallery Menu is clicked");

                break;
            }
            case R.id.nav_calendar :{
                /*Intent intent = new Intent(MainActivity.this,CalendarActivity.class);
                startActivity(intent);*/

                // showing message for now
                Toast toast = Toast.makeText(MainActivity.this,R.string.under_construction, Toast.LENGTH_LONG);
                toast.show();
                break;
            }
            case R.id.nav_signin :{
                /*Log.d(msg, "nav_signin Menu is clicked");
                Intent intent = new Intent(MainActivity.this,SignInActivity.class);
                startActivity(intent);*/

                // showing message for now
                Toast toast = Toast.makeText(MainActivity.this,R.string.under_construction, Toast.LENGTH_LONG);
                toast.show();
                break;
            }

            case R.id.nav_wordbank :{
                /*Log.d(msg, "nav_signin Menu is clicked");
                Intent intent = new Intent(MainActivity.this,SignInActivity.class);
                startActivity(intent);*/

                // showing message for now
                Toast toast = Toast.makeText(MainActivity.this,R.string.under_construction, Toast.LENGTH_LONG);
                toast.show();
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
}
