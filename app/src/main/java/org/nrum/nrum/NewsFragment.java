package org.nrum.nrum;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONObject;
import org.nrum.adapter.CustomMainNewsListAdapter;
import org.nrum.model.News;
import org.nrum.util.MDateConversion;
import org.nrum.util.MFunction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class NewsFragment extends Fragment {
    private static String currentLangID = "1";
    private List<News> mainNewsItems = new ArrayList<News>();
    private ListView mainNewsListView;
    private CustomMainNewsListAdapter mainNewsListViewAdapter;

    public NewsFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(NewsFragment.this.getActivity());
        this.currentLangID = sharedPreferences.getString("lang_list", "1");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        ListView mainNewsListView = (ListView) view.findViewById(R.id.mainNewsList);
        mainNewsListViewAdapter = new CustomMainNewsListAdapter(NewsFragment.this.getActivity(),mainNewsItems);
        mainNewsListView.setAdapter(mainNewsListViewAdapter);

        // click event for list item
        mainNewsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                int newsID = mainNewsItems.get(position).getNewsID();
                Intent intent = new Intent(NewsFragment.this.getActivity(),NewsDetailActivity.class);
                intent.putExtra("newsID", String.valueOf(newsID));
                startActivity(intent);
            }
        });
        setNews(currentLangID);
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_sync: {
                setNews(currentLangID);
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    private void setNews(String currentLangID) {
        List<org.nrum.ormmodel.News> ormNewsList = org.nrum.ormmodel.News.getNews(Constant.LIMIT_MAIN_NEWS_FETCH);
        mainNewsItems.clear();
        for (org.nrum.ormmodel.News item:ormNewsList) {
            News news = new News();
            JSONObject objTitle     = MFunction.jsonStrToObj(item.title);
            JSONObject objDetail    = MFunction.jsonStrToObj(item.details);
            JSONObject objCategory  = MFunction.jsonStrToObj(item.category_name);
            String mCategory        = MFunction.getFormatedString(objCategory,"category_name", currentLangID,50);
            String mTitle           = MFunction.getFormatedString(objTitle,"title", currentLangID,100);
            String mDetail          = MFunction.getFormatedString(objDetail,"detail", currentLangID,150);
            String mPublishDateFrom = item.publish_date_from;



            SimpleDateFormat fromDFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat toDFormat=new SimpleDateFormat("yyy-MM-dd");
            String finalDate= null;
            int fromYear=0;
            int fromMonth=0;
            int fromDay=0;
            long dbSeconds =0;
            double showHours = 0;
            double showMinutes =0;
            double showSeconds =0;
            double mRemainder =0;
            try {
                Date date = fromDFormat.parse(mPublishDateFrom);
                /* for getting yyyy,mm,dd*/
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(date);
                fromYear = calendar.get(Calendar.YEAR);
                fromMonth = calendar.get(Calendar.MONTH) + 1;
                fromDay = calendar.get(Calendar.DAY_OF_MONTH);
                dbSeconds = date.getTime()/1000;
                finalDate=   toDFormat.format(date);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
            long currentSeconds = new Date().getTime()/1000;
            long diffSeconds = currentSeconds - dbSeconds;

            if (diffSeconds < (long)86400) {
                showHours = (double)diffSeconds/(double) 3600;
                int intHrPart = (int) showHours;
                showMinutes = (showHours - (double)intHrPart) * (double)60;
                int intMPart = (int) showMinutes;
                showSeconds = (showMinutes - (double) intMPart) * (double)60;
                int intSPart = (int) showSeconds;

                String hr   = (intHrPart != 0)?String.valueOf(intHrPart) + "hr ":"";
                String m    = (intMPart != 0)?String.valueOf(intMPart) + "m ":"";
                String s    = (intSPart != 0)?String.valueOf(intSPart) + "s ":"";
                finalDate   = hr + m + s + getString(R.string.ago);
            } else {
                if(currentLangID.equals(Constant.CURRENT_LANG_ID_2) || currentLangID.equals(Constant.CURRENT_LANG_ID_3)){
                    finalDate = MDateConversion.getNepaliDate(fromYear,fromMonth,fromDay,currentLangID);
                }
            }
            /*Adding news to newsList*/
            news.setNewsID(item.news_id);
            news.setTitle(mTitle);
            news.setPublishDateFrom(finalDate);
            news.setCategoryName(mCategory);
            mainNewsItems.add(news);
        }
        // notifying list adapter about data changes
        // so that it renders the list view with updated data
        mainNewsListViewAdapter.notifyDataSetChanged();
    }
}
