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

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import java.util.HashMap;

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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // for daimajiya image slider (https://github.com/daimajia/AndroidImageSlider)
        HashMap<String,String> url_maps = new HashMap<String, String>();
        url_maps.put("Hannibal", "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
        url_maps.put("Big Bang Theory", "http://tvfiles.alphacoders.com/100/hdclearart-10.png");

        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("Morang Adhibeshan 2071",R.drawable.slider1);
        file_maps.put("Jitya Program 2071",R.drawable.slider2);

        for(String name : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);
            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            mDemoSlider.addSlider(textSliderView);
        }
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
            case R.id.nav_events :{

                Log.d(msg, "nav_events Menu is clicked");

                break;
            }
            case R.id.nav_signin :{
                Log.d(msg, "nav_signin Menu is clicked");
                /*Intent intent = new Intent(MainActivity.this,SignInActivity.class);
                startActivity(intent);*/

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
