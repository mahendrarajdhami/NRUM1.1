package org.nrum.nrum;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.RequestQueue;

import org.json.JSONObject;
import org.nrum.adapter.CustomFaqListAdapter;
import org.nrum.app.AppController;
import org.nrum.util.MFunction;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class FaqListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static String currentLangID = "en";
    private static DecimalFormat df2 = new DecimalFormat("0.00");
    private ProgressDialog pDialog;
    private List<org.nrum.model.Faq> faqList = new ArrayList<org.nrum.model.Faq>();
    private ListView listView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CustomFaqListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        currentLangID = sharedPreferences.getString("lang_list", "1");
        listView = (ListView) findViewById(R.id.list);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        adapter = new CustomFaqListAdapter(this, faqList);
        listView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(this);
        // click event for list item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                int faqID = faqList.get(position).getFaqID();
                Intent intent = new Intent(FaqListActivity.this,NewsDetailActivity.class);
                intent.putExtra("faqID", String.valueOf(faqID));
                startActivity(intent);
            }
        });

        RequestQueue mRequestQueue = AppController.getInstance().getRequestQueue();
        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(Constant.FAQ_API);

        pDialog = new ProgressDialog(this);
        pDialog.setTitle(getString(R.string.app_name));
        pDialog.setMessage(getString(R.string.loading));

        if(MFunction.isInternetAvailable(getApplicationContext())) {
            MFunction.fetchFaq();
        } else {
            Toast.makeText(FaqListActivity.this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
        }
        setFaq(currentLangID);
    }

    @Override
    public void onRefresh() {
//        MFunction.fetchFaq(swipeRefreshLayout);
        setFaq(currentLangID);
    }

    private void setFaq(String currentLangID) {
        List<org.nrum.ormmodel.Faq> ormFaqList = org.nrum.ormmodel.Faq.getAllFaq();
        faqList.clear();
        for (org.nrum.ormmodel.Faq item:ormFaqList) {
            org.nrum.model.Faq faq = new org.nrum.model.Faq();
            JSONObject objQuestion     = MFunction.jsonStrToObj(item.question);
            JSONObject objAnswer    = MFunction.jsonStrToObj(item.answer);
            JSONObject objCategory  = MFunction.jsonStrToObj(item.category_name);
            String mQuestion        = MFunction.getFormatedString(objQuestion,"question", currentLangID,100);
            String mAnswer          = MFunction.getFormatedString(objAnswer,"answer", currentLangID,150);
            String mCategory        = MFunction.getFormatedString(objCategory,"category_name", currentLangID,50);

            // Adding faq to faqList
            faq.setFaqID(item.faq_id);
            faq.setQuestion(mQuestion);
            faq.setAnswer(mAnswer);
            faq.setCategoryName(mCategory);
            faqList.add(faq);
        }
        // notifying list adapter about data changes
        // so that it renders the list view with updated data
        adapter.notifyDataSetChanged();
    }
}