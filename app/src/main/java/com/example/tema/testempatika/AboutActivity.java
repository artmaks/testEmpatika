package com.example.tema.testempatika;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONException;
import org.json.JSONObject;


public class AboutActivity extends ActionBarActivity {

    private String phoneNumber;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); // Enable navigation back button

        final Button callButton = (Button)findViewById(R.id.callButton); // Find imageView
        final Button emailButton = (Button)findViewById(R.id.emailButton); // Find imageView
        callButton.getBackground().setColorFilter(Color.parseColor("#257567"), PorterDuff.Mode.MULTIPLY);
        emailButton.getBackground().setColorFilter(Color.parseColor("#257567"), PorterDuff.Mode.MULTIPLY);

        CompanyData data = loadLocalData();
        if(data != null) {
            updateInfo(data);
        }

        showImage();
        loadingData();
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
        String url = "tel:" + phoneNumber;
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
        String [] to  = { email };
        it.putExtra(Intent.EXTRA_EMAIL, to);
        it.putExtra(Intent.EXTRA_SUBJECT, "This is email from <Name Application>");
        it.setType("message/rfc822");
        startActivity(Intent.createChooser(it, "Send"));
    }

    public void updateInfo(CompanyData data) {
        ((TextView)findViewById(R.id.companyName)).setText(data.Name);
        ((TextView)findViewById(R.id.descriptionText)).setText(data.Description);
        ((TextView)findViewById(R.id.workTime)).setText("Мы работаем без выходных: " + data.Hours + " ч");
        ((TextView)findViewById(R.id.copyright)).setText("© 2015 " + data.Name);
        phoneNumber = data.Phone;
        email = data.Email;
        saveLocalData(data);
    }

    /**
     * Серииализует объект с помощью библиотеки Gson и сохраняет в SharedPreferences
     *
     * @param data - объект класса CompanyData
     */
    public void saveLocalData(CompanyData data) {
        SharedPreferences  mPrefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(data);
        prefsEditor.putString("OrangeExpress", json);
        prefsEditor.commit();
    }

    /**
     * Загружает данные из SharedPreferences
     *
     * @return - обеъкт класса CompanyData
     */
    public CompanyData loadLocalData() {
        SharedPreferences  mPrefs = getPreferences(MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("OrangeExpress", "");
        Log.d("DebugEmpatika", "Result " + json);
        CompanyData obj = gson.fromJson(json, CompanyData.class);
        return obj;
    }

    /**
     * Загружает данные через сеть
     */
    public void loadingData() {
        //Load data
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "http://empatika-resto.appspot.com/api/company/get_company?company_id=5764144745676800";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("DebugEmpatika", response.toString());
                    String name = response.getString("app_name");
                    String description = response.getString("description");
                    String hours = response.getJSONArray("schedule").getJSONObject(0).getString("hours");
                    String phone = response.getString("phone");
                    String email = response.getJSONArray("support_emails").getString(0);
                    CompanyData data = new CompanyData(name, description, hours, phone, email);
                    updateInfo(data);
                }
                catch (Exception e) {
                    Log.d("DebugEmpatika", e.getMessage());
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("DebugEmpatika", "Error");
            }
        });
        queue.add(jsObjRequest);
    }

    public void showImage() {
        ImageLoader imageLoader = ImageLoader.getInstance(); // Get instance of Universal Image Loader
        final ImageView imageView = (ImageView)findViewById(R.id.logo); // Find imageView
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
}
