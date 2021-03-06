package org.nrum.nrum;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONObject;
import org.nrum.app.AppController;
import org.nrum.ormmodel.Banner;
import org.nrum.ormmodel.Notice;
import org.nrum.util.MFunction;
import org.nrum.util.SecurePreferences;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SliderLayout mDemoSlider;
    private static String currentLangID;
    String msg = "LogInfo : ";
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FirebaseAuth mAuth;
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;

    private NavigationView navigationView;

    private TextView userName;
    private TextView userEmail;
    private Button signInBtn;
    private Button signOutBtn;

    public NetworkImageView nIV;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);

        userName  = (TextView)header.findViewById(R.id.userName);
        userEmail = (TextView)header.findViewById(R.id.userEmail);

        signInBtn  = (Button) findViewById(R.id.nav_signin);
        signOutBtn = (Button) findViewById(R.id.nav_signout);
        nIV = (NetworkImageView) header.findViewById(R.id.userImage);

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //.requestIdToken("891142423918-i8e1aprht300oj9buij4ll5sp3v4spo8.apps.googleusercontent.com")
                .requestIdToken("891142423918-hap9p44pv5ttk575m4ktbk6gs6lcech6.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();

        mDemoSlider = (SliderLayout)findViewById(R.id.slider);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        setTitle(R.string.app_name);


        /*For Tab Layout*/
        viewPager = (android.support.v4.view.ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        currentLangID = sharedPreferences.getString("lang_list", "1");
        setNoticeText(currentLangID);
        setSlider(currentLangID);
        manageUI(isLogin());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if(account != null){

                    nIV.setImageUrl(account.getPhotoUrl().toString(),AppController.getInstance().getImageLoader());
                    nIV.setDefaultImageResId(R.drawable.nrum_logo);
                    nIV.setErrorImageResId(R.drawable.nrum_logo);
                    MFunction.putMyPrefVal("userImage",account.getPhotoUrl().toString(),getApplicationContext());
                    MFunction.putMyPrefVal("userName",account.getDisplayName(),getApplicationContext());
                    MFunction.putMyPrefVal("userEmail",account.getEmail(),getApplicationContext());

                    userName.setText(MFunction.getMyPrefVal("userName",getApplicationContext()));
                    userEmail.setText(MFunction.getMyPrefVal("userEmail",getApplicationContext()));
                    navigationView.getMenu().findItem(R.id.nav_signin).setVisible(false);
                    navigationView.getMenu().findItem(R.id.nav_member).setVisible(true);
                    navigationView.getMenu().findItem(R.id.nav_signout).setVisible(true);

                }
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
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
                Toast.makeText(MainActivity.this, getText(R.string.loading), Toast.LENGTH_LONG).show();
                MFunction.fetchAllData();
                setSlider(currentLangID);
                setNoticeText(currentLangID);
                return false;
            }
            case R.id.action_contact: {
                Intent mIntent = new Intent(Intent.ACTION_SENDTO);
                mIntent.setData(Uri.parse("mailto:"));
                mIntent.putExtra(Intent.EXTRA_EMAIL  , new String[] {Constant.NRUM_EMAIL});
                mIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                startActivity(Intent.createChooser(mIntent, getString(R.string.send_email_via)));
                return true;
            }
            case R.id.action_rate_app: {
                Context context = getApplicationContext();
                String appID = context.getPackageName();
                Uri uri = Uri.parse("market://details?id=" + appID);
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appID)));
                }
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
            case R.id.nav_post :{
                Intent intent = new Intent(MainActivity.this,PostListActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_faq :{
                Intent intent = new Intent(MainActivity.this,FaqListActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_member :{
                Intent intent = new Intent(MainActivity.this,MemberListActivity.class);
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
                Toast.makeText(MainActivity.this,"Multilang is Remaining", Toast.LENGTH_SHORT).show();
                break;
            }

            case R.id.nav_wordbank :{
                break;
            }
            case R.id.nav_settings :{
                Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_signin :{
                signIn();
                break;
            }
            case R.id.nav_signout :{
                signOut();
                break;
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // click events for button
    public void openAbout(View view)
    {
        Intent intent = new Intent(MainActivity.this, AboutActivity.class);
        startActivity(intent);
    }

    public void openFaqList(View view)
    {
        Intent intent = new Intent(MainActivity.this, FaqListActivity.class);
        startActivity(intent);
    }

    public void openMemberList(View view)
    {
        if(isLogin()) {
            Intent intent = new Intent(MainActivity.this, MemberListActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Please Sign In First!", Toast.LENGTH_SHORT).show();
        }
    }

    public void showNotification(View view){
        Button b = (Button)view;
        String notice = b.getText().toString();
        notice = b.getTag().toString();
        final Toast noticeToast = Toast.makeText(MainActivity.this,notice, Toast.LENGTH_LONG);
        noticeToast.show();
        new CountDownTimer(5, 1000)
        {
            public void onTick(long millisUntilFinished) {noticeToast.show();}
            public void onFinish() {noticeToast.show();}

        }.start();
    }

    private void setNoticeText(final String currentLangID){
        final Button noticeButton = (Button)findViewById(R.id.noticeButton);
        Notice latestNotice = Notice.getLatestNotice();
        String mTitle = null;
        String mDetail = null;
        if (latestNotice != null) {
            JSONObject objTitle = MFunction.jsonStrToObj(latestNotice.notice_title);
            JSONObject objDetail = MFunction.jsonStrToObj(latestNotice.detail);
            mTitle = MFunction.getFormatedString(objTitle,"notice_title", currentLangID,80);
            mDetail = MFunction.getFormatedString(objDetail,"detail", currentLangID);

            Animation mAnimation = new AlphaAnimation(1, 0.5f);
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
            if(mDetail != null) {
                noticeButton.setTag(mDetail);
            } else {
                noticeButton.setTag(getString(R.string.no_latest_notice));
            }
        }
    }

    private void setSlider(final String currentLangID) {
        List<Banner> ormBannerList = Banner.getAllBanners();
        mDemoSlider.removeAllSliders();
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

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new NewsFragment(), getString(R.string.tab_latest_news));
        adapter.addFragment(new PostFragment(), getString(R.string.tab_latest_programs));
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        SecurePreferences preferences = new SecurePreferences(getBaseContext(), Constant.PREFERENCE_NAME, Constant.PREFERENCE_ENCRYPT_KEY, true);
        preferences.clear();
        finish();
        startActivity(getIntent());
    }

    private  boolean isLogin(){
        boolean isLogin = false;
        String userEmail = MFunction.getMyPrefVal("userEmail",getApplicationContext());

        if(TextUtils.isEmpty(userEmail)) {
            isLogin = false;
        } else {
            isLogin = true;
        }
        return isLogin;
    }

    private void manageUI(boolean isLogin){
        nIV.setDefaultImageResId(R.drawable.nrum_logo);
        nIV.setErrorImageResId(R.drawable.nrum_logo);
        if(isLogin) {
            nIV.setImageUrl(MFunction.getMyPrefVal("userImage",getApplicationContext()),AppController.getInstance().getImageLoader());
            userName.setText(MFunction.getMyPrefVal("userName",getApplicationContext()));
            userEmail.setText(MFunction.getMyPrefVal("userEmail",getApplicationContext()));

            navigationView.getMenu().findItem(R.id.nav_signin).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_member).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_signout).setVisible(true);
        } else {
            navigationView.getMenu().findItem(R.id.nav_signin).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_member).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_signout).setVisible(false);
        }
    }
}