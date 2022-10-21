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
        finish();//인텐트 종료
        Intent intent2 = getIntent(); //인텐트
        startActivity(intent2); //액티비티 열기
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
                //토스트로 에러날리기
                Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
            }else {
                //더미데이터에 읽어온 값들 넣기
                try {
                    JSONObject jj = null;
                    jj = new JSONObject(result);
                    JSONArray ja = new JSONArray(jj.getString("Info"));
                    JSONObject jjj = null;
                    int leng = Integer.parseInt(jj.getString("Leng"));
                    //웹에서 읽어온 값들 PlaceInfos에 저장
                    String text = "test";
                    for(int i = 0; i < leng; i++){
                        jjj = ja.getJSONObject(i);
                        if(jjj.getString("place").equals(thisPlace)){
                            if (!jjj.getString("doh").equals("null")) {
                                Doh.setText(jjj.getString("doh") + "도");
                            }
                            if (!jjj.getString("si").equals("null"))
                                Si.setText(jjj.getString("si") + "시");
                            if (!jjj.getString("gun").equals("null"))
                                Gun.setText(jjj.getString("gun") + "군");
                            if (!jjj.getString("gu").equals("null"))
                                Gu.setText(jjj.getString("gu") + "구");
                            DisdPlace.setText(jjj.getString("place"));
                            //공간에따른 인구수 위험도계산
                            Double nowN = Double.parseDouble(jjj.getString("nowN"));
                            Double ruleN = Double.parseDouble(jjj.getString("ruleN"));
                            if ((nowN / ruleN) < 0.333333) {
                                Caution.setText("사람이 거의 없습니다.");
                                Caution.setTextColor(Color.GREEN);
                            } else if ((nowN / ruleN) < 0.666666) {
                                Caution.setText("사람이 조금 많습니다.");
                                Caution.setTextColor(Color.YELLOW);
                            } else {
                                Caution.setText("사람이 매우 많습니다.");
                                Caution.setTextColor(Color.RED);
                            }
                            NCaution.setText("현재인원 : " + jjj.getString("nowN") + "\n권장인원 : " + jjj.getString("ruleN"));
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