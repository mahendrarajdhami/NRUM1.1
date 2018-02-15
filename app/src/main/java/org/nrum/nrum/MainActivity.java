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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
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
import org.nrum.ormmodel.Banner;
import org.nrum.ormmodel.Notice;
import org.nrum.util.MFunction;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]


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

        /*For Tab Layout*/
        viewPager = (android.support.v4.view.ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        currentLangID = sharedPreferences.getString("lang_list", "1");

        /*if(MFunction.isInternetAvailable(getApplicationContext())) {
            MFunction.fetchAllData();
        } else {
            Toast.makeText(MainActivity.this,getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
        }*/
        setNoticeText(currentLangID);
        setSlider(currentLangID);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
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
                /*Log.d(msg, "nav_signin Menu is clicked");
                Intent intent = new Intent(MainActivity.this,SignInActivity.class);
                startActivity(intent);*/
                Toast.makeText(MainActivity.this,R.string.under_construction, Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(MainActivity.this, MemberListActivity.class);
        startActivity(intent);
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
        mAuth.signOut();

        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //updateUI(null);
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        //hideProgressDialog();
        if (user != null) {
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            View header = navigationView.getHeaderView(0);

            ImageView userImage = (ImageView) header.findViewById(R.id.userImage);
            TextView userName = (TextView)header.findViewById(R.id.userName);
            TextView userEmail = (TextView)header.findViewById(R.id.userEmail);
            userName.setText(user.getDisplayName());
            userEmail.setText(user.getEmail());

            //findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            //findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
        } else {
            /*mStatusTextView.setText(R.string.signed_out);
            mDetailTextView.setText(null);

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);*/
        }
    }

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        //showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // [START_EXCLUDE]
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_google]
}