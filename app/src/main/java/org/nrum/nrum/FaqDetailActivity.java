package org.nrum.nrum;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONObject;
import org.nrum.ormmodel.Faq;
import org.nrum.util.MFunction;

public class FaqDetailActivity extends AppCompatActivity {

    String faqID = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final String  currentLangID = sharedPreferences.getString("lang_list", "1");
        // get passed var
        String faqIDStr = getIntent().getStringExtra("faqID");
        Faq faq = Faq.getItem(Integer.parseInt(faqIDStr));
        JSONObject objQuestion = MFunction.jsonStrToObj(faq.question);
        JSONObject objAnswer = MFunction.jsonStrToObj(faq.answer);
        String mQuestion = MFunction.getFormatedString(objQuestion,"question", currentLangID,200);
        String mAnswer = MFunction.getFormatedString(objAnswer,"answer", currentLangID);
        TextView textQuestion = (TextView)findViewById(R.id.question);
        TextView textAnswer = (TextView)findViewById(R.id.answer);
        textQuestion.setText(mQuestion);
        textAnswer.setText(mAnswer);
    }

    private ShareActionProvider mShareActionProvider;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);

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
            case R.id.action_share: {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBodyText = "Check it out. Your message goes here";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
                startActivity(Intent.createChooser(sharingIntent, "Shearing Option"));
                return true;
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
