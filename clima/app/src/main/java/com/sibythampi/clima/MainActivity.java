package com.sibythampi.clima;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    public void findweather(View view){

        EditText cityname=(EditText)findViewById(R.id.editText);

        InputMethodManager mgr=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(cityname.getWindowToken(),0);

        String name=cityname.getText().toString();
        System.out.println("City:"+name);

        DownloadTask task = new DownloadTask();
        String result="";

        try {

            result = task.execute("http://api.openweathermap.org/data/2.5/weather?q="+name+"&appid=1e56d33cc3e3e909da765cc8c7fdfe26").get();


        } catch (InterruptedException e) {

            e.printStackTrace();

        } catch (ExecutionException e) {

            e.printStackTrace();

        }

        Log.i("Contents Of URL", result);



    }



    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {

                url = new URL(urls[0]);

                urlConnection = (HttpURLConnection)url.openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while (data != -1) {

                    char current = (char) data;

                    result += current;

                    data = reader.read();

                }

                return result;

            }
            catch(Exception e) {

                e.printStackTrace();

                return "Failed";

            }


        }
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            String mainw="";
            String desc="";
            try {
                JSONObject jsonObject = new JSONObject(result);
                String re=jsonObject.getString("weather");
                System.out.println("Weather:"+re);
                JSONArray array=new JSONArray(re);

                for(int i=0;i<array.length();i++){

                    JSONObject jpart= array.getJSONObject(i);
                     mainw=jpart.getString("main");
                     desc=jpart.getString("description");
                    System.out.println("main:"+mainw);
                    System.out.println("descrip:"+desc);
                     }
                //System.out.println("main:"+mainw);

                TextView rr=(TextView)findViewById(R.id.textView2);
                rr.setText(" "+mainw+":"+desc);



            } catch (Exception e) {
                e.printStackTrace();
            }




        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    }


}

