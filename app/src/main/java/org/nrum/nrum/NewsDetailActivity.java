package org.nrum.nrum;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.nrum.ormmodel.News;

public class NewsDetailActivity extends AppCompatActivity {

    String newsID = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // get passed var
        String newsIDStr = getIntent().getStringExtra("newsID");
        News news = News.getItem(Integer.parseInt(newsIDStr));
        String featureImage = news.feature_image;
        String title = news.title;
        String detail = news.details;
        String url = "http://192.168.100.2/bs.dev/nrum/uploads/company_1/news/750_";
        ImageView imageView = (ImageView)findViewById(R.id.featureImage);
        TextView textTitle = (TextView)findViewById(R.id.title);
        TextView textDetail = (TextView)findViewById(R.id.detail);

        // Setting Data (image,title,detail)
//        Picasso.with(getApplicationContext()).load(url.concat(featureImage)).into(imageView);
        Picasso.with(getApplicationContext()).load(url.concat(featureImage)).centerCrop().fit()
                .placeholder(R.mipmap.nrum_logo)
                .error(R.mipmap.nrum_logo)
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
        textTitle.setText(title);
        textDetail.setText(detail);
    }

    private ShareActionProvider mShareActionProvider;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_news_detail, menu);

        // changing color of share menu
        Drawable drawable = menu.findItem(R.id.action_share).getIcon();
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, ContextCompat.getColor(this,R.color.colorMenu));
        menu.findItem(R.id.action_share).setIcon(drawable);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBodyText = "Check it out. Your message goes here";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Subject here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
                startActivity(Intent.createChooser(sharingIntent, "Shearing Option"));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    // Somewhere in the application.
    public void doShare(Intent shareIntent) {
        // When you want to share set the share intent.
        mShareActionProvider.setShareIntent(shareIntent);
    }
}
