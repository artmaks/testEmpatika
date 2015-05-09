package com.example.tema.testempatika;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;


public class AboutActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); // Enable navigation back button

        ImageLoader imageLoader = ImageLoader.getInstance(); // Get instance of Universal Image Loader
        final ImageView imageView = (ImageView)findViewById(R.id.logo); // Find imageView
        final Button callButton = (Button)findViewById(R.id.callButton); // Find imageView
        final Button emailButton = (Button)findViewById(R.id.emailButton); // Find imageView

        callButton.getBackground().setColorFilter(Color.parseColor("#257567"), PorterDuff.Mode.MULTIPLY);
        emailButton.getBackground().setColorFilter(Color.parseColor("#257567"), PorterDuff.Mode.MULTIPLY);

        //Options for display images
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .displayer(new RoundedBitmapDisplayer(1000))
                .build();

        //Config
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(options)
                .build();
        ImageLoader.getInstance().init(config);
        imageLoader.clearMemoryCache();
        imageLoader.clearDiscCache();

        //Show circle image
        imageLoader.displayImage("drawable://" + R.mipmap.ic_launcher, imageView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Кнопка возврата на пред. activity
        if (id == android.R.id.home) {
            this.finish();
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Сделать звонок по заданному номеру (нажатие кнопки)
     *
     * @param v
     */
    public void makeCall(View v) {
        String url = "tel:+79851833080";
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(url));
        startActivity(intent);
    }

    /**
     * Перейти в почтовое приложение (нажате кнопки)
     *
     * @param v
     */
    public void sendEmail(View v) {
        Intent it = new Intent(Intent.ACTION_SEND);
        it.setData(Uri.parse("mailto:"));
        String [] to  = {"temamaksimov@gmail.com"};
        it.putExtra(Intent.EXTRA_EMAIL, to);
        it.putExtra(Intent.EXTRA_SUBJECT, "This is email from <Name Application>");
        it.setType("message/rfc822");
        startActivity(Intent.createChooser(it, "Send"));
    }
}
