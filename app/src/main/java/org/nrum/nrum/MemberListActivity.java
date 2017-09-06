package org.nrum.nrum;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.RequestQueue;

import org.json.JSONObject;
import org.nrum.adapter.CustomMemberListAdapter;
import org.nrum.app.AppController;
import org.nrum.util.MFunction;

import java.util.ArrayList;
import java.util.List;

public class MemberListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    private static String currentLangID = "en";
    private ProgressDialog pDialog;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<org.nrum.model.Member> memberList = new ArrayList<org.nrum.model.Member>();
    private ListView listView;
    private CustomMemberListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        currentLangID = sharedPreferences.getString("lang_list", "1");
        ListView listView = (ListView) findViewById(R.id.list);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        adapter = new CustomMemberListAdapter(this, memberList);
        listView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(this);
        RequestQueue mRequestQueue = AppController.getInstance().getRequestQueue();
        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(Constant.MEMBER_API);

        pDialog = new ProgressDialog(this);
        pDialog.setTitle(getString(R.string.app_name));
        pDialog.setMessage(getString(R.string.loading));

        if(MFunction.isInternetAvailable(getApplicationContext())) {
            MFunction.fetchNews(swipeRefreshLayout);
        } else {
            Toast.makeText(MemberListActivity.this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
        }
        setMember(currentLangID);
    }
    /**
     * This method is called when swipe refresh is pulled down
     */
    @Override
    public void onRefresh() {
        MFunction.fetchNews(swipeRefreshLayout);
        setMember(currentLangID);
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
    private void setMember(String currentLangID) {
        List<org.nrum.ormmodel.Member> ormMemberList = org.nrum.ormmodel.Member.getAllMember();
        memberList.clear();
        for (org.nrum.ormmodel.Member item:ormMemberList) {
            org.nrum.model.Member member = new org.nrum.model.Member();
            JSONObject objName              = MFunction.jsonStrToObj(item.name);
            JSONObject objDesignation       = MFunction.jsonStrToObj(item.designation);
            JSONObject objEmail             = MFunction.jsonStrToObj(item.email);
            String mName           = MFunction.getFormatedString(objName,"name", currentLangID,100);
            String mDesignation          = MFunction.getFormatedString(objDesignation,"designation", currentLangID,150);
            String mCategory        = MFunction.getFormatedString(objEmail,"email", currentLangID,50);

            /*Adding member to memberList*/
            member.setMemberID(item.member_id);
            member.setName(mName);
            member.setDesignation(mDesignation);
            member.setProfileImage(item.profile_image);
            member.setEmail(mCategory);
            member.setPhone(item.phone);
            member.setAddress(item.address);
            memberList.add(member);
        }
        // notifying list adapter about data changes
        // so that it renders the list view with updated data
        adapter.notifyDataSetChanged();
    }
}
