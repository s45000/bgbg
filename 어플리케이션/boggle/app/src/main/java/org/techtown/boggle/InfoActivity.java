package org.techtown.boggle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class InfoActivity extends AppCompatActivity {
    private static String TAG = "netTest2_MainActivity";
    String info[];
    String thisPlace;

    TextView Doh;
    TextView Si;
    TextView Gun;
    TextView Gu;
    TextView DisdPlace;
    TextView Caution;
    TextView NCaution;
    TextView Update;
    TextView Notice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Doh = (TextView)findViewById(R.id.Doh);
        Si = (TextView)findViewById(R.id.Si);
        Gun = (TextView)findViewById(R.id.Gun);
        Gu = (TextView)findViewById(R.id.Gu);
        DisdPlace = (TextView)findViewById(R.id.DisdPlace);
        Caution = (TextView)findViewById(R.id.Caution);
        NCaution = (TextView)findViewById(R.id.NCaution);
        Update = (TextView)findViewById(R.id.Update);
        Notice = (TextView)findViewById(R.id.Notice);

        GetData task = new GetData();
        task.execute("http://116.89.189.17/connect.php");

        View reb = findViewById(R.id.refresh);
        reb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reFresh();
            }
        });

        Intent intent = getIntent();
        info = intent.getExtras().getStringArray("info");
        thisPlace = info[4];
    }
    void reFresh(){
        finish();//????????? ??????
        Intent intent2 = getIntent(); //?????????
        startActivity(intent2); //???????????? ??????
    }
    private class GetData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(InfoActivity.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

            Log.d(TAG, "response  - " + result);

            if (result == null){
                //???????????? ???????????????
                Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
            }else {
                //?????????????????? ????????? ?????? ??????
                try {
                    JSONObject jj = null;
                    jj = new JSONObject(result);
                    JSONArray ja = new JSONArray(jj.getString("Info"));
                    JSONObject jjj = null;
                    int leng = Integer.parseInt(jj.getString("Leng"));
                    //????????? ????????? ?????? PlaceInfos??? ??????
                    String text = "test";
                    for(int i = 0; i < leng; i++){
                        jjj = ja.getJSONObject(i);
                        if(jjj.getString("place").equals(thisPlace)){
                            if (!jjj.getString("doh").equals("null")) {
                                Doh.setText(jjj.getString("doh") + "???");
                            }
                            if (!jjj.getString("si").equals("null"))
                                Si.setText(jjj.getString("si") + "???");
                            if (!jjj.getString("gun").equals("null"))
                                Gun.setText(jjj.getString("gun") + "???");
                            if (!jjj.getString("gu").equals("null"))
                                Gu.setText(jjj.getString("gu") + "???");
                            DisdPlace.setText(jjj.getString("place"));
                            //??????????????? ????????? ???????????????
                            Double nowN = Double.parseDouble(jjj.getString("nowN"));
                            Double ruleN = Double.parseDouble(jjj.getString("ruleN"));
                            if ((nowN / ruleN) < 0.333333) {
                                Caution.setText("????????? ?????? ????????????.");
                                Caution.setTextColor(Color.GREEN);
                            } else if ((nowN / ruleN) < 0.666666) {
                                Caution.setText("????????? ?????? ????????????.");
                                Caution.setTextColor(Color.YELLOW);
                            } else {
                                Caution.setText("????????? ?????? ????????????.");
                                Caution.setTextColor(Color.RED);
                            }
                            NCaution.setText("???????????? : " + jjj.getString("nowN") + "\n???????????? : " + jjj.getString("ruleN"));
                            Update.setText(jjj.getString("updateTime"));
                            Notice.setText(jjj.getString("notice"));

                        }
                    }
                }catch(JSONException e){e.printStackTrace();}

            }
        }

        @Override
        protected String doInBackground(String... params) {
            String serverURL = params[0];
            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.connect();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }
                bufferedReader.close();

                return sb.toString().trim();
            } catch (Exception e) {
                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();

                return null;
            }
        }
    }
}