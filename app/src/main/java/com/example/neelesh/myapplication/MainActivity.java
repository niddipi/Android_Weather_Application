package com.example.neelesh.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Neelesh on 10/21/2017.
 */
public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.MyApplication.MESSAGE";
    private String zipcodelist=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void Finish(View view){
        finish();
        System.exit(0);
    }
    public void sendMessage(View view) throws IOException {
        String someVariable;
        Calendar calendar = Calendar.getInstance();
        int Hr24=calendar.get(Calendar.HOUR_OF_DAY);
        Log.i("time",String.valueOf(Hr24));
        String endtime =null;
        Date today = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();
        EditText editText = (EditText) findViewById(R.id.Edittext);
        zipcodelist = editText.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String CurrentDate = sdf.format(today);
        String tomorow = sdf.format(tomorrow);
        if(Hr24 >= 20)
        {
            endtime = tomorow;
        }
        else
        {
            endtime = CurrentDate;
        }
        Log.i("dateeee",CurrentDate);
        MyAsyncTask task = new MyAsyncTask(this);
        String Url_weather = "https://graphical.weather.gov/xml/sample_products/browser_interface/ndfdXMLclient.php?zipCodeList="+zipcodelist+"&product=time-series&begin="+CurrentDate+"T00:00:00&end="+endtime+"T23:59:00&maxt=maxt";
        Log.i("dateeee",Url_weather);
        String Url_city = "ziptasticapi.com";
        task.execute(Url_weather,Url_city,zipcodelist);
    }

    public class MyAsyncTask extends AsyncTask<String,Void,String[]>   {
            private Context context;
        private MyAsyncTask(Context context) {
            this.context = context.getApplicationContext();
        }

            protected String[] doInBackground(String... urls) {

                try {
                    String Url= urls[0];
                    String url_city = urls[1];
                    String zipcodelist = urls[2];
                    URL url_weather = new URL(Url);
                    HttpURLConnection urlConnection = (HttpURLConnection) url_weather.openConnection();
                    String jsonStr=null;
                    String url_t = null;
                    String[] result = zipcodelist.split("\\+");
                    String[] citylist = new String[result.length];
                    InputStream inputStream =null;
                    try  {
                        city_parse parse_json = new city_parse();
                        InputStream in = url_weather.openStream();
                        weather_parser parse_xml = new weather_parser();
                        String[] temp = parse_xml.parse(in,zipcodelist);


                        for (int x=0; x<result.length; x++) {
                            url_t = "http://"+url_city+"/"+result[x];
                            Log.i("url",url_t);
                            URL url_temp = new URL(url_t);
                            HttpURLConnection urlConnection_city = (HttpURLConnection) url_temp.openConnection();
                            inputStream = url_temp.openStream();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                            StringBuilder sb = new StringBuilder();
                            String line = null;
                            while ((line = reader.readLine()) != null)
                            {
                                sb.append(line + "\n");
                            }
                            jsonStr = sb.toString();
                            Log.i("json",jsonStr);
                           String city = parse_json.parse(jsonStr);
                            citylist[x] = "\nDaily Maximun Temperature for \n"+city+" is "+temp[x]+" 'F\n\n";
                            urlConnection_city.disconnect();
                        }

                        return citylist;
                    } finally {
                        urlConnection.disconnect();
                    }
                } catch (Exception e) {
                    Log.e("ERROR", e.getMessage(), e);
                    return null;

                }
            }

            protected void onPostExecute(String[] result) {
                Intent intent = new Intent(context, DisplayMessageActivity.class);
                //Intent intent = new Intent(context, ScrollingActivity.class);
                intent.putExtra(EXTRA_MESSAGE, result);
                startActivity(intent);
                return;
            }
        }
}
